package com.medicohealthcare.model;

/**
 * Created by Narendra on 19-04-2016.
 */
public class CustomTemplateId {
    public Integer getCustomTemplateId() {
        return templateId;
    }

    public void setTreatmentId(Integer templateId) {
        this.templateId = templateId;
    }

    public CustomTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    private Integer templateId;

}
