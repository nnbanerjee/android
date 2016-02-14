package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 20-Mar-15.
 */
public class AddDependent {

    private String patientId;
    private String email;
    private String password;
    private ArrayList<AddDependentElement> dependents;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
                "patientId='" + patientId + '\'' +
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
}
