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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.medico.util.AlarmService;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.medico.application.MyApi;
import com.medico.util.DatabaseHandler;
import Model.NotificationVM;
import Model.Reminder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 17-Mar-15.
 */
public class AddNewReminder extends Fragment {

    private String doctorId = "";
    private Button buttonCalender1,buttonCalender2,buttonTime1,buttonTime2,buttonTime3,buttonBack,buttonAdd,buttonAddTime1,buttonAddTime2,back;
    private TimePicker timePicker;
    private DateFormat formate = DateFormat.getDateInstance();
    private Calendar calendar1 = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private TextView editStartDate,editEndDate,editTime1,editTime2,editTime3;
    private int mHour, mMinute;
    private EditText titleTv;
    private String validation = "";
    private int flagValidation = 0;
    private RelativeLayout setTimeLayout2,setTimeLayout3;
    private String title,startDateString,endDateString,timeString1,timeString2,timeString3;
    private int flagAddTime1,flagAddTime2;
    private int am_pm;
    private int flag = 0;
    TextView globalTv;
    MyApi api;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });

    }

    public void goBack()
    {
        Fragment frag2 = new ManageReminder();
        System.out.println("I am in Resume Method::::");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void manageScreenIcons()
    {
        globalTv.setText("Add New Reminder");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reminder_add,container,false);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();
            }
        });
        doctorId = session.getString("sessionID",null);
        mHour = calendar1.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar1.get(Calendar.MINUTE);
        am_pm = calendar1.get(Calendar.AM_PM);
        flagAddTime1 = 1;
        flagAddTime2 = 1;
        flag = 1;
        buttonCalender1 = (Button)view.findViewById(R.id.start_date_picker);
        buttonCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });
        buttonCalender2 = (Button)view.findViewById(R.id.end_date_picker);
        buttonCalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate();
            }
        });
        setTimeLayout2 = (RelativeLayout)view.findViewById(R.id.multiple_time2);
        setTimeLayout3 = (RelativeLayout)view.findViewById(R.id.multiple_time3);
        editStartDate = (TextView)view.findViewById(R.id.edit_start_date);
        editEndDate = (TextView)view.findViewById(R.id.edit_end_date);
        editTime1 = (TextView)view.findViewById(R.id.edit_time1);
        editTime2 = (TextView)view.findViewById(R.id.edit_time2);
        editTime3 = (TextView)view.findViewById(R.id.edit_time3);
        titleTv = (EditText)view.findViewById(R.id.alarm);
        buttonBack = (Button)view.findViewById(R.id.back);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        titleTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(titleTv);
            }
        });

        buttonAdd = (Button)view.findViewById(R.id.done);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                int flagLayout2 = 0;
                int flagLayout3 = 0;
                flagValidation = 0;
                validation = "";
                timeString2 = "";
                timeString3 = "";
                title = titleTv.getText().toString();
                startDateString = editStartDate.getText().toString();
                endDateString = editStartDate.getText().toString();
                timeString1 = editTime1.getText().toString();

                ArrayList<String> alarmIds = new ArrayList<String>();
                String alarmId = "";


                if(flagAddTime1 == 0) {
                    timeString2 = editTime2.getText().toString();
                    flagLayout2 = 1;
                }

                if(flagAddTime2 == 0) {
                    timeString3 = editTime3.getText().toString();
                    flagLayout3 = 1;
                }


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
                if(endDateString.equals(""))
                {
                    validation = validation+"\nPlease Enter Start Date";
                    flagValidation = 1;
                }
                if(timeString1.equals(""))
                {
                    validation = validation+"\nPlease Enter Time1";
                    flagValidation = 1;
                }

                if((flagAddTime1 == 0)&&(flag != 1)) {
                    if (timeString2.equals("")) {
                        validation = validation + "\nPlease Enter Time2";
                        flagValidation = 1;

                    }
                }

                if((flagAddTime1 == 0)&&(flagAddTime1 != 0)) {
                    if (timeString3.equals("")) {
                        validation = validation + "\nPlease Enter Time2";
                        flagValidation = 1;

                    }
                }



                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                }
                else
                {

                    ArrayList<Reminder> remList = new ArrayList<Reminder>();
                    startDateString = editStartDate.getText().toString();
                    endDateString = editEndDate.getText().toString();

                    timeString1 = editTime1.getText().toString();
                    Reminder remStartTime1 = new Reminder();
                    remStartTime1.setDate(startDateString);
                    remStartTime1.setTime(timeString1);
                    Reminder remEndTime1 = new Reminder();
                    remEndTime1.setDate(endDateString);
                    remEndTime1.setTime(timeString1);
                    remList.add(remStartTime1);
                    remList.add(remEndTime1);

                    System.out.println("FlagLayout2 "+flagLayout2);
                    System.out.println("FlagLayout3 "+flagLayout3);
                    if(flagLayout2 == 1)
                    {
                        timeString2 = editTime2.getText().toString();
                        Reminder remStartTime2 = new Reminder();
                        remStartTime2.setDate(startDateString);
                        remStartTime2.setTime(timeString2);
                        Reminder remEndTime2 = new Reminder();
                        remEndTime2.setDate(endDateString);
                        remEndTime2.setTime(timeString2);
                        remList.add(remStartTime2);
                        remList.add(remEndTime2);

                    }
                    if(flagLayout3 == 1)
                    {
                        timeString3 = editTime3.getText().toString();
                        System.out.println("Time String3::::"+timeString3);
                        Reminder remStartTime3 = new Reminder();
                        remStartTime3.setDate(startDateString);
                        remStartTime3.setTime(timeString3);
                        Reminder remEndTime3 = new Reminder();
                        remEndTime3.setDate(endDateString);
                        remEndTime3.setTime(timeString3);
                        remList.add(remStartTime3);
                        remList.add(remEndTime3);

                    }

                    for(Reminder remResult : remList)
                    {
                        System.out.println("Reminder Date::: "+remResult.getDate());
                        System.out.println("Reminder Time::: "+remResult.getTime());

                        String[] arrayDate = remResult.getDate().split("-");
                        int day = Integer.parseInt(arrayDate[0]);
                        int month = Integer.parseInt(arrayDate[1]);
                        int year = Integer.parseInt(arrayDate[2]);

                        String[] arrayTime = remResult.getTime().split(":");
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

                        /*Intent intentDelete = new Intent(getActivity(), AlarmService.class);
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(),(int)trigerTime,
                                intentDelete, PendingIntent.FLAG_ONE_SHOT);
                        am.cancel(pendingIntent1);

                        System.out.println("Delete Alarm::::::::::::::::::::::::");*/

                        Toast.makeText(getActivity(), "Reminder is set ",Toast.LENGTH_SHORT).show();

                        System.out.println("Reminder is set //////////////////////");
                        alarmId = ""+trigerTime;
                        alarmIds.add(alarmId);



                    }


                    for (String s : alarmIds)
                    {
                        System.out.println("Alarm Id::::: "+s);
                    }
                    ArrayList<Reminder> arraySaveDatabase = new ArrayList<Reminder>();
                    System.out.println("RemList:::::::::"+remList.size());
                    int len = remList.size();
                    System.out.println("Alarm Array:::::::: "+alarmIds.size());
                    for(int i=0;i<len;i++)
                    {
                        Reminder remOne = new Reminder();
                        remOne.setId(doctorId);
                        System.out.println("In Loop :::::::: "+alarmIds.get(i));
                        remOne.setAlarmId(alarmIds.get(i));
                        remOne.setDate(remList.get(i).getDate());
                        remOne.setTime(remList.get(i).getTime());
                        remOne.setTitle(title);
                        arraySaveDatabase.add(remOne);
                    }
                    System.out.println(":::::::::::Before Save ");
                    for(Reminder remTwo : arraySaveDatabase)
                    {
                        System.out.println("Reminder USerId "+remTwo.getId());
                        System.out.println("Reminder Title "+remTwo.getTitle());
                        System.out.println("Reminder Date "+remTwo.getDate());
                        System.out.println("Reminder Time "+remTwo.getTime());
                        System.out.println("Reminder AlarmId "+remTwo.getAlarmId());
                    }

                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    Boolean status = databaseHandler.saveReminder(arraySaveDatabase);

                    ArrayList<String> uniqueIds = new ArrayList<String>();
                    ArrayList<Reminder> reminderAll = databaseHandler.getAllReminder(doctorId);

                    System.out.println("Database Retrieve:::::::::::::::");
                    for(Reminder rem : reminderAll)
                    {
                        System.out.println("Reminder USerId "+rem.getId());
                        System.out.println("Reminder Title "+rem.getTitle());
                        System.out.println("Reminder Date "+rem.getDate());
                        System.out.println("Reminder Time "+rem.getTime());
                        System.out.println("Reminder AlarmId "+rem.getAlarmId());
                        System.out.println("Reminder UniqueId "+rem.getUniqueId());
                        uniqueIds.add(rem.getUniqueId());
                    }
                    for(Reminder rem: arraySaveDatabase){
                        rem.setAlarmId(null);
                        rem.setUniqueId(null);
                        rem.setSelected(null);

                    }
                    NotificationVM vm = new NotificationVM();
                    vm.reminders = arraySaveDatabase;
                    api.saveNotification(vm,new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            if(s.equalsIgnoreCase("success")){
                            Fragment frag2 = new ManageReminder();
                            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame,frag2,null);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                            }
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

        buttonTime1 = (Button)view.findViewById(R.id.time_picker1);
        buttonTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime1();

            }
        });

        buttonTime2 = (Button)view.findViewById(R.id.time_picker2);
        buttonTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime2();

            }
        });

        buttonTime3 = (Button)view.findViewById(R.id.time_picker3);
        buttonTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime3();

            }
        });

        buttonAddTime1 = (Button)view.findViewById(R.id.add_time1);
        buttonAddTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flagAddTime1 == 1)
                {
                    setTimeLayout2.setVisibility(View.VISIBLE);
                    buttonAddTime1.setBackgroundResource(R.drawable.minus);
                    flagAddTime1 = 0;
                }
                else
                {
                    flagAddTime1 = 1;
                    setTimeLayout2.setVisibility(View.GONE);
                    buttonAddTime1.setBackgroundResource(R.drawable.add);

                }


            }
        });

        buttonAddTime2 = (Button)view.findViewById(R.id.add_time2);
        buttonAddTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAddTime2 == 1)
                {
                    setTimeLayout3.setVisibility(View.VISIBLE);
                    buttonAddTime2.setBackgroundResource(R.drawable.minus);
                    flagAddTime2 = 0;
                }
                else
                {
                    flagAddTime2 = 1;
                    setTimeLayout3.setVisibility(View.GONE);
                    buttonAddTime2.setBackgroundResource(R.drawable.add);
                }
            }
        });
        manageScreenIcons();
        return view;
    }
    public void updateStartDate()
    {
       // editStartDate.setText(formate.format(calendar1.getTime()));
        editStartDate.setText(""+calendar1.get(Calendar.DAY_OF_MONTH)+"-"+calendar1.get(Calendar.MONTH)+"-"+calendar1.get(Calendar.YEAR));
    }
    public void setStartDate(){

        new DatePickerDialog(getActivity(),d1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)).show();
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

    public void updateEndDate()
    {
      /*  editEndDate.setText(formate.format(calendar2.getTime()));*/
        editEndDate.setText(""+calendar2.get(Calendar.DAY_OF_MONTH)+"-"+calendar2.get(Calendar.MONTH)+"-"+calendar2.get(Calendar.YEAR));
    }
    public void setEndDate(){

        new DatePickerDialog(getActivity(),d2,calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),calendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar2.set(Calendar.YEAR,year);
            calendar2.set(Calendar.MONTH,monthOfYear);
            calendar2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateEndDate();
        }

    };

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

    public void setTime2()
    {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        editTime2.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }
    public void setTime3()
    {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        editTime3.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }
    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(titleTv)) ret = false;


        return ret;
    }


    public long D2MS(int month, int day, int year, int hour, int minute, int seconds) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, seconds);
        return c.getTimeInMillis();
    }
}
