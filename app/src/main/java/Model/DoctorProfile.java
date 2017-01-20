package Model;

import java.util.ArrayList;
import java.util.List;

public class DoctorProfile implements PersonProfile
{

    private Person person;
    private List<Dependent> dependents = new ArrayList<Dependent>();
    private List<Delegation> delegates = new ArrayList<Delegation>();
    private int patientCount;
    private int appointments;
    private int financeCount;
    private int feebackCount;

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
    public int getPatientCount() {
        return patientCount;
    }

    /**
     *
     * @param patientCount
     * The patientCount
     */
    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    /**
     *
     * @return
     * The appointments
     */
    public int getAppointments() {
        return appointments;
    }

    /**
     *
     * @param appointments
     * The appointments
     */
    public void setAppointments(int appointments) {
        this.appointments = appointments;
    }

    /**
     *
     * @return
     * The financeCount
     */
    public int getFinanceCount() {
        return financeCount;
    }

    /**
     *
     * @param financeCount
     * The financeCount
     */
    public void setFinanceCount(int financeCount) {
        this.financeCount = financeCount;
    }

    /**
     *
     * @return
     * The feebackCount
     */
    public int getFeebackCount() {
        return feebackCount;
    }

    /**
     *
     * @param feebackCount
     * The feebackCount
     */
    public void setFeebackCount(int feebackCount) {
        this.feebackCount = feebackCount;
    }

    public boolean getDependentProfile()
    {
        return false;
    }

    public void setDependentProfile(boolean dependentProfile)
    {

    }

}