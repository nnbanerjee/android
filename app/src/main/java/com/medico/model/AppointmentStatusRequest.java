package com.medico.model;

/**
 * Created by Narendra on 07-04-2017.
 */

public class AppointmentStatusRequest
{
    public static int APPOINTMENT_STATUS = 0;
    public static int VISIT_TYPE = 1;
    public static int VISIT_STATUS = 2;

    public Integer appointmentId;
    public Integer appointmentStatus;
    public Integer visitType;
    public Integer appointmentVisitStatus;
    public AppointmentStatusRequest(Integer appointmentId, Integer param,int type)
    {
        this.appointmentId = appointmentId;
        switch(type)
        {

            case 0:
                this.appointmentStatus = param;
                break;
            case 1:
                this.visitType = param;
                break;
            case 2:
                this.appointmentVisitStatus = param;
                break;
        }

    }

}
