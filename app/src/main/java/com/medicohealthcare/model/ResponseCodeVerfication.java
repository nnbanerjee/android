package com.medicohealthcare.model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class ResponseCodeVerfication extends ServerResponseStatus
{
    public ResponseCodeVerfication(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    private Integer appointmentId;
}
