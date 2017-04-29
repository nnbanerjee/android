package com.medico.model;

/**
 * Created by Narendra on 03-04-2017.
 */

public class SearchParameterRequest
{
    public Double lattitude;
    public Double longitude;
    public Double radius = 10.0;
    public String daysOfWeek;
    public String gender = "M";
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
    public int role = 0;
    public Integer loginUserId;
}
