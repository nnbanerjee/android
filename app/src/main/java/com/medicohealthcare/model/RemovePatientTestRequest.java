package com.medicohealthcare.model;

/**
 * Created by Narendra on 14-03-2016.
 */
public class RemovePatientTestRequest {
    //{"patientTestId": 3,"loggedinUserId":104,"userType":1}

    private Integer patientTestId;

    public RemovePatientTestRequest(Integer patientTestId, Integer loggedinUserId) {
        this.patientTestId = patientTestId;
        this.loggedinUserId = loggedinUserId;
        this.userType = userType;
    }

    private Integer loggedinUserId;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getPatientTestId() {
        return patientTestId;
    }

    public void setPatientTestId(Integer patientTestId) {
        this.patientTestId = patientTestId;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    private String userType;
}
