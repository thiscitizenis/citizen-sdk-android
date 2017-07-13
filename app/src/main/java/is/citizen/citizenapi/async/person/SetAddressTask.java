package is.citizen.citizenapi.async.person;


import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.PersonNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Address;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

public class SetAddressTask extends AsyncTask<Object, Void, Integer>
{
    public interface AsyncResponse {
		void setAddressTaskFinished(Integer statusCode, Address address);
	}

	private static final String TAG = SetAddressTask.class.getSimpleName();

	private AsyncResponse delegate;
	private PersonService personService;
    private Address address = null;


	public SetAddressTask(AsyncResponse delegate, PersonService personService) {
		this.delegate = delegate;
		this.personService = personService;
	}


	@Override
	protected Integer doInBackground(Object... params) {

        String personId = (String)  params[0];
        Address address = (Address) params[1];
		String apiKey   = (String)  params[2];

		Integer statusCode = Constant.CITIZEN_SIGNUP_CODE_FAIL;

        if (personId == null || address == null || apiKey == null) {
			return Constant.CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS;
		}

		try {
            this.address = personService.addAddress(personId, address, apiKey);
            if (this.address != null) {
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
		delegate.setAddressTaskFinished(statusCode, address);
	}
}
