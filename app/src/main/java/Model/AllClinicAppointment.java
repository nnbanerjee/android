package Model;

import java.util.ArrayList;
import java.util.List;


public class AllClinicAppointment {
	
	public Integer doctorId;
	public Integer patientId;
	public Integer clinicId;
	public String appointmentDate;
	
	public List<ShiftAppointment> shift1 = new ArrayList<ShiftAppointment>();
	public List<ShiftAppointment> shift2 = new ArrayList<ShiftAppointment>();
	public List<ShiftAppointment> shift3 = new ArrayList<ShiftAppointment>();
	
}
