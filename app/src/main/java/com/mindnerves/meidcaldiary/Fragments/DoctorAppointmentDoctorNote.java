package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.DoctorNotesVM;
import Model.ReminderVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentDoctorNote extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue;
    EditText doctorNotes;
    Button saveDoctorNotes;
    String[] symptoms_item = null;
    String patientId,doctorId,appointmentTime,appointmentDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_doctor_note, container,false);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        patientId = session.getString("patientId","");
        doctorId = session.getString("doctorId","");
        appointmentDate = session.getString("doctor_patient_appointmentDate","");
        appointmentTime = session.getString("doctor_patient_appointmentTime","");

        symptoms_item = getResources().getStringArray(R.array.symptoms);
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        doctorNotes = (EditText) view.findViewById(R.id.doctorNotes);
        saveDoctorNotes = (Button) view.findViewById(R.id.saveDoctorNotes);

        symptomsValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, symptoms_item));
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        diagnosisValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, symptoms_item));
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        saveDoctorNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(symptomsValue.getText().toString().length() == 0){
                    symptomsValue.setError("Please Enter Symptoms");
                }else if(diagnosisValue.getText().toString().length() == 0){
                    diagnosisValue.setError("Please Enter diagnosis");
                }else {
                    DoctorNotesVM doctorNotesVM = new DoctorNotesVM();
                    doctorNotesVM.setSymptoms(symptomsValue.getText().toString());
                    doctorNotesVM.setDiagnosis(diagnosisValue.getText().toString());
                    doctorNotesVM.setDoctorNotes(doctorNotes.getText().toString());
                    doctorNotesVM.setDoctorId(doctorId);
                    doctorNotesVM.setPatientId(patientId);
                    doctorNotesVM.setAppointmentDate(appointmentDate);
                    doctorNotesVM.setAppointmentTime(appointmentTime);

                    saveDoctorNotesData(doctorNotesVM);
                }
            }
        });

        getDoctorNotes(doctorId,patientId,appointmentDate,appointmentTime);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void  goToBack(){
        Fragment fragment;
        fragment = new AllDoctorPatientAppointment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDoctorNotes(String doctorId, String patientId, String appointmentDate, String appointmentTime){
        api.getDoctorNotesData(doctorId, patientId, appointmentDate,appointmentTime, new Callback<DoctorNotesVM>() {
            @Override
            public void success(DoctorNotesVM doctorNotesVM, Response response) {
                //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                if(doctorNotesVM.getDoctorId() != null){
                    symptomsValue.setText(doctorNotesVM.getSymptoms());
                    diagnosisValue.setText(doctorNotesVM.getDiagnosis());
                    doctorNotes.setText(doctorNotesVM.getDoctorNotes());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void saveDoctorNotesData(DoctorNotesVM doctorNotesVM){
        api.saveDoctorNotes(doctorNotesVM, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


}
