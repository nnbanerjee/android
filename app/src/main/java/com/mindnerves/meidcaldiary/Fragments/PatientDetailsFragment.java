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

import Application.MyApi;
import Model.AllPatients;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientDetailsFragment extends Fragment {

    String patientId,patientEmail,doctorId = "";
    MyApi api;
    SharedPreferences session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_profile_details, container,false);

        TextView doctorName = (TextView) view.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) view.findViewById(R.id.doctorSpeciality);
        final Button appointmentsBtn = (Button) view.findViewById(R.id.clinicsBtn);
        final Button profileBtn = (Button) view.findViewById(R.id.profileBtn);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointmentDate);
        ImageView viewImage = (ImageView)view.findViewById(R.id.doctorImg);
        TextView show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        ImageView viewAll = (ImageView) view.findViewById(R.id.viewAll);
        TextView lastVisitedValue = (TextView) view.findViewById(R.id.lastVisitedValue);
        ImageView closeMenu = (ImageView)view.findViewById(R.id.downImg);
        TextView lastAppointment = (TextView)view.findViewById(R.id.lastAppointmentValue);
        TextView totalAppointment = (TextView)view.findViewById(R.id.totalAppointment);
        TextView nextAppointment = (TextView)view.findViewById(R.id.appointmentDate);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv.setText(session.getString("patient_Name", "Medical Diary"));
        patientId = session.getString("patientId", null);
        viewImage.setBackgroundResource(R.drawable.patient);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        Global global = (Global) getActivity().getApplicationContext();
        List<AllPatients> patientsResults = global.getAllPatients();

        for(int i = 0; i < patientsResults.size(); i++){
            //System.out.println("doctorSearchResponses.get(i).getEmail() = "+doctorSearchResponses.get(i).getEmail());
            if(patientId.equals(patientsResults.get(i).getId())){
                AllPatients patients = patientsResults.get(i);
                doctorName.setText(patients.getName());
                if(patients.getBookDate() != null){
                    appointmentDate.setText(patients.getBookDate() + " "+ patients.getBookTime());
                }
                patientId = patients.getId();
                patientEmail = patients.getEmail();
                doctorId = patients.getDoctorId();
                doctorSpeciality.setText(patients.getSpeciality());
                if(patients.getAddress()==null || patients.getAddress().equals("")){
                    lastVisitedValue.setText("Not Visited ");
                }else{
                    lastVisitedValue.setText(patients.getAddress());
                }
                if(patients.getLastAppointmentDate()==null || patients.getAppointmentDate().equals("")){
                    lastAppointment.setText("None");
                }else{
                    lastAppointment.setText(patients.getAppointmentDate()+" "+patients.getAppointmentTime());
                }
                if(patients.getBookDate()==null || patients.getBookDate().equals("")){
                    appointmentDate.setText("None");
                }else{
                    appointmentDate.setText(patients.getBookDate()+" "+patients.getBookTime());
                }
                totalAppointment.setText(""+patients.getTotalAppointment());
            }
        }
        getPatientProfile();
       // getClinicsProfile();

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllDoctorPatientAppointment();// DoctorProfileDetails();
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patientEmail", patientEmail);
                editor.putString("doctorId", doctorId);
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from patient details");
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }
        });

        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentsBtn.setBackgroundResource(R.drawable.square_grey_color);
                appointmentsBtn.setTextColor(Color.parseColor("#000000"));
                profileBtn.setBackgroundResource(R.drawable.square_blue_color);
                profileBtn.setTextColor(Color.parseColor("#ffffff"));
                //ClinicDetailsFragment
                getClinicsProfile();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentsBtn.setBackgroundResource(R.drawable.square_blue_color);
                appointmentsBtn.setTextColor(Color.parseColor("#ffffff"));
                profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                profileBtn.setTextColor(Color.parseColor("#000000"));
                getPatientProfile();

            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllDoctorsPatient();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void getClinicsProfile(){
        Fragment fragment = new ClinicAllPatientFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }

    public void getPatientProfile(){
        Fragment fragment = new PatientProfileDetails();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
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
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void  goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Medical Diary");
        Fragment fragment = new AllDoctorsPatient();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
}
