package is.citizen.citizenexampleapp.examples;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.SecureRandom;

import is.citizen.citizenapi.async.signup.CreateUserTask;
import is.citizen.citizenapi.resource.User;
import is.citizen.citizenapi.service.UserService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Create a new user.
 *
 * New user's can be created by specifying a username, password and passphrase.
 *
 * Username and password are self-explanatory. The passphrase is used to generate the
 * user's 'mnemonic code', which is used to encrypt their private key for crypto
 * operations. The passphrase can also be used to recover their mnemonic code should
 * they switch phones etc.
 *
 * A User object is returned upon successfully creating the user. This object primarily
 * concerns login and key functionality.
 */

public class CreateNewUserExample extends Activity
    implements CreateUserTask.AsyncResponse
{
    private static final String TAG = CreateNewUserExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private SecureRandom random = new SecureRandom();

    private User userReceived = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final String primaryEmail = new BigInteger(130, random).toString(32).substring(0, 8) + "@test.com";
        final String password = "Test1234";
        final String passphrase = "Test12";

        final Activity activity = this;

        final UserService userService = new UserService();
        final CreateUserTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Create User Example");
        resultDescription.setText("Email:");
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);
        numericalInput.setVisibility(View.GONE);
        numericalInput.setVisibility(View.GONE);
        spinnerInput.setVisibility(View.GONE);

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateUserTask createUserTask = new CreateUserTask(callback, userService);
                createUserTask.execute(primaryEmail, password, passphrase, null);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PasswordLoginExample.class);
                Bundle params = new Bundle();
                params.putString("userId", userReceived.getId());
                params.putString("personId", userReceived.getPersonId());
                params.putString("email", userReceived.getPrimaryEmail());
                params.putString("password", password);
                params.putString("passphrase", passphrase);
                params.putString("mnemonic", userReceived.getMnemonicCode());
                params.putString("apiKey", userReceived.getApiKey());
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void createUserTaskFinished(Integer statusCode, User user) {
        if (statusCode == Constant.CITIZEN_SIGNUP_CODE_SUCCESS) {
            resultValue.setText(user.getPrimaryEmail());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
            this.userReceived = user;
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
