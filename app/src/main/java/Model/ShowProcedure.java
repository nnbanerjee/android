package Model;

/**
 * Created by MNT on 06-Apr-15.
 */
public class ShowProcedure {

    private String id;
    private String doctorId;
    private String procedureName;
    private String category;
    private String numberOfTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getNumberOfTemplate() {
        return numberOfTemplate;
    }

    public void setNumberOfTemplate(String numberOfTemplate) {
        this.numberOfTemplate = numberOfTemplate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
