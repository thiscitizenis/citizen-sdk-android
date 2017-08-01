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

import is.citizen.citizenapi.async.person.GetPersonTask;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;


/*
 * Get decrypted user data such as name, date of birth, address etc in a Person object.
 *
 * This call needs the user's mnemonic code as an argument so that the user's private key
 * can be decrypted. This is then used to decrypt the user's data.
 *
 */

public class GetPersonExample extends Activity
    implements GetPersonTask.AsyncResponse
{
    private static final String TAG = GetPersonExample.class.getSimpleName();

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

        final PersonService personService = new PersonService();
        final GetPersonTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Get Person Example");
        resultDescription.setText("Surname: ");
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
                GetPersonTask getPersonTask = new GetPersonTask(callback, personService);
                getPersonTask.execute(apiKey, mnemonic);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, IssueTokenExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void getPersonTaskFinished(Integer statusCode, Person person) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(person.getLastName());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
