package com.medicohealthcare.model;

/**
 * Created by Narendra on 10-03-2016.
 */
public class DiagnosticStatusRequest
{
    private Integer testId;
    private Integer alarmStatus;

    public DiagnosticStatusRequest(Integer medicineId, Integer alarmStatus) {
        this.testId = medicineId;
        this.alarmStatus = alarmStatus;
    }

    public Integer getMedicineId() {
        return testId;
    }

    public void setMedicineId(Integer testId) {
        this.testId = testId;
    }

    public Integer getLoggedinUserId() {
        return alarmStatus;
    }

    public void setLoggedinUserId(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }


}
