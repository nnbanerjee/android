package Model;

import java.util.List;


public class AllTreatmentPlanVm {
	public String id;
	public Integer doctorId;
	public Integer patientId;
	public String patientAppointmentDate;
	public String patientAppointmentTime;
    public String grandTotal;
    public String totalDue;
    public String total;

    public String discount;
    public String tax;
    public String advance;

	public List<AllProcedureVm> procedure;
	
}
