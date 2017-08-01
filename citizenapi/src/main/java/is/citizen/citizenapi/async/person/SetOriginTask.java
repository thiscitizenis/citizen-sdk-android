package is.citizen.citizenapi.async.person;


import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.PersonNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

public class SetOriginTask extends AsyncTask<Object, Void, Integer>
{
    public interface AsyncResponse {
		void setOriginTaskFinished(Integer statusCode, Person person);
	}

	private static final String TAG = SetOriginTask.class.getSimpleName();

	private AsyncResponse delegate;
	private PersonService personService;
    private Person person = null;


	public SetOriginTask(AsyncResponse delegate, PersonService personService) {
		this.delegate = delegate;
		this.personService = personService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

        Person person = (Person) params[0];
		String apiKey = (String) params[1];

		Integer statusCode = Constant.CITIZEN_SIGNUP_CODE_FAIL;

        if (person == null || apiKey == null) {
			return Constant.CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS;
		}

		try {
            this.person = personService.updateOrigin(person, apiKey);
            if (this.person != null) {
                statusCode = Constant.CITIZEN_REST_CODE_SUCCESS;
            }
        } catch (UnauthorisedException e) {
            Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_UNAUTHORIZED;
        } catch (PersonNotFoundException e) {
            Log.e(TAG, "Caught exception: person not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_PERSON_NOT_FOUND;
        } catch (HttpException e) {
            Log.e(TAG, "Caught exception: http: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_HTTP_ERROR;
        }

        return statusCode;
    }


	protected void onPostExecute(Integer statusCode) {
		delegate.setOriginTaskFinished(statusCode, person);
	}
}
