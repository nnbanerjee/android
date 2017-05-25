package com.medicohealthcare.model;

/**
 * Created by Narendra on 16-02-2017.
 */

public class Clinic1
{
    public String clinicName;
    public Long landLineNumber;
    public Long mobile;
    public String address;
    public String location;
    public String email;
    public String doctorId;
    public String speciality;
    public Integer idClinic;
    public Integer type;
    public String about;
    public String description;
    public String service;
    public String timing;
    public String imageUrl;
    public String searchNavigation;
    public String country;
    public String isoCountry;
    public Integer addedBy;
    public Double locationLat;
    public Double locationLong;
    public String city;
    public Integer rating;
    public Integer reviews;
    public Integer recommendations;
    public Integer numberOfDoctors;
    public Integer numberOfServices;
    public Byte linkedWith;

    private boolean isChanged = true;

    @Override
    public String toString()
    {
        return clinicName;
    }

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
        if(clinicName == null )
            canBeSaved = false;
        else if(mobile == null || mobile.longValue() > 0 == false)
            canBeSaved = false;
        else if(locationLat == null || locationLat.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(locationLong == null || locationLong.doubleValue() > 0 == false)
            canBeSaved = false;
        else if(email == null )
            canBeSaved = false;
        else if(address == null )
            canBeSaved = false;
        else if(type == null || type.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(speciality == null && speciality.trim().length() > 0 == false)
            canBeSaved = false;
        else if(city == null || city.trim().length() > 0 == false)
            canBeSaved = false;
        else if(country == null || country.trim().length() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
}
