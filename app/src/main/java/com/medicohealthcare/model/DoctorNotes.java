package com.medicohealthcare.model;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorNotes {

    public Integer idNotes;
    public Integer appointmentId;
    public String diagnosis;
    public String symptoms;
    public String doctorNotes;

    public boolean isChanged = false;

    public Integer getIdNotes()
    {
        return idNotes;
    }
    public void setIdNotes(Integer idNotes)
    {
        this.idNotes = idNotes;
    }
    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        if(this.diagnosis == null || this.diagnosis.equalsIgnoreCase(diagnosis) == false) {
            this.diagnosis = diagnosis;
            isChanged = true;
        }
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        if(this.symptoms == null || this.symptoms.equalsIgnoreCase(symptoms) == false) {
            this.symptoms = symptoms;
            isChanged = true;
        }

    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        if(this.doctorNotes == null || this.doctorNotes.equalsIgnoreCase(doctorNotes) == false) {
            this.doctorNotes = doctorNotes;
            isChanged = true;
        }

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
        if((symptoms == null || symptoms.trim().length() == 0) &&
                (doctorNotes == null || doctorNotes.trim().length() == 0) &&
                (diagnosis == null || diagnosis.trim().length() >= 0 == false))
            canBeSaved = false;

        return canBeSaved;
    }

 }
