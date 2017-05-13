package com.medico.view.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.view.home.ParentFragment;

/**
 * Created by Narendra on 11-03-2016.
 */
public class RegistrationSuccessfulView extends ParentFragment
{


    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.registration_successful,
                container, false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Registration Successful");
        Bundle bundle = getActivity().getIntent().getExtras();
        String personName = bundle.getString("person_name");
        int role = bundle.getInt(PROFILE_ROLE);
        int gender = bundle.getInt("gender");
        int profileId = bundle.getInt(PROFILE_ID);
        TextView name = (TextView)view.findViewById(R.id.person_name);
        TextView registration = (TextView)view.findViewById(R.id.registration);
        TextView message = (TextView)view.findViewById(R.id.message);
        name.setText((role==DOCTOR?"Dr. ":(gender==0?"Mr.":"Ms"))+ personName);
        registration.setText("Registration Number : " + profileId);
        if(role==PATIENT)
            message.setText(getActivity().getString(R.string.patient_successful_registration));
        else if(role == DOCTOR)
            message.setText(getActivity().getString(R.string.doctor_successful_registration));
        Button login = (Button)view.findViewById(R.id.login);
        hideBusy();
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}