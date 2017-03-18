package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.ManagePatientFragment;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import com.medico.application.MyApi;
import Model.BucketPatient;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 24-Mar-15.
 */
public class AddNewPatient extends Fragment {
    public MyApi api;
    private String doctorId = "";
    private EditText nameTv, emailTv, mobileTv, locationTv, countryCodeTv;
    private Button doneButton, backButton, mapButton, back;
    private String name, email, mobile, location, countryCode;
    private int flagValidaltion = 0;
    private String validation = "";
    private ArrayList<Patient> alreadyAdded;
    TextView globalTv;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        final Global go = (Global) getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if (locationString.equals("")) {
            locationTv.setText("");
        } else {
            locationTv.setText(locationString);
        }

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void manageScreenIcon() {
        globalTv.setText("Add New Patient");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }

    public void goBack() {
        final Global go = (Global) getActivity().getApplicationContext();
        go.setLocation("");
        Fragment frag2 = new AddPatient();
        System.out.println("I am in Resume Method::::");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_patient, container, false);
        getActivity().getActionBar().hide();
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", null);
        nameTv = (EditText) view.findViewById(R.id.name);
        emailTv = (EditText) view.findViewById(R.id.email);
        mobileTv = (EditText) view.findViewById(R.id.mobile);
        locationTv = (EditText) view.findViewById(R.id.location);
        countryCodeTv = (EditText) view.findViewById(R.id.country_code);
        doneButton = (Button) view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameTv.getText().toString();
                email = emailTv.getText().toString();
                mobile = mobileTv.getText().toString();
                location = locationTv.getText().toString();
                countryCode = countryCodeTv.getText().toString();
                flagValidaltion = 0;
                validation = "";

                if (name.equals("")) {
                    validation = "Please Enter Name";
                    flagValidaltion = 1;
                } else if (email.equals("")) {
                    validation = validation + "\n" + "Please Enter Email";
                    flagValidaltion = 1;
                } else if (mobile.equals("")) {
                    validation = validation + "\n" + "Please Enter Mobile Number";
                    flagValidaltion = 1;
                } else if (location.equals("")) {
                    validation = validation + "\n" + "Please Enter Location";
                    flagValidaltion = 1;
                } else if (countryCode.equals("")) {
                    validation = validation + "\n" + "Please Enter Country Code";
                    flagValidaltion = 1;
                }

                int len = mobile.length();
                if (flagValidaltion == 1) {
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                } else if (len < 10) {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number Properly", Toast.LENGTH_SHORT).show();
                } else {

                    mobile = countryCode + mobile;

                    BucketPatient patient = new BucketPatient();
                    patient.setName(name);
                    patient.setLocation(location);
                    patient.setEmailID(email);
                    patient.setMobileNumber(mobile);

                    api.addPatient(doctorId, patient, new Callback<String>() {
                        @Override
                        public void success(String response, Response response2) {
                            if (response.equalsIgnoreCase("Success")) {
                                Toast.makeText(getActivity(), "Patient Added Successfully", Toast.LENGTH_LONG);
                                Fragment fragment = new ManagePatientFragment();
                                FragmentManager fragmentManger = getFragmentManager();
                                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Patient").commit();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });

                }
            }
        });
        mapButton = (Button) view.findViewById(R.id.location_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(), MapActivity.class);
                startActivity(intObj);
            }
        });
        backButton = (Button) view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Global go = (Global) getActivity().getApplicationContext();
                go.setLocation("");
                Fragment frag2 = new AddPatient();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        manageScreenIcon();
        return view;
    }


}
