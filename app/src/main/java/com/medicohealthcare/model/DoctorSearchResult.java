package com.medicohealthcare.model;

import java.util.List;

/**
 * Created by Narendra on 01-05-2017.
 */

public class DoctorSearchResult
{
    public Clinic1 clinic;
    public List<PersonSlot> personSlotList;


    public static class PersonSlot
    {
        public Person person;
        public PersonDetailProfile personDetails;
        public List<DoctorClinic> slots;
    }
}
