package com.medicohealthcare.model;

/**
 * Created by Narendra on 03-02-2016.
 */
public class DoctorId1
{
    public DoctorId1(Integer doctorId)
    {
        this.doctorId = doctorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    private Integer doctorId;
}
