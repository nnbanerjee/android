package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapter.HorizontalListAdapter;
import Model.AlarmReminderVM;

/**
 * Created by User on 7/7/15.
 */
public class EditDoseFragment extends DialogFragment {
    ImageView calenderImg;
    private int hour;
    private int minute;
    EditText time;
    Global global;
    Button cancel,save;
    HorizontalListView horizontalList;
    String doseTime,timeText;
    AlarmReminderVM alarmReminder = new AlarmReminderVM();
    AlarmReminderVM alarmTime;
    List<AlarmReminderVM> alarmReminderList;
    List<AlarmReminderVM> alarmReminders;
    int position;
    int selected;
    public static EditDoseFragment newInstance() {
        return new EditDoseFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_time_dialog,container,false);
        calenderImg = (ImageView)view.findViewById(R.id.calendar);
        time = (EditText)view.findViewById(R.id.time);
        global = (Global) getActivity().getApplicationContext();
        alarmTime =  global.getAlaramObj();
        doseTime = global.getTimeChange();
        alarmReminderList = global.getAlarmTime();
        timeText= global.getTimeText();
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
        horizontalList = (HorizontalListView) getActivity().findViewById(R.id.horizontalList);
        alarmReminders = new ArrayList<AlarmReminderVM>();
        position=global.getSelectedPostionOfMedicineScheduleFromHorizontalList();
        selected=global.getSelectedPostionOfMedicineScheduleFromVerticalList();

        if(doseTime.equals("Time1"))
        {
            time.setText(timeText);
        }
        /*else if(doseTime.equals("Time2"))
        {
            time.setText(alarmTime.getTime2());
        }
        else if(doseTime.equals("Time3"))
        {
            time.setText(alarmTime.getTime3());
        }
        else if(doseTime.equals("Time4"))
        {
            time.setText(alarmTime.getTime4());
        }
        else if(doseTime.equals("Time5"))
        {
            time.setText(alarmTime.getTime5());
        }
        else if(doseTime.equals("Time6"))
        {
            time.setText(alarmTime.getTime6());
        }*/
        cancel = (Button)view.findViewById(R.id.cancel_template);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDoseFragment.this.getDialog().cancel();
            }
        });
        save = (Button)view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeText = "";
                timeText = time.getText().toString();
                int len = alarmReminderList.size();
             //   for(int i=0;i<len;i++)
              //  {
//                    if((alarmReminderList.get(i).getAlarmDate().toString()).equals(alarmTime.getAlarmDate().toString()))
                 //   {
                       // AlarmReminderVM newObj = new AlarmReminderVM();
                     //   newObj.setId(alarmReminderList.get(i).getId());
                    //    newObj.setAlarmDate(alarmReminderList.get(i).getAlarmDate());


                        AlarmReminderVM obj=alarmReminderList.get(position);

                        List<String> time= new ArrayList<String>();
                        for (int j=0; j< obj.getTimes().size();j++ ){
                            if(j==selected)
                                time.add(timeText);
                            else
                                time.add(obj.getTimes().get(j));
                        }


                    /*    if(doseTime.equals("Time1"))
                        {
                            newObj.setTime1(timeText);
                            newObj.setTime2(alarmReminderList.get(i).getTime2());
                            newObj.setTime3(alarmReminderList.get(i).getTime3());
                            newObj.setTime4(alarmReminderList.get(i).getTime4());
                            newObj.setTime5(alarmReminderList.get(i).getTime5());
                            newObj.setTime6(alarmReminderList.get(i).getTime6());
                            alarmReminders.add(newObj);

                        }
                        else if(doseTime.equals("Time2"))
                        {
                            newObj.setTime2(timeText);
                            newObj.setTime1(alarmReminderList.get(i).getTime1());
                            newObj.setTime3(alarmReminderList.get(i).getTime3());
                            newObj.setTime4(alarmReminderList.get(i).getTime4());
                            newObj.setTime5(alarmReminderList.get(i).getTime5());
                            newObj.setTime6(alarmReminderList.get(i).getTime6());
                            alarmReminders.add(newObj);
                        }
                        else if(doseTime.equals("Time3"))
                        {
                            newObj.setTime3(timeText);
                            newObj.setTime2(alarmReminderList.get(i).getTime2());
                            newObj.setTime1(alarmReminderList.get(i).getTime1());
                            newObj.setTime4(alarmReminderList.get(i).getTime4());
                            newObj.setTime5(alarmReminderList.get(i).getTime5());
                            newObj.setTime6(alarmReminderList.get(i).getTime6());
                            alarmReminders.add(newObj);
                        }
                        else if(doseTime.equals("Time4"))
                        {
                            newObj.setTime4(timeText);
                            newObj.setTime2(alarmReminderList.get(i).getTime2());
                            newObj.setTime3(alarmReminderList.get(i).getTime3());
                            newObj.setTime1(alarmReminderList.get(i).getTime1());
                            newObj.setTime5(alarmReminderList.get(i).getTime5());
                            newObj.setTime6(alarmReminderList.get(i).getTime6());
                            alarmReminders.add(newObj);
                        }
                        else if(doseTime.equals("Time5"))
                        {
                            newObj.setTime5(timeText);
                            newObj.setTime2(alarmReminderList.get(i).getTime2());
                            newObj.setTime3(alarmReminderList.get(i).getTime3());
                            newObj.setTime4(alarmReminderList.get(i).getTime4());
                            newObj.setTime1(alarmReminderList.get(i).getTime1());
                            newObj.setTime6(alarmReminderList.get(i).getTime6());
                            alarmReminders.add(newObj);
                        }
                        else if(doseTime.equals("Time6"))
                        {
                            newObj.setTime6(timeText);
                            newObj.setTime2(alarmReminderList.get(i).getTime2());
                            newObj.setTime3(alarmReminderList.get(i).getTime3());
                            newObj.setTime4(alarmReminderList.get(i).getTime4());
                            newObj.setTime5(alarmReminderList.get(i).getTime5());
                            newObj.setTime1(alarmReminderList.get(i).getTime1());
                            alarmReminders.add(newObj);
                        }*/
                //    }
             //       else
                //    {
             //           alarmReminders.add(alarmReminderList.get(i));
               //     }
//
         //       }
                System.out.println("Size:::::::"+alarmReminderList.size());

                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), alarmReminderList);
                horizontalList.setAdapter(hrAdapter);
                global.setAlarmTime(alarmReminderList);
                EditDoseFragment.this.getDialog().cancel();
            }
        });
        return view;
    }

    public void setTime(){
        new TimePickerDialog(getActivity(), timePickerListener, hour, minute,false).show();
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;
            updateTime(hour,minute);
        }
    };
    private void updateTime(int hours, int mins) {

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

        if(time != null){
            time.setText(aTime);
        }
    }
}
