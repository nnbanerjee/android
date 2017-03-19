package com.medico.model;

/**
 * Created by Narendra on 10-03-2016.
 */
public class RemoveMedicineRequest {
    //{"medicineId":8,"loggedinUserId":104,"userType":1}
    private Integer medicineId;

    public RemoveMedicineRequest(Integer medicineId, Integer loggedinUserId) {
        this.medicineId = medicineId;
        this.loggedinUserId = loggedinUserId;
    }

    private Integer loggedinUserId;

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }


}
