package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ClinicAppointmentVM;

/**
 * Created by User on 08-10-2015.
 */
public class ClinicAppointmentAdapter extends BaseAdapter {
    Activity activity;
    List<ClinicAppointmentVM> appointments;
    LayoutInflater inflater;

    public ClinicAppointmentAdapter(Activity activity,List<ClinicAppointmentVM> appointments){
        this.activity = activity;
        this.appointments = appointments;
    }
    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View cv = convertView;
        if (cv == null) {
            cv = inflater.inflate(R.layout.clinic_appointment, null);
        }
        TextView appointmentDate = (TextView)cv.findViewById(R.id.appointment_date);
        TextView appointmentTime = (TextView)cv.findViewById(R.id.appointment_time);
        appointmentDate.setText(appointments.get(position).getAppointmentDate());
        appointmentTime.setText(appointments.get(position).getAppointmentTime());
        return cv;
    }
}
