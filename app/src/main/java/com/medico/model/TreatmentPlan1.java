package com.medico.model;

import java.util.List;

/**
 * Created by mindnerves on 6/25/2015.
 */
public class TreatmentPlan1
{
    public Integer appointmentId;
    public Integer invoiceId;
    public Integer treatmentId;
    public Integer categoryId;
    public Integer parentId;
    public String templateName;
    public String templateSubName;
    public String icon;
    public Integer doctorId;
    public Integer patientId;
    public List<TreatmentField> treatmentFields;

    public boolean isChanged = false;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateSubName() {
        return templateSubName;
    }

    public void setTemplateSubName(String templateSubName) {
        this.templateSubName = templateSubName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;

        if(appointmentId == null || appointmentId.intValue() >= 0 == false)
            canBeSaved = false;
        else if(invoiceId == null || invoiceId.intValue() > 0 == false)
            canBeSaved = false;
        else if(categoryId == null || categoryId.intValue() > 0 == false)
            canBeSaved = false;
        if(patientId == null || patientId.intValue() >= 0 == false)
            canBeSaved = false;
        else if(invoiceId == null || invoiceId.intValue() > 0 == false)
            canBeSaved = false;
        else if(categoryId == null || categoryId.intValue() > 0 == false)
            canBeSaved = false;
        else if(templateName == null || templateName.trim().length() > 0 == false )
            canBeSaved = false;
        else if(templateSubName == null || templateSubName.trim().length() > 0 == false )
            canBeSaved = false;
        else if(patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if(doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if(treatmentFields == null || treatmentFields.size() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }


    public static class TreatmentField
    {
        public Integer treatmentAttributeId;
        public Integer fieldId;
        public String fieldName;
        public String fieldDisplayName;
        public String value;
        public Integer treatmentId;

    }

    public List<TreatmentField> getTreatmentField()
    {
        return treatmentFields;
    }
    public void setTreatmentFields(List<TreatmentField> treatmentFields)
    {
        if(this.treatmentFields == null || this.treatmentFields.equals(treatmentFields) == false) {
            this.treatmentFields = treatmentFields;
            isChanged = true;
        }
    }

}
