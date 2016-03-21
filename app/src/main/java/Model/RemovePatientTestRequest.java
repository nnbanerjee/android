package Model;

/**
 * Created by Narendra on 14-03-2016.
 */
public class RemovePatientTestRequest {
    //{"patientTestId": 3,"loggedinUserId":104,"userType":1}

    private String patientTestId;

    public RemovePatientTestRequest(String patientTestId, String loggedinUserId, String userType) {
        this.patientTestId = patientTestId;
        this.loggedinUserId = loggedinUserId;
        this.userType = userType;
    }

    private String loggedinUserId;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPatientTestId() {
        return patientTestId;
    }

    public void setPatientTestId(String patientTestId) {
        this.patientTestId = patientTestId;
    }

    public String getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(String loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    private String userType;
}
