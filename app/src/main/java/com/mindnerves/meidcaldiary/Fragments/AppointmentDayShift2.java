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
import com.medico.application.MyApi;
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
 * Created by User on 9/7/15.
 */
public class AppointmentDayShift2 extends Fragment {
    String slot2StartTime,slot2EndTime;
    Calendar startTimeSlot2 = null;
    Calendar endTimeSlot2 = null;
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
    MyAdapter adapter;
    Global global;
    String backString = "";
    Clinic clinic = new Clinic();
    String type = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.appointment_day,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        rightArrow = (Button)view.findViewById(R.id.right_arrow);
        leftArrow = (Button)view.findViewById(R.id.left_arrow);
        global = (Global) getActivity().getApplicationContext();
        dateTv = (TextView)view.findViewById(R.id.date_text);
        clinicId = session.getString("patient_clinicId","");
        doctorId = session.getString("sessionID","");
        type = session.getString("loginType",null);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Back Button clicked/////////////////////////////");
                goBack();
            }
        });
        stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
        backString = getArguments().getString("fragment");
        System.out.println("shift2//////////////////"+backString);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        List<ShiftAppointment> shift2 = null;
                        List<Clinic> clinics = global.getAllClinicsList();
                        for(Clinic c : clinics)
                        {
                            if(c.getIdClinic().equals(clinicId))
                            {
                                clinic = c;
                                break;
                            }
                        }
                        if(ClinicList.size() != 0) {
                            AllClinicAppointment allClinicAppointment = ClinicList.get(0);
                            if(allClinicAppointment.shift2.size() != 0){
                                shift2 = allClinicAppointment.shift2;
                                ShiftAppointment shiftAppointment = shift2.get(0);
                                String[] time =  shiftAppointment.timeSlot.split("to");
                                startTimeSlot2 = getStringToCal(time[0]);
                                endTimeSlot2 = getStringToCal(time[1]);
                                slot2StartTime = time[0];
                                slot2EndTime = time[1];
                            }
                            else
                            {
                                List<Clinic> clinicList = global.getAllClinicsList();

                                for(Clinic clinic  : clinicList) {
                                    if (clinic.getIdClinic().equals(""+clinicId)) {
                                        String[] time =  clinic.getShift2().getShiftTime().split("to");
                                        startTimeSlot2 = getStringToCal(time[0]);
                                        endTimeSlot2 = getStringToCal(time[1]);
                                        slot2StartTime = time[0];
                                        slot2EndTime = time[1];
                                        shift2 = new ArrayList<ShiftAppointment>();
                                    }
                                }
                            }
                        }
                        else
                        {
                            List<Clinic> clinicList = global.getAllClinicsList();

                            for(Clinic clinic  : clinicList) {
                                if (clinic.getIdClinic().equals(""+clinicId)) {
                                    String[] time =  clinic.getShift2().getShiftTime().split("to");
                                    startTimeSlot2 = getStringToCal(time[0]);
                                    endTimeSlot2 = getStringToCal(time[1]);
                                    slot2StartTime = time[0];
                                    slot2EndTime = time[1];
                                    shift2 = new ArrayList<ShiftAppointment>();
                                }
                            }
                        }
                        int count = 0;
                        if(startTimeSlot2 != null)
                        {
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
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDate = countDate-1;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH,countDate);
                currentDate = cal.getTime();
                System.out.println("Addition of Date::::::"+currentDate.toString());
                dateTv.setText(df.format(currentDate));
                api.getAllClinicsAppointmentData(doctorId,clinicId,currentDate,new Callback<List<AllClinicAppointment>>() {
                    @Override
                    public void success(List<AllClinicAppointment> ClinicList, Response response) {
                        List<ShiftAppointment> shift2 = null;
                        List<Clinic> clinics = global.getAllClinicsList();
                        for(Clinic c : clinics)
                        {
                            if(c.getIdClinic().equals(clinicId))
                            {
                                clinic = c;
                                break;
                            }
                        }
                        if(ClinicList.size() != 0) {
                            AllClinicAppointment allClinicAppointment = ClinicList.get(0);
                            if(allClinicAppointment.shift2.size() != 0){
                                shift2 = allClinicAppointment.shift2;
                                ShiftAppointment shiftAppointment = shift2.get(0);
                                String[] time =  shiftAppointment.timeSlot.split("to");
                                startTimeSlot2 = getStringToCal(time[0]);
                                endTimeSlot2 = getStringToCal(time[1]);
                                slot2StartTime = time[0];
                                slot2EndTime = time[1];
                            }
                        }
                        else
                        {
                            List<Clinic> clinicList = global.getAllClinicsList();

                            for(Clinic clinic  : clinicList) {
                                if (clinic.getIdClinic().equals(""+clinicId)) {
                                    String[] time =  clinic.getShift2().getShiftTime().split("to");
                                    startTimeSlot2 = getStringToCal(time[0]);
                                    endTimeSlot2 = getStringToCal(time[1]);
                                    slot2StartTime = time[0];
                                    slot2EndTime = time[1];
                                    shift2 = new ArrayList<ShiftAppointment>();
                                }
                            }
                        }
                        int count = 0;
                        if(startTimeSlot2 != null)
                        {
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
        getAllClinicsAppointment();

        return view;
    }

    public void getAllClinicsAppointment()
    {
        dateTv.setText(df.format(currentDate));
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        api.getAllClinicsAppointmentData(doctorId,clinicId,currentDate,new Callback<List<AllClinicAppointment>>() {
            @Override
            public void success(List<AllClinicAppointment> ClinicList, Response response) {
                List<ShiftAppointment> shift2 = null;
                System.out.println("clinicList = "+ClinicList.size());
                List<Clinic> clinics = global.getAllClinicsList();
                for(Clinic c : clinics)
                {
                    if(c.getIdClinic().equals(clinicId))
                    {
                        clinic = c;
                        break;
                    }
                }
                if(ClinicList.size() != 0)
                {
                    AllClinicAppointment allClinicAppointment = ClinicList.get(0);
                    if(allClinicAppointment.shift2.size() != 0){
                        shift2 = allClinicAppointment.shift2;
                        ShiftAppointment shiftAppointment = shift2.get(0);
                        String[] time =  shiftAppointment.timeSlot.split("to");
                        startTimeSlot2 = getStringToCal(time[0]);
                        endTimeSlot2 = getStringToCal(time[1]);
                        slot2StartTime = time[0];
                        slot2EndTime = time[1];
                    }
                }
                else
                {
                    List<Clinic> clinicList = global.getAllClinicsList();

                    for(Clinic clinic  : clinicList) {
                        if (clinic.getIdClinic().equals(""+clinicId)) {
                            String[] time =  clinic.getShift2().getShiftTime().split("to");
                            startTimeSlot2 = getStringToCal(time[0]);
                            endTimeSlot2 = getStringToCal(time[1]);
                            slot2StartTime = time[0];
                            slot2EndTime = time[1];
                            shift2 = new ArrayList<ShiftAppointment>();
                        }
                    }
                }
                int count = 0;
                if(startTimeSlot2 != null)
                {
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

    public void goBack()
    {
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinic");
        Fragment fragmentMain;
        if(backString.equalsIgnoreCase("from_summary"))
        {
            fragmentMain = new DoctorAllClinics();
        }
        else
        {
            fragmentMain = new DoctorClinicFragment();
        }

        FragmentManager fragmentMangerMain = getActivity().getFragmentManager();
        fragmentMangerMain.beginTransaction().replace(R.id.content_frame,fragmentMain,"Doctor Consultations").addToBackStack(null).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goBack();
                    return true;
                }
                return false;
            }
        });
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
}
