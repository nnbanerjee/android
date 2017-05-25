package com.medicohealthcare.model;

import java.io.Serializable;

/**
 * Created by MNT on 17-Feb-15.
 */
public class DoctorSearchResponse implements Serializable {

    public String idPerson;
    public String name;
    public String email;
    public String mobileNumber;
    public String location;
    boolean selected = false;
    public String speciality;
    private String doctorId;
    private String dateOfBirth;
    private String gender;
    private String blood_group;
    private String allergic_to;
    private String type;
    public String emailID;
    public String bookDate;
    public String bookTime;
    public String shift;
    public Integer clinicId;
    public String lastVisited;
    public String star;
    public String reviews;
    public String lastVisitedTime;
    public String previousClinicId;
    public String lastVisitedClinicId;
    public String lastVisitedShift;

    public String getPreviousAppointment() {
        return previousAppointment;
    }

    public void setPreviousAppointment(String previousAppointment) {
        this.previousAppointment = previousAppointment;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String previousAppointment;
    public String previousDate;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getAllergic_to() {
        return allergic_to;
    }

    public void setAllergic_to(String allergic_to) {
        this.allergic_to = allergic_to;
    }

}
