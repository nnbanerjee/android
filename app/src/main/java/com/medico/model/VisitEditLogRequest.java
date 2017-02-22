package com.medico.model;

/**
 * Created by Narendra on 15-03-2016.
 */
public class VisitEditLogRequest
{
    public VisitEditLogRequest(Integer appointmentId, int typeOfLog) {
        this.appointmentId = appointmentId;
        this.typeOfLog = typeOfLog;
    }

    //{"appointmentId": 585,"typeOfLog":4}
    private Integer appointmentId;

    public int getTypeOfLog() {
        return typeOfLog;
    }

    public void setTypeOfLog(int typeOfLog) {
        this.typeOfLog = typeOfLog;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    private int typeOfLog;
}
