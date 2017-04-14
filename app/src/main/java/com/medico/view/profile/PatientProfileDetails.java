package com.medico.view.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.Person;
import com.medico.model.ProfileId;
import com.medico.view.home.ParentFragment;

import java.text.DateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientProfileDetails extends ParentFragment {


    ProgressDialog progress;

    TextView personId;
    EditText name,email,dob,country,city,mobile,allergicTo;
    AutoCompleteTextView mAutocompleteView;
    Spinner gender_spinner,mobile_country,specialization,bloodGroup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.person_profile_edit_view, container,false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        RelativeLayout profilePic = (RelativeLayout) view.findViewById(R.id.layout20);
        profilePic.setVisibility(View.GONE);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        dob = (EditText) view.findViewById(R.id.dob);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        country = (EditText) view.findViewById(R.id.country_spinner);
        city = (EditText) view.findViewById(R.id.city);
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        specialization = (Spinner) view.findViewById(R.id.specialization);
        allergicTo = (EditText)view.findViewById(R.id.allergic_to);
        bloodGroup = (Spinner) view.findViewById(R.id.bloodGroup);
        TextView relationText = (TextView) view.findViewById(R.id.relation_text);
        Spinner relation = (Spinner) view.findViewById(R.id.relation);
        relationText.setVisibility(View.GONE);
        relation.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final Activity activity = getActivity();
        progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
        Bundle bundle = activity.getIntent().getExtras();
        Integer patientId = bundle.getInt(PATIENT_ID);
        api.getProfile1(new ProfileId(patientId), new Callback<Person>() {
            @Override
            public void success(Person patient, Response response) {
                progress.dismiss();
                email.setText(patient.getEmail());
                mobile.setText(patient.getMobile().toString());
                mAutocompleteView.setText(patient.getLocation());
                gender_spinner.setSelection(patient.getGender().intValue());
                bloodGroup.setSelection(getIndex(bloodGroup.getAdapter(),patient.getBloodGroup(),0));
                allergicTo.setText(patient.getAllergicTo());
                dob.setText(DateFormat.getDateInstance().format(new Date(patient.getDateOfBirth())));
                name.setText(patient.getName());

            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                error.printStackTrace();
                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndex(Adapter adapter, String specialization, int profile)
    {
        for(int i = 0; i < adapter.getCount(); i++)
        {
            if(adapter.getItem(i).toString().equalsIgnoreCase(specialization))
                return i;
        }
        return 0;
    }
}
