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

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.tokens.GetTokenTask;
import is.citizen.citizenapi.async.tokens.VerifyTokenTask;
import is.citizen.citizenapi.enums.PropertyType;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;

/*
 * Verify a signed token.
 *
 * This functionality uses the signing user's public key to verify that they
 * signed the token.
 */

public class VerifySignedTokenExample extends Activity
    implements GetTokenTask.AsyncResponse, VerifyTokenTask.AsyncResponse
{
    private static final String TAG = VerifySignedTokenExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private final CryptoService cryptoService = new CryptoService();
    private final TokenService tokenService = new TokenService(cryptoService);
    private final GetTokenTask.AsyncResponse getTokenCallback = this;
    private final VerifyTokenTask.AsyncResponse verifyTokenCallback = this;


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

        exampleDescription.setText("Verify Token Signature Example");
        resultDescription.setText("Result:");
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);
        numericalInput.setVisibility(View.GONE);
        spinnerInput.setVisibility(View.GONE);

        final Bundle params = getIntent().getExtras();
        if (params == null) {
            Log.e(TAG, "Unable to get parameters from previous activity");
            resultValue.setText("Unable to get parameters from previous example.");
            return;
        }

        final String apiKey = params.getString("apiKey");
        final String mnemonic = params.getString("mnemonic");
        final String tokenId = params.getString("tokenId_3");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTokenTask getTokenTask = new GetTokenTask(getTokenCallback, tokenService);
                getTokenTask.execute(tokenId, apiKey, mnemonic);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity, GrantWebLoginTokenExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void getTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            VerifyTokenTask verifyTokenTask = new VerifyTokenTask(verifyTokenCallback, tokenService);
            verifyTokenTask.execute(token);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }


    public void verifyTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(token.getMetaData().get(PropertyType.TOKEN_SIGNATURE_VERIFICATION_RESULT.toString()).toString());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
