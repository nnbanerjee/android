package com.medico.model;

/**
 * Created by Narendra on 12-03-2017.
 */

public class DoctorAppointment
{


    public Integer appointmentId;
    public Integer doctorId;
    public Integer patientId;
    public Integer clinicId;
    public Long appointmentDate;
    public Integer type;
    public Integer sequenceNumber;
    public Byte appointmentStatus;
    public Byte visitType;
    public Byte visitStatus;
    public Byte rating;
    public Integer userType;
    public Integer doctorClinicId;
    public String referredBy;
    public String reviews;
    public Integer new_case_id;

    boolean isChanged = false;

    public Long getAppointmentDate()
    {
        return appointmentDate;
    }

    public void setAppointmentDate(Long appointmentDate)
    {
        if(this.appointmentDate == null || this.appointmentDate.longValue() != appointmentDate.longValue()) {
            this.appointmentDate = appointmentDate;
            isChanged = true;
        }
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {

        if(this.sequenceNumber == null || this.sequenceNumber.intValue()!= sequenceNumber && sequenceNumber.intValue() > 0) {
            this.sequenceNumber = sequenceNumber;
            isChanged = true;
        }
    }

    public Byte getVisitType() {
        return visitType;
    }

    public void setVisitType(Byte visitType)
    {
        if(this.visitType == null || this.visitType.byteValue() != visitType.byteValue() ) {
            this.visitType = visitType;
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
        if(doctorId == null || doctorId.intValue() >= 0 == false)
            canBeSaved = false;
        else if(patientId == null || patientId.intValue() > 0 == false)
            canBeSaved = false;
        else if(clinicId == null || clinicId.intValue() > 0 == false)
            canBeSaved = false;
        else if(appointmentDate == null || appointmentDate.longValue() > 0 == false)
            canBeSaved = false;
        else if(type == null || type.intValue() >= 0 == false)
            canBeSaved = false;
        else if(sequenceNumber == null || sequenceNumber.intValue() >= 0 == false)
            canBeSaved = false;
        else if(appointmentStatus == null || appointmentStatus.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(visitStatus == null || visitStatus.byteValue() > 0 == false)
            canBeSaved = false;
        else if(visitType == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        else if(doctorClinicId == null || doctorClinicId.intValue() > 0 == false)
            canBeSaved = false;

        return canBeSaved;
    }
}
