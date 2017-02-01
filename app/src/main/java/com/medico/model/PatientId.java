package com.medico.model;

/**
 * Created by Narendra on 02-04-2016.
 */
public class PatientId {

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public PatientId(int patientId) {
        this.patientId = patientId;
    }

    private int patientId;
}
