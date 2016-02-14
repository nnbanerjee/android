package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.AppointmentWeekAdapter;
import Adapter.MyAdapter;
import Adapter.WeekAdapter;
import Application.MyApi;
import Model.AllClinicAppointment;
import Model.Appointment;
import Model.Shift1;
import Model.ShiftAppointment;
import Model.Week;
import Model.WeekShow;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by User on 6/30/15.
 */
public class AppointmentWeek extends Fragment {

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
    ListView slot2List1,slot2List2,slot2List3,slot2List4,slot2List5,slot2List6,slot2List7;
    ListView slot3List1,slot3List2,slot3List3,slot3List4,slot3List5,slot3List6,slot3List7;
    Calendar cal = Calendar.getInstance();
    ArrayList<WeekShow> weekList;
    ArrayList<WeekShow> weekSlot1List1;
    ArrayList<WeekShow> weekSlot1List2;
    ArrayList<WeekShow> weekSlot1List3;
    ArrayList<WeekShow> weekSlot1List4;
    ArrayList<WeekShow> weekSlot1List5;
    ArrayList<WeekShow> weekSlot1List6;
    ArrayList<WeekShow> weekSlot1List7;
    ArrayList<WeekShow> weekSlot2List1;
    ArrayList<WeekShow> weekSlot2List2;
    ArrayList<WeekShow> weekSlot2List3;
    ArrayList<WeekShow> weekSlot2List4;
    ArrayList<WeekShow> weekSlot2List5;
    ArrayList<WeekShow> weekSlot2List6;
    ArrayList<WeekShow> weekSlot2List7;
    ArrayList<WeekShow> weekSlot3List1;
    ArrayList<WeekShow> weekSlot3List2;
    ArrayList<WeekShow> weekSlot3List3;
    ArrayList<WeekShow> weekSlot3List4;
    ArrayList<WeekShow> weekSlot3List5;
    ArrayList<WeekShow> weekSlot3List6;
    ArrayList<WeekShow> weekSlot3List7;
    ArrayList<WeekShow> dateAppointments;
    ArrayList<Date> dateList = new ArrayList<Date>();
    ArrayList<Week> weeks = new ArrayList<Week>();
    View convertView;
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }
    public void  goToBack()
    {
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinics and Labs");
        Fragment fragment;
        fragment = new DoctorClinicFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_week,container,false);
        convertView = view;
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        list1 = (ListView)view.findViewById(R.id.list1);
        list2 = (ListView)view.findViewById(R.id.list2);
        list3 = (ListView)view.findViewById(R.id.list3);
        list4 = (ListView)view.findViewById(R.id.list4);
        list5 = (ListView)view.findViewById(R.id.list5);
        list6 = (ListView)view.findViewById(R.id.list6);
        list7 = (ListView)view.findViewById(R.id.list7);
        slot2List1 = (ListView)view.findViewById(R.id.slot2_list1);
        slot2List2 = (ListView)view.findViewById(R.id.slot2_list2);
        slot2List3 = (ListView)view.findViewById(R.id.slot2_list3);
        slot2List4 = (ListView)view.findViewById(R.id.slot2_list4);
        slot2List5 = (ListView)view.findViewById(R.id.slot2_list5);
        slot2List6 = (ListView)view.findViewById(R.id.slot2_list6);
        slot2List7 = (ListView)view.findViewById(R.id.slot2_list7);
        slot3List1 = (ListView)view.findViewById(R.id.slot3_list1);
        slot3List2 = (ListView)view.findViewById(R.id.slot3_list2);
        slot3List3 = (ListView)view.findViewById(R.id.slot3_list3);
        slot3List4 = (ListView)view.findViewById(R.id.slot3_list4);
        slot3List5 = (ListView)view.findViewById(R.id.slot3_list5);
        slot3List6 = (ListView)view.findViewById(R.id.slot3_list6);
        slot3List7 = (ListView)view.findViewById(R.id.slot3_list7);
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
        slot2List1.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List2.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List3.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List4.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List5.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List6.setOnTouchListener(new View.OnTouchListener() {
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
        slot2List7.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List1.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List2.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List3.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List4.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List5.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List6.setOnTouchListener(new View.OnTouchListener() {
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
        slot3List7.setOnTouchListener(new View.OnTouchListener() {
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


        /*WeekAdapter weekAdapter = new WeekAdapter(getActivity(),weeks);
        list1.setAdapter(weekAdapter);
        list2.setAdapter(weekAdapter);
        list3.setAdapter(weekAdapter);
        list4.setAdapter(weekAdapter);
        list5.setAdapter(weekAdapter);
        list6.setAdapter(weekAdapter);
        list7.setAdapter(weekAdapter);
        slot2List1.setAdapter(weekAdapter);
        slot2List2.setAdapter(weekAdapter);
        slot2List3.setAdapter(weekAdapter);
        slot2List4.setAdapter(weekAdapter);
        slot2List5.setAdapter(weekAdapter);
        slot2List6.setAdapter(weekAdapter);
        slot2List7.setAdapter(weekAdapter);
        slot3List1.setAdapter(weekAdapter);
        slot3List2.setAdapter(weekAdapter);
        slot3List3.setAdapter(weekAdapter);
        slot3List4.setAdapter(weekAdapter);
        slot3List5.setAdapter(weekAdapter);
        slot3List6.setAdapter(weekAdapter);
        slot3List7.setAdapter(weekAdapter);*/
        showAppointmentSummary();
        return view;
    }

    public Calendar getStringToCal(String str){

        String[] time = str.split(":");
        String[] timeMin = time[1].split(" ");
        int hr = Integer.parseInt(time[0].trim());
        int min =  Integer.parseInt(timeMin[0].trim());
        String am = timeMin[1].trim();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR, hr);
        if(am.equals("AM")){
            cal.set(Calendar.AM_PM,Calendar.AM);
        }else{
            cal.set(Calendar.AM_PM,Calendar.PM);
        }
        //cal.setTime(date);
        return cal;
    }



    public void showAppointmentSummary()
    {
           Date dateFirst = dateList.get(0);
           weekList = new ArrayList<WeekShow>();

           api.getAllClinicWeekAppointment(doctorId, clinicId, dateFirst.toString(), new Callback<List<AllClinicAppointment>>() {
               @Override
               public void success(List<AllClinicAppointment> allClinicAppointments, Response response) {

                   for(Date d : dateList)
                   {
                       for(AllClinicAppointment appointment : allClinicAppointments)
                       {
                            if((dfYear.format(d)).equals(appointment.appointmentDate))
                            {
                                List<ShiftAppointment> shift1List = appointment.shift1;
                                List<ShiftAppointment> shift2List = appointment.shift2;
                                List<ShiftAppointment> shift3List = appointment.shift3;
                                System.out.println("Condition true:::::::");

                                if(shift1List.size() != 0)
                                {
                                    for(ShiftAppointment shift : shift1List) {
                                        WeekShow w1 = new WeekShow();
                                        w1.setSlot("SLOT1");
                                        w1.setAppointmentDate(appointment.appointmentDate);
                                        w1.setBookTime(shift.bookTime);
                                        w1.setPerson(shift.patientInfo);
                                        weekList.add(w1);
                                    }

                                }
                                if(shift2List.size() != 0)
                                {
                                    for(ShiftAppointment shift : shift2List) {
                                        WeekShow w1 = new WeekShow();
                                        w1.setSlot("SLOT2");
                                        w1.setAppointmentDate(appointment.appointmentDate);
                                        w1.setBookTime(shift.bookTime);
                                        w1.setPerson(shift.patientInfo);
                                        weekList.add(w1);
                                    }

                                }
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
                   weekSlot1List1 = new ArrayList<WeekShow>();
                   weekSlot1List2 = new ArrayList<WeekShow>();
                   weekSlot1List3 = new ArrayList<WeekShow>();
                   weekSlot1List4 = new ArrayList<WeekShow>();
                   weekSlot1List5 = new ArrayList<WeekShow>();
                   weekSlot1List6 = new ArrayList<WeekShow>();
                   weekSlot1List7 = new ArrayList<WeekShow>();
                   weekSlot2List1 = new ArrayList<WeekShow>();
                   weekSlot2List2 = new ArrayList<WeekShow>();
                   weekSlot2List3 = new ArrayList<WeekShow>();
                   weekSlot2List4 = new ArrayList<WeekShow>();
                   weekSlot2List5 = new ArrayList<WeekShow>();
                   weekSlot2List6 = new ArrayList<WeekShow>();
                   weekSlot2List7 = new ArrayList<WeekShow>();
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
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List1.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List1.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List1.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 2:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List2.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List2.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List2.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 3:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List3.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List3.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List3.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 4:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List4.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List4.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List4.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 5:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List5.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List5.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List5.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 6:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List6.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List6.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List6.add(ws);
                                            }

                                        }
                                    }
                                    break;
                            case 7:
                                    for(WeekShow ws : weekList)
                                    {
                                        if((dfYear.format(d)).equals(ws.getAppointmentDate()))
                                        {
                                            if((ws.getSlot()).equalsIgnoreCase("slot1"))
                                            {
                                                weekSlot1List7.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot2"))
                                            {
                                                weekSlot2List7.add(ws);
                                            }
                                            else if((ws.getSlot()).equalsIgnoreCase("slot3"))
                                            {
                                                weekSlot3List7.add(ws);
                                            }

                                        }
                                    }
                                    break;

                        }
                       count = count + 1;
                   }

                   if(weekSlot1List1.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List1.add(weekEmpty);

                   }
                   if(weekSlot1List2.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List2.add(weekEmpty);

                   }
                   if(weekSlot1List3.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List3.add(weekEmpty);

                   }
                   if(weekSlot1List4.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List4.add(weekEmpty);

                   }
                   if(weekSlot1List5.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List5.add(weekEmpty);

                   }
                   if(weekSlot1List6.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List6.add(weekEmpty);

                   }
                   if(weekSlot1List7.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot1List7.add(weekEmpty);

                   }
                   if(weekSlot2List1.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List1.add(weekEmpty);

                   }
                   if(weekSlot2List2.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List2.add(weekEmpty);

                   }
                   if(weekSlot2List3.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List3.add(weekEmpty);

                   }
                   if(weekSlot2List4.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List4.add(weekEmpty);

                   }
                   if(weekSlot2List5.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List5.add(weekEmpty);

                   }
                   if(weekSlot2List6.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List6.add(weekEmpty);

                   }
                   if(weekSlot2List7.size() == 0)
                   {
                       WeekShow weekEmpty = new WeekShow();
                       weekEmpty.setBookTime("N.A.");
                       weekSlot2List7.add(weekEmpty);

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

                   AppointmentWeekAdapter slot1List1Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List1);
                   AppointmentWeekAdapter slot1List2Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List2);
                   AppointmentWeekAdapter slot1List3Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List3);
                   AppointmentWeekAdapter slot1List4Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List4);
                   AppointmentWeekAdapter slot1List5Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List5);
                   AppointmentWeekAdapter slot1List6Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List6);
                   AppointmentWeekAdapter slot1List7Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot1List7);
                   AppointmentWeekAdapter slot2List1Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List1);
                   AppointmentWeekAdapter slot2List2Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List2);
                   AppointmentWeekAdapter slot2List3Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List3);
                   AppointmentWeekAdapter slot2List4Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List4);
                   AppointmentWeekAdapter slot2List5Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List5);
                   AppointmentWeekAdapter slot2List6Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List6);
                   AppointmentWeekAdapter slot2List7Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot2List7);
                   AppointmentWeekAdapter slot3List1Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List1);
                   AppointmentWeekAdapter slot3List2Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List2);
                   AppointmentWeekAdapter slot3List3Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List3);
                   AppointmentWeekAdapter slot3List4Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List4);
                   AppointmentWeekAdapter slot3List5Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List5);
                   AppointmentWeekAdapter slot3List6Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List6);
                   AppointmentWeekAdapter slot3List7Adapter = new AppointmentWeekAdapter(getActivity(),weekSlot3List7);
                   list1.setAdapter(slot1List1Adapter);
                   list2.setAdapter(slot1List2Adapter);
                   list3.setAdapter(slot1List3Adapter);
                   list4.setAdapter(slot1List4Adapter);
                   list5.setAdapter(slot1List5Adapter);
                   list6.setAdapter(slot1List6Adapter);
                   list7.setAdapter(slot1List7Adapter);
                   slot2List1.setAdapter(slot2List1Adapter);
                   slot2List2.setAdapter(slot2List2Adapter);
                   slot2List3.setAdapter(slot2List3Adapter);
                   slot2List4.setAdapter(slot2List4Adapter);
                   slot2List5.setAdapter(slot2List5Adapter);
                   slot2List6.setAdapter(slot2List6Adapter);
                   slot2List7.setAdapter(slot2List7Adapter);
                   slot3List1.setAdapter(slot3List1Adapter);
                   slot3List2.setAdapter(slot3List2Adapter);
                   slot3List3.setAdapter(slot3List3Adapter);
                   slot3List4.setAdapter(slot3List4Adapter);
                   slot3List5.setAdapter(slot3List5Adapter);
                   slot3List6.setAdapter(slot3List6Adapter);
                   slot3List7.setAdapter(slot3List7Adapter);

               }

               @Override
               public void failure(RetrofitError error) {
                    error.printStackTrace();
               }
           });


    }
}
