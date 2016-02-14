package Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 23-02-2015.
 */
public class Clinic implements Serializable {

    private  String clinicName;
    private boolean selected;
    private String landLineNumber;
    private String mobileNumber;
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
