package com.medico.model;

import java.util.List;

/**
 * Created by Narendra on 05-02-2017.
 */

public class PatientMedicine
{

    public Integer medicineId;

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedicinName() {
        return medicinName;
    }

    public void setMedicinName(String medicinName) {
        this.medicinName = medicinName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Byte getSchedule() {
        return schedule;
    }

    public void setSchedule(Byte schedule) {
        this.schedule = schedule;
    }

    public Integer getDosesPerSchedule() {
        return dosesPerSchedule;
    }

    public void setDosesPerSchedule(Integer dosesPerSchedule) {
        this.dosesPerSchedule = dosesPerSchedule;
    }

    public Integer getDurationSchedule() {
        return durationSchedule;
    }

    public void setDurationSchedule(Integer durationSchedule) {
        this.durationSchedule = durationSchedule;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Byte getReminder() {
        return reminder;
    }

    public void setReminder(Byte reminder) {
        this.reminder = reminder;
    }

    public Byte getAutoSchedule() {
        return autoSchedule;
    }

    public void setAutoSchedule(Byte autoSchedule) {
        this.autoSchedule = autoSchedule;
    }

    public String getDoctorInstruction() {
        return doctorInstruction;
    }

    public void setDoctorInstruction(String doctorInstruction) {
        this.doctorInstruction = doctorInstruction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<MedicineSchedule> getMedicineSchedule() {
        return medicineSchedule;
    }

    public void setMedicineSchedule(List<MedicineSchedule> medicineSchedule) {
        this.medicineSchedule = medicineSchedule;
    }

    public Integer patientId;
    public Integer doctorId;
    public Integer loggedinUserId;
    public Integer appointmentId;
    public String medicinName;
    public String doctorName;
    public Byte schedule;
    public Integer dosesPerSchedule;
    public Integer durationSchedule;
    public Long startDate;
    public Long endDate;

    public Byte reminder;
    public Byte autoSchedule;

    public String doctorInstruction;

    public Integer type;

    //public int userType;
    public List<MedicineSchedule> medicineSchedule;

    public PatientMedicine()
    {

    }

    public static class MedicineSchedule {
        public Long scheduleTime;
        public MedicineSchedule(Long time)
        {
            scheduleTime = time;
        }
    }


}
