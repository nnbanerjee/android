package com.medico.model;

/**
 * Created by Narendra on 03-04-2017.
 */

public class SearchParameterRequest
{
    public Double lat;
    public Double lon;
    public Double radius;
    public String daysOfWeek;
    public String gender;
    public Long timeToStart;
    public Long timeToEnd;
    public String speciality;
    public String country;
    public Integer personId;
    public String emailAddr;
    public String mobileNum;
    public String personName;
    public String city;
    public Integer reqId;
    public int page = 1;
    public int rows = 10;
}
