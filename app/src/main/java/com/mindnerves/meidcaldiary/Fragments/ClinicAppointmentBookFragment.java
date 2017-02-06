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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.ClinicAppointmentAdapter;
import Application.MyApi;
import Model.ClinicAppointment;
import Model.ClinicAppointmentVM;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Feb-15.
 */
public class ClinicAppointmentBookFragment extends Fragment {

    MyApi api;
    SharedPreferences session;
    String doctorId;
    ClinicDetailVm clinicDoctor;
    Global global;
    String clinicId = "";
    ProgressDialog progress;
    ListView slot1List,slot2List,slot3List;
    String userId;
    TextView shift1Time,shift2Time,shift3Time,shift1Days,doctorName,slot1Text,slot2Text,slot3Text,
             bookOnlineShift1,bookOnlineShift2,bookOnlineShift3;
    Button rightArrowShift1,rightArrowShift2,rightArrowShift3;
    TextView shift2Days,shift3Days;
    RelativeLayout layout1,layout2,layout3;
    String type = "";
    String doctor = "";
    ClinicAppointmentAdapter slot1Adapter,slot2Adapter,slot3Adapter;
    ArrayList<ClinicAppointmentVM> slot1Array,slot2Array,slot3Array,appointments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_appointment_book, container, false);

        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = session.getString("sessionID", null);
        Button backBtn = (Button)getActivity().findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackWindow();
            }
        });
        global = (Global) getActivity().getApplicationContext();
        doctorId = session.getString("clinicDoctorId", null);
        bookOnlineShift1 = (TextView)view.findViewById(R.id.book_online_slot1);
        bookOnlineShift2 = (TextView)view.findViewById(R.id.book_online_slot2);
        bookOnlineShift3 = (TextView)view.findViewById(R.id.book_online_slot3);
        shift1Time = (TextView) view.findViewById(R.id.shift1Time);
        shift2Time = (TextView) view.findViewById(R.id.shift2Time);
        shift3Time = (TextView) view.findViewById(R.id.shift3Time);
        shift1Days = (TextView) view.findViewById(R.id.shift1Days);
        shift2Days = (TextView) view.findViewById(R.id.shift2Days);
        shift3Days = (TextView) view.findViewById(R.id.shift3Days);
        slot1Text = (TextView)view.findViewById(R.id.slot1_text);
        slot2Text = (TextView)view.findViewById(R.id.slot2_text);
        slot3Text = (TextView)view.findViewById(R.id.slot3_text);
        rightArrowShift1 = (Button)view.findViewById(R.id.book_online_right_arrow1);
        rightArrowShift2 = (Button)view.findViewById(R.id.book_online_right_arrow2);
        rightArrowShift3 = (Button)view.findViewById(R.id.book_online_right_arrow3);
        clinicId = session.getString("patient_clinicId",null);
        List<ClinicDetailVm> clinics = global.getAllDoctorClinicList();
        layout1 = (RelativeLayout)view.findViewById(R.id.shift1Layout);
        layout2 = (RelativeLayout)view.findViewById(R.id.shift2Layout);
        layout3 = (RelativeLayout)view.findViewById(R.id.shift3Layout);
        slot1List = (ListView)view.findViewById(R.id.list_slot1);
        slot2List = (ListView)view.findViewById(R.id.list_slot2);
        slot3List = (ListView)view.findViewById(R.id.list_slot3);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        getAllAppointment();
        for(ClinicDetailVm clinic : clinics)
        {
            if(clinic.getClinicId().equals(clinicId))
            {
                doctor = clinic.getDoctorName();
                clinicDoctor = clinic;
                doctorId = clinic.getDoctorId();
                System.out.println("Clinic Doctor Id:::::::"+clinic.getDoctorId());
                if((clinic.getSlot1().getDays()) != null)
                {
                    shift1Days.setVisibility(View.VISIBLE);
                    shift1Time.setVisibility(View.VISIBLE);
                    slot1Text.setVisibility(View.VISIBLE);
                    shift1Time.setText(clinic.getSlot1().getStartTimes());
                    String finalStringDay = AppointmentDays(clinic.getSlot1().getDays());
                    shift1Days.setText(finalStringDay);
                    slot1List.setVisibility(View.VISIBLE);
                    System.out.println("doctorId= " + doctorId);


                }
                else
                {
                    layout1.setVisibility(View.GONE);
                    shift1Days.setVisibility(View.GONE);
                    shift1Time.setVisibility(View.GONE);
                    slot1Text.setVisibility(View.GONE);
                    slot1List.setVisibility(View.GONE);

                }
                if((clinic.getSlot2().getDays()) != null)
                {
                    shift2Days.setVisibility(View.VISIBLE);
                    shift2Time.setVisibility(View.VISIBLE);
                    slot2Text.setVisibility(View.VISIBLE);
                    shift2Time.setText(clinic.getSlot2().getStartTimes());
                    String finalStringDay = AppointmentDays(clinic.getSlot2().getDays());
                    shift2Days.setText(finalStringDay);
                    slot2List.setVisibility(View.VISIBLE);
                }
                else
                {
                    layout2.setVisibility(View.GONE);
                    shift2Days.setVisibility(View.GONE);
                    shift2Time.setVisibility(View.GONE);
                    slot2Text.setVisibility(View.GONE);
                    slot2List.setVisibility(View.GONE);

                }
                if((clinic.getSlot3().getDays()) != null)
                {
                    shift3Days.setVisibility(View.VISIBLE);
                    shift3Time.setVisibility(View.VISIBLE);
                    slot3Text.setVisibility(View.VISIBLE);
                    shift3Time.setText(clinic.getSlot3().getStartTimes());
                    slot3List.setVisibility(View.VISIBLE);
                    String finalStringDay = AppointmentDays(clinic.getSlot3().getDays());
                    shift3Days.setText(finalStringDay);


                }
                else
                {
                    layout3.setVisibility(View.GONE);
                    shift3Days.setVisibility(View.GONE);
                    shift3Time.setVisibility(View.GONE);
                    slot3Text.setVisibility(View.GONE);
                    slot3List.setVisibility(View.GONE);
                }
                break;
            }
        }

        bookOnlineShift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift1");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        rightArrowShift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift1");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        bookOnlineShift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift2");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }
        });
        rightArrowShift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift2");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        bookOnlineShift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift3");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        rightArrowShift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("clinicId", clinicDoctor.getClinicId());
                args.putString("clinicShift", "shift3");
                args.putString("appointmentTime", null);
                args.putString("appointmentDate", null);
                Fragment fragment = new ClinicAppointmentBook();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }
    public void goBackWindow()
    {
        Fragment fragment = new ClinicDetailsFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }


    public void getAllAppointment()
    {

        System.out.println("patientId= "+userId);
        System.out.println("doctorId= "+doctorId);
        appointments = new ArrayList<ClinicAppointmentVM>();
        int docId = Integer.parseInt(doctorId);
        api.getPatientAppointment(docId,userId,new Callback<List<ClinicAppointment>>() {
            @Override
            public void success(List<ClinicAppointment> clinicAppointments, Response response) {
                for(ClinicAppointment patientAppointments : clinicAppointments)
                {
                    ClinicAppointmentVM vm = new ClinicAppointmentVM();
                    Date date = new Date(Long.parseLong(patientAppointments.getAppointmentDate()));
                    Format format = new SimpleDateFormat("yyyy-MM-dd");
                    vm.setAppointmentDate(format.format(date));
                    vm.setAppointmentTime(patientAppointments.getBookTime());
                    vm.setShift(patientAppointments.getShift());
                    appointments.add(vm);
                }
                slot1Array = new ArrayList<ClinicAppointmentVM>();
                slot2Array = new ArrayList<ClinicAppointmentVM>();
                slot3Array = new ArrayList<ClinicAppointmentVM>();
                for(ClinicAppointmentVM vm : appointments){
                    System.out.println("value= "+vm.getShift());
                    System.out.println("slot1 Condition= "+(vm.getShift().equalsIgnoreCase("shift1")));
                    if(vm.getShift().equalsIgnoreCase("shift1")){
                        slot1Array.add(vm);
                    }else if(vm.getShift().equalsIgnoreCase("shift2")){
                        slot2Array.add(vm);
                    }else if(vm.getShift().equalsIgnoreCase("shift3")){
                        slot3Array.add(vm);
                    }
                }
                System.out.println("slot1Array= "+slot1Array.size());
                System.out.println("slot2Array= "+slot2Array.size());
                System.out.println("slot3Array= "+slot3Array.size());
                System.out.println("Patient Appointments= "+appointments.size());
                if(slot1Array.size() == 0){
                    slot1List.setVisibility(View.GONE);
                }else{

                    slot1Adapter = new ClinicAppointmentAdapter(getActivity(),slot1Array);
                    slot1List.setAdapter(slot1Adapter);
                }
                if(slot2Array.size() == 0){
                    slot2List.setVisibility(View.GONE);
                }else{

                    slot2Adapter = new ClinicAppointmentAdapter(getActivity(),slot2Array);
                    slot2List.setAdapter(slot2Adapter);
                }
                if(slot3Array.size() == 0){
                    slot3List.setVisibility(View.GONE);
                }else{
                    slot3Adapter = new ClinicAppointmentAdapter(getActivity(),slot3Array);
                    slot3List.setAdapter(slot3Adapter);
                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });



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

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBackWindow();
                    return true;
                }
                return false;
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



}