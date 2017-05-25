package com.medicohealthcare.model;

/**
 * Created by Narendra on 27-01-2017.
 */
import java.util.List;

public class DoctorProfileList
{
    private List<DoctorShortProfile> doctorList;

    public DoctorProfileList(List<DoctorShortProfile> doctorList)
    {
        this.doctorList = doctorList;
    }

    public List<DoctorShortProfile> getDoctorList() {
        return doctorList;
    }



    public void setDoctorList(List<DoctorShortProfile> doctorList)
    {
        this.doctorList = doctorList;
    }



}
