package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 06-Apr-15.
 */
public class UpdateField {

    private String fieldId;
    private ArrayList<FieldElement> fields;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public ArrayList<FieldElement> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldElement> fields) {
        this.fields = fields;
    }
}
