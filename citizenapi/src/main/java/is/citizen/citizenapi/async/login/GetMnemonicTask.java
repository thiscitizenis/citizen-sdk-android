package is.citizen.citizenapi.async.login;

import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.UserNotFoundException;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.exception.UnauthorisedException;


public class GetMnemonicTask extends AsyncTask<Object, Void, Integer>
{
	public interface AsyncResponse {
		void getMnemonicTaskFinished(Integer statusCode, String mnemonic);
	}

	private static final String TAG = GetMnemonicTask.class.getSimpleName();

	private AsyncResponse delegate;
	private UserService userService;
    private String mnemonic = null;


	public GetMnemonicTask(AsyncResponse delegate, UserService userService) {
		this.delegate = delegate;
		this.userService = userService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

        String email   = (String) params[0];
		String passphrase = (String) params[1];
		String apiKey     = (String) params[2];

		Integer statusCode = Constant.CITIZEN_REST_CODE_FAIL;

        if (email == null || passphrase == null || apiKey == null) {
			return Constant.CITIZEN_REST_CODE_MISSING_PARAMETERS;
		}

		User user = new User();
		user.setPrimaryEmail(email);
        user.setPassPhrase(passphrase);

	    try {
			mnemonic = userService.getMnemonic(user, apiKey);
            if (mnemonic != null) {
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
		delegate.getMnemonicTaskFinished(statusCode, mnemonic);
	}
}
