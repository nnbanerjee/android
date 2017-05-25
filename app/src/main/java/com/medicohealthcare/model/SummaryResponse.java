package com.medicohealthcare.model;

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
    public Byte treatmentPlanEnabled = 0;
    public List<MedicinePrescribed> medicinePrescribed;
    public List<TestPrescribed> testPrescribed;
    public String errorCode = null;
    public int status = 1;

    public transient boolean isChanged = false;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
        isChanged = true;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
        isChanged = true;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
        isChanged = true;
    }

    public Long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Long visitDate) {
        if(this.visitDate == null || this.visitDate.equals(visitDate) == false) {
            this.visitDate = visitDate;
            isChanged = true;
        }
    }

    public Byte getVisitType() {
        return visitType;
    }

    public void setVisitType(Byte visitType) {
        if(this.visitType == null || this.visitType.equals(visitType) == false) {
            this.visitType = visitType;
            isChanged = true;
        }
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        if(this.referredBy == null || this.referredBy.equals(referredBy) == false)
        {
            this.referredBy = referredBy;
            isChanged = true;
        }
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        if(this.clinicId == null || this.clinicId.equals(clinicId) == false) {
            this.clinicId = clinicId;
            isChanged = true;
        }
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
        isChanged = true;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        if(this.symptoms == null || this.symptoms.equals(symptoms) == false) {
            this.symptoms = symptoms;
            isChanged = true;
        }
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        if(this.diagnosis == null || this.diagnosis.equals(diagnosis) == false) {
            this.diagnosis = diagnosis;
            isChanged = true;
        }
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
        isChanged = true;
    }

    public Byte getTreatmentPlanEnabled() {
        return treatmentPlanEnabled;
    }

    public void setTreatmentPlanEnabled(Byte treatmentPlanEnabled) {
        this.treatmentPlanEnabled = treatmentPlanEnabled;
        isChanged = true;
    }

    public List<MedicinePrescribed> getMedicinePrescribed() {
        return medicinePrescribed;
    }

    public void setMedicinePrescribed(List<MedicinePrescribed> medicinePrescribed) {
        this.medicinePrescribed = medicinePrescribed;
        isChanged = true;
    }

    public List<TestPrescribed> getTestPrescribed() {
        return testPrescribed;
    }

    public void setTestPrescribed(List<TestPrescribed> testPrescribed) {
        this.testPrescribed = testPrescribed;
        isChanged = true;
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

    public boolean isChanged()
    {
        return isChanged;
    }
    public void setChanged(boolean changed)
    {
        isChanged = changed;
    }
    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if(patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if(visitDate == null || visitDate.longValue() > 0 == false)
            canBeSaved = false;
        else if(visitType == null )
            canBeSaved = false;
        else if(clinicId == null || clinicId.intValue() > 0 == false)
            canBeSaved = false;
        else if(symptoms == null || symptoms.trim().length() > 0 == false)
            canBeSaved = false;
        else if(diagnosis == null || diagnosis.trim().length() > 0 == false)
            canBeSaved = false;
        else if(loggedinUserId == null || loggedinUserId.intValue() > 0 == false)
            canBeSaved = false;
        else if(treatmentPlanEnabled == null )
            canBeSaved = false;

        return canBeSaved;
    }
}