package is.citizen.citizenexampleapp.examples;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import is.citizen.citizenapi.async.login.EnrollAuthPublicKeyTask;
import is.citizen.citizenapi.exception.CryptoException;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Create a public/private key pair for use when logging in and signing tokens.
 *
 * The Android Key Store allows the creation of key which can only be used if the
 * user has authenticated within a given time period. After the user authenticates,
 * the app can use the private key to sign data.
 *
 * The fingerprint login works as follows:
 *
 *   1) Get a nonce from the Citizen Service
 *
 *   2) Sign the nonce using the private key.
 *
 *   3) Send the username, nonce and signed nonce to the Citizen Service
 *
 * For this mechanism to work, the the public key must be sent to the Citizen
 * Service in advance so that signatures can be verified with it.
 *
 * This example creates a key pair for the Android Key Store and registers the
 * public part of the key with the Citizen Service.
 *
 * Note that this key is not used to encrypt or decrypt data - only for logging
 * in and signing tokens.
 *
 */

public class CreateFingerprintKeyExample extends Activity
    implements EnrollAuthPublicKeyTask.AsyncResponse
{
    private static final String TAG = CreateFingerprintKeyExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final Activity activity = this;

        final UserService userService = new UserService();
        final CryptoService cryptoService = new CryptoService();
        final EnrollAuthPublicKeyTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Create Fingerprint Key Example");
        resultDescription.setText("Public Key:");
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

        final String userId = params.getString("userId");
        final String apiKey = params.getString("apiKey");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cryptoService.createKeyPair(Constant.CITIZEN_FINGERPRINT_AUTH_KEY, true);
                    String encodedAuthPublicKey = cryptoService.getEncodedPublicKey(Constant.CITIZEN_FINGERPRINT_AUTH_KEY);
                    EnrollAuthPublicKeyTask enrollAuthPublicKeyTask = new EnrollAuthPublicKeyTask(callback, userService);
                    enrollAuthPublicKeyTask.execute(userId, encodedAuthPublicKey, apiKey);
                } catch (CryptoException e) {
                    Log.e(TAG, "Caught exception: " + e.getMessage());
                    resultValue.setText("Exception");
                }
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FingerprintLoginExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void enrollPublicKeyTaskFinished(Integer statusCode, User user) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(user.getAuthPublicKey());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
