package com.medico.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Model.Shift1;
import Model.Shift2;
import Model.Shift3;
import Model.appointments;

/**
 * Created by User on 23-02-2015.
 */
public class Clinic implements Serializable {

    private String clinicName;
    private boolean selected;
    private String landLineNumber;
    private String mobile;
    private String location;
    private String email;
    private String idClinic;
    private String doctorId;
    public String bookDate;
    public String bookTime;
    public String shift;
    public String lastVisited;
    public Shift1 shift1;
    public Shift2 shift2;
    public Shift3 shift3;
    public String onlineAppointment;
    public String speciality;
    public String totalAppointmentCount;
    public String doctorEmail;
    String parameter = "null";
    public String appointmentType;
    public String appointmentDate;
    public String appointmentTime;
    public Boolean alarmFlag = false;
    public Boolean upcomingFlag = false;
    public String star;
    public String reviews;
    public String type;
    public String about;
    public String description;
    public String service;
    public String lastVisitedTime;
    public String timing;

    public String address;
    public String imageURL;
    public String searchNavigation;
    public String country;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String imageUrl;

    public String getDoctorClinicId() {
        return doctorClinicId;
    }

    public void setDoctorClinicId(String doctorClinicId) {
        this.doctorClinicId = doctorClinicId;
    }

    private String doctorClinicId;

    /**
     * @return The appointments
     */
    public List<appointments> getAppointments() {
        return appointmentsObj;
    }

    /**
     * @param appointmentsObj The appointmentsObj
     */
    public void setAppointments(List<appointments> appointmentsObj) {
        this.appointmentsObj = appointmentsObj;
    }

    private List<appointments> appointmentsObj = new ArrayList<appointments>();

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTotalAppointmentCount() {
        return totalAppointmentCount;
    }

    public void setTotalAppointmentCount(String totalAppointmentCount) {
        this.totalAppointmentCount = totalAppointmentCount;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Boolean getUpcomingFlag() {
        return upcomingFlag;
    }

    public void setUpcomingFlag(Boolean upcomingFlag) {
        this.upcomingFlag = upcomingFlag;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLastVisitedTime() {
        return lastVisitedTime;
    }

    public void setLastVisitedTime(String lastVisitedTime) {
        this.lastVisitedTime = lastVisitedTime;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSearchNavigation() {
        return searchNavigation;
    }

    public void setSearchNavigation(String searchNavigation) {
        this.searchNavigation = searchNavigation;
    }


    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIdClinic() {
        return idClinic;
    }

    public void setIdClinic(String idClinic) {
        this.idClinic = idClinic;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public Shift1 getShift1() {
        return shift1;
    }

    public void setShift1(Shift1 shift1) {
        this.shift1 = shift1;
    }

    public Shift2 getShift2() {
        return shift2;
    }

    public void setShift2(Shift2 shift2) {
        this.shift2 = shift2;
    }

    public Shift3 getShift3() {
        return shift3;
    }

    public void setShift3(Shift3 shift3) {
        this.shift3 = shift3;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getOnlineAppointment() {
        return onlineAppointment;
    }

    public void setOnlineAppointment(String onlineAppointment) {
        this.onlineAppointment = onlineAppointment;
    }

    public Boolean getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(Boolean alarmFlag) {
        this.alarmFlag = alarmFlag;
    }
}
