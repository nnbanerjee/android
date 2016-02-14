package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Application.MyApi;
import Model.AddDependent;
import Model.AddDependentDoctor;
import Model.AddDependentElement;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 8/11/15.
 */


public class AddDependencyDoctorDialog extends DialogFragment {
    Button addDenpdency,back,skip;
    EditText password,emailId;
    Spinner accessLevelSpinner;
    String[] accessLevel;
    Global global;
    Patient patient;
    TextView name;
    String doctorId;
    String type;
    public MyApi api;
    public static AddDependencyDoctorDialog newInstance() {
        return new AddDependencyDoctorDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dependency_dialog,container,false);
        back = (Button)view.findViewById(R.id.back_dependent);
        global = (Global) getActivity().getApplicationContext();
        patient = global.getPatient();
        System.out.println("Name::::::::::::::"+patient.getName());
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("type",null);
        addDenpdency = (Button)view.findViewById(R.id.add_new_dependent);
        name = (TextView)view.findViewById(R.id.dependent_name);
        name.setText(patient.getName());
        doctorId = global.getDoctorId();
        password = (EditText)view.findViewById(R.id.dependent_password);
        emailId = (EditText)view.findViewById(R.id.dependent_email);
        accessLevelSpinner = (Spinner)view.findViewById(R.id.access_level_dependent);
        accessLevel = getResources().getStringArray(R.array.access_level);
        skip = (Button)view.findViewById(R.id.skip_dependent);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String passwordString = password.getText().toString();
                String access = accessLevelSpinner.getSelectedItem().toString();
                int flagValidation = 0;
                String validationString = "";

                if(flagValidation == 1)
                {

                    Toast.makeText(getActivity(),validationString,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(access.equalsIgnoreCase("Read Only"))
                    {
                        access = "R";
                    }
                    else if(access.equalsIgnoreCase("Full Access"))
                    {
                        access = "F";
                    }
                    AddDependentDoctor addDependent = new AddDependentDoctor();
                    addDependent.setDoctorId(doctorId);
                    addDependent.setEmail(email);
                    addDependent.setPassword(passwordString);
                    addDependent.setParentType(type);
                    AddDependentElement ade = new AddDependentElement();
                    ade.setId(patient.getPatientId());
                    ade.setAccessLevel(access);
                    ArrayList<AddDependentElement> selectedElements = new ArrayList<AddDependentElement>();
                    selectedElements.add(ade);
                    addDependent.setDependents(selectedElements);
                    api.addDoctorDependent(addDependent,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();
                            System.out.println("URl "+response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Dependency  Request Send To User", Toast.LENGTH_LONG).show();
                                AddDependencyDoctorDialog.this.getDialog().cancel();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    });

                }


            }
        });
        accessLevelSpinner.setAdapter(new AccessLevelSpinner(getActivity(), R.layout.customize_spinner,accessLevel));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDependencyDoctorDialog.this.getDialog().cancel();
            }
        });
        addDenpdency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String passwordString = password.getText().toString();
                String access = accessLevelSpinner.getSelectedItem().toString();
                int flagValidation = 0;
                String validationString = "";
                if(email.equals(""))
                {
                    flagValidation = 1;
                    validationString = "Please Enter Email";
                }
                if(passwordString.equals(""))
                {
                    flagValidation = 1;
                    validationString = validationString + "\nPlease Enter Password";
                }

                if(flagValidation == 1)
                {

                    Toast.makeText(getActivity(),validationString,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AddDependentDoctor addDependent = new AddDependentDoctor();
                    addDependent.setDoctorId(doctorId);
                    addDependent.setEmail(email);
                    addDependent.setPassword(passwordString);
                    AddDependentElement ade = new AddDependentElement();
                    ade.setId(patient.getPatientId());
                    ade.setAccessLevel(access);
                    ArrayList<AddDependentElement> selectedElements = new ArrayList<AddDependentElement>();
                    selectedElements.add(ade);
                    addDependent.setDependents(selectedElements);
                    api.addDoctorDependentWithoutConfirmat(addDependent,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();
                            System.out.println("URl "+response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Dependency  Added", Toast.LENGTH_LONG).show();
                                AddDependencyDoctorDialog.this.getDialog().cancel();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    });

                }


            }
        });
        getDialog().setTitle("Add Dependency");
        return view;
    }
    public class AccessLevelSpinner extends ArrayAdapter<String>
    {
        public AccessLevelSpinner(Context ctx, int txtViewResourceId, String[] objects)
        {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.access_level_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(accessLevel[position]);
            return mySpinner;
        }


    }
}
