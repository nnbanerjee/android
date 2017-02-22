package com.medico.model;

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

    @Override
    public String toString()
    {
        return clinicName;
    }
}
