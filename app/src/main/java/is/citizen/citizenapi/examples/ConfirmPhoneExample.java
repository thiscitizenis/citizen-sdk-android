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

import is.citizen.citizenapi.R;
import is.citizen.citizenapi.async.person.ConfirmPhoneTask;
import is.citizen.citizenapi.resource.Phone;
import is.citizen.citizenapi.service.PersonService;
import is.citizen.citizenapi.util.Constant;


/*
 * Confirm the user's phone using the code sent to them via SMS.
 *
 * Whether or not the phone was confirmed successfully can be determined from the
 * returned Phone object.
 */

public class ConfirmPhoneExample extends Activity
    implements ConfirmPhoneTask.AsyncResponse
{
    private static final String TAG = ConfirmPhoneExample.class.getSimpleName();

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
        final ConfirmPhoneTask.AsyncResponse callback = this;

        exampleDescription = (TextView) findViewById(R.id.example_description);
        resultDescription = (TextView) findViewById(R.id.example_result_description);
        resultValue = (TextView) findViewById(R.id.example_result_value);
        nextExampleText = (TextView) findViewById(R.id.example_next_example);
        nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        numericalInput = (EditText) findViewById(R.id.example_numerical_input);
        spinnerInput = (Spinner) findViewById(R.id.example_spinner);

        exampleDescription.setText("Confirm Phone Example");
        resultDescription.setText("Enter SMS Confirm Code: ");
        nextExampleText.setVisibility(View.GONE);
        nextExampleButton.setVisibility(View.GONE);
        spinnerInput.setVisibility(View.GONE);

        final Bundle params = getIntent().getExtras();
        if (params == null) {
            Log.e(TAG, "Unable to get parameters from previous activity");
            resultValue.setText("Unable to get parameters from previous example.");
            return;
        }

        final String apiKey  = params.getString("apiKey");
        final String phoneId = params.getString("phoneId");

        final Phone phone = new Phone();
        phone.setId(phoneId);

        FloatingActionButton runExampleButton = (FloatingActionButton) findViewById(R.id.example_run_button);
        runExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmCode = numericalInput.getText().toString();
                if (confirmCode == null || confirmCode.length() < 1) {
                    numericalInput.setError("Confirm code cannot be blank");
                } else {
                    phone.setSmsConfirmCode(confirmCode);
                    ConfirmPhoneTask confirmPhoneTask = new ConfirmPhoneTask(callback, personService);
                    confirmPhoneTask.execute(phone, apiKey);
                }
            }
        });

        FloatingActionButton nextExampleButton = (FloatingActionButton) findViewById(R.id.example_next_button);
        nextExampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SetOriginExample.class);
                intent.putExtras(params);
                startActivity(intent);
            }
        });
    }


    public void confirmPhoneTaskFinished(Integer statusCode, Phone phone) {
        if (statusCode == Constant.CITIZEN_REST_CODE_SUCCESS) {
            if (phone.getSmsConfirmed()) {
                resultValue.setText("Phone Confirmed");
            } else {
                resultValue.setText("Phone Not Confirmed");
            }
            nextExampleText.setVisibility(View.VISIBLE);
            nextExampleButton.setVisibility(View.VISIBLE);
        } else {
            resultValue.setText("Error: " + statusCode.toString());
        }
    }
}
