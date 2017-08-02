package is.citizen.citizenapi.async.login;

import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.User;


public class PasswordLoginTask  extends AsyncTask<Object, Void, Integer> {

     public interface AsyncResponse {
        void passwordAuthenticationTaskFinished(Integer statusCode, User user);
    }

    private static final String TAG = PasswordLoginTask.class.getSimpleName();

    private PasswordLoginTask.AsyncResponse delegate;
    private UserService userService;
    private User user = null;


    public PasswordLoginTask(PasswordLoginTask.AsyncResponse delegate,
                             UserService userService)
    {
        this.delegate = delegate;
        this.userService = userService;
    }


    protected Integer doInBackground(Object... params) {

        String username = (String) params[0];
        String password = (String) params[1];

        Integer statusCode = Constant.CITIZEN_LOGIN_CODE_FAIL;

        if (username == null || password == null) {
            Log.e(TAG, "One or more parameters is null");
            return Constant.CITIZEN_LOGIN_CODE_MISSING_PARAMETERS;
        }

        try {
            user = userService.loginUserPass(username, password);
            if (user != null) {
                statusCode = Constant.CITIZEN_LOGIN_CODE_SUCCESS;
            }
        } catch (UnauthorisedException e) {
            Log.e(TAG, "FingerprintLoginTask: caught exception: unauthorised: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_UNAUTHORIZED;
        } catch (UserNotFoundException e) {
            Log.e(TAG, "FingerprintLoginTask: caught exception: user not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_USER_NOT_FOUND;
        } catch (HttpException e) {
            Log.e(TAG, "FingerprintLoginTask: caught exception: http: " + e.getMessage());
            statusCode = Constant.CITIZEN_LOGIN_CODE_HTTP_ERROR;
        }

        return statusCode;
    }


    @Override
    protected void onPostExecute(Integer statusCode) {
        delegate.passwordAuthenticationTaskFinished(statusCode, user);
    }
}
