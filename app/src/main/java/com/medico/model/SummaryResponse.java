package com.medico.model;

import java.util.List;

public class SummaryResponse {

    public Integer doctorId;
    public Integer patientId;
    public Integer appointmentId;
    public Long visitDate;
    public Byte visitType;
    public String referredBy;
    public Integer clinicId;
    public String clinicName;
    public String symptoms;
    public String diagnosis;
    public Integer loggedinUserId;
    public Byte treatmentPlanEnabled;
    public List<MedicinePrescribed> medicinePrescribed;
    public List<TestPrescribed> testPrescribed;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Long visitDate) {
        this.visitDate = visitDate;
    }

    public Byte getVisitType() {
        return visitType;
    }

    public void setVisitType(Byte visitType) {
        this.visitType = visitType;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    public Byte getTreatmentPlanEnabled() {
        return treatmentPlanEnabled;
    }

    public void setTreatmentPlanEnabled(Byte treatmentPlanEnabled) {
        this.treatmentPlanEnabled = treatmentPlanEnabled;
    }

    public List<MedicinePrescribed> getMedicinePrescribed() {
        return medicinePrescribed;
    }

    public void setMedicinePrescribed(List<MedicinePrescribed> medicinePrescribed) {
        this.medicinePrescribed = medicinePrescribed;
    }

    public List<TestPrescribed> getTestPrescribed() {
        return testPrescribed;
    }

    public void setTestPrescribed(List<TestPrescribed> testPrescribed) {
        this.testPrescribed = testPrescribed;
    }

    public class MedicinePrescribed {
        public Integer medicineId;
        public String medicineName;
        public Long startDateTime;
        public Long endDateTime;
        public Byte reminder;
    }

    public class TestPrescribed {
        public Integer testId;
        public String testName;
        public Long dateTime;
        public Byte reminder;
    }



}