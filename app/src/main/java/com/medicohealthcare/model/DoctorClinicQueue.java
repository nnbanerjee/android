package com.medicohealthcare.model;

/**
 * Created by Narendra on 13-06-2017.
 */

public class DoctorClinicQueue extends ServerResponseStatus
{
    public Integer numberOfAppointments;
    public Integer queueStatus;
    public Integer doctorClinicId;
    public Byte autoConfirm;
    public Byte availability;
    public String daysOfWeek;
    public Integer feeBooking;
    public Integer feesConsultation;
    public Integer numberOfPatients;
    public Integer percentageOverbook;
    public String slotName;
    public Byte slotNumber;
    public Byte slotType;
    public Long timeToStart;
    public Long timeToStop;
    public Integer visitDuration;
    public Clinic1 clinic;
    public Person doctor;
    public String clinicName;
    public Person assistant;
}
