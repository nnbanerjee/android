package Model;

/**
 * Created by Narendra on 15-03-2016.
 */
public class VisitEditLogRequest
{
    public VisitEditLogRequest(String appointmentId, int typeOfLog) {
        this.appointmentId = appointmentId;
        this.typeOfLog = typeOfLog;
    }

    //{"appointmentId": 585,"typeOfLog":4}
    private String appointmentId;

    public int getTypeOfLog() {
        return typeOfLog;
    }

    public void setTypeOfLog(int typeOfLog) {
        this.typeOfLog = typeOfLog;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    private int typeOfLog;
}
