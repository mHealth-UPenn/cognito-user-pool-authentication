package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;

public class HomeScreenActivity extends AppCompatActivity {

    // Cognito user objects
    private CognitoUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = AppHelper.getPool().getCurrentUser();
        TextView tv = (TextView) findViewById(R.id.loggedInUser);
        tv.setText("You are logged in as " + user.getUserId());
    }

    public void logOut(View view) {
        user.signOut();
        finish();
    }


}
