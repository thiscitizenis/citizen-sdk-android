package is.citizen.citizenapi.async.person;


import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.PersonNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Phone;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

public class SetPhoneTask extends AsyncTask<Object, Void, Integer>
{
    public interface AsyncResponse {
		void setPhoneTaskFinished(Integer statusCode, Phone phone);
	}

	private static final String TAG = SetPhoneTask.class.getSimpleName();

	private AsyncResponse delegate;
	private PersonService personService;
    private Phone phone = null;


	public SetPhoneTask(AsyncResponse delegate, PersonService personService) {
		this.delegate = delegate;
		this.personService = personService;
	}

	@Override
	protected Integer doInBackground(Object... params) {

        String personId = (String) params[0];
        Phone phone     = (Phone)  params[1];
		String apiKey   = (String) params[2];

		Integer statusCode = Constant.CITIZEN_SIGNUP_CODE_FAIL;

        if (phone == null || apiKey == null) {
			return Constant.CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS;
		}

		try {
            this.phone = personService.addPhone(personId, phone, apiKey);
            if (this.phone != null) {
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
		delegate.setPhoneTaskFinished(statusCode, phone);
	}
}
