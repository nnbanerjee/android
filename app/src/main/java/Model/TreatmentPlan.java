package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mindnerves on 6/25/2015.
 */
public class TreatmentPlan {
    private String appointmentId;
    private String invoiceId;
    private String treatmentId;
    private String categoryId;
    private String parentId;
    private String templateName;
    private String templateSubName;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
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

    public List<TreatmentField> getTreatmentFields() {
        return treatmentFields;
    }

    public void setTreatmentFields(List<TreatmentField> treatmentFields) {
        this.treatmentFields = treatmentFields;
    }

    private String icon;
    private List<TreatmentField> treatmentFields = new ArrayList<TreatmentField>();

    ///Old
  /*  private Integer Id;
    private String doctorId;
    private String procedureId;
    private String templateId;
    private String patientId;
    private String patientAppointmentDate;
    private String patientAppointmentTime;
    private ArrayList<Field> fieldArrayList;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientAppointmentDate() {
        return patientAppointmentDate;
    }

    public void setPatientAppointmentDate(String patientAppointmentDate) {
        this.patientAppointmentDate = patientAppointmentDate;
    }

    public String getPatientAppointmentTime() {
        return patientAppointmentTime;
    }

    public void setPatientAppointmentTime(String patientAppointmentTime) {
        this.patientAppointmentTime = patientAppointmentTime;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    public ArrayList<Field> getFieldArrayList() {
        return fieldArrayList;
    }

    public void setFieldArrayList(ArrayList<Field> fieldArrayList) {
        this.fieldArrayList = fieldArrayList;
    }*/
}
