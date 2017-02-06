package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import Adapter.CalendarAdapter;
import Adapter.MyAdapter;
import Application.MyApi;
import Model.AllClinicAppointment;
import Model.Appointment;
import com.medico.model.Clinic;
import Model.ShiftAppointment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by User on 7/2/15.
 */
public class CalendarFragment extends Fragment {

    StickyListHeadersListView stickyList;
    String doctorId,clinicId;
    SharedPreferences session;
    String slot1StartTime,slot1EndTime,slot2StartTime,slot2EndTime,slot3StartTime,slot3EndTime;
    Calendar startTimeSlot1 = null;
    Calendar endTimeSlot1 = null;
    Calendar startTimeSlot2 = null;
    Calendar endTimeSlot2 = null;
    Calendar startTimeSlot3 = null;
    Calendar endTimeSlot3 = null;
    ProgressDialog progress;
    Global global;
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    MyApi api;
    public GregorianCalendar month, itemmonth;// calendar instances.
    String type = "";
    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    MyAdapter adapterList;
    ImageView plus,minus;
    GridView gridview;
    boolean checkCalOpen = false;
    public ArrayList<String> items;
    Clinic clinic = new Clinic();// container to store calendar items which
    // needs showing the event marker

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();
        stickyList = (StickyListHeadersListView) getActivity().findViewById(R.id.list_month);
        items = new ArrayList<String>();
        global = (Global) getActivity().getApplicationContext();
        adapter = new CalendarAdapter(getActivity().getApplicationContext(), month);
        clinicId = session.getString("patient_clinicId","");
        doctorId = session.getString("sessionID","");
        RelativeLayout open = (RelativeLayout) view.findViewById(R.id.open);
        plus = (ImageView) view.findViewById(R.id.plus);
        minus = (ImageView) view.findViewById(R.id.minus);
        type = session.getString("loginType",null);
        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        handler = new Handler();
        handler.post(calendarUpdater);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) view.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkCalOpen){
                    plus.setVisibility(View.GONE);
                    minus.setVisibility(View.VISIBLE);
                    checkCalOpen = false;
                    gridview.setVisibility(View.VISIBLE);
                }else{
                    plus.setVisibility(View.VISIBLE);
                    minus.setVisibility(View.GONE);
                    checkCalOpen = true;
                    gridview.setVisibility(View.GONE);
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate;
                showToast(selectedGridDate);
                try
                {
                    currentDate = df.parse(selectedGridDate);
                    System.out.println("CurrentDate::::::"+currentDate.toString());
                    getAllClinicsAppointmentByDate(currentDate);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        Date currentDate = new Date();
        System.out.println("CurrentDate::::::"+currentDate.toString());
        getAllClinicsAppointmentByDate(currentDate);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment appointment = (Appointment)adapterList.getItem(position);
                System.out.println("Appointment:::::::"+appointment.getAppointmentTime());
                global.setAppointment(appointment);
                if(appointment.getPersonInfo()== null)
                {
                    if(appointment.getAppointmentStatus().equals("Available")) {
                        System.out.println("Patient Not Present///////////////");
                        Fragment fragment = new AddDoctorAppointment();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                    else
                    {
                        if(type.equalsIgnoreCase("doctor"))
                        {
                            Fragment fragment = new EditDoctorAppointment();
                            FragmentManager fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                        }
                    }

                }
            }
        });
        return view;
    }
    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    protected void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        checkCalOpen = true;
        gridview.setVisibility(View.GONE);

    }

    public void refreshCalendar() {
        TextView title = (TextView) getView().findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            for (int i = 0; i < 7; i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add("2012-09-12");
                items.add("2012-10-07");
                items.add("2012-10-15");
                items.add("2012-10-20");
                items.add("2012-11-30");
                items.add("2012-11-28");
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

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

    public void getAllClinicsAppointmentByDate(Date currentDateFirst)
    {
        appointments.clear();
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        api.getAllClinicsAppointmentData(doctorId, clinicId, currentDateFirst, new Callback<List<AllClinicAppointment>>() {
            @Override
            public void success(List<AllClinicAppointment> ClinicList, Response response) {

                List<ShiftAppointment> shift1 = null;
                List<ShiftAppointment> shift2 = null;
                List<ShiftAppointment> shift3 = null;
                System.out.println("clinicList = "+ClinicList.size());
                //Global global = (Global) getActivity().getApplicationContext();
                //global.setClinicDetailVm(clinicDetailVm);
                List<Clinic> clinics = global.getAllClinicsList();
                for(Clinic c : clinics)
                {
                    if(c.getIdClinic().equals(clinicId))
                    {
                        clinic = c;
                        break;
                    }
                }
                if(ClinicList.size() != 0){
                    AllClinicAppointment allClinicAppointment = ClinicList.get(0);

                    if(allClinicAppointment.shift1.size() != 0){
                        shift1 = allClinicAppointment.shift1;
                        ShiftAppointment shiftAppointment = shift1.get(0);
                        String[] time =  shiftAppointment.timeSlot.split("to");
                        startTimeSlot1 = getStringToCal(time[0]);
                        endTimeSlot1 = getStringToCal(time[1]);
                        slot1StartTime = time[0];
                        slot1EndTime = time[1];
                    }
                    if(allClinicAppointment.shift2.size() != 0){
                        shift2 = allClinicAppointment.shift2;
                        ShiftAppointment shiftAppointment = shift2.get(0);
                        String[] time =  shiftAppointment.timeSlot.split("to");
                        startTimeSlot2 = getStringToCal(time[0]);
                        endTimeSlot2 = getStringToCal(time[1]);
                        slot2StartTime = time[0];
                        slot2EndTime = time[1];
                    }
                    if(allClinicAppointment.shift3.size() != 0){
                        shift3 = allClinicAppointment.shift3;
                        ShiftAppointment shiftAppointment = shift3.get(0);
                        String[] time =  shiftAppointment.timeSlot.split("to");
                        startTimeSlot3 = getStringToCal(time[0]);
                        endTimeSlot3 = getStringToCal(time[1]);
                        slot3StartTime = time[0];
                        slot3EndTime = time[1];
                    }
                }
                else
                {
                    List<Clinic> clinicList = global.getAllClinicsList();

                    for(Clinic clinic  : clinicList)
                    {
                        if (clinic.getIdClinic().equals(""+clinicId)) {

                            if(clinic.getShift1() != null) {
                                String[] time = clinic.getShift1().getShiftTime().split("to");
                                startTimeSlot1 = getStringToCal(time[0]);
                                endTimeSlot1 = getStringToCal(time[1]);
                                slot1StartTime = time[0];
                                slot1EndTime = time[1];
                                shift1 = new ArrayList<ShiftAppointment>();
                            }
                            if(clinic.getShift2() != null) {
                                String[] time = clinic.getShift2().getShiftTime().split("to");
                                startTimeSlot2 = getStringToCal(time[0]);
                                endTimeSlot2 = getStringToCal(time[1]);
                                slot2StartTime = time[0];
                                slot2EndTime = time[1];
                                shift2 = new ArrayList<ShiftAppointment>();
                            }
                            if(clinic.getShift3() != null) {
                                String[] time = clinic.getShift3().getShiftTime().split("to");
                                startTimeSlot3 = getStringToCal(time[0]);
                                endTimeSlot3 = getStringToCal(time[1]);
                                slot3StartTime = time[0];
                                slot3EndTime = time[1];
                                shift3 = new ArrayList<ShiftAppointment>();
                            }

                        }
                    }
                }

                int count = 0;
                if(startTimeSlot1 != null) {
                    while (endTimeSlot1.getTimeInMillis() > startTimeSlot1.getTimeInMillis()) {
                        count = count + 1;
                        String timeHHMM = null;
                        timeHHMM = startTimeSlot1.get(Calendar.HOUR) + ":" + startTimeSlot1.get(Calendar.MINUTE);
                        if (startTimeSlot1.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                        } else {
                            timeHHMM = timeHHMM + "PM";
                        }
                        Appointment app = new Appointment();
                        app.setAppointmentCount(count);
                        app.setSlot("Slot 1");

                        app.setAppointmentTime(timeHHMM);
                        app.setStartTime(slot1StartTime);
                        app.setEndTime(slot1EndTime);
                        if(clinic.getShift1().getShiftAvailability() != null) {
                            app.setAppointmentStatus(clinic.getShift1().getShiftAvailability());
                        }

                        if (shift1 != null || shift1.size() != 0) {
                            for (ShiftAppointment shiftAppointment : shift1) {
                                if (shiftAppointment.bookTime.equals(timeHHMM)) {
                                    app.setAppointmentType(shiftAppointment.appointmentType);
                                    if(shiftAppointment.patientInfo != null) {
                                        app.setPersonInfo(shiftAppointment.patientInfo);
                                    }
                                    else
                                    {
                                        app.setAppointmentStatus(shiftAppointment.status);
                                    }
                                    break;
                                }
                            }
                        }

                        appointments.add(app);
                        startTimeSlot1.add(Calendar.MINUTE, 15);
                    }
                }
                count = 0;
                if(startTimeSlot2!=null) {
                    while (endTimeSlot2.getTimeInMillis() > startTimeSlot2.getTimeInMillis()) {
                        count = count + 1;
                        String timeHHMM = null;
                        timeHHMM = startTimeSlot2.get(Calendar.HOUR) + ":" + startTimeSlot2.get(Calendar.MINUTE);
                        if (startTimeSlot2.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                        } else {
                            timeHHMM = timeHHMM + "PM";
                        }
                        Appointment app = new Appointment();
                        app.setAppointmentCount(count);
                        app.setSlot("Slot 2");
                        app.setAppointmentTime(timeHHMM);
                        app.setStartTime(slot2StartTime);
                        app.setEndTime(slot2EndTime);
                        if(clinic.getShift2().getShiftAvailability() != null) {
                            app.setAppointmentStatus(clinic.getShift2().getShiftAvailability());
                        }

                        if (shift2 != null || shift2.size() != 0) {
                            for (ShiftAppointment shiftAppointment : shift2) {
                                if (shiftAppointment.bookTime.equals(timeHHMM)) {
                                    app.setAppointmentType(shiftAppointment.appointmentType);
                                    if(shiftAppointment.patientInfo != null) {
                                        app.setPersonInfo(shiftAppointment.patientInfo);
                                    }
                                    else
                                    {
                                        app.setAppointmentStatus(shiftAppointment.status);
                                    }
                                    break;
                                }
                            }
                        }
                        appointments.add(app);
                        startTimeSlot2.add(Calendar.MINUTE, 15);
                    }
                }
                count = 0;
                if(startTimeSlot3!=null) {
                    while (endTimeSlot3.getTimeInMillis() > startTimeSlot3.getTimeInMillis()) {
                        count = count + 1;
                        String timeHHMM = null;
                        timeHHMM = startTimeSlot3.get(Calendar.HOUR) + ":" + startTimeSlot3.get(Calendar.MINUTE);
                        if (startTimeSlot3.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                        } else {
                            timeHHMM = timeHHMM + "PM";
                        }
                        Appointment app = new Appointment();
                        app.setAppointmentCount(count);
                        app.setSlot("Slot 3");
                        app.setAppointmentTime(timeHHMM);
                        app.setStartTime(slot3StartTime);
                        app.setEndTime(slot3EndTime);
                        if(clinic.getShift3().getShiftAvailability() != null) {
                            app.setAppointmentStatus(clinic.getShift3().getShiftAvailability());
                        }
                        if (shift3 != null || shift3.size() != 0) {
                            for (ShiftAppointment shiftAppointment : shift3) {
                                if (shiftAppointment.bookTime.equals(timeHHMM)) {
                                    app.setAppointmentType(shiftAppointment.appointmentType);
                                    if(shiftAppointment.patientInfo != null) {
                                        app.setPersonInfo(shiftAppointment.patientInfo);
                                    }
                                    else
                                    {
                                        app.setAppointmentStatus(shiftAppointment.status);
                                    }
                                    break;
                                }
                            }
                        }

                        appointments.add(app);
                        startTimeSlot3.add(Calendar.MINUTE, 15);
                    }
                }
                adapterList = new MyAdapter(getActivity().getApplicationContext(),appointments);
                stickyList.setAdapter(adapterList);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
