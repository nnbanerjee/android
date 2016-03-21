package Model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class ResponseCodeVerfication {
    public ResponseCodeVerfication(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    private String appointmentId;
}
