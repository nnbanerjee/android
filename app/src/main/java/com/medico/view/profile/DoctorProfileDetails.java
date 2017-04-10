package com.medico.view.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.Person;
import com.medico.model.ProfileId;
import com.medico.application.R;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorProfileDetails extends ParentFragment {


    ProgressDialog progress;

    TextView emailId, mobileId, locationId, genderId, bloodGroup, allergic_to,dateOfBirthId,patientIdText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_profile_fragment, container,false);

        emailId = (TextView) view.findViewById(R.id.emailId);
        mobileId = (TextView) view.findViewById(R.id.mobileId);
        locationId = (TextView) view.findViewById(R.id.locationId);
        genderId = (TextView) view.findViewById(R.id.genderId);
        bloodGroup = (TextView) view.findViewById(R.id.bloodGroup);
        allergic_to = (TextView) view.findViewById(R.id.allergic_to);
        dateOfBirthId = (TextView) view.findViewById(R.id.dateOfBirthId);
        patientIdText = (TextView) view.findViewById(R.id.textPatient);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final Activity activity = getActivity();
        progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
        Bundle bundle = activity.getIntent().getExtras();
        Integer patientId = bundle.getInt(DOCTOR_ID);
        api.getProfile1(new ProfileId(patientId), new Callback<Person>() {
            @Override
            public void success(Person patient, Response response) {
                progress.dismiss();
                emailId.setText(patient.getEmail());
                mobileId.setText(patient.getMobile().toString());
                locationId.setText(patient.getLocation());
                genderId.setText(patient.getGender().intValue() == 0?"Male":"Female");
                bloodGroup.setText(patient.getBloodGroup());
                allergic_to.setText(patient.getAllergicTo());
                dateOfBirthId.setText(DateFormat.getDateInstance().format(new Date(patient.getDateOfBirth())));
                patientIdText.setText(patient.getName());

            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                error.printStackTrace();
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
