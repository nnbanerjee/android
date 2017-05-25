package com.medicohealthcare.model;

/**
 * Created by Narendra on 29-04-2017.
 */

public class ClinicPersonRequest
{
    public Integer personId;
    public Integer clinicId;
    public ClinicPersonRequest(Integer clinicId, Integer personId)
    {
        this.personId = personId;
        this.clinicId = clinicId;
    }
}
