package com.medicohealthcare.model;

import java.util.List;

/**
 * Created by Narendra on 11-03-2017.
 */

public class DoctorSlotBookings
{

    public Long date;
    public Integer doctorClinicSlotId;
    public List<PersonBooking> bookings;

    public class PersonBooking
    {
        public Person patient;
        public Integer sequenceNo;
        public Byte isHoliday;
        public Byte visitType;
        public Long lastAppointment;
        public Long upcomingAppointments;
        public Integer appointmentId;
        public Byte appointmentStatus;
        public Byte visitStatus;
        public Integer numberOfVisits;

        public PersonBooking(DoctorAppointment appointment, Person patient)
        {
            this.patient = patient;
            sequenceNo = appointment.sequenceNumber;
            visitStatus = appointment.visitStatus;
            visitType = appointment.visitType;
//            lastAppointment
        }

    }


}
