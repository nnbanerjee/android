package com.medico.model;

/**
 * Created by Narendra on 02-04-2016.
 */
public class PatientId {

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public PatientId(Integer patientId) {
        this.patientId = patientId;
    }

    private Integer patientId;
    private Integer page = 1;
    private Integer rows = 100;
}
