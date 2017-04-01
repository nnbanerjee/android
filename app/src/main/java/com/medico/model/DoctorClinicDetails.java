package com.medico.model;

import java.util.List;

/**
 * Created by Narendra on 09-03-2017.
 */

public class DoctorClinicDetails
{
    public Clinic1 clinic;
    public List<AppointmentCounts> datecounts;
    public List<ClinicSlots> slots;

    public class ClinicSlots
    {
        public Byte slotNumber;
        public String name;
        public String daysOfWeek;
        public Long startTime;
        public Long endTime;
        //public Integer numberOfAppointmentsForToday;
        public Integer 	doctorClinicId;
        public Byte autoConfirm;
        public Byte availability;
        public Integer feeBooking;
        public Integer feesConsultation;
        public Integer percentageOverbook;
        public Byte slotType;
        public Integer visitDuration;
        public Integer numberOfPatients;
        //public Integer upComingAppointments;
        //public Integer todaysAppointments;
        public List<AppointmentCounts> counts;

    }
    public class AppointmentCounts
    {
        public Long date;
        public int counts;
    }

    public DoctorClinicDetails.ClinicSlots getSlot(Integer doctorClinicId)
    {
        for(ClinicSlots slot:slots)
        {
            if(slot.doctorClinicId.intValue() == doctorClinicId.intValue())
                return slot;
        }
        return null;
    }

}
