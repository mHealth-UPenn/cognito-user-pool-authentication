package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

public class SignUpActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.editTextRegUserId);
        password = (EditText) findViewById(R.id.editTextRegUserPassword);
        email = (EditText) findViewById(R.id.editTextRegEmail);
    }

    public void processSignUp(View v) {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        String usernameInput = username.getText().toString();
        if (usernameInput.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Username cannot be empty.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String passwordInput = password.getText().toString();
        if (passwordInput.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Password cannot be empty.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String emailInput = email.getText().toString();
        if (emailInput.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Email cannot be empty.",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(email.getHint()),
                    emailInput);
        }

        AppHelper.getPool().signUpInBackground(usernameInput, passwordInput, userAttributes,
                null, signUpHandler);
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Confirm sign up if desired
            // See AmazonCognitoYourUserPoolsDemo
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(getApplicationContext(),
                    "Sign up failed",
                    Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    };
}
