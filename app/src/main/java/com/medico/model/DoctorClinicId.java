package com.medico.model;

/**
 * Created by Narendra on 11-03-2017.
 */

public class DoctorClinicId
{
    Integer doctorClinicId;
    Long fromDate, toDate;
    public DoctorClinicId(Integer doctorClinicId, Long fromDate, Long toDate)
    {
        this.doctorClinicId = doctorClinicId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    public DoctorClinicId(Integer doctorClinicId)
    {
        this.doctorClinicId = doctorClinicId;
    }
    public DoctorClinicId()
    {

    }
}
