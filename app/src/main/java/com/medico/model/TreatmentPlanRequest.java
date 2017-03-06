package com.medico.model;

/**
 * Created by Narendra on 06-03-2017.
 */

public class TreatmentPlanRequest
{
    Integer appointmentId;
    Integer categoryId;

    public TreatmentPlanRequest(Integer appointmentId, Integer categoryId)
    {
        this.appointmentId = appointmentId;
        this.categoryId = categoryId;
    }
}
