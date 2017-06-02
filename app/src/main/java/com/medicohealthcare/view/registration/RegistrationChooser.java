package com.medicohealthcare.view.registration;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentFragment;

/**
 * Created by Narendra on 11-03-2016.
 */
public class RegistrationChooser extends ParentFragment
{
    public static final String MyPREFERENCES = "MyPrefs";
    public SharedPreferences session;
    private EditText email;
    private EditText profileId;
    RadioButton doctor, assistant, patient, existing_profile, create_profile;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.registration_choose,
                container, false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Registration Types");
        doctor = (RadioButton) view.findViewById(R.id.doctor);
        assistant = (RadioButton) view.findViewById(R.id.assistant);
        patient = (RadioButton) view.findViewById(R.id.patient);
        profileId = (EditText) view.findViewById(R.id.profile_id);
        existing_profile = (RadioButton) view.findViewById(R.id.existing_profile);
        existing_profile.setEnabled(false);
        create_profile = (RadioButton) view.findViewById(R.id.create_profile);
        next = (Button) view.findViewById(R.id.next);
        create_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doctor.setEnabled(true);
                assistant.setEnabled(true);
                patient.setEnabled(true);
                profileId.setEnabled(false);
            }
        });
        existing_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doctor.setEnabled(false);
                assistant.setEnabled(false);
                patient.setEnabled(false);
                profileId.setEnabled(true);
            }
        });
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(create_profile.isChecked())
                {
                    if(patient.isChecked())
                    {
                        Bundle bundle = new Bundle();
                        bundle.putInt(PROFILE_ROLE,PATIENT);
                        getActivity().getIntent().putExtras(bundle);
                        FragmentManager manager = getActivity().getFragmentManager();
                        ParentFragment assistantListView = new PersonProfileRegistrationView();
                        FragmentTransaction fft1 = getFragmentManager().beginTransaction();
                        fft1.add(R.id.service, assistantListView,PersonProfileRegistrationView.class.getName()).addToBackStack(PersonProfileRegistrationView.class.getName()).commit();
                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putInt(PROFILE_ROLE,DOCTOR);
                        getActivity().getIntent().putExtras(bundle);
                        FragmentManager manager = getActivity().getFragmentManager();
                        ParentFragment assistantListView = new DoctorProfileRegistrationView();
                        FragmentTransaction fft1 = getFragmentManager().beginTransaction();
                        fft1.add(R.id.service, assistantListView,DoctorProfileRegistrationView.class.getName()).addToBackStack(PersonProfileRegistrationView.class.getName()).commit();
                    }
                }
                else
                {

                }
            }
        });
        return view;

    }


}
