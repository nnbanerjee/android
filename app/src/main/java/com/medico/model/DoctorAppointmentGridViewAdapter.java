package com.medico.model;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.medico.util.PARAM;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Narendra on 11-03-2017.
 */

public class DoctorAppointmentGridViewAdapter extends BaseAdapter
{
    Activity activity;
    DoctorSlotBookings doctorSlotBookings;
    List<DoctorHoliday> doctorSlotHolidays;
    Long [] timings;
    Integer selectedSequenceNumber = 0;
    public DoctorAppointmentGridViewAdapter(Activity context, DoctorSlotBookings doctorSlotBookings, List<DoctorHoliday> doctorSlotHolidays)
    {
        activity = context;
        this.doctorSlotBookings = doctorSlotBookings;
        this.doctorSlotHolidays = doctorSlotHolidays;
        timings = getTimings();
    }
    public int getCount() {
        return timings.length;
    }

    public Object getItem(int position)
    {
        return timings[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Button imageView;
        if (convertView == null) {
         // if it's not recycled, initialize some attributes
            imageView = new Button(activity);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
        }
        else
            imageView = (Button) convertView;
        Bundle bundle = activity.getIntent().getExtras();
        Integer patientId = bundle.getInt(PARAM.PATIENT_ID);
        if (doctorSlotBookings != null && doctorSlotBookings.bookings != null && doctorSlotBookings.bookings.size() > 0) {
            for(DoctorSlotBookings.PersonBooking booking: doctorSlotBookings.bookings) {
                if(booking.sequenceNo.intValue() == position+1) {
                    if(patientId.intValue() == booking.patient.getId().intValue()) {
                        if (booking.appointmentStatus == PARAM.APPOINTMENT_CONFIRMED)
                            imageView.setBackground(activity.getResources().getDrawable(mThumbIds[1]));
                        else if (booking.appointmentStatus == PARAM.APPOINTMENT_TENTATIVE)
                            imageView.setBackground(activity.getResources().getDrawable(mThumbIds[2]));
                        imageView.setEnabled(false);
                    }
                    else
                    {
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
                        imageView.setTextColor(Color.RED);
                        imageView.setEnabled(false);
                    }
                    break;
                }
                else {
                    if(selectedSequenceNumber.intValue() == position + 1)
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[3]));
                    else
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
                    imageView.setTextColor(Color.GREEN);
                }
            }
        }
        else {
            if(selectedSequenceNumber.intValue() == position + 1)
                imageView.setBackground(activity.getResources().getDrawable(mThumbIds[3]));
            else
                imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
            imageView.setTextColor(Color.GREEN);
        }
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        imageView.setText(format.format(new Date(timings[position])));
        imageView.setTag(new Integer(position + 1));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSequenceNumber = (Integer)v.getTag();
                notifyDataSetChanged();
            }
        });
        return imageView;
    }

    private Integer[] mThumbIds = {
        R.drawable.booking_available_high, R.drawable.booking_confirmed_high,
        R.drawable.booking_tentative_high, R.drawable.booking_selected_high};

    private Long[] getTimings()
    {
        Bundle bundle = activity.getIntent().getExtras();
        Long startTime = bundle.getLong(PARAM.SLOT_START_DATETIME);
        Long endTime = bundle.getLong(PARAM.SLOT_END_DATETIME);
        Integer vistDuration = bundle.getInt(PARAM.SLOT_VISIT_DURATION);
        int numberOfPatients = Math.round((endTime - startTime)/(vistDuration * 60 * 1000));
        Long[] timings = new Long[numberOfPatients];
        for(int i = 0; i < timings.length; i++)
            timings[i] = startTime + i * vistDuration* 60 * 1000;

        return timings;
    }
}
