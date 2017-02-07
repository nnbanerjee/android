package com.medico.model;

import java.util.List;

import Utils.PARAM;

/**
 * Created by Narendra on 05-02-2017.
 */

public class PatientMedicine
{

    private Integer medicineId;
    private Integer patientId;
    private Integer doctorId;
    private Integer loggedinUserId;
    private Integer appointmentId;
    private String medicinName;
    private String doctorName;
    private Byte schedule;
    private Integer dosesPerSchedule;
    private Integer durationSchedule;
    private Long startDate;
    private Long endDate;
    private Byte reminder;
    private Byte autoSchedule;

    private String doctorInstruction;

    private Integer type;

    //public int userType;
    private List<MedicineSchedule> medicineSchedule;

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

    public Long getEndDate()
    {
        endDate = startDate + (dosesPerSchedule * durationSchedule -1) * durationBetweenDoses();
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
    public long[] getAlarmSchedule()
    {
        if(autoSchedule == 1 || medicineSchedule == null || medicineSchedule.size() == 0)
        {
            long startTime = getStartDate();
            long[] schedule = new long[dosesPerSchedule * getDurationSchedule()];
            for (int i = 0; i < schedule.length; i++) {
                schedule[i] = startTime + i * durationBetweenDoses();
            }
            return schedule;
        }
        else
        {
            long[] schedule = new long[medicineSchedule.size()];
            int i = 0;
            for(MedicineSchedule datetime: medicineSchedule)
            {
                schedule[i] = datetime.scheduleTime;
                i++;
            }
            return schedule;
        }
    }


    public long durationBetweenDoses()
    {
        //calculate the duration between doses
        long durationBetweenDoses = 24 * 60 * 60 * 1000 / getDosesPerSchedule();
        switch (schedule)
        {
            case PARAM.DAY:
                break;
            case PARAM.WEEK:
                durationBetweenDoses = 7 * durationBetweenDoses;
                break;
            case PARAM.MONTH:
                durationBetweenDoses = 30 * durationBetweenDoses;
                break;
            case PARAM.YEAR:
                durationBetweenDoses = 365 * durationBetweenDoses;
                break;
        }
        return durationBetweenDoses;
    }

}
