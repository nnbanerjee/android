package Model;

/**
 * Created by Narendra on 22-02-2016.
 */
public class DoctorClinicAppointments {
    //{"doctorId":"1","doctor_clinic_id":"5","appointmentDate":"1455906600000"}

    private String doctorId;
    private String appointmentDate;
    private String doctor_clinic_id;

    public DoctorClinicAppointments(String doctor_clinic_id, String fromDate, String toDate) {
        this.doctor_clinic_id = doctor_clinic_id;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    private String fromDate;

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    private String toDate;





    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctor_clinic_id() {
        return doctor_clinic_id;
    }

    public void setDoctor_clinic_id(String doctor_clinic_id) {
        this.doctor_clinic_id = doctor_clinic_id;
    }



}
