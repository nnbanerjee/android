package com.medicohealthcare.model;

/**
 * Created by Narendra on 18-03-2017.
 */

public class LinkedPersonRequest
{
    Integer sourcePersonId;
    Integer targetPersonId;
    Integer role;

    public LinkedPersonRequest(Integer sourcePersonId, Integer targetPersonId,Integer requestedPersonRole )
    {
        this.sourcePersonId = sourcePersonId;
        this.targetPersonId = targetPersonId;
        role = requestedPersonRole;
    }
    public LinkedPersonRequest(Integer sourcePersonId, Integer requestedPersonRole )
    {
        this.sourcePersonId = sourcePersonId;
        role = requestedPersonRole;  
    }
}
