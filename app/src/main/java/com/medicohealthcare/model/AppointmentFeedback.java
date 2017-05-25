package com.medicohealthcare.model;

/**
 * Created by Narendra on 14-03-2017.
 */

public class AppointmentFeedback
{
    public Integer appointmentId;
    public Byte appointmentVisitStatus;
    public String reviews;
    public Byte rating;
    public Byte recommendation;
    public boolean isChanged = false;

    public void setAppointmentVisitStatus(Byte status)
    {
        if(this.appointmentVisitStatus == null || this.appointmentVisitStatus.byteValue() != status.byteValue())
        {
            this.appointmentVisitStatus = status;
            isChanged = true;
        }
    }
    public void setVisitReview(String reviews)
    {
        if(this.reviews == null || this.reviews.equalsIgnoreCase(reviews) == false)
        {
            this.reviews = reviews;
            isChanged = true;
        }
    }
    public void setVisitRating(Byte rating)
    {
        if(this.rating == null || this.rating.byteValue() != rating.byteValue())
        {
            this.appointmentVisitStatus = rating;
            isChanged = true;
        }
    }

    public void setRecommendation(Byte recommendation)
    {
        if(this.recommendation == null || this.recommendation.byteValue() != recommendation.byteValue())
        {
            this.recommendation = recommendation;
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
    public boolean canBeSaved(boolean isPatient)
    {
        boolean canBeSaved = true;
        if(appointmentId == null || appointmentId.intValue() >= 0 == false)
            canBeSaved = false;
        else if(appointmentVisitStatus == null || appointmentVisitStatus.byteValue() >= 0 == false)
            canBeSaved = false;
        if(isPatient)
        {
            if (rating == null || rating.byteValue() >= 0 == false)
                canBeSaved = false;
            else if (recommendation == null || recommendation.byteValue() >= 0 == false)
                canBeSaved = false;
            else if (reviews == null || reviews.trim().length() > 0 == false)
                canBeSaved = false;
        }

        return canBeSaved;
    }
}
