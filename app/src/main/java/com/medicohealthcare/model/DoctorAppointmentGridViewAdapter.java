package com.medicohealthcare.model;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.util.PARAM;

import java.text.DateFormat;
import java.util.Calendar;
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
    Date date;

    public Integer getSelectedSequenceNumber() {
        return selectedSequenceNumber;
    }
    public Long getSelectedAppointmentTime() {
        if(selectedSequenceNumber > 0)
        return timings[selectedSequenceNumber-1];
        else
            return null;
    }

    Integer selectedSequenceNumber = 0;
    public DoctorAppointmentGridViewAdapter(Activity context, DoctorSlotBookings doctorSlotBookings, List<DoctorHoliday> doctorSlotHolidays, Date date)
    {
        activity = context;
        this.doctorSlotBookings = doctorSlotBookings;
        this.doctorSlotHolidays = doctorSlotHolidays;
        timings = getTimings();
        this.date = date;
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
            imageView = (Button)activity.getLayoutInflater().inflate(R.layout.appointment_grid_layout, null);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        }
        else
            imageView = (Button) convertView;
        Bundle bundle = activity.getIntent().getExtras();
        Integer patientId = bundle.getInt(PARAM.PATIENT_ID);
        Integer sequenceNumber = bundle.getInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER);

       if(doctorSlotHolidays != null && doctorSlotHolidays.size() > 0)
           setHoliday(imageView, position, patientId);


        if (imageView.isEnabled() && doctorSlotBookings != null && doctorSlotBookings.bookings != null
                && doctorSlotBookings.bookings.size() > 0) {
            setBookings(imageView,position,patientId, sequenceNumber);
        }

        if(imageView.isEnabled())
        {
            if(selectedSequenceNumber.intValue() == position + 1)
                imageView.setBackground(activity.getResources().getDrawable(mThumbIds[3]));
            else
                imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
//            imageView.setTextColor(Color.GREEN);
            imageView.setTextColor(activity.getResources().getColor(R.color.medico_green));
        }
        if (imageView.isEnabled() )
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date(timings[position]));
            calendar2.set(Calendar.YEAR,calendar1.get(Calendar.YEAR));
            calendar2.set(Calendar.MONTH,calendar1.get(Calendar.MONTH));
            calendar2.set(Calendar.DAY_OF_MONTH,calendar1.get(Calendar.DAY_OF_MONTH));

            if(calendar2.getTime().getTime() < new Date().getTime())
            {
                imageView.setTextColor(activity.getResources().getColor(R.color.medico_lightgreen));
                imageView.setEnabled(false);
            }

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
        R.drawable.rectagle_white, R.drawable.booking_confirmed,
        R.drawable.booking_tentative, R.drawable.booking_selected};

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
        Long currentTime = new Date().getTime();
        return timings;
    }

    private void setHoliday(Button imageView, int position, Integer patientId)
    {
        for(DoctorHoliday holiday: doctorSlotHolidays) {
            if(holiday.status == 1) {
                switch (holiday.type) {
                    case 0:
                        if (holiday.sequenceNo != position + 1)
                            break;
                    case 1:
                    case 2:
                    default:
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
                        imageView.setEnabled(false);
//                        imageView.setTextColor(Color.GRAY);
                        imageView.setTextColor(activity.getResources().getColor(R.color.medico_gray));
                }
            }

        }

    }

    private void setBookings(Button imageView, int position, Integer patientId, Integer sequenceNumber)
    {
        for(DoctorSlotBookings.PersonBooking booking: doctorSlotBookings.bookings) {
            if(booking.sequenceNo.intValue() == position+1) {
                if(patientId.intValue() == booking.patient.getId().intValue()) {
                    if (booking.appointmentStatus == PARAM.APPOINTMENT_CONFIRMED)
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[1]));
                    else if (booking.appointmentStatus == PARAM.APPOINTMENT_TENTATIVE)
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[2]));
                    if(sequenceNumber != null && sequenceNumber.intValue() == booking.sequenceNo.intValue())
                    {
//                        imageView.setBackgroundColor(Color.BLUE);
                        imageView.setBackground(activity.getResources().getDrawable(mThumbIds[3]));
                        imageView.setTextColor(activity.getResources().getColor(R.color.medico_blue));
                    }

                    imageView.setEnabled(false);
                }
                else
                {
                    imageView.setBackground(activity.getResources().getDrawable(mThumbIds[0]));
                    imageView.setTextColor(activity.getResources().getColor(R.color.medico_red));
//                    imageView.setTextColor(Color.RED);
                    imageView.setEnabled(false);
                }
                break;
            }
        }
    }


}
