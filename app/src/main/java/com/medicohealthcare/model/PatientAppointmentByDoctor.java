package com.medicohealthcare.model;

import java.util.List;


public class PatientAppointmentByDoctor {
	
	public Integer doctorId;
	public Integer patientId;
	public String name;
	public List<ClinicAppointment> clinicList;
	
	public class ClinicAppointment {
		public Integer idClinic;
		public Integer doctorClinicId;
		public Byte slotNumber;
		public List<Appointments> appointments;


	}

	public class Appointments {
		public Integer appointmentId;
		public Long dateTime;
		public Byte visitType;
		public Integer sequenceNumber;
		public Byte appointmentStatus;
	}

	

}
