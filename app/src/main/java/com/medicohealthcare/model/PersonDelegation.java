package com.medicohealthcare.model;

/**
 * Created by Narendra on 07-07-2017.
 */

public class PersonDelegation
{
    public int delegatingPersonId;
    public int receivingPersonId;
    public int accessLevel;
    public String relation;
    public int type;
    public int status;

    public PersonDelegation(int delegatingPersonId, int receivingPersonId, int accessLevel, String relation, int status)
    {
        this.delegatingPersonId = delegatingPersonId;
        this.receivingPersonId = receivingPersonId;
        this.accessLevel = accessLevel;
        this.relation = relation;
        this.type = 1;
        this.status = status;
    }
}
