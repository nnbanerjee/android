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
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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


        doctor = (RadioButton) view.findViewById(R.id.doctor);
        assistant = (RadioButton) view.findViewById(R.id.assistant);
        patient = (RadioButton) view.findViewById(R.id.patient);
        profileId = (EditText) view.findViewById(R.id.profile_id);
        existing_profile = (RadioButton) view.findViewById(R.id.existing_profile);
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

                if(create_profile.isChecked() )
                {
                    if(patient.isChecked())
                        registerProfile(PATIENT,0);
                    else if(assistant.isChecked())
                        registerProfile(ASSISTANT,0);
                    else if(doctor.isChecked())
                        registerProfile(DOCTOR,0);
                }
                else
                {
                    if( profileId.getText().toString().trim().length() > 0)
                    {
                        getProfile(new Integer(profileId.getText().toString().trim()));
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please enter profile id", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        });
        return view;

    }

    private void getProfile(Integer profileId)
    {
        if(profileId != null && profileId.intValue() > 0 )
        {
            api.getProfile(new ProfileId(profileId), new Callback<Person>()
            {
                @Override
                public void success(Person person, Response response)
                {
                    if(person.errorCode == -1 )
                    {
                        if (person != null && person.getId() != null && person.getStatus().intValue() == 2)
                        {
                            if(person.getPrime().intValue() == 1)
                                registerProfile(person.role, person.id);
                            else
                                Toast.makeText(getActivity(), "Dependent Profile, cannot be registered", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(), "Profile already registered", Toast.LENGTH_LONG).show();
                    }
                    else if(person.errorCode == 201)
                        Toast.makeText(getActivity(), "Profile does not exist", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_LONG).show();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
    }

    private void registerProfile(int role, int profileId)
    {
        Bundle bundle = new Bundle();
        if(profileId > 0)
            bundle.putInt(PROFILE_ID, new Integer(profileId));
        switch(role)
        {
            case PATIENT:
                bundle.putInt(PROFILE_ROLE,PATIENT);
                getActivity().getIntent().putExtras(bundle);
                FragmentManager manager = getActivity().getFragmentManager();
                ParentFragment assistantListView = new PersonProfileRegistrationView();
                FragmentTransaction fft1 = getFragmentManager().beginTransaction();
                fft1.add(R.id.service, assistantListView,PersonProfileRegistrationView.class.getName()).addToBackStack(PersonProfileRegistrationView.class.getName()).commit();
                break;
            case ASSISTANT:
                bundle.putInt(PROFILE_ROLE,ASSISTANT);
                getActivity().getIntent().putExtras(bundle);
                FragmentManager manager1 = getActivity().getFragmentManager();
                ParentFragment assistantView = new PersonProfileRegistrationView();
                FragmentTransaction fft = getFragmentManager().beginTransaction();
                fft.add(R.id.service, assistantView,PersonProfileRegistrationView.class.getName()).addToBackStack(PersonProfileRegistrationView.class.getName()).commit();
                break;
            case DOCTOR:
                bundle.putInt(PROFILE_ROLE,DOCTOR);
                getActivity().getIntent().putExtras(bundle);
                FragmentManager manager2 = getActivity().getFragmentManager();
                ParentFragment doctorView = new DoctorProfileRegistrationView();
                FragmentTransaction fft2 = getFragmentManager().beginTransaction();
                fft2.add(R.id.service, doctorView,DoctorProfileRegistrationView.class.getName()).addToBackStack(DoctorProfileRegistrationView.class.getName()).commit();
                break;
            default:
                new RuntimeException();
        }
    }

    public void onStart()
    {
        super.onStart();
        setTitle("Registration");
    }


}
