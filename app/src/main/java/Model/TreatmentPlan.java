package Model;

import java.util.ArrayList;

/**
 * Created by mindnerves on 6/25/2015.
 */
public class TreatmentPlan {

    private Integer Id;
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
    }
}
