package com.medico.model;

/**
 * Created by Narendra on 29-02-2016.
 */
public class SummaryRequest {
    public SummaryRequest(String patientId, String appointmentId) {
        this.patientId = patientId;
        this.appointmentId = appointmentId;
    }

    //{"appointmentId":1, "patientId":3}
    private String patientId;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    private String appointmentId;
}
