package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 3/24/15.
 */
public class AddConfirmDeny {

    private String patientId;
    private ArrayList<AddDependentElement> parents;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<AddDependentElement> getParents() {
        return parents;
    }

    public void setParents(ArrayList<AddDependentElement> parents) {
        this.parents = parents;
    }
}
