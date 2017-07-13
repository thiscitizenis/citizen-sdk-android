package is.citizen.citizenapi.async.signup;

import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.DuplicateUserException;
import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;

public class CreateUserTask extends AsyncTask<Object, Void, Integer>
{
    public interface AsyncResponse {
		void createUserTaskFinished(Integer statusCode, User user);
	}

	private static final String TAG = CreateUserTask.class.getSimpleName();

	private AsyncResponse delegate;
	private UserService userService;
    private User user = null;


	public CreateUserTask(AsyncResponse delegate, UserService userService) {
		this.delegate = delegate;
		this.userService = userService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

        String username      = (String) params[0];
		String password      = (String) params[1];
		String passPhrase    = (String) params[2];
		String authPublicKey = (String) params[3];

		Integer statusCode = Constant.CITIZEN_SIGNUP_CODE_FAIL;

        if (username == null || password == null || passPhrase == null) {
			return Constant.CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS;
		}

	    try {
			user = userService.createUser(username, password, passPhrase, authPublicKey);
            if (user != null) {
				statusCode = Constant.CITIZEN_SIGNUP_CODE_SUCCESS;
			}
		} catch (UnauthorisedException e) {
			Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
			statusCode = Constant.CITIZEN_SIGNUP_CODE_UNAUTHORIZED;
		} catch (DuplicateUserException e) {
			Log.e(TAG, "Caught exception: duplicate user: " + e.getMessage());
			statusCode = Constant.CITIZEN_SIGNUP_CODE_DUPLICATE_USER;
		} catch (HttpException e) {
			Log.e(TAG, "Caught exception: http: " + e.getMessage());
			statusCode = Constant.CITIZEN_SIGNUP_CODE_HTTP_ERROR;
		}

		return statusCode;
	}


	protected void onPostExecute(Integer statusCode) {
		delegate.createUserTaskFinished(statusCode, user);
	}
}
