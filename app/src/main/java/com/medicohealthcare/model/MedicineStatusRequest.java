package com.medicohealthcare.model;

/**
 * Created by Narendra on 10-03-2016.
 */
public class MedicineStatusRequest
{
    private Integer medicineId;
    private Integer alarmStatus;

    public MedicineStatusRequest(Integer medicineId, Integer alarmStatus) {
        this.medicineId = medicineId;
        this.alarmStatus = alarmStatus;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getLoggedinUserId() {
        return alarmStatus;
    }

    public void setLoggedinUserId(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }


}
