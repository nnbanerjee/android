package com.medico.model;

/**
 * Created by Narendra on 27-01-2017.
 */
import java.util.List;

public class PatientProfileList
{
    private List<PatientShortProfile> patientlist;

    public PatientProfileList(List<PatientShortProfile> patientList)
    {
        patientlist = patientList;
    }
    public List<PatientShortProfile> getPatientlist() {
        return patientlist;
    }



    public void setPatientlist(List<PatientShortProfile> patientList) {
        this.patientlist = patientList;
    }



}
