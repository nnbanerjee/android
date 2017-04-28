package com.medico.view.home;

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
import com.medico.application.R;
import com.medico.model.DoctorProfile;
import com.medico.util.PARAM;
import com.medico.view.appointment.ManageDoctorAppointment;
import com.medico.view.finance.ManageFinanceView;
import com.medico.view.profile.ManagePatientProfile;
import com.medico.view.review.ManagePatientReviewView;

//import com.mindnerves.meidcaldiary.Fragments.AllManageFinance;
//import com.mindnerves.meidcaldiary.Fragments.DoctorAllFeedback;

//import Model.DoctorProfile;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorMenusManage extends ParentFragment {

    String doctorId = "";
    Button drawar, logout;
    SharedPreferences session;
    private TextView totalPatientCount;
    private TextView totalAppointments;
    private TextView totalFinance;
    private TextView totalFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_home_menu, container, false);
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RelativeLayout patientsProfilelayout = (RelativeLayout) view.findViewById(R.id.document_category);
        RelativeLayout manage_finance = (RelativeLayout) view.findViewById(R.id.manage_finance);
        RelativeLayout manageAppointment = (RelativeLayout) view.findViewById(R.id.manage_appointment);
        RelativeLayout manage_feedback = (RelativeLayout) view.findViewById(R.id.manage_feedback);
        TextView doctorBtn = (TextView)view.findViewById(R.id.doctorBtn);
        TextView manage_appointment_text = (TextView)view.findViewById(R.id.manage_appointment_text);
        TextView manage_finance_text = (TextView)view.findViewById(R.id.manage_finance_text);
        TextView feedback_text = (TextView)view.findViewById(R.id.feedback_text);
        totalPatientCount = (TextView) view.findViewById(R.id.total_patients);
        totalAppointments = (TextView) view.findViewById(R.id.total_appointments);
        totalFinance = (TextView) view.findViewById(R.id.total_finance);
        totalFeedback = (TextView) view.findViewById(R.id.total_feedback);
        updateCount();

        patientsProfilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("i am here::::::::::::");
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManagePatientProfile.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();

            }
        });
        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("i am here::::::::::::");
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManagePatientProfile.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();

            }
        });
        manage_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManageFinanceView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
        });
        manage_finance_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManageFinanceView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
        });
        manageAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManageDoctorAppointment.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        manage_appointment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManageDoctorAppointment.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        manage_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManagePatientReviewView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
        });
        feedback_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putString(PARAM.COUNTRY_NAME,HomeActivity.getParentAtivity().personProfile.getPerson().getCountry());
                Intent intObj = new Intent(getActivity(), ManagePatientReviewView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
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
