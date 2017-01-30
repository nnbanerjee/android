package com.medico.model;

/**
 * Created by Narendra on 27-01-2017.
 */

public class PatientShortProfile
{
    private Integer patientId;
    private String name;
    private String profession;
    private String address;
    private String imageUrl;
    private Long lastVisit;
    private Long upcomingVisit;
    private Integer numberOfVisits;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
