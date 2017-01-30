package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mindnerves.meidcaldiary.R;
import com.mindnerves.meidcaldiary.SigninActivity;
import com.mindnerves.meidcaldiary.SigninActivityAssistance;
import com.mindnerves.meidcaldiary.SigninActivityDoctor;

import Application.MyApi;

/**
 * Created by Narendra on 11-03-2016.
 */
public class RegistrationChooser extends Fragment {


    MyApi api;
    public static final String MyPREFERENCES = "MyPrefs";
    public SharedPreferences session;
    private EditText email;
    private EditText password;
    ProgressDialog progress;
    ImageView doctor, assistant, patient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_choose,
                container, false);
        doctor = (ImageView) view.findViewById(R.id.doctor);
        assistant = (ImageView) view.findViewById(R.id.assistant);
        patient = (ImageView) view.findViewById(R.id.patient);

      //  getActivity().getActionBar().hide();

        assistant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intObj = new Intent(getActivity(), SigninActivityAssistance.class);
                startActivity(intObj);
                assistant.setBackground(getResources().getDrawable(R.drawable.assistant_on));
                doctor.setBackground(getResources().getDrawable(R.drawable.doctor_off) );
                patient.setBackground(getResources().getDrawable(R.drawable.patient_off) );
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                            /*Intent intObj = new Intent(getActivity(), SigninActivity.class);
                            startActivity(intObj);*/
                Intent intObj = new Intent(getActivity(), SigninActivityDoctor.class);
                startActivity(intObj);
                assistant.setBackground(getResources().getDrawable(R.drawable.assistant_off));
                doctor.setBackground(getResources().getDrawable(R.drawable.doctor_on) );
                patient.setBackground(getResources().getDrawable(R.drawable.patient_off));
            }
        });

        patient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                            /*Intent intObj = new Intent(getActivity(), SigninActivity.class);
                            startActivity(intObj);*/

                Intent intObj = new Intent(getActivity(), SigninActivity.class);
                startActivity(intObj);
                assistant.setBackground(getResources().getDrawable(R.drawable.assistant_off));
                doctor.setBackground(getResources().getDrawable(R.drawable.doctor_off) );
                patient.setBackground(getResources().getDrawable(R.drawable.patient_on));
            }
        });


        view.findViewById(R.id.next)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                            /*Intent intObj = new Intent(getActivity(), SigninActivity.class);
                            startActivity(intObj);*/
                    }
                });


        return view;

    }


}
