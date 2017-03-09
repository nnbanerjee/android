package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import com.medico.model.Clinic;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ClinicFragment extends Fragment {

    String clinicId = "";
    MyApi api;
    SharedPreferences session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clinic_profile_details, container,false);
        TextView clinicName = (TextView) view.findViewById(R.id.clinicName);
        TextView speciality = (TextView) view.findViewById(R.id.speciality);
        final Button appointmentBtn = (Button) view.findViewById(R.id.appointmentBtn);
        final Button profileBtn = (Button) view.findViewById(R.id.profileBtn);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointmentDate);
        TextView show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        TextView viewAll = (TextView) view.findViewById(R.id.viewAll);
        TextView totalAppointment = (TextView)view.findViewById(R.id.total_appointment);
        TextView lastVisitedValue = (TextView) view.findViewById(R.id.lastVisitedValue);
        ImageView doctorImg = (ImageView) view.findViewById(R.id.doctorImg);
        ImageView downImg = (ImageView)view.findViewById(R.id.downImg);
        Button rightArrow = (Button)view.findViewById(R.id.nextBtn);
        doctorImg.setBackgroundResource(R.drawable.clinic);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv.setText(session.getString("patient_clinicName", "Medical Diary"));
        clinicId = session.getString("patient_clinicId",null);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        Global global = (Global) getActivity().getApplicationContext();
        List<Clinic> clinicsList = global.getAllClinicsList();
        System.out.println("Clinic List:::::::::::"+clinicsList.size());
        for(Clinic clinic  : clinicsList){
            if(clinic.getIdClinic().equals(clinicId)){
                clinicName.setText(clinic.getClinicName());
                speciality.setText(clinic.getClinicName());
                if(clinic.getBookDate() != null){
                    if(clinic.getBookDate().equals("")){
                        appointmentDate.setText("None");
                    }else {
                        appointmentDate.setText(clinic.getBookDate() + " " + clinic.getBookTime());
                    }
                }else{
                    appointmentDate.setText("None");
                }
                if(clinic.lastVisited == null){
                    lastVisitedValue.setText("Not Visited ");
                }else{
                    lastVisitedValue.setText(clinic.lastVisited);
                }

                if(clinic.totalAppointmentCount.equals("0")){
                    totalAppointment.setText("0");
                }else{
                    totalAppointment.setText(clinic.totalAppointmentCount);
                }
            }
        }
        GetClinicAppointment();
        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBtn.setBackgroundResource(R.drawable.square_blue_color);
                appointmentBtn.setTextColor(Color.parseColor("#ffffff"));
                profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                profileBtn.setTextColor(Color.parseColor("#000000"));
                //ClinicAllDoctorFragment
                GetClinicAppointment();
            }
        });
        downImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                appointmentBtn.setTextColor(Color.parseColor("#000000"));
                profileBtn.setBackgroundResource(R.drawable.square_blue_color);
                profileBtn.setTextColor(Color.parseColor("#ffffff"));
                Fragment fragment = new ClinicProfileDetails();
                Bundle bun = new Bundle();
                bun.putString("fragment","ClinicFragment");
                FragmentManager fragmentManger = getFragmentManager();
                fragment.setArguments(bun);
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AppointmentsPatient();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void GetClinicAppointment(){
        Fragment fragment = new ClinicDetailsFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onStop();
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


    public void  goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinics and Labs");

        Fragment fragment;
        String logString = session.getString("loginType",null);
        if(logString.equals("Doctor"))
        {
            fragment = new DoctorAllClinics();
        }
        else
        {
            fragment = new PatientAllClinics();
        }

        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
}
