package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.WeekShow;

/**
 * Created by User on 7/15/15.
 */
public class AppointmentWeekAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<WeekShow> appointmentWeeks;
    LayoutInflater inflater;

    public AppointmentWeekAdapter(Activity activity,ArrayList<WeekShow> appointmentWeeks)
    {
        this.activity = activity;
        this.appointmentWeeks = appointmentWeeks;
    }
    @Override
    public int getCount() {
        return appointmentWeeks.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentWeeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.appointment_week_element, null);
        ImageView image = (ImageView)convertView.findViewById(R.id.image_patient);
        TextView tv = (TextView)convertView.findViewById(R.id.time_text);

        if(appointmentWeeks.get(position).getBookTime().equalsIgnoreCase("N.A."))
        {
            image.setImageResource(R.drawable.na);
            tv.setText(appointmentWeeks.get(position).getBookTime());
        }
        else
        {
            image.setImageResource(R.drawable.patient);
            tv.setText(appointmentWeeks.get(position).getBookTime());
        }


        return convertView;
    }
}
