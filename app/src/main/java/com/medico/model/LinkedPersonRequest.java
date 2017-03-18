package com.medico.model;

/**
 * Created by Narendra on 18-03-2017.
 */

public class LinkedPersonRequest
{
    Integer sourcePersonId;
    Integer role;

    public LinkedPersonRequest(Integer personId, Integer requestedPersonRole)
    {
        sourcePersonId = personId;
        role = requestedPersonRole;
    }
}
