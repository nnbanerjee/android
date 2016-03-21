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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Application.MyApi;
import Model.Clinic;
import Model.ClinicDetailVm;
import Model.PatientClinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Feb-15.
 */
public class DoctorClinicAppointmentFragment extends Fragment {

    MyApi api;
    SharedPreferences session;
    String doctorId;
    ClinicDetailVm clinicDoctor;
    Global global;
    String clinicId = "";
    ProgressDialog progress;
    String userId;
    TextView shift1Time,shift2Time,shift3Time,shift1Days,doctorName,slot1Text,slot2Text,slot3Text,slotCount1,slotCount2,slotCount3;
    Button rightArrow1,rightArrow2,rightArrow3;
    TextView shift2Days,shift3Days;
    RelativeLayout layout1,layout2,layout3;
    String type = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_clinic_appointment, container, false);

        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = session.getString("sessionID", null);

        global = (Global) getActivity().getApplicationContext();
        doctorId = session.getString("clinicDoctorId", null);
        shift1Time = (TextView) view.findViewById(R.id.shift1Time);
        shift2Time = (TextView) view.findViewById(R.id.shift2Time);
        shift3Time = (TextView) view.findViewById(R.id.shift3Time);
        shift1Days = (TextView) view.findViewById(R.id.shift1Days);
        shift2Days = (TextView) view.findViewById(R.id.shift2Days);
        shift3Days = (TextView) view.findViewById(R.id.shift3Days);
        slot1Text = (TextView)view.findViewById(R.id.slot1_text);
        slot2Text = (TextView)view.findViewById(R.id.slot2_text);
        slot3Text = (TextView)view.findViewById(R.id.slot3_text);
        slotCount1 = (TextView)view.findViewById(R.id.shift1_text);
        slotCount2 = (TextView)view.findViewById(R.id.shift2_text);
        slotCount3 = (TextView)view.findViewById(R.id.shift3_text);
        rightArrow1 = (Button)view.findViewById(R.id.shift1_right_arrow);
        rightArrow2 = (Button)view.findViewById(R.id.shift2_right_arrow);
        rightArrow3 = (Button)view.findViewById(R.id.shift3_right_arrow);
        clinicId = session.getString("patient_clinicId",null);
        List<Clinic> clinicsList = global.getAllClinicsList();
       // System.out.println("clinicList="+ clinicsList.size());

       /* for(Clinic clinic  : clinicsList) {
            if (clinic.getIdClinic().equals(clinicId))
            {


                if((clinic.getShift1()) != null)
                {
                    shift1Days.setVisibility(View.VISIBLE);
                    shift1Time.setVisibility(View.VISIBLE);
                    slot1Text.setVisibility(View.VISIBLE);
                    slotCount1.setVisibility(View.VISIBLE);
                    rightArrow1.setVisibility(View.VISIBLE);
                    shift1Time.setText(clinic.getShift1().getShiftTime());
                    slotCount1.setText(clinic.getShift1().getAppointmentCount());
                    String finalStringDay = AppointmentDays(clinic.getShift1().getDays());
                    shift1Days.setText(finalStringDay);
                }
                else
                {
                    shift1Days.setVisibility(View.GONE);
                    shift1Time.setVisibility(View.GONE);
                    slot1Text.setVisibility(View.GONE);
                    slotCount1.setVisibility(View.GONE);
                    rightArrow1.setVisibility(View.GONE);
                }
                if((clinic.getShift2()) != null)
                {
                    shift2Days.setVisibility(View.VISIBLE);
                    shift2Time.setVisibility(View.VISIBLE);
                    slot2Text.setVisibility(View.VISIBLE);
                    slotCount2.setVisibility(View.VISIBLE);
                    rightArrow2.setVisibility(View.VISIBLE);
                    shift2Time.setText(clinic.getShift2().getShiftTime());
                    slotCount2.setText(clinic.getShift2().getAppointmentCount());
                    String finalStringDay = AppointmentDays(clinic.getShift2().getDays());
                    shift2Days.setText(finalStringDay);
                }
                else
                {
                    shift2Days.setVisibility(View.GONE);
                    shift2Time.setVisibility(View.GONE);
                    slot2Text.setVisibility(View.GONE);
                    slotCount2.setVisibility(View.GONE);
                    rightArrow2.setVisibility(View.GONE);
                }
                if((clinic.getShift3()) != null)
                {
                    shift3Days.setVisibility(View.VISIBLE);
                    shift3Time.setVisibility(View.VISIBLE);
                    slot3Text.setVisibility(View.VISIBLE);
                    slotCount3.setVisibility(View.VISIBLE);
                    shift3Time.setText(clinic.getShift3().getShiftTime());
                    slotCount3.setText(clinic.getShift3().getAppointmentCount());
                    rightArrow3.setVisibility(View.VISIBLE);
                    String finalStringDay = AppointmentDays(clinic.getShift3().getDays());
                    shift3Days.setText(finalStringDay);
                }
                else
                {
                    shift3Days.setVisibility(View.GONE);
                    shift3Time.setVisibility(View.GONE);
                    slot3Text.setVisibility(View.GONE);
                    slotCount3.setVisibility(View.GONE);
                    rightArrow3.setVisibility(View.GONE);
                }
                break;
            }
        }*/

        //Get Doctor Id from application class


        layout1 = (RelativeLayout)view.findViewById(R.id.shift1Layout);
        layout2 = (RelativeLayout)view.findViewById(R.id.shift2Layout);
        layout3 = (RelativeLayout)view.findViewById(R.id.shift3Layout);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","from_clinic_appointment");
                Fragment fragment = new ShowShift1();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","from_clinic_appointment");
                Fragment fragment = new ShowShift2();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","from_clinic_appointment");
                Fragment fragment = new ShowShift3();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();


            }
        });

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        return view;
    }


    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

    }

    public String AppointmentDays(String days)
    {
        System.out.println("i am here:::::::");
        Boolean mon = false;
        Boolean tue = false;
        Boolean wed = false;
        Boolean thr = false;
        Boolean fri = false;
        Boolean sat = false;
        Boolean sun = false;

        if(days.contains("Mon"))
        {
            mon = true;
        }
        if(days.contains("Tue"))
        {
            tue = true;
        }
        if(days.contains("Wed"))
        {
            wed = true;
        }
        if(days.contains("Thu"))
        {
            thr = true;
        }
        if(days.contains("Fri"))
        {
            fri = true;
        }
        if(days.contains("Sat"))
        {
            sat = true;
        }
        if(days.contains("Sun"))
        {
            sun = true;
        }

        if((mon==true)&&(tue==true)&&(wed==true)&&(thr==true)&&(fri==true)&&(sat==true)&&(sun==true))
        {
            days = "Mon-sun";
        }
        return days;

    }

    public void getClinicAppointment(String doctorId, String id, String shift, String time, String date){

        Bundle args = new Bundle();
        args.putString("doctorId", doctorId);
        args.putString("clinicId", id);
        args.putString("clinicShift", shift);
        args.putString("appointmentTime", time);
        args.putString("appointmentDate", date);
        Fragment fragment = new ClinicDoctorAppointmentFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }

    public String getTextOfDays(String days){

        if(days == null){
            return  "Mon - Sun";
        }
        if(days.contains("Mon") && days.contains("Tue") && days.contains("Wed")
                && days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && days.contains("Sun")){
            return  "Mon - Sun";
        }else{
            return days;
        }
    }

    public String  getTimeTextValue(String start, String end){
        if(start == null){

            return "No Shift scheduled !!!";
        }else{
            return start+" - "+ end;
        }
    }


    public void saveVisitedData(String doctorId, String patientId, String clinicId, String shift, Integer visited){

        MyApi api;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getActivity().getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        api.saveVisitedPatientAppointment(doctorId, patientId,clinicId,shift,visited, new Callback<String>() {
            @Override
            public void success(String str, Response response) {
                progress.dismiss();
                Toast.makeText(getActivity(), "Saved Successfully !!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

}