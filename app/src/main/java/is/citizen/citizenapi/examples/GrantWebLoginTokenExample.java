package is.citizen.citizenapi.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.tokens.GetRequestedTokensTask;
import is.citizen.citizenapi.async.tokens.GetTokenTask;
import is.citizen.citizenapi.async.tokens.RespondTokenTask;
import is.citizen.citizenapi.enums.TokenStatus;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;

/*
 * Grant a web token. Web tokens are used to log in to the Citizen Servce from a web browser.
 *
 * All the token calls so far have passed the mnemonic as an argument because encrypted data
 * is involved. The mnemonic is a sensitive piece of data that is usually and should be stored
 * securely on the user's phone.
 *
 * If a user wants to access tokens from a relatively insecure environment, such as a web browser,
 * a temporary key can be used in place of the mnemonic. The process involves generating ECDH
 * shared secrets between three parties - the browser, the Citizen Service and the user's phone.
 *
 * The process is started by the user visiting the Citizen website and sending a web login token
 * to their account. The web login token is then received and granted on the user's phone. Upon
 * granting the web login token, the user's browser session automatically logs in to the Citizen
 * Service and can use a temporary key to decrypt data.
 *
 * This example retrieves requested tokens for the user's account and grants any that are web login
 * tokens.
 */

public class GrantWebLoginTokenExample extends Activity
    implements GetRequestedTokensTask.AsyncResponse, GetTokenTask.AsyncResponse, RespondTokenTask.AsyncResponse
{
    private static final String TAG = GrantWebLoginTokenExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private final CryptoService cryptoService = new CryptoService();
    private final TokenService tokenService = new TokenService(cryptoService);
    private final GetRequestedTokensTask.AsyncResponse getRequestedTokensCallback = this;
    private final GetTokenTask.AsyncResponse getTokenCallback = this;
    private final RespondTokenTask.AsyncResponse respondTokenCallback = this;

    private String apiKey = null;
    private String mnemonic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final Activity activity = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        final Bundle params = getIntent().getExtras();
        if (params == null) {
            Log.e(TAG, "Unable to get parameters from previous activity");
            resultValue.setText("Unable to get parameters from previous example.");
            return;
        }

        String username = params.getString("username");
        apiKey = params.getString("apiKey");
        mnemonic = params.getString("mnemonic");

        exampleDescription.setText("Grant Web Login Token Example");
        resultDescription.setText("User: " + username);
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);
        numericalInput.setVisibility(View.GONE);
        spinnerInput.setVisibility(View.GONE);

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRequestedTokensTask getRequestedTokensTask = new GetRequestedTokensTask(getRequestedTokensCallback, tokenService);
                getRequestedTokensTask.execute(Constant.CITIZEN_SORT_DATE, apiKey, mnemonic);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        nextExampleButton.setVisibility(View.GONE);
    }


    public void getRequestedTokensTaskFinished(Integer statusCode, List<Token> tokens) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                if (token.getTokenStatus().toString().equals(TokenStatus.WEB_ACCESS_REQUEST.toString())) {
                    GetTokenTask getTokenTask = new GetTokenTask(getTokenCallback, tokenService);
                    getTokenTask.execute(token.getId(), apiKey, mnemonic);
                }
            }
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }


    public void getTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            token.setTokenStatus(TokenStatus.GRANTED);
            RespondTokenTask respondTokenTask = new RespondTokenTask(respondTokenCallback, tokenService);
            respondTokenTask.execute(token, apiKey, mnemonic);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }


    public void respondTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(token.getTokenStatus().toString());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
