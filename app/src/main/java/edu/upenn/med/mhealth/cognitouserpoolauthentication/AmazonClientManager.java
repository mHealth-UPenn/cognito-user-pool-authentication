package edu.upenn.med.mhealth.cognitouserpoolauthentication;

import android.content.Context;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class AmazonClientManager {

    private CognitoCachingCredentialsProvider credentialsProvider = null;
    private Context context;
    private AmazonDynamoDBClient ddb = null; //Specific to client

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    /**
     * Get AWS Client
     * REPLACE FOR DESIRED SERVICE
     */
    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public CognitoCachingCredentialsProvider credentialsProvider() {
        validateCognitoCredentials();
        return credentialsProvider;
    }

    public void validateCognitoCredentials() {
        if (credentialsProvider == null) {
            initCredentialsProvider();
        }
    }

    public void validateCredentials() {
        if (ddb == null) {
            initClients();
        }
    }

    private void initCredentialsProvider() {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                AppHelper.IDENTITY_POOL_ID,
                Regions.US_EAST_1); // Region of Identity Pool
    }

    /**
     * REPLACE FOR DESIRED SERVICE
     */
    private void initClients() {
        ddb = new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1)); // Region of client setup
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
        if (
            // STS
            // http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                ex.getErrorCode().equals("IncompleteSignature")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InvalidClientTokenId")
                        || ex.getErrorCode().equals("OptInRequired")
                        || ex.getErrorCode().equals("RequestExpired")
                        || ex.getErrorCode().equals("ServiceUnavailable")

                        // REPLACE FOR DESIRED SERVICE
                        // DynamoDB
                        // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                        || ex.getErrorCode().equals("AccessDeniedException")
                        || ex.getErrorCode().equals("IncompleteSignatureException")
                        || ex.getErrorCode().equals(
                        "MissingAuthenticationTokenException")
                        || ex.getErrorCode().equals("ValidationException")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InternalServerError")) {

            return true;
        }
        return false;
    }
}
