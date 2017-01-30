package com.medico.model;

/**
 * Created by Narendra on 03-02-2016.
 */
public class DoctorId
{
    public DoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    private String doctorId;
}
