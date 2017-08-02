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

import is.citizen.citizenapi.async.login.PasswordLoginTask;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Log in with a username and password.
 *
 * Upon successful login a User object is returned.
 *
 * Note here that when logging in the mnemonic code is not contained in the User
 * object as it is intended to be stored securely on the phone. It can be fetched
 * using GetMnemonicTask if needed.
 *
 */

public class PasswordLoginExample extends Activity
    implements PasswordLoginTask.AsyncResponse
{
    private static final String TAG = PasswordLoginExample.class.getSimpleName();

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
        final PasswordLoginTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Password Auth Example");
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
        final String password = params.getString("password");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordLoginTask passwordLoginTask = new PasswordLoginTask(callback, userService);
                passwordLoginTask.execute(username, password);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CreateFingerprintKeyExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }

    @Override
    public void passwordAuthenticationTaskFinished(Integer statusCode, User user) {
        if (statusCode == Constant.CITIZEN_LOGIN_CODE_SUCCESS ) {
            resultValue.setText(user.getApiKey());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
