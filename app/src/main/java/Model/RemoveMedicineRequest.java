package Model;

/**
 * Created by Narendra on 10-03-2016.
 */
public class RemoveMedicineRequest {
    //{"medicineId":8,"loggedinUserId":104,"userType":1}
    private String medicineId;

    public RemoveMedicineRequest(String medicineId, String loggedinUserId, String userType) {
        this.medicineId = medicineId;
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

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(String loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    private String userType;

}
