package com.medicohealthcare.model;

/**
 * Created by Narendra on 23-03-2017.
 */

public class ClinicSlotDetails
{
    public Integer doctorClinicId;
    public Byte autoConfirm;
    public Byte availability;
    public String daysOfWeek;
    public Integer feeBooking;
    public Integer feesConsultation;
    public Integer numberOfPatients;
    public Integer percentageOverbook;
    public String slotName;
    public Byte slotNumber;
    public Byte slotType;
    public Long timeToStart;
    public Long TimeToStop;
    public Integer visitDuration;
    public Integer clinicId;
    public Integer doctorId;
    public Integer assistantId;
    public String clinicName;

    private boolean isChanged = true;

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
        if(daysOfWeek == null || daysOfWeek.trim().length() > 0 == false)
            canBeSaved = false;
        else if(feesConsultation == null || feesConsultation.intValue() > 0 == false)
            canBeSaved = false;
        else if(numberOfPatients == null || numberOfPatients.intValue() > 0 == false)
            canBeSaved = false;
        else if(slotName == null || slotName.trim().length() > 0 == false )
            canBeSaved = false;
        else if(slotNumber == null || slotNumber.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(slotType == null || slotType.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(autoConfirm == null || autoConfirm.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(availability == null || availability.byteValue() >= 0 == false)
            canBeSaved = false;
        else if(timeToStart == null || timeToStart.longValue() > 0 == false)
            canBeSaved = false;
        else if(TimeToStop == null || TimeToStop.longValue() > 0 == false)
            canBeSaved = false;
        else if(visitDuration == null || visitDuration.intValue() > 0 == false)
            canBeSaved = false;
        else if(clinicId == null || clinicId.intValue() > 0 == false)
            canBeSaved = false;
        else if(doctorId == null || doctorId.intValue() > 0 == false)
            canBeSaved = false;
        return canBeSaved;
    }

}
