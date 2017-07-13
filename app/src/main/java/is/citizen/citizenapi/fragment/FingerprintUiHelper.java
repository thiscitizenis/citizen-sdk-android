package is.citizen.citizenapi.fragment;

import android.annotation.TargetApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import is.citizen.citizenapi.R;

/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */
@TargetApi(23)
public class FingerprintUiHelper extends FingerprintManagerCompat.AuthenticationCallback
{
    public interface Callback {
		void onAuthenticated();

		void onError();
	}

    private static final String TAG = FingerprintUiHelper.class.getSimpleName();

	private static final long ERROR_TIMEOUT_MILLIS = 1600;
	private static final long SUCCESS_DELAY_MILLIS = 1300;

	private final FingerprintManagerCompat mFingerprintManager;
	private final ImageView mIcon;
	private final TextView mErrorTextView;
	private final Callback mCallback;
	private CancellationSignal mCancellationSignal;

	private boolean mSelfCancelled;

	private Runnable mResetErrorTextRunnable = new Runnable()
		{
		    @Override
			public void run() {
				mErrorTextView.setTextColor(
					mErrorTextView.getResources().getColor(R.color.fingerprint_warning, null)
				);
				mErrorTextView.setText(
					mErrorTextView.getResources().getString(R.string.fingerprint_hint)
				);
				mIcon.setImageResource(R.drawable.ic_fp_40px);
			}
		};

	FingerprintUiHelper(FingerprintManagerCompat fingerprintManager,
                        ImageView icon, TextView errorTextView, Callback callback)
	{
		mFingerprintManager = fingerprintManager;
		mIcon = icon;
		mErrorTextView = errorTextView;
		mCallback = callback;
	}


	public boolean isFingerprintAuthAvailable() {
		return (mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints());
	}


	public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {
		if (!isFingerprintAuthAvailable()) {
			return;
		}
		mCancellationSignal = new CancellationSignal();
		mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
		mFingerprintManager .authenticate(cryptoObject, 0 /* flags */, mCancellationSignal, this, null);
		mIcon.setImageResource(R.drawable.ic_fp_40px);
	}


	public void stopListening() {
		if (mCancellationSignal != null) {
			mSelfCancelled = true;
			mCancellationSignal.cancel();
			mCancellationSignal = null;
		}
	}

	@Override
	public void onAuthenticationError(int errMsgId, CharSequence errString) {
		if (!mSelfCancelled) {
			showError(errString);
			mIcon.postDelayed(new Runnable()
				{
				@Override
				public void run()
					{
					mCallback.onError();
					}
				}, ERROR_TIMEOUT_MILLIS);
		}
	}

	@Override
	public void onAuthenticationHelp(int helpMsgId, CharSequence helpString)
		{
		showError(helpString);
		}

	@Override
	public void onAuthenticationFailed() {
		showError(mIcon.getResources().getString(
				R.string.fingerprint_not_recognized));
	}

	@Override
	public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
		mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
		mIcon.setImageResource(R.drawable.ic_fingerprint_success);
		mErrorTextView.setTextColor(
				mErrorTextView.getResources().getColor(R.color.fingerprint_success, null));
		mErrorTextView.setText(mErrorTextView.getResources().getString(R.string.fingerprint_success));
		mIcon.postDelayed(new Runnable()
			{
			@Override
			public void run()
				{
				mCallback.onAuthenticated();
				}
			}, SUCCESS_DELAY_MILLIS);
	}

	private void showError(CharSequence error) {
		mIcon.setImageResource(R.drawable.ic_fingerprint_error);
		mErrorTextView.setText(error);
		mErrorTextView.setTextColor(
				mErrorTextView.getResources().getColor(R.color.fingerprint_warning, null));
		mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
		mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
	}
}
