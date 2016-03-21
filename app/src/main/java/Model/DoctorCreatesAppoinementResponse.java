package Model;

/**
 * Created by Narendra on 26-02-2016.
 */
public class DoctorCreatesAppoinementResponse {
    public DoctorCreatesAppoinementResponse(String status, String appointmentId) {
        this.status = status;
        this.appointmentId = appointmentId;
    }

    //{"status":1,"appointmentId":12}
    private String status;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String appointmentId;
}