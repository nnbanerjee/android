package com.medicohealthcare.view.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

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

    TextView personId;
    EditText name,email,dob,country,city,mobile,allergicTo;
    AutoCompleteTextView mAutocompleteView;
    Spinner gender_spinner,mobile_country,bloodGroup;
    MultiAutoCompleteTextView specialization;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.person_profile_edit_view, container,false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        RelativeLayout profilePic = (RelativeLayout) view.findViewById(R.id.layout20);
        profilePic.setVisibility(View.GONE);
        RelativeLayout passwordField = (RelativeLayout) view.findViewById(R.id.layout30);
        profilePic.setVisibility(View.GONE);
        name = (EditText) view.findViewById(R.id.name);
        name.setEnabled(false);
        email = (EditText) view.findViewById(R.id.email);
        email.setEnabled(false);
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        gender_spinner.setEnabled(false);
        dob = (EditText) view.findViewById(R.id.dob);
        dob.setEnabled(false);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        ImageButton calendar = (ImageButton)view.findViewById(R.id.dob_calendar);
        calendar.setEnabled(false);
        Button locationButton = (Button)view.findViewById(R.id.current_location_button);
        locationButton.setEnabled(false);
        Button deleteButton = (Button)view.findViewById(R.id.location_delete_button);
        deleteButton.setEnabled(false);
        mAutocompleteView.setEnabled(false);
        country = (EditText) view.findViewById(R.id.country_spinner);
        country.setEnabled(false);
        city = (EditText) view.findViewById(R.id.city);
        city.setEnabled(false);
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile.setEnabled(false);
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        mobile_country.setEnabled(false);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setEnabled(false);
        allergicTo = (EditText)view.findViewById(R.id.allergic_to);
        allergicTo.setEnabled(false);
        bloodGroup = (Spinner) view.findViewById(R.id.bloodGroup);
        bloodGroup.setEnabled(false);
        TextView relationText = (TextView) view.findViewById(R.id.relation_text);
        RelativeLayout relation = (RelativeLayout) view.findViewById(R.id.layout_relation);
        relationText.setVisibility(View.GONE);
        relation.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final Activity activity = getActivity();
        showBusy();
        Bundle bundle = activity.getIntent().getExtras();
        final Integer patientId = bundle.getInt(PATIENT_ID);
        api.getProfile1(new ProfileId(patientId), new Callback<Person>() {
            @Override
            public void success(Person patient, Response response) {

                country.setText(patient.getCountry());
                city.setText(patient.getCity());
                email.setText(patient.getEmail());
                String[] countryCode = {patient.location};
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,countryCode);
                mobile_country.setAdapter(countryAdapter);
                mobile.setText(patient.getMobile().toString());
                specialization.setText(patient.getSpeciality());
                mAutocompleteView.setText(patient.getAddress());
                gender_spinner.setSelection(patient.getGender().intValue());
                if(bloodGroup != null)
                    bloodGroup.setSelection(getIndex(bloodGroup.getAdapter(),patient.getBloodGroup(),0));
                allergicTo.setText(patient.getAllergicTo());
                if(patient.getDateOfBirth() != null)
                    dob.setText(DateFormat.getDateInstance().format(new Date(patient.getDateOfBirth())));
                else
                    dob.setText("");
                name.setText(patient.getName());
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
