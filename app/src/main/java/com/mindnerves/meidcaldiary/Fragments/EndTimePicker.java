package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.medico.view.PatientMedicinReminder;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

/**
 * Created by User on 8/3/15.
 */
public class EndTimePicker extends Fragment {
    Button addButton,cancelButton;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView dateValue;
    Bundle saveToBundle = new Bundle();
    Global global;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_date_time_picker_dialog,container,false);
        final SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        dateValue = (TextView)getActivity().findViewById(R.id.dateValue);
        addButton = (Button)view.findViewById(R.id.add_template);
        final Bundle args = getArguments();
        global = (Global) getActivity().getApplicationContext();
        datePicker = (DatePicker)view.findViewById(R.id.datePicker1);
        timePicker = (TimePicker)view.findViewById(R.id.timePicker1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = showMonth(datePicker.getMonth());
                int year = datePicker.getYear();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String dateString = year+"-"+month+"-"+day;
                String timeString = updateTime(hour,minute);
                Fragment fragment = new PatientMedicinReminder();
                saveToBundle = args;
                global.setEndDateString(timeString+"  "+dateString);
                fragment.setArguments(saveToBundle);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_template);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientMedicinReminder();
                saveToBundle = args;
                global.setEndDateString(args.getString("endTime").toString());
                fragment.setArguments(saveToBundle);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }

    public int showMonth(int month)
    {
        int showMonth = month;
        switch(showMonth)
        {
            case 0:
                showMonth = showMonth + 1;
                break;
            case 1:
                showMonth = showMonth + 1;
                break;
            case 2:
                showMonth = showMonth + 1;
                break;
            case 3:
                showMonth = showMonth + 1;
                break;
            case 4:
                showMonth = showMonth + 1;
                break;
            case 5:
                showMonth = showMonth + 1;
                break;
            case 6:
                showMonth = showMonth + 1;
                break;
            case 7:
                showMonth = showMonth + 1;
                break;
            case 8:
                showMonth = showMonth + 1;
                break;
            case 9:
                showMonth = showMonth + 1;
                break;
            case 10:
                showMonth = showMonth + 1;
                break;
            case 11:
                showMonth = showMonth + 1;
                break;

        }
        return showMonth;
    }

    private String updateTime(int hours, int mins)
    {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }


}
