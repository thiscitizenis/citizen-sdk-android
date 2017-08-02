package is.citizen.citizenapi.async.login;

import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;


public class EnrollAuthPublicKeyTask extends AsyncTask<Object, Void, Integer>
{
	public interface AsyncResponse {
		void enrollPublicKeyTaskFinished(Integer statusCode, User user);
	}

	private static final String TAG = EnrollAuthPublicKeyTask.class.getSimpleName();

	private AsyncResponse delegate;
	private UserService userService;
    private User user = null;


	public EnrollAuthPublicKeyTask(AsyncResponse delegate, UserService userService) {
		this.delegate = delegate;
		this.userService = userService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

        String userId        = (String) params[0];
		String authPublicKey = (String) params[1];
		String apiKey        = (String) params[2];

		Integer statusCode = Constant.CITIZEN_REST_CODE_FAIL;

        if (userId == null || authPublicKey == null || apiKey == null) {
			return Constant.CITIZEN_REST_CODE_MISSING_PARAMETERS;
		}

	    try {
			user = userService.enrollAuthPublicKey(userId, authPublicKey, apiKey);
            if (user != null) {
				statusCode = Constant.CITIZEN_REST_CODE_SUCCESS;
			}
		} catch (UnauthorisedException e) {
			Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
			statusCode = Constant.CITIZEN_REST_CODE_UNAUTHORIZED;
		} catch (UserNotFoundException e) {
            Log.e(TAG, "Caught exception: user not found: " + e.getMessage());
			statusCode = Constant.CITIZEN_REST_CODE_USER_NOT_FOUND;
		} catch (HttpException e) {
			Log.e(TAG, "Caught exception: http: " + e.getMessage());
			statusCode = Constant.CITIZEN_REST_CODE_HTTP_ERROR;
		}

		return statusCode;
	}


	protected void onPostExecute(Integer statusCode) {
		delegate.enrollPublicKeyTaskFinished(statusCode, user);
	}
}
