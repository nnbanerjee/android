package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.medico.application.MyApi;
import Model.Appointment;
import Model.ClinicAppointment;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/8/15.
 */
public class AppointmentListAdd extends Fragment {
    SharedPreferences session;
    int doctorId,clinicId;
    Button save,cancel,back;
    String[] stringPatients;
    Spinner patientSp;
    Spinner visitType;
    MyApi api;
    String searchText = "";
    ArrayList<Patient> addPatients;
    Date currentDate = new Date();
    TextView dateTv,slotTv,appointmentTv;
    Global global;
    Appointment appointment;
    String shift;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_list_add,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        clinicId = Integer.parseInt(session.getString("patient_clinicId", ""));
        doctorId = Integer.parseInt(session.getString("clinic_doctorId", ""));
        global = (Global) getActivity().getApplicationContext();
        appointment = global.getAppointment();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        save = (Button)view.findViewById(R.id.appointment_save);
        cancel = (Button)view.findViewById(R.id.appointment_cancel);
        back = (Button)getActivity().findViewById(R.id.back_button);
        patientSp = (Spinner)view.findViewById(R.id.patient_spinner);
        slotTv = (TextView)view.findViewById(R.id.slot_appointment);
        appointmentTv = (TextView)view.findViewById(R.id.appointment_time);
        visitType = (Spinner)view.findViewById(R.id.visitType);
        dateTv = (TextView)view.findViewById(R.id.date_text);
        System.out.println("Date::::::"+dateFormat.format(currentDate));
        dateTv.setText(dateFormat.format(currentDate));
        slotTv.setText(appointment.getStartTime()+" - "+appointment.getEndTime());
        appointmentTv.setText(appointment.getAppointmentTime());
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
        if(appointment.getSlot().equalsIgnoreCase("slot 1"))
        {
            shift = "shift1";
        }
        else if(appointment.getSlot().equalsIgnoreCase("slot 2"))
        {
            shift = "shift2";
        }
        else
        {
            shift = "shift3";
        }
        System.out.println("Shift Appointment::::::::"+shift);

        api.searchPatients(searchText,new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> patients, Response response) {
                addPatients = new ArrayList<Patient>();
                addPatients = patients;
                stringPatients = new String[patients.size()];
                int i = 0;
                for(Patient pat : patients)
                {
                    stringPatients[i] = pat.getName();
                    i++;
                }
                patientSp.setAdapter(new PatientSpinner(getActivity(), R.layout.customize_spinner,stringPatients));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = patientSp.getSelectedItemPosition();
                Patient patient = addPatients.get(position);
                System.out.println("Patient Name::::::" + patient.getName());
                ClinicAppointment clinicAppointment = new ClinicAppointment(doctorId, patient.getEmailID(),
                        appointment.getStartTime() + " to " + appointment.getEndTime(), shift, clinicId,
                        appointment.getAppointmentTime(), dateTv.getText().toString(), "Occupied", visitType.getSelectedItem().toString());
                api.saveClinicsAppointmentData(clinicAppointment, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        Toast.makeText(getActivity(), "Appointment Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new DoctorClinicFragment();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        System.out.println("Clinic id:::::::"+clinicId);
        System.out.println("doctor id:::::::"+doctorId);
        return view;
    }
    public void goBack()
    {
        Fragment fragment = new DoctorClinicFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public class PatientSpinner extends ArrayAdapter<String>
    {
        public PatientSpinner(Context ctx, int txtViewResourceId, String[] objects)
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
            View mySpinner = inflater.inflate(R.layout.customize_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(stringPatients[position]);
            return mySpinner;
        }


    }



}
