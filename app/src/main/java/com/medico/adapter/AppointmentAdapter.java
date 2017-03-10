package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.medico.model.DoctorClinicDetails;
import com.medico.model.PatientAppointmentByDoctor;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Narendra on 10-03-2017.
 */

public class AppointmentAdapter extends BaseAdapter
{

    Activity activity;
    ProgressDialog progress;
    DoctorClinicDetails.ClinicSlots clinicSlot;
    List<PatientAppointmentByDoctor.Appointments> patientAppointments;

    // private RelativeLayout   mainRelative;

    public AppointmentAdapter(Activity context, DoctorClinicDetails.ClinicSlots clinicSlot, List<PatientAppointmentByDoctor.Appointments> patientAppointments) {
        this.activity = context;
        this.clinicSlot = clinicSlot;
        this.patientAppointments = patientAppointments;

    }
    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position)
    {
        return patientAppointments.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public int getCount()
    {
        return patientAppointments.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.appointment_list, null);
        }
        Button feedback = (Button) convertView.findViewById(R.id.feedback_btn);
        Button change = (Button) convertView.findViewById(R.id.change_btn);
        Button cancel = (Button) convertView.findViewById(R.id.cancel_btn);
        TextView appointmentDateTime = (TextView)convertView.findViewById(R.id.appointment_date_time);
        Date current = new Date();
        DateFormat formatDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);

        appointmentDateTime.setText(formatDate.format(new Date(patientAppointments.get(position).dateTime)));
        if(current.getTime() > patientAppointments.get(position).dateTime) {
            feedback.setVisibility(View.VISIBLE);
            change.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
        else
        {
            feedback.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


}
