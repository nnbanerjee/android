package com.medicohealthcare.model;

/**
 * Created by Narendra on 05-02-2017.
 */

public class PatientDiagnostic
{

    public String doctorInstruction;
    public Byte reminder;
    public String testName;
    public Long datetime;
    public Integer patientTestId;
    public Integer appointmentId;
    public Integer patientId;
    public Integer doctorId;
    public Integer clinicId;
    public Integer testId;
    public Integer referredId;
    public Integer userType;
    public Integer loggedinUserId;
    public String doctorName;
    public DiagnosticTest test;
    public Clinic1 clinic;

    private transient boolean isChanged = false;

    public Integer getPatientTestId() {
        return patientTestId;
    }

    public void setPatientTestId(Integer patientTestId) {
        this.patientTestId = patientTestId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName)
    {
        if(this.testName == null || this.testName.equals(testName) == false) {
            this.testName = testName;
            isChanged = true;
        }
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }



    public Long getStartDate() {
        return datetime;
    }

    public void setStartDate(Long datetime)
    {
        if(this.datetime==null || this.datetime.equals(datetime) == false) {
            this.datetime = datetime;
            isChanged = true;
        }
    }
    public DiagnosticTest getDiagnosticTest()
    {
        return test;
    }

    public void setDiagnosticTest(DiagnosticTest test)
    {
        testId = test.testId;
        testName = test.name;
        this.test = test;
        isChanged = true;
    }


    public Byte getReminder() {
        return reminder;
    }

    public void setReminder(Byte reminder)
    {
        if(this.reminder==null || this.reminder.equals(reminder) == false) {
            this.reminder = reminder;
            isChanged = true;
        }
    }
    public Clinic1 getClinic()
    {
        return clinic;
    }
    public Integer getClinicId()
    {
        return clinic.idClinic;
    }
    public void setClinicId(Integer clinicId)
    {
        if(this.clinicId==null || this.clinicId.equals(clinicId) == false) {
            this.clinicId = clinicId;
            isChanged = true;
        }
    }
    public void setReferredId(Integer personId)
    {
        if(this.referredId==null || this.referredId.equals(personId) == false) {
            this.referredId = personId;
            isChanged = true;
        }
    }

    public String getDoctorInstruction() {
        return doctorInstruction;
    }

    public void setDoctorInstruction(String doctorInstruction)
    {
        if(this.doctorInstruction==null || this.doctorInstruction.equals(doctorInstruction) == false) {
            this.doctorInstruction = doctorInstruction;
            isChanged = true;
        }
    }


    public PatientDiagnostic()
    {

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

        if(datetime == null || datetime.longValue() > 0 == false)
            canBeSaved = false;
        else if(reminder == null || reminder.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if(clinicId == null || clinicId.intValue() > 0 == false)
            canBeSaved = false;
        else if(appointmentId == null || appointmentId.intValue() > 0 == false)
            canBeSaved = false;
        else if(doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if(referredId == null || referredId.intValue() > 0 == false)
            canBeSaved = false;
        else if(loggedinUserId == null || loggedinUserId.intValue() > 0 == false)
            canBeSaved = false;
        else if(testId == null || testId.intValue() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }

}
