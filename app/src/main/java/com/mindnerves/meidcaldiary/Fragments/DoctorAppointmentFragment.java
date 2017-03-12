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
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.ClinicDoctorAppointmentFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Feb-15.
 */
public class DoctorAppointmentFragment extends Fragment {

    MyApi api;
    SharedPreferences session;
    String doctorId;
    ClinicDetailVm clinicDoctor;
    Global global;
    ProgressDialog progress;
    String userId;

    TextView shift1Time,shift2Time,shift3Time,shift1Days,doctorName;
    TextView shift2Days,shift3Days,bookOnline1,bookOnline2;
    TextView bookOnline3,nextAppointment1,nextAppointment2,nextAppointment3;
    TextView nextChange1,nextChange2,nextChange3,hasNextAppo;
    TextView nextAppointment1Value,nextAppointment2Value,nextAppointment3Value;
    TextView notVisited1,notVisited2,notVisited3,visited1,visited2,visited3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment, container, false);

        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = session.getString("sessionID", null);
        global = (Global) getActivity().getApplicationContext();
        doctorId = session.getString("clinicDoctorId", null);

        doctorName = (TextView) view.findViewById(R.id.doctorName);

        shift1Time = (TextView) view.findViewById(R.id.shift1Time);
        shift2Time = (TextView) view.findViewById(R.id.shift2Time);
        shift3Time = (TextView) view.findViewById(R.id.shift3Time);
        hasNextAppo = (TextView) view.findViewById(R.id.hasNextAppo);

        shift1Days = (TextView) view.findViewById(R.id.shift1Days);
        shift2Days = (TextView) view.findViewById(R.id.shift2Days);
        shift3Days = (TextView) view.findViewById(R.id.shift3Days);

        bookOnline1 = (TextView) view.findViewById(R.id.bookOnline1);
        bookOnline2 = (TextView) view.findViewById(R.id.bookOnline2);
        bookOnline3 = (TextView) view.findViewById(R.id.bookOnline3);

        nextAppointment1 = (TextView) view.findViewById(R.id.nextAppointment1);
        nextAppointment2 = (TextView) view.findViewById(R.id.nextAppointment2);
        nextAppointment3 = (TextView) view.findViewById(R.id.nextAppointment3);

        nextAppointment1Value = (TextView) view.findViewById(R.id.nextAppointment1Value);
        nextAppointment2Value = (TextView) view.findViewById(R.id.nextAppointment2Value);
        nextAppointment3Value = (TextView) view.findViewById(R.id.nextAppointment3Value);

        nextChange1 = (TextView) view.findViewById(R.id.nextChange1);
        nextChange2 = (TextView) view.findViewById(R.id.nextChange2);
        nextChange3 = (TextView) view.findViewById(R.id.nextChange3);

        notVisited1 = (TextView) view.findViewById(R.id.notVisited1);
        notVisited2 = (TextView) view.findViewById(R.id.notVisited2);
        notVisited3 = (TextView) view.findViewById(R.id.notVisited3);

        visited1 = (TextView) view.findViewById(R.id.visited1);
        visited2 = (TextView) view.findViewById(R.id.visited2);
        visited3 = (TextView) view.findViewById(R.id.visited3);

        List<ClinicDetailVm> clinicDetailVm = global.getAllDoctorClinicList();

        //Get Doctor Id from application class
        for(ClinicDetailVm detailVm : clinicDetailVm){
            if(detailVm.getDoctorId().equals(doctorId)){
                clinicDoctor = detailVm;
                break;
            }
        }

        if(clinicDoctor.isHasNextAppointment()){
            hasNextAppo.setText("true");
        }else{
           hasNextAppo.setText("false");
        }

        doctorName.setText(clinicDoctor.getDoctorName());
        shift1Days.setText(getTextOfDays(clinicDoctor.getSlot1().getDays()));
        shift2Days.setText(getTextOfDays(clinicDoctor.getSlot2().getDays()));
        shift3Days.setText(getTextOfDays(clinicDoctor.getSlot3().getDays()));
        shift1Time.setText(getTimeTextValue(clinicDoctor.getSlot1().getStartTimes(), clinicDoctor.getSlot1().getEndTimes()));
        shift2Time.setText(getTimeTextValue(clinicDoctor.getSlot2().getStartTimes(), clinicDoctor.getSlot2().getEndTimes()));
        shift3Time.setText(getTimeTextValue(clinicDoctor.getSlot3().getStartTimes(), clinicDoctor.getSlot3().getEndTimes()));

        if (clinicDoctor.getSlot3().getStartTimes() == null) {
            bookOnline3.setVisibility(View.GONE);
        } else {
            bookOnline3.setVisibility(View.VISIBLE);
        }

        if (clinicDoctor.getSlot2().getStartTimes() == null) {
            bookOnline2.setVisibility(View.GONE);
        } else {
            bookOnline2.setVisibility(View.VISIBLE);
        }

        if (clinicDoctor.getSlot1().getStartTimes() == null) {
            bookOnline1.setVisibility(View.GONE);
        } else {
            bookOnline1.setVisibility(View.VISIBLE);
        }

        nextAppointment1.setVisibility(View.GONE);
        nextAppointment2.setVisibility(View.GONE);
        nextAppointment3.setVisibility(View.GONE);
        nextChange1.setVisibility(View.GONE);
        nextChange2.setVisibility(View.GONE);
        nextChange3.setVisibility(View.GONE);

        nextAppointment1Value.setVisibility(View.GONE);
        nextAppointment2Value.setVisibility(View.GONE);
        nextAppointment3Value.setVisibility(View.GONE);

        notVisited1.setVisibility(View.GONE);
        notVisited2.setVisibility(View.GONE);
        notVisited3.setVisibility(View.GONE);

        visited1.setVisibility(View.GONE);
        visited2.setVisibility(View.GONE);
        visited3.setVisibility(View.GONE);



        // Visited call
        visited1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift1", 1);
            }
        });

        visited2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift2", 1);
            }
        });

        visited3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift3", 1);
            }
        });


        notVisited1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift1", 0);
            }
        });

        notVisited2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift2", 0);
            }
        });

        notVisited3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
                saveVisitedData(clinicDoctor.getDoctorId(),userId,clinicDoctor.getClinicId(), "shift3", 0);
            }
        });



        nextChange1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(),clinicDoctor.getClinicId(), "shift1",clinicDoctor.getAppointmentTime(),clinicDoctor.getAppointmentDate());
                //getClinicAppointment(clinicDoctor.getIdClinic(), "shift1", clinicDoctor.getAppointmentTime(), clinicDoctor.getAppointmentDate());
            }
        });

        nextChange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(),clinicDoctor.getClinicId(), "shift2",clinicDoctor.getAppointmentTime(),clinicDoctor.getAppointmentDate());
               // getClinicAppointment(clinicDoctor.getIdClinic(), "shift2", clinicDoctor.getAppointmentTime(),clinicDoctor.getAppointmentDate());
            }
        });

        nextChange3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(),clinicDoctor.getClinicId(), "shift3",clinicDoctor.getAppointmentTime(),clinicDoctor.getAppointmentDate());
                //getClinicAppointment(clinicDetailVm.get(position).getIdClinic(), "shift3", clinicDetailVm.get(position).getAppointmentTime(),clinicDetailVm.get(position).getAppointmentDate());
            }
        });


        if(hasNextAppo.getText().toString().equals("true")) {
            if (clinicDoctor.getAppointmentShift().equals("shift1")) {
                nextAppointment1.setVisibility(View.VISIBLE);
                nextChange1.setVisibility(View.VISIBLE);
                nextAppointment1Value.setVisibility(View.VISIBLE);
                nextAppointment1Value.setText(clinicDoctor.getAppointmentDate() + " " + clinicDoctor.getAppointmentTime());
                bookOnline1.setVisibility(View.INVISIBLE);
                notVisited1.setVisibility(View.VISIBLE);
                visited1.setVisibility(View.VISIBLE);

            } else if (clinicDoctor.getAppointmentShift().equals("shift2")) {
                nextAppointment2.setVisibility(View.VISIBLE);
                nextChange2.setVisibility(View.VISIBLE);
                nextAppointment2Value.setVisibility(View.VISIBLE);
                notVisited2.setVisibility(View.VISIBLE);
                visited2.setVisibility(View.VISIBLE);
                nextAppointment2Value.setText(clinicDoctor.getAppointmentDate() + " " + clinicDoctor.getAppointmentTime());
                bookOnline2.setVisibility(View.INVISIBLE);
            } else if (clinicDoctor.getAppointmentShift().equals("shift3")) {
                nextAppointment3.setVisibility(View.VISIBLE);
                nextChange3.setVisibility(View.VISIBLE);
                nextAppointment3Value.setVisibility(View.VISIBLE);
                notVisited3.setVisibility(View.VISIBLE);
                visited3.setVisibility(View.VISIBLE);
                nextAppointment3Value.setText(clinicDoctor.getAppointmentDate() + " " + clinicDoctor.getAppointmentTime());
                bookOnline3.setVisibility(View.INVISIBLE);
            }
        }

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);



        bookOnline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(),clinicDoctor.getClinicId(), "shift1",null,null);
            }
        });

        bookOnline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(), clinicDoctor.getClinicId(), "shift2",null,null);
            }
        });

        bookOnline3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClinicAppointment(clinicDoctor.getDoctorId(),clinicDoctor.getClinicId(), "shift3",null,null);
            }
        });


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