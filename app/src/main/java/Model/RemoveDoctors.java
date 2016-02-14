package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 12-Mar-15.
 */
public class RemoveDoctors {

    private String patientId;
    private ArrayList<MultipleDoctors> doctors;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<MultipleDoctors> getDoctors() {
        return doctors;
    }

    public void setMultipleDoctors(ArrayList<MultipleDoctors> doctors) {
        this.doctors = doctors;
    }
}
