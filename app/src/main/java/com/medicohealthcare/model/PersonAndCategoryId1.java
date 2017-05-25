package com.medicohealthcare.model;

/**
 * Created by Narendra on 08-04-2016.
 */
public class PersonAndCategoryId1 {
    public PersonAndCategoryId1(Integer personId, Integer categoryId) {
        this.personId = personId;
        this.categoryId = categoryId;
    }

    private Integer personId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonI(Integer personId) {
        this.personId = personId;
    }

    private Integer categoryId;
}
