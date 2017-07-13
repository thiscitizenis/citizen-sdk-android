package is.citizen.citizenapi.async.tokens;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import is.citizen.citizenapi.exception.HttpException;
import is.citizen.citizenapi.exception.TokenNotFoundException;
import is.citizen.citizenapi.exception.UnauthorisedException;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;


public class GetRequestedTokensTask extends AsyncTask<Object, Void, Integer> {
    private static final String TAG = GetRequestedTokensTask.class.getSimpleName();

    public interface AsyncResponse {
        void getRequestedTokensTaskFinished(Integer statusCode, List<Token> tokens);
    }

    private TokenService tokenService;
    private AsyncResponse delegate;
    private List<Token> tokens = null;

    public GetRequestedTokensTask(AsyncResponse delegate, TokenService tokenService) {
        this.delegate = delegate;
        this.tokenService = tokenService;
    }

    @Override
    protected Integer doInBackground(Object... params) {

        String tokenSort = (String) params[0];
        String apiKey    = (String) params[1];
        String mnemonic  = (String) params[2];

        Integer statusCode = Constant.CITIZEN_REST_CODE_FAIL;

        try {
            this.tokens = tokenService.getTokensRequested(tokenSort, apiKey, mnemonic);
            if (this.tokens != null) {
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
        delegate.getRequestedTokensTaskFinished(statusCode, tokens);
    }
}
