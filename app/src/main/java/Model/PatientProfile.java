package Model;

import java.util.ArrayList;
import java.util.List;

public class PatientProfile {

    private Person person;
    private List<Dependent> dependents = new ArrayList<Dependent>();
    private List<Delegation> delegates = new ArrayList<Delegation>();
    private int doctorsCount;
    private int clinicsCount;
    private int appointmentsCount;
    private int medicineCount;

    /**
     *
     * @return
     * The person
     */
    public Person getPerson() {
        return person;
    }

    /**
     *
     * @param person
     * The person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     *
     * @return
     * The dependents
     */
    public List<Dependent> getDependents() {
        return dependents;
    }

    /**
     *
     * @param dependents
     * The dependents
     */
    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    /**
     *
     * @return
     * The delegates
     */
    public List<Delegation> getDelegates() {
        return delegates;
    }

    /**
     *
     * @param delegates
     * The delegates
     */
    public void setDelegates(List<Delegation> delegates) {
        this.delegates = delegates;
    }

    /**
     *
     * @return
     * The patientCount
     */
    public int getDoctorsCount() {
        return doctorsCount;
    }

    /**
     *
     * @param doctorsCount
     * The patientCount
     */
    public void setDoctorsCount(int doctorsCount) {
        this.doctorsCount = doctorsCount;
    }

    /**
     *
     * @return
     * The appointments
     */
    public int getAppointmentsCount() {
        return appointmentsCount;
    }

    /**
     *
     * @param appointmentsCount
     * The appointments
     */
    public void setAppointmentsCount(int appointmentsCount) {
        this.appointmentsCount = appointmentsCount;
    }

    /**
     *
     * @return
     * The financeCount
     */
    public int getClinicsCount() {
        return clinicsCount;
    }

    /**
     *
     * @param financeCount
     * The financeCount
     */
    public void setClinicsCount(int financeCount) {
        this.clinicsCount = clinicsCount;
    }

    /**
     *
     * @return
     * The feebackCount
     */
    public int getMedicineCount() {
        return medicineCount;
    }

    /**
     *
     * @param medicineCount
     * The feebackCount
     */
    public void setMedicineCount(int medicineCount) {
        this.medicineCount = medicineCount;
    }

}