package com.medico.model;

/**
 * Created by Narendra on 18-02-2016.
 */
public class DoctorIdPatientId {

    public DoctorIdPatientId(Integer doctorId, Integer patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    private Integer doctorId;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    private Integer patientId;
    //{"patientId":3,"doctorId":1}
}
