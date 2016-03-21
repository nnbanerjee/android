package Model;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponse {

    private String doctorId;
    private String patientId;
    private String appointmentId;
    private String visitDate;
    private String visitType;
    private String referredBy;
    private String clinicId;
    private String clinicName;
    private String symptoms;
    private String diagnosis;
    private List<MedicinePrescribed> medicinePrescribed = new ArrayList<MedicinePrescribed>();

    public List<TestPrescribed> getTestPrescribed() {
        return testPrescribed;
    }

    private List<TestPrescribed> testPrescribed = new ArrayList<TestPrescribed>();

    public void setTestPrescribed(List<TestPrescribed> testPrescribed) {
        this.testPrescribed = testPrescribed;
    }


    //private String testPrescribed;

    /**
     *
     * @return
     * The doctorId
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     *
     * @param doctorId
     * The doctorId
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     *
     * @return
     * The patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     * The patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return
     * The appointmentId
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     *
     * @param appointmentId
     * The appointmentId
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     *
     * @return
     * The visitDate
     */
    public String getVisitDate() {
        return visitDate;
    }

    /**
     *
     * @param visitDate
     * The visitDate
     */
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    /**
     *
     * @return
     * The visitType
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     *
     * @param visitType
     * The visitType
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    /**
     *
     * @return
     * The referredBy
     */
    public String getReferredBy() {
        return referredBy;
    }

    /**
     *
     * @param referredBy
     * The referredBy
     */
    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    /**
     *
     * @return
     * The clinicId
     */
    public String getClinicId() {
        return clinicId;
    }

    /**
     *
     * @param clinicId
     * The clinicId
     */
    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    /**
     *
     * @return
     * The clinicName
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     *
     * @param clinicName
     * The clinicName
     */
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    /**
     *
     * @return
     * The symptoms
     */
    public String getSymptoms() {
        return symptoms;
    }

    /**
     *
     * @param symptoms
     * The symptoms
     */
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    /**
     *
     * @return
     * The diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     *
     * @param diagnosis
     * The diagnosis
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     *
     * @return
     * The medicinePrescribed
     */
    public List<MedicinePrescribed> getMedicinePrescribed() {
        return medicinePrescribed;
    }

    /**
     *
     * @param medicinePrescribed
     * The medicinePrescribed
     */
    public void setMedicinePrescribed(List<MedicinePrescribed> medicinePrescribed) {
        this.medicinePrescribed = medicinePrescribed;
    }

   /* *//**
     *
     * @return
     * The testPrescribed
     *//*
    public String getTestPrescribed() {
        return testPrescribed;
    }

    *//**
     *
     * @param testPrescribed
     * The testPrescribed
     *//*
    public void setTestPrescribed(String testPrescribed) {
        this.testPrescribed = testPrescribed;
    }*/

}