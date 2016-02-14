package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.DoctorProfile;
import Model.HomeCountDoctor;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorMenusManage extends Fragment {

    String doctorId = "";
    MyApi api;
    Button drawar,logout;
    SharedPreferences session;
    private  TextView totalPatientCount;
    private TextView totalAppointments ;
    private TextView totalFinance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_consultations,container,false);
        LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RelativeLayout patientsProfilelayout = (RelativeLayout) view.findViewById(R.id.layout1);
        RelativeLayout layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        RelativeLayout manage_finance = (RelativeLayout) view.findViewById(R.id.manage_finance);
        RelativeLayout layout4 = (RelativeLayout) view.findViewById(R.id.layout4);
        RelativeLayout manage_feedback = (RelativeLayout)view.findViewById(R.id.manage_feedback);
          totalPatientCount = (TextView)view.findViewById(R.id.total_patients);
       totalAppointments = (TextView)view.findViewById(R.id.total_appointments);
          totalFinance = (TextView)view.findViewById(R.id.total_finance);
       /* RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        doctorId = session.getString("sessionID",null);
        api.homeCountDoctor(doctorId,new Callback<HomeCountDoctor>() {
            @Override
            public void success(HomeCountDoctor homeCountDoctor, Response response) {
                totalPatientCount.setText(""+homeCountDoctor.patientCount);
                totalAppointments.setText(""+homeCountDoctor.appointments);
                totalFinance.setText(""+homeCountDoctor.financeCount);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });*/
        patientsProfilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                System.out.println("i am here::::::::::::");
                Fragment fragment = new AllDoctorsPatient();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        manage_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                Fragment fragment = new AllManageFinance();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                Fragment fragment = new DoctorAllClinics();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        manage_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                Fragment fragment = new DoctorAllFeedback();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void updateCounts(DoctorProfile doc){
        totalPatientCount.setText(""+doc.getPatientCount());
        totalAppointments.setText(""+doc.getAppointments());
        totalFinance.setText(""+doc.getFinanceCount());
    }
}
