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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.ChatAdapter;
import Application.MyApi;
import Model.Chat;
import Model.HomePatientCount;
import Model.PatientAppointmentVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class PatientMenusManage extends Fragment {

    String doctorId = "";
    MyApi api;
    String patientId;
    Global global;
    private Button drawar,logout;
    SharedPreferences session;
    TextView doctorsCount,clinicCount,firstAidCount,medicalAlertCount,appointmentCount,lastVisited;
    ImageView rightArrowDoctor,rightArrowClinic,rightArrowFirstAid,rightArrowMedicalAlert,rightArrowAppointment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_consultations,container,false);
        global = (Global)getActivity().getApplicationContext();
        RelativeLayout layout1 = (RelativeLayout) view.findViewById(R.id.layout1);
        RelativeLayout layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        RelativeLayout layout3 = (RelativeLayout) view.findViewById(R.id.layout5);
        LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
        doctorsCount = (TextView)view.findViewById(R.id.doctor_count);
        clinicCount = (TextView)view.findViewById(R.id.clinic_count);
        firstAidCount = (TextView)view.findViewById(R.id.first_aid_count);
        medicalAlertCount = (TextView)view.findViewById(R.id.medical_alert_count);
        appointmentCount = (TextView)view.findViewById(R.id.appointment_count);
        lastVisited = (TextView)view.findViewById(R.id.lastVisited);
        rightArrowDoctor = (ImageView)view.findViewById(R.id.right_arrow_doctor);
        rightArrowClinic = (ImageView)view.findViewById(R.id.right_arrow_clinic);
        rightArrowFirstAid = (ImageView)view.findViewById(R.id.right_arrow_first_aid);
        rightArrowMedicalAlert = (ImageView)view.findViewById(R.id.right_arrow_medical_alert);
        rightArrowAppointment = (ImageView)view.findViewById(R.id.right_arrow_appointments);
        api.homeCountPatient(patientId,new Callback<HomePatientCount>() {
            @Override
            public void success(HomePatientCount homePatientCount, Response response) {

                System.out.println("homePatientCount  ////////////"+homePatientCount.appointmentVm.appointmentDate);
                doctorsCount.setText(""+homePatientCount.getDoctorsCount());
                clinicCount.setText(""+homePatientCount.getClinicsCount());
                appointmentCount.setText(""+homePatientCount.getAppointmentsCount());
                System.out.println("doctorId::::::"+homePatientCount.getAppointmentVM().doctorId);
                System.out.println("time::::::"+homePatientCount.getAppointmentVM().bookTime);
                System.out.println("Date::::::"+homePatientCount.getAppointmentVM().appointmentDate);
                long val = Long.parseLong(homePatientCount.getAppointmentVM().appointmentDate);
                Date date=new Date(val);
                global.setDoctorSearchResponses(homePatientCount.doctors);
                System.out.println("DoctorList Size= "+homePatientCount.doctors.size());
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                String dateText = df2.format(date);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_doctor_email", ""+homePatientCount.getAppointmentVM().doctorId);
                editor.putString("doctor_patient_appointmentTime", ""+homePatientCount.getAppointmentVM().bookTime);
                editor.putString("doctor_patient_appointmentDate", dateText);
                editor.commit();
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.No_Data_for_Appointments,Toast.LENGTH_SHORT).show();
            }
        });
        lastVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayout();
                global.setSummaryJump(true);
                Fragment fragment = new PatientAppointmentInformation();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        rightArrowDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                global.setSummaryJump(false);
                Fragment fragment = new PatientAllDoctors();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        doctorsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                global.setSummaryJump(false);
                Fragment fragment = new PatientAllDoctors();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                global.setSummaryJump(false);
                Fragment fragment = new PatientAllDoctors();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                global.setSummaryJump(false);
                Fragment fragment = new PatientAllClinics();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                layout.setVisibility(View.GONE);
                Fragment fragment = new PatientAppointmentStatus();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void hideLayout()
    {
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final LinearLayout layoutMenus = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        drawar.setVisibility(View.GONE);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intObj = new Intent(getActivity(),HomeActivity.class);
                startActivity(intObj);
            }
        });
        profileLayout.setVisibility(View.GONE);
        layoutMenus.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }
}
