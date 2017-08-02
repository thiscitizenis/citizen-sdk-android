package is.citizen.citizenapi.async.tokens;

import android.os.AsyncTask;
import android.util.Log;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.TokenNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;


public class DeleteTokenTask extends AsyncTask<Object, Void, Integer> {
    private static final String TAG = DeleteTokenTask.class.getSimpleName();

    public interface AsyncResponse {
        void deleteTokenTaskFinished(Integer statusCode, boolean tokenDeleted);
    }

    private TokenService tokenService;
    private AsyncResponse delegate;
    private boolean tokenDeleted = false;

    public DeleteTokenTask(AsyncResponse delegate, TokenService tokenService) {
        this.delegate = delegate;
        this.tokenService = tokenService;
    }

    @Override
    protected Integer doInBackground(Object... params) {

        String tokenId = (String) params[0];
        String apiKey  = (String) params[1];

        Integer statusCode = Constant.CITIZEN_REST_CODE_FAIL;

        try {
            this.tokenDeleted = tokenService.deleteToken(tokenId, apiKey);
            if (this.tokenDeleted) {
                statusCode = Constant.CITIZEN_REST_CODE_SUCCESS;
            }
        } catch (UnauthorisedException e) {
            Log.e(TAG, "Caught exception: unauthorised: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_UNAUTHORIZED;
        } catch (TokenNotFoundException e) {
            Log.e(TAG, "Caught exception: token not found: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_TOKEN_NOT_FOUND;
        } catch (HttpException e) {
            Log.e(TAG, "Caught exception: http: " + e.getMessage());
            statusCode = Constant.CITIZEN_REST_CODE_HTTP_ERROR;
        }

        return statusCode;
    }

    @Override
    protected void onPostExecute(Integer statusCode) {
        delegate.deleteTokenTaskFinished(statusCode, tokenDeleted);
    }
}
