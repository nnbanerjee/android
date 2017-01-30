package com.medico.model;

/**
 * Created by Narendra on 02-04-2016.
 */
public class PatientId {

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public PatientId(String patientId) {
        this.patientId = patientId;
    }

    private String patientId;
}
