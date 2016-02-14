package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 20-Mar-15.
 */
public class AddDependentDoctor {

    private String doctorId;
    private String email;
    private String password;
    private ArrayList<AddDependentElement> dependents;
    private String parentType;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public ArrayList<AddDependentElement> getDependents() {
        return dependents;
    }

    public void setDependents(ArrayList<AddDependentElement> dependents) {
        this.dependents = dependents;
    }

    @Override
    public String toString() {
        return "AddDependent{" +
                "patientId='" + doctorId + '\'' +
                ", dependents=" + dependents +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailId) {
        this.email = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
