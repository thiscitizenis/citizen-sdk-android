package is.citizen.citizenapi.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.person.SetPhoneTask;
import is.citizen.citizenapi.enums.CountryCode;
import is.citizen.citizenapi.enums.PhoneType;
import is.citizen.citizenapi.resource.Phone;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;
import is.citizen.citizenapi.util.SpinnerData;


/*
 * Set the user's phone details.
 *
 * On making this call, an SMS is sent to the user's phone so they can confirm it.
 *
 * A Phone object is returned here, but phone details can also be accessed through
 * a Person object.
 */

public class SetPhoneExample extends Activity
    implements SetPhoneTask.AsyncResponse
{
    private static final String TAG = SetPhoneExample.class.getSimpleName();

    private TextView exampleDescription;
    private TextView resultDescription;
    private TextView resultValue;
    private TextView nextExampleText;
    private FloatingActionButton nextExampleButton;
    private EditText numericalInput;
    private Spinner spinnerInput;
    private SpinnerData spinnerItems[];

    private String phoneId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_default);

        final Activity activity = this;

        final PersonService personService = new PersonService();
        final SetPhoneTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Set Phone Example");
        resultDescription.setText("Enter Phone Number and Country: ");
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);

        CountryCode[] countries = CountryCode.values();
        spinnerItems = new SpinnerData[countries.length];
        spinnerItems[0] = new SpinnerData("Country", null);
        for (int i = 1; i < countries.length; i++) {
            CountryCode country = countries[i];
            String name = country.name();
            Integer code = country.getDialingCode();

            spinnerItems[i] = new SpinnerData(name + " - " + code, name);
        }

        ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<>(this, R.layout.example_spinner, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInput.setAdapter(adapter);

        final Bundle params = getIntent().getExtras();
        if (params == null) {
            Log.e(TAG, "Unable to get parameters from previous activity");
            resultValue.setText("Unable to get parameters from previous example.");
            return;
        }

        final String apiKey = params.getString("apiKey");
        final String personId = params.getString("personId");

        final Phone phone = new Phone();
        phone.setPersonId(personId);
        phone.setPhoneType(PhoneType.MOBILE);

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = numericalInput.getText().toString();
                String phoneCode = ((SpinnerData) spinnerInput.getSelectedItem()).getValue();

                if (phoneNumber == null || phoneNumber.length() < 1) {
                    numericalInput.setError("Phone cannot be blank");
                } else if (phoneCode == null || phoneCode.length() < 1) {
                    numericalInput.setError("Select a country code");
                } else {
                    phone.setCountryCode(CountryCode.valueOf(phoneCode));
                    phone.setPhoneNumber(phoneNumber);
                    phone.formatPhoneNumber();
                    SetPhoneTask setPhoneTask = new SetPhoneTask(callback, personService);
                    setPhoneTask.execute(personId, phone, apiKey);
                }
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ConfirmPhoneExample.class);
                params.putString("phoneId", phoneId);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void setPhoneTaskFinished(Integer statusCode, Phone phone) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            resultValue.setText(phone.getPhoneNumber());
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
            phoneId = phone.getId();
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
