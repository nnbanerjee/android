package com.medicohealthcare.model;

/**
 * Created by Narendra on 15-06-2017.
 */

public class DoctorClinicQueueStatus extends ServerResponseStatus
{
    public Integer doctorClinicId;
    public Integer queStatus;
    public Integer numberOfAppointments;
    public Integer numberOfPeopleBooked;
    public Integer numberOfPeopleCancelled;
    public Integer currentAppointmentNumber;
    public Integer numberOfPatientConsulted;
    public Integer numberOfPatientNotVisited;
    public Integer numberOfPatientReported;
    public Integer numberOfPatientConfirmed;
    public Integer numberOfPatientNearby;
    public Integer consultationOnHold;
}
