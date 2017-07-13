package is.citizen.citizenapi.async.login;


import android.os.AsyncTask;
import android.util.Log;

import java.security.Signature;

import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;


public class FingerprintLoginTask extends AsyncTask<Object, Void, Integer> {

    public interface AsyncResponse {
        void fingerprintAuthenticationTaskFinished(Integer statusCode, User user);
    }

    private static final String TAG = FingerprintLoginTask.class.getSimpleName();

    private FingerprintLoginTask.AsyncResponse delegate;
    private UserService userService;
    private User user = null;


    public FingerprintLoginTask(FingerprintLoginTask.AsyncResponse delegate,
                                UserService userService)
    {
        this.delegate = delegate;
        this.userService = userService;
    }


    protected Integer doInBackground(Object... params) {

        String username     = (String) params[0];
        Signature signature = (Signature) params[1];

        Integer statusCode = Constant.CITIZEN_LOGIN_CODE_FAIL;

        if (username == null || signature == null) {
            Log.e(TAG, "One or more parameters is null");
            return Constant.CITIZEN_LOGIN_CODE_MISSING_PARAMETERS;
        }

        try {
            user = userService.loginWithSignedTransaction(username, signature);
            if (user != null) {
                statusCode = Constant.CITIZEN_LOGIN_CODE_SUCCESS;
            }
        } catch (UnauthorisedException e) {
            Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_UNAUTHORIZED;
        } catch (UserNotFoundException e) {
            Log.e(TAG, "Caught exception: user not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_USER_NOT_FOUND;
        } catch (HttpException e) {
            Log.e(TAG, "Caught exception: http: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_HTTP_ERROR;
        } catch (CryptoException e) {
            Log.e(TAG, "Caught exception: crypto: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_CRYPTO_ERROR;
        }

        return statusCode;
    }


    @Override
    protected void onPostExecute(Integer statusCode) {
        delegate.fingerprintAuthenticationTaskFinished(statusCode, user);
    }
}
