package com.medico.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mindnerves on 6/25/2015.
 */
public class TreatmentPlan1 {
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

    public boolean isChanged = true;

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

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean canBeSaved() {
        boolean canBeSaved = true;

        if (appointmentId == null || appointmentId.intValue() >= 0 == false)
            canBeSaved = false;
        else if (categoryId == null || categoryId.intValue() > 0 == false)
            canBeSaved = false;
        if (patientId == null || patientId.intValue() >= 0 == false)
            canBeSaved = false;
        else if (templateName == null || templateName.trim().length() > 0 == false)
            canBeSaved = false;
        else if (templateSubName == null || templateSubName.trim().length() > 0 == false)
            canBeSaved = false;
        else if (patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if (doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if (treatmentFields == null || treatmentFields.size() > 0 == false)
            canBeSaved = false;
        else if(treatmentId != null && invoiceId == null)
            canBeSaved = false;

        return canBeSaved;
    }


    public static class TreatmentField {
        public Integer treatmentAttributeId;
        public Integer fieldId;
        public String fieldName;
        public String fieldDisplayName;
        public String value;
        public Integer treatmentId;

        public void setField(String fieldValue) {
            value = fieldValue;
        }
        public TreatmentField(CustomProcedureTemplate1.TemplateField field)
        {
            fieldId = field.fieldId;
            fieldName = field.fieldName;
            fieldDisplayName = field.fieldName;
            value = field.value;
        }

    }

    public List<TreatmentField> getTreatmentField() {
        return treatmentFields;
    }

    public void setTreatmentFields(List<TreatmentField> treatmentFields) {
        if (this.treatmentFields == null || this.treatmentFields.equals(treatmentFields) == false) {
            this.treatmentFields = treatmentFields;
            isChanged = true;
        }
    }

    public TreatmentField getField(int field) {
        for (TreatmentField field1 : this.treatmentFields) {
            if (field1.fieldId.intValue() == field)
                return field1;
        }
        return null;
    }

    public void setField(int fieldId, String fieldValue) {
        for (TreatmentField field1 : this.treatmentFields) {
            if (field1.fieldId.intValue() == fieldId) {
                field1.setField(fieldValue);
                break;
            }
        }
    }

    public TreatmentPlan1()
    {

    }
    public TreatmentPlan1(CustomProcedureTemplate1 plan)
    {
        this.categoryId = plan.categoryId;
        this.parentId = plan.parentId;
        this.templateName = plan.templateName;
        this.templateSubName = plan.templateSubName;
        this.icon = plan.icon;
        this.treatmentFields = new ArrayList<>();
        for(CustomProcedureTemplate1.TemplateField field : plan.getTemplateFields())
        {
            treatmentFields.add(new TreatmentPlan1.TreatmentField(field));
        }
    }

}
