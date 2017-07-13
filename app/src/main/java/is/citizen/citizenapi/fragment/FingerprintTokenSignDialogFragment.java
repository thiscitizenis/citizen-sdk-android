package is.citizen.citizenapi.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Signature;

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;


public class FingerprintTokenSignDialogFragment extends DialogFragment
		implements  FingerprintUiHelper.Callback
{
    public interface TokenSignFinishedCallback
	{
		void tokenSignFinished(Integer statusCode, Token token);
	}

	private static final String TAG = FingerprintTokenSignDialogFragment.class.getSimpleName();

	private TokenService tokenService;
	private Token token = null;

	private TokenSignFinishedCallback callback;

	private Button mCancelButton;
	private View mFingerprintContent;

	private FingerprintManagerCompat.CryptoObject cryptoObject;
	private FingerprintUiHelper mFingerprintUiHelper;
	private Activity mActivity;

	public void setTokenService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
		this.cryptoObject = cryptoObject;
	}


	public void setCallback(TokenSignFinishedCallback callback) {
		this.callback = callback;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
	}

	@TargetApi(23)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		getDialog().setTitle(R.string.fingerprint_sign_token);
		View v = inflater.inflate(R.layout.fingerprint_token_sign_dialog_container, container, false);
		mCancelButton = (Button) v.findViewById(R.id.cancel_button);
		mCancelButton.setText(R.string.fingerprint_cancel);
		mCancelButton.setOnClickListener(new View.OnClickListener()
			{
			@Override
			public void onClick(View view)
				{
					dismiss();
				}
			});

		mFingerprintContent = v.findViewById(R.id.fingerprint_container);
		mFingerprintUiHelper = new FingerprintUiHelper(
				FingerprintManagerCompat.from(getActivity()),
				(ImageView) v.findViewById(R.id.fingerprint_icon),
				(TextView) v.findViewById(R.id.fingerprint_status), this);

		mFingerprintUiHelper.startListening(cryptoObject);
		mFingerprintContent.setVisibility(View.VISIBLE);

		return v;
	}


	@Override
	public void onResume() {
		super.onResume();
		mFingerprintUiHelper.startListening(cryptoObject);
	}


	@Override
	public void onPause() {
		super.onPause();
		mFingerprintUiHelper.stopListening();
	}


	@TargetApi(23)
    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}


	@TargetApi(23)
	@Override
	public void onAuthenticated() {
		Signature signature = cryptoObject.getSignature();

		Integer statusCode = Constant.CITIZEN_REST_CODE_SUCCESS;

		try {
			token = tokenService.signToken(token, signature);
		} catch (CryptoException e) {
            Log.e(TAG, "Error signing token: " + e.getMessage());
			statusCode = Constant.CITIZEN_REST_CODE_CRYPTO_ERROR;
		}

		callback.tokenSignFinished(statusCode, token);
		dismiss();
	}


	@Override
	public void onError() {
		Log.e("Token", "Error biometrically approving token.");
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}
