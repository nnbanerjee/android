package com.medico.model;

/**
 * Created by Narendra on 23-03-2017.
 */

public class DoctorClinicRequest
{
    Integer doctorId;
    Integer clinicId;

    public DoctorClinicRequest(Integer doctorId, Integer clinicId)
    {
        this.doctorId = doctorId;
        this.clinicId = clinicId;
    }
}
