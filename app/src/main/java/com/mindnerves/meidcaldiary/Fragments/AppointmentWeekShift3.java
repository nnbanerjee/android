package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.AppointmentWeekAdapter;
import Application.MyApi;
import Model.AllClinicAppointment;
import Model.ShiftAppointment;
import Model.Week;
import Model.WeekShow;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/7/15.
 */
public class AppointmentWeekShift3 extends Fragment {
    View convertView;
    Button weekDay1,weekDay2,weekDay3,weekDay4,weekDay5,weekDay6,weekDay7,rightArrow,leftArrow;
    TextView dateTv;
    int count = 7;
    SharedPreferences session;
    MyApi api;
    ProgressDialog progress;
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
    SimpleDateFormat dfYear = new SimpleDateFormat("dd-MMM-yyyy");
    Date currentDate = new Date();
    String startWeek = "";
    String doctorId,clinicId;
    String endWeek = "";
    ListView stickyList;
    ListView list1,list2,list3,list4,list5,list6,list7;
    Calendar cal = Calendar.getInstance();
    ArrayList<WeekShow> weekList;
    ArrayList<WeekShow> weekSlot3List1;
    ArrayList<WeekShow> weekSlot3List2;
    ArrayList<WeekShow> weekSlot3List3;
    ArrayList<WeekShow> weekSlot3List4;
    ArrayList<WeekShow> weekSlot3List5;
    ArrayList<WeekShow> weekSlot3List6;
    ArrayList<WeekShow> weekSlot3List7;
    ArrayList<Date> dateList = new ArrayList<Date>();
    ArrayList<Week> weeks = new ArrayList<Week>();
    TextView slot1Text,slot2Text;
    LinearLayout slot1Layout,slot2Layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_week,container,false);
        convertView = view;
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        list1 = (ListView)view.findViewById(R.id.slot3_list1);
        list2 = (ListView)view.findViewById(R.id.slot3_list2);
        list3 = (ListView)view.findViewById(R.id.slot3_list3);
        list4 = (ListView)view.findViewById(R.id.slot3_list4);
        list5 = (ListView)view.findViewById(R.id.slot3_list5);
        list6 = (ListView)view.findViewById(R.id.slot3_list6);
        list7 = (ListView)view.findViewById(R.id.slot3_list7);
        slot1Text = (TextView)view.findViewById(R.id.text_slot1);
        slot2Text = (TextView)view.findViewById(R.id.text_slot2);
        slot1Layout = (LinearLayout)view.findViewById(R.id.slot_layout1);
        slot2Layout = (LinearLayout)view.findViewById(R.id.slot_layout2);
        slot1Text.setVisibility(View.GONE);
        slot2Text.setVisibility(View.GONE);
        slot1Layout.setVisibility(View.GONE);
        slot2Layout.setVisibility(View.GONE);
        weekDay1 = (Button)view.findViewById(R.id.week_day1);
        weekDay2 = (Button)view.findViewById(R.id.week_day2);
        weekDay3 = (Button)view.findViewById(R.id.week_day3);
        weekDay4 = (Button)view.findViewById(R.id.week_day4);
        weekDay5 = (Button)view.findViewById(R.id.week_day5);
        weekDay6 = (Button)view.findViewById(R.id.week_day6);
        weekDay7 = (Button)view.findViewById(R.id.week_day7);
        leftArrow = (Button)view.findViewById(R.id.left_arrow);
        rightArrow = (Button)view.findViewById(R.id.right_arrow);
        dateTv = (TextView)view.findViewById(R.id.date_text);
        clinicId = session.getString("patient_clinicId","");
        doctorId = session.getString("sessionID","");
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        stickyList = (ListView) view.findViewById(R.id.list);
        startWeek = df.format(currentDate);
        for(int i=1;i<=7;i++)
        {
            switch(i)
            {
                case 1:
                    weekDay1.setText(df.format(currentDate));
                    break;
                case 2:
                    weekDay2.setText(df.format(currentDate));
                    break;
                case 3:
                    weekDay3.setText(df.format(currentDate));
                    break;
                case 4:
                    weekDay4.setText(df.format(currentDate));
                    break;
                case 5:
                    weekDay5.setText(df.format(currentDate));
                    break;
                case 6:
                    weekDay6.setText(df.format(currentDate));
                    break;
                case 7:
                    weekDay7.setText(df.format(currentDate));
                    break;
            }
            dateList.add(currentDate);
            cal.add(Calendar.DAY_OF_MONTH,1);
            currentDate = cal.getTime();
            System.out.println("Week date:::::"+df.format(currentDate));
        }
        endWeek = dfYear.format(currentDate);
        dateTv.setText(startWeek+" to "+endWeek);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateList.clear();
                startWeek = df.format(currentDate);
                for (int i = 1; i <= 7; i++)
                {
                    switch (i) {
                        case 1:
                            weekDay1.setText(df.format(currentDate));
                            break;
                        case 2:
                            weekDay2.setText(df.format(currentDate));
                            break;
                        case 3:
                            weekDay3.setText(df.format(currentDate));
                            break;
                        case 4:
                            weekDay4.setText(df.format(currentDate));
                            break;
                        case 5:
                            weekDay5.setText(df.format(currentDate));
                            break;
                        case 6:
                            weekDay6.setText(df.format(currentDate));
                            break;
                        case 7:
                            weekDay7.setText(df.format(currentDate));
                            break;
                    }
                    dateList.add(currentDate);
                    cal.add(Calendar.DAY_OF_MONTH,1);
                    currentDate = cal.getTime();
                    System.out.println("Week date:::::"+df.format(currentDate));
                }
                endWeek = dfYear.format(currentDate);
                dateTv.setText(startWeek+" to "+endWeek);
                showAppointmentSummary();
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateList.clear();
                startWeek = df.format(currentDate);
                for (int i = -1; i >= -7; i--)
                {
                    switch (i) {
                        case -1:
                            weekDay1.setText(df.format(currentDate));
                            break;
                        case -2:
                            weekDay2.setText(df.format(currentDate));
                            break;
                        case -3:
                            weekDay3.setText(df.format(currentDate));
                            break;
                        case -4:
                            weekDay4.setText(df.format(currentDate));
                            break;
                        case -5:
                            weekDay5.setText(df.format(currentDate));
                            break;
                        case -6:
                            weekDay6.setText(df.format(currentDate));
                            break;
                        case -7:
                            weekDay7.setText(df.format(currentDate));
                            break;
                    }
                    dateList.add(currentDate);
                    cal.add(Calendar.DAY_OF_MONTH,-1);
                    currentDate = cal.getTime();
                    System.out.println("Week date:::::"+df.format(currentDate));
                }

                endWeek = dfYear.format(currentDate);
                dateTv.setText(startWeek+" to "+endWeek);
                showAppointmentSummary();
            }
        });
        Week w1 = new Week();
        w1.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w1.setAppointmentTime("9:00 AM");
        weeks.add(w1);
        Week w2 = new Week();
        w2.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w2.setAppointmentTime("9:00 AM");
        weeks.add(w2);
        Week w3 = new Week();
        w3.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w3.setAppointmentTime("9:00 AM");
        weeks.add(w3);
        Week w4 = new Week();
        w4.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w4.setAppointmentTime("9:00 AM");
        weeks.add(w4);
        Week w5 = new Week();
        w5.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w5.setAppointmentTime("9:00 AM");
        weeks.add(w5);
        Week w6 = new Week();
        w6.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w6.setAppointmentTime("9:00 AM");
        weeks.add(w6);
        Week w7 = new Week();
        w7.setUrl("http://freedesignfile.com/upload/2013/11/Nature-1-100x100.jpg");
        w7.setAppointmentTime("9:00 AM");
        weeks.add(w7);
        list1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        list7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        showAppointmentSummary();
        return convertView;
    }
    public void showAppointmentSummary()
    {
        Date dateFirst = dateList.get(0);
        weekList = new ArrayList<WeekShow>();
        api.getAllClinicWeekAppointment(doctorId,clinicId,dateFirst.toString(),new Callback<List<AllClinicAppointment>>() {
            @Override
            public void success(List<AllClinicAppointment> allClinicAppointments, Response response) {
                for(Date d : dateList)
                {
                    for(AllClinicAppointment appointment : allClinicAppointments)
                    {
                        if((dfYear.format(d)).equals(appointment.appointmentDate))
                        {
                            List<ShiftAppointment> shift3List = appointment.shift3;
                            if(shift3List.size() != 0)
                            {
                                for(ShiftAppointment shift : shift3List) {
                                    WeekShow w1 = new WeekShow();
                                    w1.setSlot("SLOT3");
                                    w1.setAppointmentDate(appointment.appointmentDate);
                                    w1.setBookTime(shift.bookTime);
                                    w1.setPerson(shift.patientInfo);
                                    weekList.add(w1);
                                }

                            }
                        }
                    }
                }
                for(WeekShow sh : weekList)
                {
                    System.out.println("Slot:::::"+sh.getSlot());
                    System.out.println("Time::::::"+sh.getBookTime());
                    System.out.println("AppointmentDate::::::"+sh.getAppointmentDate());
                }
                int count = 1;
                weekSlot3List1 = new ArrayList<WeekShow>();
                weekSlot3List2 = new ArrayList<WeekShow>();
                weekSlot3List3 = new ArrayList<WeekShow>();
                weekSlot3List4 = new ArrayList<WeekShow>();
                weekSlot3List5 = new ArrayList<WeekShow>();
                weekSlot3List6 = new ArrayList<WeekShow>();
                weekSlot3List7 = new ArrayList<WeekShow>();
                for(Date d: dateList)
                {
                    switch(count)
                    {
                        case 1:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List1.add(ws);
                                    }
                                }
                            }
                            break;
                        case 2:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List2.add(ws);
                                    }
                                }
                            }
                            break;
                        case 3:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List3.add(ws);
                                    }
                                }
                            }
                            break;
                        case 4:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List4.add(ws);
                                    }
                                }
                            }
                            break;
                        case 5:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot4")) {
                                        weekSlot3List5.add(ws);
                                    }
                                }
                            }
                            break;
                        case 6:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List6.add(ws);
                                    }
                                }
                            }
                            break;
                        case 7:
                            for(WeekShow ws : weekList)
                            {
                                if((dfYear.format(d)).equals(ws.getAppointmentDate())) {
                                    if ((ws.getSlot()).equalsIgnoreCase("slot3")) {
                                        weekSlot3List7.add(ws);
                                    }
                                }
                            }
                            break;
                    }
                    count = count + 1;
                }
                if(weekSlot3List1.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List1.add(weekEmpty);

                }
                if(weekSlot3List2.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List2.add(weekEmpty);

                }
                if(weekSlot3List3.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List3.add(weekEmpty);

                }
                if(weekSlot3List4.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List4.add(weekEmpty);

                }
                if(weekSlot3List5.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List5.add(weekEmpty);

                }
                if(weekSlot3List6.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List6.add(weekEmpty);

                }
                if(weekSlot3List7.size() == 0)
                {
                    WeekShow weekEmpty = new WeekShow();
                    weekEmpty.setBookTime("N.A.");
                    weekSlot3List7.add(weekEmpty);

                }
                AppointmentWeekAdapter slot3List1Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List1);
                AppointmentWeekAdapter slot3List2Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List2);
                AppointmentWeekAdapter slot3List3Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List3);
                AppointmentWeekAdapter slot3List4Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List4);
                AppointmentWeekAdapter slot3List5Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List5);
                AppointmentWeekAdapter slot3List6Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List6);
                AppointmentWeekAdapter slot3List7Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List7);
                list1.setAdapter(slot3List1Adapter);
                list2.setAdapter(slot3List2Adapter);
                list3.setAdapter(slot3List3Adapter);
                list4.setAdapter(slot3List4Adapter);
                list5.setAdapter(slot3List5Adapter);
                list6.setAdapter(slot3List6Adapter);
                list7.setAdapter(slot3List7Adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
