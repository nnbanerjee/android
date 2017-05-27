package com.medicohealthcare.model;

import com.medicohealthcare.util.PARAM;

/**
 * Created by Narendra on 21-03-2017.
 */

public class DependentDelegatePerson extends Person
{
    public Integer primePersonId;
    public Byte accessLevel;
    public String relation;
    public Byte delegationStatus;
    public Byte type;
    public DependentDelegatePerson()
    {

    }
    public DependentDelegatePerson(Person person)
    {
        super(person);
        id = null;
        role = PARAM.PATIENT;
        status = PARAM.UNREGISTERED;
        addedBy = person.getId();
        prime = 0;
        primePersonId = person.getId();
        accessLevel = 1;
        relation = "self";
        delegationStatus = 1;
        type = 0;
    }

}
