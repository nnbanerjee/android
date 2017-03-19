package com.medico.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 7/6/15.
 */
public class ReminderDate {

    Date date;
    ArrayList<TimeReminder> times;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<TimeReminder> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<TimeReminder> times) {
        this.times = times;
    }
}
