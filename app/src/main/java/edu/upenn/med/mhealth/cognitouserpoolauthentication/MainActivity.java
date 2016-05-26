package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Screen fields
    private EditText inUsername;
    private EditText inPassword;

    private AlertDialog userDialog;

    // User Details
    private String username;
    private String password;

    public static AmazonClientManager clientManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize application
        AppHelper.init(getApplicationContext());
        clientManager = new AmazonClientManager(this);

        inUsername = (EditText) findViewById(R.id.editTextUserId);
        inPassword = (EditText) findViewById(R.id.editTextUserPassword);

        findCurrent();
    }

    // App methods
    // Register user - start process
    public void signUp(View view) {
        Intent registerActivity = new Intent(this, SignUpActivity.class);
        startActivityForResult(registerActivity, 1);
    }

    // Login if a user is already present
    public void logIn(View view) {
        username = inUsername.getText().toString();
        if(username == null || username.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewUserIdMessage);
            label.setText(inUsername.getHint()+" cannot be empty");
            return;
        }

        AppHelper.setUser(username);

        password = inPassword.getText().toString();
        if(password == null || password.length() < 1) {
            TextView label = (TextView) findViewById(R.id.textViewUserPasswordMessage);
            label.setText(inPassword.getHint()+" cannot be empty");
            return;
        }

        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
    }

    private void findCurrent() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if(username != null) {
            AppHelper.setUser(username);
            inUsername.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void launchUser() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("name", username);
        startActivity(intent);
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession) {
            AppHelper.setCurrSession(cognitoUserSession);
            String idToken = cognitoUserSession.getIdToken().getJWTToken();

            Map<String, String> logins = new HashMap<String, String>();
            logins.put("cognito-idp.us-east-1.amazonaws.com/" + AppHelper.USER_POOL_ID, idToken);
            clientManager.credentialsProvider().setLogins(logins);
            launchUser();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Implement for MFA
            // See AmazonCognitoYourUserPoolsDemo
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation continuation,
                                             String un) {
            if(un != null) {
                username = un;
                AppHelper.setUser(un);
            }
            if(password == null) {
                inUsername.setText(un);
                password = inPassword.getText().toString();
            }

            AuthenticationDetails authenticationDetails = new AuthenticationDetails(username,
                    password, null);
            continuation.setAuthenticationDetails(authenticationDetails);
            continuation.continueTask();
        }

        @Override
        public void onFailure(Exception e) {
            TextView label = (TextView) findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");

            label = (TextView) findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");

            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }
    };

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }



}
