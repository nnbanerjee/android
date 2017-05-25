package com.medicohealthcare.model;

/**
 * Created by Narendra on 31-03-2017.
 */

public class ClinicByDoctorRequest
{
    public Integer doctorId;
    public Integer clinicId;

    public ClinicByDoctorRequest(Integer doctorId, Integer clinicId)
    {
        this.doctorId = doctorId;
        this.clinicId = clinicId;
    }
}
