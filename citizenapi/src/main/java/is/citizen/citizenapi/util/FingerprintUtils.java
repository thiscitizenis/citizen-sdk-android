package is.citizen.citizenapi.util;


import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.security.Signature;

import is.citizen.citizenapi.exception.FingerprintDialogException;
import is.citizen.citizenapi.fragment.FingerprintTokenSignDialogFragment;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.exception.FingerprintException;
import is.citizen.citizenapi.fragment.FingerprintAuthenticationDialogFragment;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.FingerprintService;
import is.citizen.citizenapi.service.TokenService;


public class FingerprintUtils
{
    private static final String TAG = FingerprintUtils.class.getSimpleName();

    private static final String FINGERPRINT_AUTHENTICATION_DIALOG_FRAGMENT_TAG = "fingerprintAuthenticationDialogFragment";
    private static final String FINGERPRINT_TOKEN_SIGN_DIALOG_FRAGMENT_TAG = "fingerprintTokenSignDialogFragment";

    /*
     * Convenience function to call a dialog to log in using the phone fingerprint signing mechanism.
     *
     * An Activity using this should implement:  FingerprintAuthenticationDialogFragment.LoginAttemptFinishedCallback callback:
     *
     *   public void loginAttemptFinished(Integer statusCode, User user) {
     *      ...
     *   }
     *
     */
    public static void loginWithFingerprintAuthenticationDialog(Activity activity,
                                                                FingerprintAuthenticationDialogFragment.LoginAttemptFinishedCallback callback,
                                                                FingerprintService fingerprintService,
                                                                UserService userService,
                                                                String email)
            throws FingerprintDialogException
    {
        Signature signature = null;

        try {
            signature = fingerprintService.getFingerprintSignature();
        } catch (FingerprintException e) {
            throw new FingerprintDialogException(e.getMessage());
        }

        if (signature != null) {
            // Show the fingerprint dialog. The user has the option to use the fingerprint with
            // crypto, or you can fall back to using a server-side verified password.
            FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();
            fragment.setCallback(callback);
            fragment.setUserService(userService);
            fragment.setEmail(email);
            fragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(signature));

            fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
            fragment.show(activity.getFragmentManager(), FINGERPRINT_AUTHENTICATION_DIALOG_FRAGMENT_TAG);
        } else {
             // This happens if the lock screen has been disabled or or a fingerprint got
             // enrolled. Thus show the dialog to authenticate with their password first
             // and ask the user if they want to authenticate with fingerprints in the
             // future
             FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();
             fragment.setCallback(callback);
             fragment.setUserService(userService);
             fragment.setEmail(email);
             fragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(signature));
             fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
             fragment.show(activity.getFragmentManager(), FINGERPRINT_AUTHENTICATION_DIALOG_FRAGMENT_TAG);
        }
    }


    /*
     * Convenience function to call a dialog to sign tokens using the phone fingerprint signing mechanism.
     *
     * An Activity using this should implement FingerprintTokenSignDialogFragment.TokenSignFinishedCallback:
     *
     *   public void tokenSignFinished(Integer statusCode, Token token) {
     *     ...
     *   }
     *
     */
    public static void signTokenWithFingerprintAuthenticationDialog(Activity activity,
                                                                    FingerprintTokenSignDialogFragment.TokenSignFinishedCallback callback,
                                                                    FingerprintService fingerprintService,
                                                                    TokenService tokenService,
                                                                    Token token)
            throws FingerprintDialogException
    {
        Signature signature = null;

        try {
            signature = fingerprintService.getFingerprintSignature();
        } catch (FingerprintException e) {
            throw new FingerprintDialogException(e.getMessage());
        }

        if (signature != null) {
            // Show the fingerprint dialog.
            FingerprintTokenSignDialogFragment fragment = new FingerprintTokenSignDialogFragment();
            fragment.setCallback(callback);
            fragment.setTokenService(tokenService);
            fragment.setToken(token);
            fragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(signature));
            fragment.show(activity.getFragmentManager(), FINGERPRINT_TOKEN_SIGN_DIALOG_FRAGMENT_TAG);
        }
    }
}
