package com.medicohealthcare.model;

import java.util.List;

/**
 * Created by Narendra on 01-05-2017.
 */

public class DoctorSearch
{
    public Person person;
    public Clinic1 clinic;
    public List<DoctorClinic> slots;

    public DoctorSearch(Person person, Clinic1 clinic, List<DoctorClinic> slots)
    {
        this.person = person;
        this.clinic = clinic;
        this.slots = slots;
    }
}
