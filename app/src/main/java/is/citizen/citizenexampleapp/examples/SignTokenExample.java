package is.citizen.citizenexampleapp.examples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import is.citizen.citizenapi.async.tokens.RespondTokenTask;
import is.citizen.citizenapi.enums.PropertyType;
import is.citizen.citizenapi.enums.TokenStatus;
import is.citizen.citizenapi.exception.FingerprintDialogException;
import is.citizen.citizenapi.fragment.FingerprintTokenSignDialogFragment;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.FingerprintService;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.FingerprintUtils;

import is.citizen.citizenexampleapp.R;

/*
 * The key pair that was generated in CreateFingerprintKeyExample.java
 * can also be used to sign a token.
 *
 * This gives extra confidence that the user has granted consent for access
 * to their data.
 *
 * Similarly to logging using a fingerprint signature, a UI element is also
 * provided here given that there is a lot of boilerplate code involved in
 * implementing this functionality in Android.
 */

public class SignTokenExample extends Activity
    implements RespondTokenTask.AsyncResponse, FingerprintTokenSignDialogFragment.TokenSignFinishedCallback
{
    private static final String TAG = SignTokenExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private String apiKey;
    private String mnemonic;
    private CryptoService cryptoService = new CryptoService();
    private TokenService tokenService = new TokenService(cryptoService);
    private RespondTokenTask.AsyncResponse callback = this;

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

        exampleDescription.setText("Sign Token Example");
        resultDescription.setText("Signed Token ID:");
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);
        numericalInput.setVisibility(View.GONE);
        spinnerInput.setVisibility(View.GONE);

        final Context context = this.getApplicationContext();
        final FingerprintTokenSignDialogFragment.TokenSignFinishedCallback tokenSignFinishedCallback = this;
        final FingerprintService fingerprintService = new FingerprintService(context, cryptoService);

        final Bundle params = getIntent().getExtras();
        if (params == null) {
            Log.e(TAG, "Unable to get parameters from previous activity");
            resultValue.setText("Unable to get parameters from previous example.");
            return;
        }

        final String tokenId = params.getString("tokenId_3");
        apiKey = params.getString("apiKey");
        mnemonic = params.getString("mnemonic");

        final Token token = new Token();
        token.setId(tokenId);
        token.setTokenStatus(TokenStatus.GRANTED);

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FingerprintUtils.signTokenWithFingerprintAuthenticationDialog(activity, tokenSignFinishedCallback, fingerprintService, tokenService, token);
                } catch (FingerprintDialogException e) {
                    Log.e(TAG, "Caught exception: " + e.getMessage());
                    resultValue.setText("Exception");
                }
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, VerifySignedTokenExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void tokenSignFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            RespondTokenTask respondTokenTask = new RespondTokenTask(callback, tokenService);
            respondTokenTask.execute(token, apiKey, mnemonic);
        }
    }


    public void respondTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText((String) token.getMetaData().get(PropertyType.SIGNED_TOKEN_ID.toString()));
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
