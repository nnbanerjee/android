package com.medicohealthcare.model;

/**
 * Created by Narendra on 11-03-2017.
 */

public class DoctorHoliday
{
    public Integer doctorHolidayId;
    public Integer idHoliday;
    public Long startDate;
    public Long endDate;
    public Integer sequenceNo;
    public Byte status;
    public Integer clinicId;
    public Integer doctorClinicId;
    public Integer doctorId;
    public Byte type;

    public DoctorHoliday(Integer doctorId, Long startDate, Long endDate, Byte type)
    {
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }
    public DoctorHoliday(Integer doctorId, Long startDate, Long endDate, Byte type, Integer sequenceNo,Integer clinicId, Integer doctorClinicId)
    {
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.sequenceNo = sequenceNo;
        this.clinicId = clinicId;
        this.doctorClinicId = doctorClinicId;
        this.status = 1;
    }
    public DoctorHoliday(Integer idHoliday, Integer sequenceNo)
    {
        this.doctorHolidayId = idHoliday;
        this.idHoliday = idHoliday;
        this.sequenceNo = sequenceNo;
    }
    public DoctorHoliday()
    {

    }
}
