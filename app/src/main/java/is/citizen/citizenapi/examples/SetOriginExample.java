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

import org.joda.time.DateTime;

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.person.SetOriginTask;
import is.citizen.citizenapi.enums.CountryName;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

/*
 * Set the user's date of birth, place of birth and nationality.
 *
 * These currently need to be set together, but they will be able to be set
 * individually in a future release.
 *
 * Note here that the data in the person object is returned encrypted because
 * that structure has ready been initialised in an earlier call and has been
 * encrypted on the Citizen Service.
 *
 */

public class SetOriginExample extends Activity
    implements SetOriginTask.AsyncResponse
{
    private static final String TAG = SetOriginExample.class.getSimpleName();

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
        final SetOriginTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Set Origin Example");
        resultDescription.setText("Result: ");
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
        final String personId = params.getString("personId");

        final Person person = new Person();
        person.setId(personId);
        person.setDateOfBirth(new DateTime(1984, 3, 26, 10, 0, 0, 0));
        person.setCountryNationality(CountryName.GB);
        person.setPlaceOfBirth("London");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetOriginTask setOriginTask = new SetOriginTask(callback, personService);
                setOriginTask.execute(person, apiKey);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SetAddressExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void setOriginTaskFinished(Integer statusCode, Person person) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText("Origin Details Sent");
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
