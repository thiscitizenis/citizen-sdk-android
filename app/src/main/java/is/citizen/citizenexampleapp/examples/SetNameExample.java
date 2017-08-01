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

import is.citizen.citizenapi.async.person.SetNameTask;
import is.citizen.citizenapi.enums.GenderType;
import is.citizen.citizenapi.enums.NameTitle;
import is.citizen.citizenapi.resource.Name;
import is.citizen.citizenapi.resource.Person;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Set the user's name.
 *
 * A Person object is returned here rather than the User object returned  when logging in.
 * The Person object primarily concerns personal data such as name, date of birth, address
 * etc.
 *
 */

public class SetNameExample extends Activity
    implements SetNameTask.AsyncResponse
{
    private static final String TAG = SetNameExample.class.getSimpleName();

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
        final SetNameTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Set Name Example");
        resultDescription.setText("First Name: ");
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

        final Name name = new Name();
        name.setTitle(NameTitle.MR.toString());
        name.setFirstName("John");
        name.setMiddleName("Paul");
        name.setLastName("Doe");
        name.setGender(GenderType.MALE.toString());

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetNameTask setNameTask = new SetNameTask(callback, personService);
                setNameTask.execute(personId, name, apiKey);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SetPhoneExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void setNameTaskFinished(Integer statusCode, Person person) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(person.getFirstName());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
