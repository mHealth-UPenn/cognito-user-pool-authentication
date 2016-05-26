package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.services.cognitoidentityprovider.model.AttributeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by julieengel on 5/26/16.
 */
public class AppHelper {

    private static Map<String, String> signUpFieldsC2O;

    private static AppHelper appHelper;
    private static CognitoUserPool userPool;

    // Change the next five lines of code to run this demo on your user pool

    /**
     * Add your USER POOL ID here
     */
    public static final String USER_POOL_ID = "us-east-1_naS1vLAeO";

    /**
     * Add your APP ID here
     */
    private static final String CLIENT_ID = "56t5vr0di9ve7vco195gcfdda";

    /**
     * APP SECRET associated with your app id - if the App id does not have an associated App secret,
     * set the App secret to null.
     * e.g. clientSecret = null;
     */
    private static final String CLIENT_SECRET = "1a783bac6st0tdrc5uphv6shpcerses1klhf1tgjj6a9ubhsph7k";

    /**
     * Add your IDENTITY POOL ID here
     */
    public static final String IDENTITY_POOL_ID = "us-east-1:48504083-07ee-45e3-9852-4a75f4d194d5";

    // Note that spaces are not allowed in the table name
    public static final String TEST_TABLE_NAME = "Test2";

    // User details from the service
    private static CognitoUserSession currSession;
    private static CognitoUserDetails userDetails;

    // User details to display - they are the current values, including any local modification

    public static void init(Context context) {
        if (appHelper != null && userPool != null) {
            return;
        }

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

        if (userPool == null) {
            userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET,
                    new ClientConfiguration());
        }

        signUpFieldsC2O = new HashMap<String, String>();
        signUpFieldsC2O.put("Email","email");
    }

    public static CognitoUserPool getPool() {
        return userPool;
    }

    public static Map<String, String> getSignUpFieldsC2O() {
        return signUpFieldsC2O;
    }

    public static void setCurrSession(CognitoUserSession session) {
        currSession = session;
    }

}
