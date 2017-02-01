package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.MyAdapter;
import Application.MyApi;
import Model.AllClinicAppointment;
import Model.Appointment;
import Model.Clinic;
import Model.ShiftAppointment;
import Model.TimeInterval;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by User on 6/30/15.
 */
public class AppointmentDay extends Fragment {

    String slot1StartTime,slot1EndTime,slot2StartTime,slot2EndTime,slot3StartTime,slot3EndTime;
    Calendar startTimeSlot1 = null;
    Calendar endTimeSlot1 = null;
    Calendar startTimeSlot2 = null;
    Calendar endTimeSlot2 = null;
    Calendar startTimeSlot3 = null;
    Calendar endTimeSlot3 = null;
    String doctorId,clinicId;
    SharedPreferences session;
    ProgressDialog progress;
    MyApi api;
    StickyListHeadersListView stickyList;
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    Date currentDate = new Date();
    Button rightArrow,leftArrow;
    TextView dateTv;
    int countDate = 0;
    Global global;
    MyAdapter adapter;
    String shift1AppointmentStatus = "";
    String shift2AppointmentStatus = "";
    String shift3AppointmentStatus = "";
    String type = "";
    Clinic clinic = new Clinic();
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
        final View view = inflater.inflate(R.layout.appointment_day,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        rightArrow = (Button)view.findViewById(R.id.right_arrow);
        leftArrow = (Button)view.findViewById(R.id.left_arrow);
        dateTv = (TextView)view.findViewById(R.id.date_text);
        global = (Global) getActivity().getApplicationContext();
        clinicId = session.getString("patient_clinicId","");
        doctorId = session.getString("sessionID","");
        type = session.getString("loginType",null);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);

        getAllClinicsAppointment();

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointments.clear();
                countDate = countDate +1;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH,countDate);
                currentDate = cal.getTime();
                System.out.println("Addition of Date::::::"+currentDate.toString());
                dateTv.setText(df.format(currentDate));
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                api.getAllClinicsAppointmentData(doctorId,clinicId,currentDate,new Callback<List<AllClinicAppointment>>() {
                    @Override
                    public void success(List<AllClinicAppointment> ClinicList, Response response) {

                        List<ShiftAppointment> shift1 = null;
                        List<ShiftAppointment> shift2 = null;
                        List<ShiftAppointment> shift3 = null;
                        System.out.println("clinicList = "+ClinicList.size());
                        //Global global = (Global) getActivity().getApplicationContext();
                        //global.setClinicDetailVm(clinicDetailVm);

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
                                if (clinic.getIdClinic().equals(clinicId)) {

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
                        if(startTimeSlot1 != null)
                        {
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
                                app.setAppointmentStatus(clinic.getShift1().getShiftAvailability());

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
                                app.setAppointmentStatus(clinic.getShift2().getShiftAvailability());


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
                                app.setAppointmentStatus(clinic.getShift3().getShiftAvailability());
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

                        adapter = new MyAdapter(getActivity().getApplicationContext(),appointments);
                        stickyList.setAdapter(adapter);
                        progress.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointments.clear();
                countDate = countDate-1;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH,countDate);
                currentDate = cal.getTime();
                System.out.println("Addition of Date::::::"+currentDate.toString());
                dateTv.setText(df.format(currentDate));
                api.getAllClinicsAppointmentData(doctorId,clinicId,currentDate,new Callback<List<AllClinicAppointment>>() {
                    @Override
                    public void success(List<AllClinicAppointment> ClinicList, Response response) {
                        List<ShiftAppointment> shift1 = null;
                        List<ShiftAppointment> shift2 = null;
                        List<ShiftAppointment> shift3 = null;
                        System.out.println("clinicList = "+ClinicList.size());
                        //Global global = (Global) getActivity().getApplicationContext();
                        //global.setClinicDetailVm(clinicDetailVm);

                        if(ClinicList.size() != 0)
                        {
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
                                if (clinic.getIdClinic().equals(clinicId)) {

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
                                app.setAppointmentStatus(clinic.getShift1().getShiftAvailability());
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
                                app.setAppointmentStatus(clinic.getShift2().getShiftAvailability());

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
                                app.setAppointmentStatus(clinic.getShift3().getShiftAvailability());
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

                        adapter = new MyAdapter(getActivity().getApplicationContext(),appointments);
                        stickyList.setAdapter(adapter);

                        progress.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });

            }
        });
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment appointment = (Appointment)adapter.getItem(position);
                System.out.println("Appointment:::::::"+appointment.getAppointmentTime());
                global.setAppointment(appointment);
                if(appointment.getPersonInfo()== null) {
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

    public void getAllClinicsAppointment()
    {
        adapter = null;
        dateTv.setText(df.format(currentDate));
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        api.getAllClinicsAppointmentData(doctorId, clinicId, currentDate, new Callback<List<AllClinicAppointment>>() {
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
                        if (clinic.getIdClinic().equals(clinicId)) {

                            if(clinic.getShift1() != null) {
                                String[] time = clinic.getShift1().getShiftTime().split("to");
                                startTimeSlot1 = getStringToCal(time[0]);
                                endTimeSlot1 = getStringToCal(time[1]);
                                slot1StartTime = time[0];
                                slot1EndTime = time[1];
                                shift1 = new ArrayList<ShiftAppointment>();
                                shift1AppointmentStatus = clinic.getShift1().getShiftAvailability();

                            }
                            if(clinic.getShift2() != null) {
                                String[] time = clinic.getShift2().getShiftTime().split("to");
                                startTimeSlot2 = getStringToCal(time[0]);
                                endTimeSlot2 = getStringToCal(time[1]);
                                slot2StartTime = time[0];
                                slot2EndTime = time[1];
                                shift2 = new ArrayList<ShiftAppointment>();
                                shift2AppointmentStatus = clinic.getShift2().getShiftAvailability();

                            }
                            if(clinic.getShift3() != null) {
                                String[] time = clinic.getShift3().getShiftTime().split("to");
                                startTimeSlot3 = getStringToCal(time[0]);
                                endTimeSlot3 = getStringToCal(time[1]);
                                slot3StartTime = time[0];
                                slot3EndTime = time[1];
                                shift3 = new ArrayList<ShiftAppointment>();
                                shift3AppointmentStatus = clinic.getShift3().getShiftAvailability();

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

                        if (shift1.size() != 0) {
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
                        if (shift2.size() != 0) {
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
                        if (shift3.size() != 0) {
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
                for(Appointment appointment : appointments)
                {
                    System.out.println("Status::::::::::::"+appointment.getAppointmentStatus());
                }
                adapter = new MyAdapter(getActivity().getApplicationContext(),appointments);
                stickyList.setAdapter(adapter);
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