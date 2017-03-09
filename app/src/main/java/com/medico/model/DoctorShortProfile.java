package com.medico.model;

/**
 * Created by Narendra on 27-01-2017.
 */

public class DoctorShortProfile
{
    public Integer doctorId;
    public String name;
    public String profession;
    public String address;
    public String imageUrl;
    public Long lastVisit;
    public Long upcomingVisit;
    public Integer numberOfVisits;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Long lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Long getUpcomingVisit()
    {
        return upcomingVisit;
    }

    public void setUpcomingVisit(Long upcomingVisit) {
        this.upcomingVisit = upcomingVisit;
    }

    public Integer getNumberOfVisits() {
        return numberOfVisits;
    }

    public void setNumberOfVisits(Integer numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }
}
