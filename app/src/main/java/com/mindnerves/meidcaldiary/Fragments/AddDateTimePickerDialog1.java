package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
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

import com.mindnerves.meidcaldiary.R;

/**
 * Created by MNT on 27-Mar-15.
 */
public class AddDateTimePickerDialog1 extends DialogFragment {

    Button addButton,cancelButton;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView endDateValue;
    static AddDateTimePickerDialog1 newInstance() {
        return new AddDateTimePickerDialog1();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_date_time_picker_dialog,container,false);
        getDialog().setTitle("Set Date and Time");
        final SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        endDateValue = (TextView)getActivity().findViewById(R.id.endDateValue);
        addButton = (Button)view.findViewById(R.id.add_template);
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
                SharedPreferences.Editor editor = session.edit();
                editor.putString("date", dateString);
                editor.putString("time", timeString);
                editor.commit();
                endDateValue.setText(timeString+"  "+dateString);
                AddDateTimePickerDialog1.this.getDialog().cancel();
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_template);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AddDateTimePickerDialog1.this.getDialog().cancel();
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
