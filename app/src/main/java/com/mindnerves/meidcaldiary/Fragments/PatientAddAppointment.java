package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mindnerves.meidcaldiary.R;

/**
 * Created by User on 10-10-2015.
 */
public class PatientAddAppointment extends Fragment{
    Button doctorConsultant,diagnosisTest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_add_appointment, container, false);
        doctorConsultant = (Button)view.findViewById(R.id.doctor_consultant);
        diagnosisTest = (Button)view.findViewById(R.id.diagnostic_test);
        getDoctorConsultantProfile();
        doctorConsultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorConsultant.setBackgroundResource(R.drawable.square_blue_color_appointment);
                diagnosisTest.setBackgroundResource(R.drawable.square_grey_color_appointment);
                doctorConsultant.setTextColor(Color.parseColor("#ffffff"));
                diagnosisTest.setTextColor(Color.parseColor("#000000"));
                getDoctorConsultantProfile();
            }
        });
        diagnosisTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diagnosisTest.setBackgroundResource(R.drawable.square_blue_color_appointment);
                doctorConsultant.setBackgroundResource(R.drawable.square_grey_color_appointment);
                diagnosisTest.setTextColor(Color.parseColor("#ffffff"));
                doctorConsultant.setTextColor(Color.parseColor("#000000"));
                getDiagnosisTestReminderProfile();
            }
        });
        return view;
    }
    public void getDoctorConsultantProfile(){
        Fragment fragment = new DoctorConsultant();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    public void getDiagnosisTestReminderProfile(){
        Fragment fragment = new DiagnosisTestReminder();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
}
