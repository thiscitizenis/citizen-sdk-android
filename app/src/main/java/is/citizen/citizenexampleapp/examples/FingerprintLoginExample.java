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

import is.citizen.citizenapi.exception.FingerprintDialogException;
import is.citizen.citizenapi.fragment.FingerprintAuthenticationDialogFragment;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.FingerprintService;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.FingerprintUtils;

import is.citizen.citizenexampleapp.R;

/*
 * Log in with a fingerprint. This assumes that a public key has been registered with
 * the Citizen Service for the user (see CreateFingerprintKeyExample.java).
 *
 * A UI element is provided in the API given that there is a lot of boilerplate code
 * involved in signing data with a fingerprint secured private key. This UI element
 * also offers the user the option of signing in with their username and password if
 * they choose, or if the there is an error with the fingerprint login.
 *
 */

public class FingerprintLoginExample extends Activity
    implements FingerprintAuthenticationDialogFragment.LoginAttemptFinishedCallback
{
    private static final String TAG = FingerprintLoginExample.class.getSimpleName();

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

        final Context context = this.getApplicationContext();
        final Activity activity = this;

        final UserService userService = new UserService();
        final CryptoService cryptoService = new CryptoService();
        final FingerprintService fingerprintService = new FingerprintService(context, cryptoService);

        final FingerprintAuthenticationDialogFragment.LoginAttemptFinishedCallback fingerprintCallback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Fingerprint Auth Example");
        resultDescription.setText("API Key:");
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

        final String username = params.getString("username");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FingerprintUtils.loginWithFingerprintAuthenticationDialog(activity, fingerprintCallback, fingerprintService, userService, username);
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
                Intent intent = new Intent(activity, SetNameExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loginAttemptFinished(Integer statusCode, User user) {
        if (statusCode == Constant.CITIZEN_LOGIN_CODE_SUCCESS ) {
            resultValue.setText(user.getApiKey());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
