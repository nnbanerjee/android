package com.medicohealthcare.model;

/**
 * Created by Narendra on 18-03-2017.
 */

public class DependentDelegatePersonRequest
{
    Integer personId;
    Integer type;

    public DependentDelegatePersonRequest(Integer personId, Integer type)
    {
        this.personId = personId;
        this.type = type;
    }
}
