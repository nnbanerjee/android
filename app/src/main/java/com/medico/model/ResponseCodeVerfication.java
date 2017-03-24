package com.medico.model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class ResponseCodeVerfication
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

    private Integer status;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    private Integer appointmentId;
}
