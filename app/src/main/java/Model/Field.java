package Model;

/**
 * Created by MNT on 28-Mar-15.
 */
public class Field {

    private String fieldId;
    private String templateId;
    private String fieldDisplayName;
    private String fieldName;
    private String fieldType;
    private String fieldDefaultValue;
    private String doctorId;
    private String patientId;
    private Boolean selected = false;

    public  Field (){ }

    public Field(String fieldId, String templateId,String fieldDisplayName, String fieldName, String fieldType, String fieldDefaultValue){
        this.fieldId = fieldId;
        this.templateId = templateId;
        this.fieldDisplayName = fieldDisplayName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public String getFieldDefaultValue() {
        return fieldDefaultValue;
    }

    public void setFieldDefaultValue(String fieldDefaultValue) {
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
