package com.medico.model;

import java.util.List;

import com.medico.util.PARAM;

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

    private transient boolean isChanged = false;

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

    public void setMedicinName(String medicinName)
    {
        if(this.medicinName == null || this.medicinName.equals(medicinName) == false) {
            this.medicinName = medicinName;
            isChanged = true;
        }
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
    public void setSchedule(Byte schedule)
    {
        if(this.schedule == null || this.schedule.equals(schedule) == false) {
            this.schedule = schedule;
            isChanged = true;
        }

    }

    public Integer getDosesPerSchedule() {
        return dosesPerSchedule;
    }

    public void setDosesPerSchedule(Integer dosesPerSchedule)
    {
        if(this.dosesPerSchedule==null || this.dosesPerSchedule.equals(dosesPerSchedule) == false) {
            this.dosesPerSchedule = dosesPerSchedule;
            isChanged = true;
        }
    }

    public Integer getDurationSchedule() {
        return durationSchedule;
    }

    public void setDurationSchedule(Integer durationSchedule)
    {
        if(this.durationSchedule==null||this.durationSchedule == null || this.durationSchedule.equals(durationSchedule) == false) {
            this.durationSchedule = durationSchedule;
            isChanged = true;
        }
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate)
    {
        if(this.startDate==null || this.startDate.equals(startDate) == false) {
            this.startDate = startDate;
            isChanged = true;
        }
    }

    public Long getEndDate()
    {
        endDate = startDate + (dosesPerSchedule * durationSchedule -1) * durationBetweenDoses();
        return endDate;
    }

    public void setEndDate(Long endDate)
    {
        if(this.endDate == null || this.endDate.equals(endDate) == false) {
            this.endDate = endDate;
            isChanged = true;
        }
    }

    public Byte getReminder() {
        return reminder;
    }

    public void setReminder(Byte reminder)
    {
        if(this.reminder==null || this.reminder.equals(reminder) == false) {
            this.reminder = reminder;
            isChanged = true;
        }
    }

    public Byte getAutoSchedule() {
        return autoSchedule;
    }

    public void setAutoSchedule(Byte autoSchedule)
    {
        if(this.autoSchedule==null || this.autoSchedule.equals(autoSchedule) == false) {
            this.autoSchedule = autoSchedule;
            isChanged = true;
        }
    }

    public String getDoctorInstruction() {
        return doctorInstruction;
    }

    public void setDoctorInstruction(String doctorInstruction)
    {
        if(this.doctorInstruction==null || this.doctorInstruction.equals(doctorInstruction) == false) {
            this.doctorInstruction = doctorInstruction;
            isChanged = true;
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type)
    {
        if(this.type==null || this.type.equals(type) == false) {
            this.type = type;
            isChanged = true;
        }
    }

    public List<MedicineSchedule> getMedicineSchedule() {
        return medicineSchedule;
    }

    public void setMedicineSchedule(List<MedicineSchedule> medicineSchedule)
    {
        if(this.medicineSchedule==null || this.medicineSchedule.equals(medicineSchedule) == false) {
            this.medicineSchedule = medicineSchedule;
            isChanged = true;
        }
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

    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(schedule == null || schedule.intValue() >= 0 == false)
            canBeSaved = false;
        else if(dosesPerSchedule == null || dosesPerSchedule.intValue() > 0 == false)
            canBeSaved = false;
        else if(durationSchedule == null || durationSchedule.intValue() > 0 == false)
            canBeSaved = false;
        else if(medicinName == null || medicinName.trim().length() > 0 == false )
            canBeSaved = false;
        else if(startDate == null || startDate.longValue() > 0 == false)
            canBeSaved = false;
        else if(endDate == null || endDate.longValue() > 0 == false)
            canBeSaved = false;
        else if(reminder == null || reminder.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(autoSchedule == null || autoSchedule.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if(doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if(loggedinUserId == null || loggedinUserId.intValue() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }

}
