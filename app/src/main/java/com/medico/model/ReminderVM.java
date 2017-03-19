package com.medico.model;

import java.util.List;

public class ReminderVM {
	
	public Long id;
	public Integer doctorId;
	public String patientId;
	public String appointmentDate;
	public String appointmentTime;
	public String medicinName;
	public String startDate;
	public String endDate;
	public Integer duration;
	public Integer numberOfDoses;
	public String schedule;
	public String doctorInstruction;
    public String ownerType;
	
	public String visitDate;
	public String visitType;
	public String referredBy;
	public String symptoms;
	public String diagnosis;
	public String medicinePrescribed;
	public String testsPrescribed;
	
	public List<AlarmReminderVM> alarmReminderVMList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public String getMedicinName() {
        return medicinName;
    }

    public void setMedicinName(String medicinName) {
        this.medicinName = medicinName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getNumberOfDoses() {
        return numberOfDoses;
    }

    public void setNumberOfDoses(Integer numberOfDoses) {
        this.numberOfDoses = numberOfDoses;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDoctorInstruction() {
        return doctorInstruction;
    }

    public void setDoctorInstruction(String doctorInstruction) {
        this.doctorInstruction = doctorInstruction;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicinePrescribed() {
        return medicinePrescribed;
    }

    public void setMedicinePrescribed(String medicinePrescribed) {
        this.medicinePrescribed = medicinePrescribed;
    }

    public String getTestsPrescribed() {
        return testsPrescribed;
    }

    public void setTestsPrescribed(String testsPrescribed) {
        this.testsPrescribed = testsPrescribed;
    }

    public List<AlarmReminderVM> getAlarmReminderVMList() {
        return alarmReminderVMList;
    }

    public void setAlarmReminderVMList(List<AlarmReminderVM> alarmReminderVMList) {
        this.alarmReminderVMList = alarmReminderVMList;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
