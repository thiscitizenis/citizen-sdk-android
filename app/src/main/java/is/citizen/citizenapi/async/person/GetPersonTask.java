package is.citizen.citizenapi.async.person;


import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.MnemonicCodeNotFoundException;
import is.citizen.citizenapi.exception.PersonNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

public class GetPersonTask extends AsyncTask<Object, Void, Integer>
{
    public interface AsyncResponse {
		void getPersonTaskFinished(Integer statusCode, Person person);
	}

	private static final String TAG = GetPersonTask.class.getSimpleName();

	private AsyncResponse delegate;
	private PersonService personService;
    private Person person = null;


	public GetPersonTask(AsyncResponse delegate, PersonService personService) {
		this.delegate = delegate;
		this.personService = personService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

		String apiKey   = (String) params[0];
        String mnemonic = (String) params[1];

		Integer statusCode = Constant.CITIZEN_SIGNUP_CODE_FAIL;

        if (apiKey == null || mnemonic == null) {
			return Constant.CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS;
		}

		try {
            this.person = personService.getPerson(apiKey, mnemonic);
            if (this.person != null) {
                statusCode = Constant.CITIZEN_REST_CODE_SUCCESS;
            }
        } catch (UnauthorisedException e) {
            Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_UNAUTHORIZED;
        } catch (PersonNotFoundException e) {
            Log.e(TAG, "Caught exception: person not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_PERSON_NOT_FOUND;
        } catch (MnemonicCodeNotFoundException e) {
            Log.e(TAG, "Caught exception: mnemonic code not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_MNEMONIC_CODE_NOT_FOUND;
        } catch (HttpException e) {
            Log.e(TAG, "Caught exception: http: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_HTTP_ERROR;
        }

        return statusCode;
    }


	protected void onPostExecute(Integer statusCode) {
		delegate.getPersonTaskFinished(statusCode, person);
	}
}
