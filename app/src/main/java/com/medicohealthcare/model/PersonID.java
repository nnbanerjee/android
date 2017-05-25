package com.medicohealthcare.model;

/**
 * Created by Narendra on 17-03-2016.
 */
public class PersonID {
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public PersonID(Integer personId) {
        this.personId = personId;
    }

    private Integer personId;
}
