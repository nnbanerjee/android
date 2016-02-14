package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 03-Apr-15.
 */
public class RemovePatients {

    private String doctorId;
    private ArrayList<MultiplePatient> patients;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public ArrayList<MultiplePatient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<MultiplePatient> patients) {
        this.patients = patients;
    }
}
