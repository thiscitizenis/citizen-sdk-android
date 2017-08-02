package is.citizen.citizenexampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import is.citizen.citizenexampleapp.examples.CreateNewUserExample;


/* This activity is the start of a simple app to demonstrate the Citizen API.
 *
 * It goes through the following actions:
 *
 *   - Create a new user (CreateNewUserExample.java).
 *
 *   - Log in with a username and password (PasswordLoginExample.java).
 *
 *   - Create a key pair for fingerprint login and signing (CreateFingerprintKeyExample.java).
 *
 *   - Log in using a fingerprint signature (FingerprintLoginExample.java).
 *
 *   - Set the user's name (SetNameExample.java).
 *
 *   - Set the user's phone details (SetPhoneExample.java).
 *
 *   - Confirm the user's phone with a code sent by SMS (ConfirmPhoneExample.java).
 *
 *   - Set the user's date of birth, nationality and place of birth (SetOriginExample.java).
 *
 *   - Set the user's address (SetAddressExample.java).
 *
 *   - Get the user's details eg name, phone, DOB etc (GetPersonExample.java).
 *
 *   - Issue tokens (IssueTokenExample.java).
 *
 *   - Get token requests (GetRequestedTokensExample.java).
 *
 *   - Grant a token (GrantTokenExample.java).
 *
 *   - Decline a token (DeclineTokenExample.java).
 *
 *   - Verify a token (VerifySignedTokenExample.java).
 *
 *   - Log in in via a web browser by approving a token (GrantWebLoginTokenExample.java).
 *
 */


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity mainActivity = this;

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            Intent intent = new Intent(mainActivity, CreateNewUserExample.class);
            startActivity(intent);
            }
        });
    }
}
