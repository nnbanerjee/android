package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import com.medico.application.MyApi;
import Model.AllPatients;
import Utils.UtilSingleInstance;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientProfileDetails extends Fragment {

    MyApi api;
    SharedPreferences session;
    String patientId = "";
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_profile_fragment, container,false);

        TextView emailId = (TextView) view.findViewById(R.id.emailId);
        TextView mobileId = (TextView) view.findViewById(R.id.mobileId);
        TextView locationId = (TextView) view.findViewById(R.id.locationId);
        TextView genderId = (TextView) view.findViewById(R.id.genderId);
        TextView bloodGroup = (TextView) view.findViewById(R.id.bloodGroup);
        TextView allergic_to = (TextView) view.findViewById(R.id.allergic_to);
        TextView dateOfBirthId = (TextView) view.findViewById(R.id.dateOfBirthId);
        TextView patientIdText = (TextView) view.findViewById(R.id.textPatient);


        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("patientId", null);
        patientIdText.setText(patientId);
        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu);

        Global global = (Global) getActivity().getApplicationContext();
        AllPatients allPatients = global.getSelectedPatientsProfile();
        String strGender="";
       // for(int i = 0; i < allPatients.size(); i++){
          //  if(patientId.equals(allPatients.get(i).getpatientId())){
                AllPatients patients = allPatients;
        if(patients!=null){
                emailId.setText(patients.getEmail());
                if (patients.getGender()!=null&&patients.getGender().equalsIgnoreCase("0"))
                    strGender = "Male";
                else
                    strGender = "Female";

                genderId.setText(strGender);
                System.out.println("Date od birth = "+patients.getDateOfBirth());
                if(patients.getDateOfBirth() != null){
                    dateOfBirthId.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(""+patients.getDateOfBirth()));
                }
               // dateOfBirthId.setText(doctorSearchResponse.getDateOfBirth());
                mobileId.setText(patients.getMobile());
                locationId.setText(patients.getAddress());
                bloodGroup.setText(patients.getBloodGroup());
                allergic_to.setText(patients.getAllergicTo());
                //specialityId.setText(doctorSearchResponse.getSpecialization());

           // }
      }

        return view;
    }
}
