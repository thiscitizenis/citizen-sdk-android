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

import is.citizen.citizenapi.async.person.SetAddressTask;
import is.citizen.citizenapi.enums.AddressType;
import is.citizen.citizenapi.enums.CountryName;
import is.citizen.citizenapi.resource.Address;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;

import is.citizen.citizenexampleapp.R;

/*
 * Set the user's address.
 *
 * The address can be subsequently accessed through a Person object.
 *
 */

public class SetAddressExample extends Activity
    implements SetAddressTask.AsyncResponse
{
    private static final String TAG = SetAddressExample.class.getSimpleName();

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
        final SetAddressTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Set Address Example");
        resultDescription.setText("PostCode: ");
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

        final String personId = params.getString("personId");
        final String apiKey = params.getString("apiKey");

        final Address address = new Address();
        address.setAddressLine1("101 Main Street");
        address.setAddressLine2("Main Avenue");
        address.setAddressLine3("Main Town");
        address.setCity("Mainton");
        address.setState("Maine");
        address.setCountryName(CountryName.GB);
        address.setAddressType(AddressType.HOME);
        address.setPostCode("111 222");

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetAddressTask setAddressTask = new SetAddressTask(callback, personService);
                setAddressTask.execute(personId, address, apiKey);
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GetPersonExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void setAddressTaskFinished(Integer statusCode, Address address) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(address.getPostCode());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
