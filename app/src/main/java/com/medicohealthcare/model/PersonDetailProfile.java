package com.medicohealthcare.model;

/**
 * Created by Narendra on 15-03-2017.
 */

public class PersonDetailProfile
{
    public Integer personId;
    public String awards;
    public String briefDescription;
    public String experience;
    public String institution;
    public String memberOf;
    public String qualification;
    public String registrationNo;
    public String searchNavigation;
    public String services;
    public String practiceName;
    public String specialization;
    public String uploadFileUrl;
    public Double ratings;
    public Integer reviews;
    public Integer recommendations;
    public Byte clinics;
    public Integer numberOfServices;
    public Long dateOfDegreeRegistration;
    boolean isChanged = true;

    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(personId == null || personId.intValue() > 0 == false)
            canBeSaved = false;
        else if(registrationNo == null )
            canBeSaved = false;
        else if(dateOfDegreeRegistration == null || dateOfDegreeRegistration.longValue() > 0 == false)
            canBeSaved = false;
        else if(registrationNo == null || registrationNo.trim().length() > 0 == false)
            canBeSaved = false;
        else if(practiceName == null || practiceName.trim().length() > 0 == false)
            canBeSaved = false;
        else if(uploadFileUrl == null || uploadFileUrl.trim().length() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
}
