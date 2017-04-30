package com.medico.model;

import java.util.List;

public class PatientAppointmentsVM 
{
	public List<Appointments> upcomingAppointments;
	public List<Appointments> historicalAppointments;

	public static class Appointments
	{
		public Integer appointmentId;
		public Person doctor;
		public Integer patientId;
		public Integer clinicId;
		public Long appointmentDate;
		public Integer type;
		public Integer sequenceNumber;
		public Byte appointmentStatus;
		public Byte visitType;
		public Byte visitStatus;
		public Byte rating;
		public Integer nrOfVisits;
		public Integer doctorClinicId;
		public String referred_by;
		public String reviews;
		public Integer new_case_id;

	}
}
