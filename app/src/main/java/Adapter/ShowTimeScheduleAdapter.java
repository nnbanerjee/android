package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Model.Clinic;
import Model.Day;
import Model.Schedule;

/**
 * Created by MNT on 27-Feb-15.
 */
public class ShowTimeScheduleAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Day> schedules;
    private TextView dayTv,shiftFromTv1,shifttoTv1,shiftFromTv2,shifttoTv2,shift1,shift2,shiftFromTv3,shifttoTv3;

    public ShowTimeScheduleAdapter(Activity activity, ArrayList<Day> schedules) {
        this.activity = activity;
        this.schedules = schedules;
    }
    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;

        if (null == convertView)
        {
            row = inflater.inflate(R.layout.clinic_element_new, null);
        }
        else
        {
            row = convertView;
        }


        dayTv = (TextView) row.findViewById(R.id.day);
        dayTv.setText(schedules.get(position).getDay());

        System.out.println("Day "+schedules.get(position).getDay());
        System.out.println("From 1 "+schedules.get(position).getFrom1());
        System.out.println("TO 1 "+schedules.get(position).getTo1());
        System.out.println("From2 "+schedules.get(position).getFrom2());
        System.out.println("to2 "+schedules.get(position).getTo2());

        shift1 = (TextView)row.findViewById(R.id.shift1);
        shift2 = (TextView)row.findViewById(R.id.shift2);
        shiftFromTv1 = (TextView)row.findViewById(R.id.shift1_time_from);
        shifttoTv1 = (TextView)row.findViewById(R.id.shift1_time_to);
        shiftFromTv2 = (TextView)row.findViewById(R.id.shift2_time_from);
        shifttoTv2 = (TextView)row.findViewById(R.id.shift2_time_to);
        shiftFromTv3 = (TextView)row.findViewById(R.id.shift3_time_from);
        shifttoTv3 = (TextView)row.findViewById(R.id.shift3_time_to);

        if(schedules.get(position).getFrom1() == null)
        {
            shiftFromTv1.setText("Not Available");
        }
        else
        {
            shiftFromTv1.setText(schedules.get(position).getFrom1());
        }
        if(schedules.get(position).getTo1() == null)
        {
            shifttoTv1.setText("Not Available");
        }
        else
        {
            shifttoTv1.setText(schedules.get(position).getTo1());
        }

        if(schedules.get(position).getFrom2() == null)
        {
            shiftFromTv2.setText("Not Available");
        }
        else
        {
            shiftFromTv2.setText(schedules.get(position).getFrom2());
        }
        if(schedules.get(position).getTo2() == null)
        {
            shifttoTv2.setText("Not Available");
        }
        else
        {
            shifttoTv2.setText(schedules.get(position).getTo2());
        }
        if(schedules.get(position).getFrom3() == null)
        {
            shiftFromTv3.setText("Not Available");
        }
        else
        {
            shiftFromTv3.setText(schedules.get(position).getFrom3());
        }
        if(schedules.get(position).getTo3() == null)
        {
            shifttoTv3.setText("Not Available");
        }
        else
        {
            shifttoTv3.setText(schedules.get(position).getTo3());
        }
        return row;
    }
}
