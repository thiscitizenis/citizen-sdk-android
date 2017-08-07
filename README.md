# Android SDK for the Citizen secure login and data exchange service.

More about the Citizen Service can be found here:

  https://www.citizen.is/

and here:

  https://developers.citizen.is/

The project contains a skeleton app with examples of the following tasks:

  - Create a new user (CreateNewUserExample.java).
  - Log in with a username and password (PasswordLoginExample.java).
  - Create a key pair for fingerprint login and signing (CreateFingerprintKeyExample.java).
  - Log in using a fingerprint signature (FingerprintLoginExample.java).
  - Set the user's name (SetNameExample.java).
  - Set the user's phone details (SetPhoneExample.java).
  - Confirm the user's phone with a code sent by SMS (ConfirmPhoneExample.java).
  - Set the user's date of birth, nationality and place of birth (SetOriginExample.java).
  - Set the user's address (SetAddressExample.java).
  - Get the user's details eg name, phone, DOB etc (GetPersonExample.java).
  - Issue tokens (IssueTokenExample.java).
  - Get token requests (GetRequestedTokensExample.java).
  - Grant a token (GrantTokenExample.java).
  - Decline a token (DeclineTokenExample.java).
  - Verify a token (VerifySignedTokenExample.java).
  - Log in in via a web browser by approving a token (GrantWebLoginTokenExample.java).

The SDK can be added to an Android project by including the following line to the dependencies section of the app's build.gradle:

  compile 'is.citizen:citizen-android-sdk:1.0'
