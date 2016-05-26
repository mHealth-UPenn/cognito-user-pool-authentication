package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;

public class HomeScreenActivity extends AppCompatActivity {

    // Cognito user objects
    private CognitoUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = AppHelper.getPool().getCurrentUser();
        TextView tv = (TextView) findViewById(R.id.loggedInUser);
        tv.setText("You are logged in as " + user.getUserId() + ".");

        new SetTableTask().execute();
    }

    public void logOut(View view) {
        user.signOut();
        finish();
    }

    private class SetTableTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void...voids){
            DynamoDBManager.createTable();
            return null;
        }
    }
}
