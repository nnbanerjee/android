package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;


import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Time;

/**
 * Created by MNT on 24-Feb-15.
 */
public class TimeAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Time> timeList;
    private CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    private Button selectAll;
    private String fromTime,toTime;
    private Spinner hourTo,minuteTo,hourFrom,minuteFrom,ampmFrom,ampmTo;


    public TimeAdapter(Activity activity, List<Time> timeList) {
        this.activity = activity;
        this.timeList = timeList;
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeList.get(position);
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
            convertView = inflater.inflate(R.layout.clinic_element, null);

        final int pos = position;
        final Time toc = timeList.get(position);

        monday = (CheckBox) convertView.findViewById(R.id.monday);

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {


                } else {

                }
            }
        });

        tuesday = (CheckBox) convertView.findViewById(R.id.tuesday);

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {

                } else {

                }
            }
        });

        wednesday = (CheckBox) convertView.findViewById(R.id.wednesday);

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;


                if (cb.isChecked()) {

                } else {

                }
            }
        });

        thursday = (CheckBox) convertView.findViewById(R.id.thursday);

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;



                if (cb.isChecked()) {



                } else {

                }
            }
        });

        friday = (CheckBox) convertView.findViewById(R.id.friday);

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;


                if (cb.isChecked()) {


                } else {

                }
            }
        });

        saturday = (CheckBox) convertView.findViewById(R.id.saturday);

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;


                if (cb.isChecked()) {


                } else {

                }
            }
        });

        sunday = (CheckBox) convertView.findViewById(R.id.sunday);

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;


                if (cb.isChecked()) {


                } else {

                }
            }
        });

        selectAll = (Button) convertView.findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((selectAll.getText().toString()).equals("Select All"))
                {

                    monday.setChecked(true);
                    tuesday.setChecked(true);
                    wednesday.setChecked(true);
                    thursday.setChecked(true);
                    friday.setChecked(true);
                    saturday.setChecked(true);
                    sunday.setChecked(true);


                    selectAll.setText("DeSelect All");
                }
                else
                {
                    monday.setChecked(false);
                    tuesday.setChecked(false);
                    wednesday.setChecked(false);
                    thursday.setChecked(false);
                    friday.setChecked(false);
                    saturday.setChecked(false);
                    sunday.setChecked(false);


                    selectAll.setText("DeSelect All");
                }

            }
        });


        convertView.setTag(timeList);

        hourFrom =  (Spinner) convertView.findViewById(R.id.hourfrom);

        ArrayAdapter<CharSequence> hourFromAdapter = ArrayAdapter.createFromResource(activity,
                R.array.hour_list, android.R.layout.simple_spinner_item);

        hourFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourFrom.setAdapter(hourFromAdapter);


        ampmFrom =  (Spinner) convertView.findViewById(R.id.ampmfrom);

        ArrayAdapter<CharSequence> ampmfromAdapter = ArrayAdapter.createFromResource(activity,
                R.array.am_pm, android.R.layout.simple_spinner_item);

        ampmfromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ampmFrom.setAdapter(ampmfromAdapter);

        ampmTo =  (Spinner) convertView.findViewById(R.id.ampmto);

        ArrayAdapter<CharSequence> ampmtoAdapter = ArrayAdapter.createFromResource(activity,
                R.array.am_pm, android.R.layout.simple_spinner_item);

        ampmtoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ampmTo.setAdapter(ampmtoAdapter);

        minuteFrom =  (Spinner) convertView.findViewById(R.id.minutefrom);

        ArrayAdapter<CharSequence> minuteFromAdapter = ArrayAdapter.createFromResource(activity,
                R.array.minute_list, android.R.layout.simple_spinner_item);

        minuteFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        minuteFrom.setAdapter(minuteFromAdapter);

        hourTo = (Spinner) convertView.findViewById(R.id.hourto);

        ArrayAdapter<CharSequence> hourToAdapter = ArrayAdapter.createFromResource(activity,
                R.array.hour_list, android.R.layout.simple_spinner_item);

        hourToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourTo.setAdapter(hourToAdapter);

        minuteTo = (Spinner) convertView.findViewById(R.id.minuteto);

        ArrayAdapter<CharSequence> minuteToAdapter = ArrayAdapter.createFromResource(activity,
                R.array.minute_list, android.R.layout.simple_spinner_item);

        minuteToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        minuteTo.setAdapter(minuteToAdapter);


       fromTime = hourFrom.getSelectedItem().toString()+":"+minuteFrom.getSelectedItem().toString() +" "+ampmFrom.getSelectedItem().toString();
       toTime = hourTo.getSelectedItem().toString()+":"+minuteTo.getSelectedItem().toString()+" "+ampmTo.getSelectedItem().toString();




        return convertView;
    }
}
