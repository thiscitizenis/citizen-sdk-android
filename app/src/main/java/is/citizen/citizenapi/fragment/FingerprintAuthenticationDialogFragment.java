package is.citizen.citizenapi.fragment;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Signature;

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.login.FingerprintLoginTask;
import is.citizen.citizenapi.async.login.PasswordLoginTask;
import is.citizen.citizenapi.service.UserService;

import is.citizen.citizenapi.resource.User;


public class FingerprintAuthenticationDialogFragment extends DialogFragment
		implements FingerprintLoginTask.AsyncResponse, PasswordLoginTask.AsyncResponse,
		           TextView.OnEditorActionListener, FingerprintUiHelper.Callback
{
	public interface LoginAttemptFinishedCallback {
        void loginAttemptFinished(Integer statusCode, User user);
    }

    private static final String TAG = FingerprintAuthenticationDialogFragment.class.getSimpleName();

    private UserService userService;
	private String username;

	private LoginAttemptFinishedCallback callback;

	private FingerprintManagerCompat.CryptoObject cryptoObject;

	private InputMethodManager inputMethodManager;
    private FingerprintUiHelper fingerprintUiHelper;

	private FingerprintLoginTask fingerprintLoginTask;
	private PasswordLoginTask passwordLoginTask;

	public enum Stage {
		FINGERPRINT,
		NEW_FINGERPRINT_ENROLLED,
		PASSWORD
	}

	private Stage stage = Stage.FINGERPRINT;

	private Button mCancelButton;
	private Button mBackupDialogButton;
	private View mFingerprintContent;
	private View mBackupContent;
	private EditText passwordInput;
	private CheckBox mUseFingerprintFutureCheckBox;
	private TextView mPasswordDescriptionTextView;
	private TextView mNewFingerprintEnrolledTextView;

	private final Runnable mShowKeyboardRunnable = new Runnable() {
		@Override
		public void run() {
			inputMethodManager.showSoftInput(passwordInput, 0);
		}
	};

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCallback(LoginAttemptFinishedCallback callback) {
		this.callback = callback;
	}

	public void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
		this.cryptoObject = cryptoObject;
	}

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        fingerprintLoginTask = new FingerprintLoginTask(this, userService);
		passwordLoginTask = new PasswordLoginTask(this, userService);
    }

    @Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().setTitle(getString(R.string.fingerprint_sign_in));
        View v = inflater.inflate(R.layout.fingerprint_auth_dialog_container, container, false);

		mFingerprintContent = v.findViewById(R.id.fingerprint_container);
		mBackupContent = v.findViewById(R.id.backup_container);
		mPasswordDescriptionTextView = (TextView) v.findViewById(R.id.password_description);
		mUseFingerprintFutureCheckBox = (CheckBox)
				v.findViewById(R.id.use_fingerprint_in_future_check);
		mNewFingerprintEnrolledTextView = (TextView)
				v.findViewById(R.id.new_fingerprint_enrolled_description);

		passwordInput = (EditText) v.findViewById(R.id.password);
		passwordInput.setOnEditorActionListener(this);

		mCancelButton = (Button) v.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view) {
					dismiss();
				}
			});

		mBackupDialogButton = (Button) v.findViewById(R.id.backup_dialog_button);
		mBackupDialogButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view) {
				if (stage == Stage.FINGERPRINT) {
					goToBackup();
				} else {
					verifyPassword();
				}
			}
			});

		fingerprintUiHelper = new FingerprintUiHelper(
				FingerprintManagerCompat.from(getActivity()),
				(ImageView) v.findViewById(R.id.fingerprint_icon),
				(TextView) v.findViewById(R.id.fingerprint_status), this);

		updateStage();

        return v;
    }

    @Override
	public void onResume() {
		super.onResume();
		if (stage == Stage.FINGERPRINT) {
			fingerprintUiHelper.startListening(cryptoObject);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		fingerprintUiHelper.stopListening();
	}

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);

		inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

    private void updateStage() {
		switch (stage) {
			case FINGERPRINT:
				mCancelButton.setText(R.string.fingerprint_cancel);
				mBackupDialogButton.setText(R.string.fingerprint_use_password);
				mFingerprintContent.setVisibility(View.VISIBLE);
				mBackupContent.setVisibility(View.GONE);
				break;
			case NEW_FINGERPRINT_ENROLLED:
				// Intentional fall through
			case PASSWORD:
				mCancelButton.setText(R.string.fingerprint_cancel);
				mBackupDialogButton.setText(R.string.fingerprint_ok);
				mFingerprintContent.setVisibility(View.GONE);
				mBackupContent.setVisibility(View.VISIBLE);
				if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
					mPasswordDescriptionTextView.setVisibility(View.GONE);
					mNewFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
					mUseFingerprintFutureCheckBox.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	private void goToBackup() {
		stage = Stage.PASSWORD;
		updateStage();
		passwordInput.requestFocus();
		passwordInput.postDelayed(mShowKeyboardRunnable, 500);
		fingerprintUiHelper.stopListening();
	}

	private void verifyPassword() {
		if (!passwordLoginTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			String password = passwordInput.getText().toString();
			passwordLoginTask = new PasswordLoginTask(this, userService);
			passwordLoginTask.execute(username, password);
		}
	}

	@Override
	public void fingerprintAuthenticationTaskFinished(Integer code, User user) {
		callback.loginAttemptFinished(code, user);
        dismiss();
    }

    @Override
    public void passwordAuthenticationTaskFinished(Integer code, User user) {
		callback.loginAttemptFinished(code, user);
		dismiss();
    }

    @Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_GO) {
			verifyPassword();
			return true;
		}
		return false;
	}

	@TargetApi(23)
	@Override
	public void onAuthenticated() {
		if (!fingerprintLoginTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			Signature signature = cryptoObject.getSignature();
			fingerprintLoginTask = new FingerprintLoginTask(this, userService);

			fingerprintLoginTask.execute(username, signature);
		}
	}

	@Override
	public void onError() {
		goToBackup();
	}

	@Override
	public void dismiss() {
		if (fingerprintLoginTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			fingerprintLoginTask.cancel(true);
		}
		if (passwordLoginTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			passwordLoginTask.cancel(true);
		}

		super.dismiss();
	}
}
