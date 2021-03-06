package com.medicohealthcare.model;

/**
 * Created by Narendra on 03-04-2017.
 */

public class SearchParameterRequest
{
    public Double lattitude;
    public Double longitude;
    public Double radius = 100.0;
    public String daysOfWeek;
    public Integer gender = -1;
    public Long timeToStart;
    public Long timeToEnd;
    public String speciality;
    public String country;
    public Integer personId;
    public Integer clinicId;
    public String emailAddr;
    public String mobileNum;
    public String personName;
    public String clinicName;
    public String countryName;
    public String city;
    public int page = 1;
    public int rows = 100;
    public int role = 0;
    public Integer loginUserId;
    public Integer loggedinUserId;

    public boolean isValid()
    {
        boolean canBeSaved = true;
        if(lattitude == null || lattitude.intValue() >= 0 == false)
            canBeSaved = false;
        else if(longitude == null || longitude.intValue() > 0 == false)
            canBeSaved = false;
        return canBeSaved;
    }
}
