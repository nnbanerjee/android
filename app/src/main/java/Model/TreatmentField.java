package Model;

/**
 * Created by Narendra on 19-03-2016.
 */
public class TreatmentField {

    private String fieldId;
    private String fieldName;
    private String value;

    public TreatmentField(String fieldId, String fieldName, String value, String treatmentId) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.value = value;
        this.treatmentId = treatmentId;
    }

    private String treatmentId;

    /**
     *
     * @return
     * The fieldId
     */
    public String getFieldId() {
        return fieldId;
    }

    /**
     *
     * @param fieldId
     * The fieldId
     */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    /**
     *
     * @return
     * The fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     *
     * @param fieldName
     * The fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The treatmentId
     */
    public String getTreatmentId() {
        return treatmentId;
    }

    /**
     *
     * @param treatmentId
     * The treatmentId
     */
    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

}
