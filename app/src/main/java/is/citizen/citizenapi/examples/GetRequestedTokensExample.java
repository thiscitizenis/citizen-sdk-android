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
import is.citizen.citizenapi.enums.AccessType;
import is.citizen.citizenapi.resource.Token;
import is.citizen.citizenapi.service.CryptoService;
import is.citizen.citizenapi.service.TokenService;
import is.citizen.citizenapi.util.Constant;

/*
 * Get a list of requested tokens.
 *
 * A list of issued tokens can be similarly obtained using the GetIssuedTokens task.
 *
 * Token calls usually require a user's mnemonic because user data, including email addresses
 * are stored encrypted on the Citizen Service.
 */

public class GetRequestedTokensExample extends Activity
    implements GetRequestedTokensTask.AsyncResponse
{
    private static final String TAG = CreateFingerprintKeyExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;

    private String tokenId_1 = null;
    private String tokenId_2 = null;
    private String tokenId_3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final Activity activity = this;

        final CryptoService cryptoService = new CryptoService();
        final TokenService tokenService = new TokenService(cryptoService);
        final GetRequestedTokensTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Get Tokens Example");
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

        final String apiKey = params.getString("apiKey");
        final String mnemonic = params.getString("mnemonic");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRequestedTokensTask getRequestedTokensTask = new GetRequestedTokensTask(callback, tokenService);
                getRequestedTokensTask.execute(Constant.CITIZEN_SORT_DATE, apiKey, mnemonic);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GrantTokenExample.class);
                params.putString("tokenId_1", tokenId_1);
                params.putString("tokenId_2", tokenId_2);
                params.putString("tokenId_3", tokenId_3);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void getRequestedTokensTaskFinished(Integer statusCode, List<Token> tokens) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(tokens.get(0).getId());
            for (int i = 0; i < 3; i++) {
                if (AccessType.contains(tokens.get(i).getAccess(), AccessType.TOKEN_SIGNATURE)) {
                    tokenId_3 = tokens.get(i).getId();
                } else if (tokenId_2 == null) {
                    tokenId_2 = tokens.get(i).getId();
                } else {
                    tokenId_1 = tokens.get(i).getId();
                }
            }
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
