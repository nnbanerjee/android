package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.medico.application.MyApi;
import com.medico.util.PARAM;
import com.mindnerves.meidcaldiary.Fragments.AllManageFinance;
import com.mindnerves.meidcaldiary.Fragments.DoctorAllFeedback;
import com.mindnerves.meidcaldiary.R;

import Model.DoctorProfile;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorMenusManage extends Fragment {

    String doctorId = "";
    MyApi api;
    Button drawar, logout;
    SharedPreferences session;
    private TextView totalPatientCount;
    private TextView totalAppointments;
    private TextView totalFinance;
    private TextView totalFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_consultations, container, false);
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RelativeLayout patientsProfilelayout = (RelativeLayout) view.findViewById(R.id.layout1);
        RelativeLayout layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        RelativeLayout manage_finance = (RelativeLayout) view.findViewById(R.id.manage_finance);
        RelativeLayout layout4 = (RelativeLayout) view.findViewById(R.id.layout4);
        RelativeLayout manage_feedback = (RelativeLayout) view.findViewById(R.id.manage_feedback);
        totalPatientCount = (TextView) view.findViewById(R.id.total_patients);
        totalAppointments = (TextView) view.findViewById(R.id.total_appointments);
        totalFinance = (TextView) view.findViewById(R.id.total_finance);
        totalFeedback = (TextView) view.findViewById(R.id.total_feedback);
        updateCount();

        patientsProfilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
//                layout.setVisibility(View.GONE);
                System.out.println("i am here::::::::::::");
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                Intent intObj = new Intent(getActivity(), ManagePatientProfile.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();

            }
        });

        manage_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
                 layout.setVisibility(View.GONE);
                Fragment fragment = new AllManageFinance();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                Intent intObj = new Intent(getActivity(), ManagePatientProfile.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        manage_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
                 layout.setVisibility(View.GONE);
                Fragment fragment = new DoctorAllFeedback();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }


    public void updateCount(){
        Gson gson = new Gson();
        String json = session.getString("DoctorProfileLoggedInCounts", "");
        DoctorProfile doctorProfile = gson.fromJson(json, DoctorProfile.class);
        updateCounts(doctorProfile);
    }

    public void updateCounts(DoctorProfile doc) {
        if(doc!=null) {
            totalPatientCount.setText("" + doc.getPatientCount());
            totalAppointments.setText("" + doc.getAppointments());
            totalFinance.setText("" + doc.getFinanceCount());
            totalFeedback.setText("" + doc.getFeebackCount());
        }
    }
}
