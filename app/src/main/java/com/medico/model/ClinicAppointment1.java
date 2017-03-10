package com.medico.model;

import java.util.List;

/**
 * Created by Narendra on 09-03-2017.
 */

public class ClinicAppointment1
{
    public Integer idClinic;
    public Integer doctorClinicId;
    public Byte slotNumber;
    public List<Appointments> appointments;

    public class Appointments {
        public Integer appointmentId;
        public Long dateTime;
        public Byte visitType;
        public Integer sequenceNumber;
        public Byte appointmentStatus;
    }
}
