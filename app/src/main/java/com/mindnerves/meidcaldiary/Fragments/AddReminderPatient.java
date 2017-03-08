package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.medico.util.AlarmService;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.Calendar;

import Application.MyApi;
import DB.DatabaseHandler;
import Model.NotificationVM;
import Model.Reminder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/23/15.
 */
public class AddReminderPatient extends Fragment {
    private String patientId = "";
    private Button buttonCalender1,buttonTime1,buttonAdd,buttonBack;
    private TextView editStartDate,editTime1;
    private int mHour, mMinute;
    private EditText titleTv;
    private String validation = "";
    private int flagValidation = 0;
    private String title,startDateString,timeString1;
    private Calendar calendar1 = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private int am_pm;
    MyApi api;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_add_patient,container,false);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        BackStress.staticflag = 0;
        globalTv.setText("Add New Reminder");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        patientId = session.getString("sessionID",null);
        mHour = calendar1.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar1.get(Calendar.MINUTE);
        am_pm = calendar1.get(Calendar.AM_PM);
        buttonCalender1 = (Button)view.findViewById(R.id.start_date_picker);
        titleTv = (EditText)view.findViewById(R.id.alarm);
        editStartDate = (TextView)view.findViewById(R.id.edit_start_date);
        editTime1 = (TextView)view.findViewById(R.id.edit_time1);
        buttonTime1 = (Button)view.findViewById(R.id.time_picker1);
        buttonTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime1();

            }
        });
        buttonCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });
        buttonAdd = (Button)view.findViewById(R.id.done);
        buttonBack = (Button)view.findViewById(R.id.back);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleTv.getText().toString();
                startDateString = editStartDate.getText().toString();
                timeString1 = editTime1.getText().toString();
                ArrayList<String> alarmIds = new ArrayList<String>();
                String alarmId = "";
                if(title.equals(""))
                {
                    validation = "Please Enter Title";
                    flagValidation = 1;
                }
                if(startDateString.equals(""))
                {
                    validation = validation+"\nPlease Enter Start Date";
                    flagValidation = 1;
                }
                if(timeString1.equals(""))
                {
                    validation = validation+"\nPlease Enter Time1";
                    flagValidation = 1;
                }
                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                }
                else
                {

                    startDateString = editStartDate.getText().toString();
                    timeString1 = editTime1.getText().toString();
                    String[] arrayDate = startDateString.split("-");
                    int day = Integer.parseInt(arrayDate[0]);
                    int month = Integer.parseInt(arrayDate[1]);
                    int year = Integer.parseInt(arrayDate[2]);
                    String[] arrayTime = timeString1.split(":");
                    int hour = Integer.parseInt(arrayTime[0]);
                    int minute = Integer.parseInt(arrayTime[1]);
                    System.out.println("Integer Date "+day+" "+month+" "+year);
                    System.out.println("Integer Time "+hour+" "+minute);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    System.out.println("Hour "+hour+"::::::" +minute);
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
                    Intent intent = new Intent(getActivity(), AlarmService.class);
                    intent.putExtra("title",title);
                    long trigerTime = D2MS(calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), hour,minute, 0);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) trigerTime,
                            intent, PendingIntent.FLAG_ONE_SHOT);
                    am.set(AlarmManager.RTC_WAKEUP, trigerTime , pendingIntent);
                    Toast.makeText(getActivity(), "Reminder is set ",Toast.LENGTH_SHORT).show();
                    alarmId = ""+trigerTime;
                    Reminder remOne = new Reminder();
                    remOne.setId(patientId);
                    System.out.println("In Loop :::::::: "+alarmId);
                    remOne.setAlarmId(alarmId);
                    remOne.setDate(startDateString);
                    remOne.setTime(timeString1);
                    remOne.setTitle(title);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    Boolean status = databaseHandler.saveReminderPatient(remOne);
                    ArrayList<Reminder> reminderAll = databaseHandler.getAllReminderPatient(patientId);
                    for(Reminder rem : reminderAll)
                    {
                        System.out.println("Reminder USerId "+rem.getId());
                        System.out.println("Reminder Title "+rem.getTitle());
                        System.out.println("Reminder Date "+rem.getDate());
                        System.out.println("Reminder Time "+rem.getTime());
                        System.out.println("Reminder AlarmId "+rem.getAlarmId());
                        System.out.println("Reminder UniqueId "+rem.getUniqueId());
                    }
                    for(Reminder rem: reminderAll){
                        rem.setAlarmId(null);
                        rem.setUniqueId(null);
                        rem.setSelected(null);

                    }
                    NotificationVM vm = new NotificationVM();
                    vm.reminders = reminderAll;
                    api.saveNotification(vm,new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            Fragment frag2 = new ManageReminderPatient();
                            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame,frag2,null);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageReminderPatient();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,frag2,null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;
    }
    public void setStartDate(){

        new DatePickerDialog(getActivity(),d1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void setTime1()
    {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        editTime1.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }
    public void updateStartDate()
    {
        // editStartDate.setText(formate.format(calendar1.getTime()));
        editStartDate.setText(""+calendar1.get(Calendar.DAY_OF_MONTH)+"-"+calendar1.get(Calendar.MONTH)+"-"+calendar1.get(Calendar.YEAR));
    }
    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar1.set(Calendar.YEAR,year);
            calendar1.set(Calendar.MONTH,monthOfYear);
            calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateStartDate();
        }

    };
    public long D2MS(int month, int day, int year, int hour, int minute, int seconds) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, seconds);
        return c.getTimeInMillis();
    }
    public void goBack()
    {
        Fragment frag2 = new ManageReminderPatient();
        System.out.println("I am in Resume Method::::");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                }
                return false;
            }
        });

    }
}
