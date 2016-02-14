package Model;

/**
 * Created by MNT on 20-Mar-15.
 */
public class AddDependentElement {

    private String id;
    private String accessLevel;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddDependentElement{" +
                "id='" + id + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
