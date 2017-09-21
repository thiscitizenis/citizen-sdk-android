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

import is.citizen.citizenapi.async.tokens.IssueTokenTask;
import is.citizen.citizenapi.enums.AccessType;
import is.citizen.citizenapi.enums.TokenDurationType;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Issue token requests.
 *
 * The following data is need to create a token:
 *
 *   Requester email - email address of the the user requesting data
 *   User email      - email address of the user from whom data is being requested
 *   Token duration  - specifies how long the requester can access the data
 *   Token access    - specifies the type of data requested (eg, name, DOB, address etc).
 *
 * Upon sending the token, the recipient user will receive a notification on their phone
 * and can grant or decline access to the data specified in it.
 *
 * In this example, the issuing and receiving user are the same, but in ordinary use they
 * would be different users.
 *
 */

public class IssueTokenExample extends Activity
    implements IssueTokenTask.AsyncResponse
{
    private static final String TAG = CreateFingerprintKeyExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private int tokensIssued = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final Activity activity = this;

        final CryptoService cryptoService = new CryptoService();
        final TokenService tokenService = new TokenService(cryptoService);
        final IssueTokenTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Issue Token Example");
        resultDescription.setText("Token Id:");
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

        final String email = params.getString("email");
        final String apiKey = params.getString("apiKey");
        final String mnemonic = params.getString("mnemonic");

        final Token token_1 = new Token();
        token_1.setRequesterEmail(email);
        token_1.setUserEmail(email);
        token_1.setDurationType(TokenDurationType.MONTH);
        token_1.setDuration(2);
        int access = 0;
        access = AccessType.add(access, AccessType.NAME);
        access = AccessType.add(access, AccessType.DOB);
        token_1.setAccess(access);

        final Token token_2 = new Token();
        token_2.setRequesterEmail(email);
        token_2.setUserEmail(email);
        token_2.setDurationType(TokenDurationType.WEEK);
        token_2.setDuration(6);
        access = 0;
        access = AccessType.add(access, AccessType.NAME);
        access = AccessType.add(access, AccessType.PHONE);
        token_2.setAccess(access);

        final Token token_3 = new Token();
        token_3.setRequesterEmail(email);
        token_3.setUserEmail(email);
        token_3.setDurationType(TokenDurationType.MONTH);
        token_3.setDuration(2);
        access = 0;
        access = AccessType.add(access, AccessType.NAME);
        access = AccessType.add(access, AccessType.DOB);
        access = AccessType.add(access, AccessType.TOKEN_SIGNATURE);
        token_3.setAccess(access);


        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IssueTokenTask issueTokenTask_1 = new IssueTokenTask(callback, tokenService);
                issueTokenTask_1.execute(token_1, apiKey, mnemonic);
                IssueTokenTask issueTokenTask_2 = new IssueTokenTask(callback, tokenService);
                issueTokenTask_2.execute(token_2, apiKey, mnemonic);
                IssueTokenTask issueTokenTask_3 = new IssueTokenTask(callback, tokenService);
                issueTokenTask_3.execute(token_3, apiKey, mnemonic);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GetRequestedTokensExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void issueTokenTaskFinished(Integer statusCode, Token token) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS && tokensIssued == 2) {
            resultValue.setText(token.getId());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS ) {
            tokensIssued++;
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
