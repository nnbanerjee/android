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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.medico.view.PatientVisitDatesView;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.Clinic;
import Model.ClinicAppointment;
import Model.PersonID;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/8/15.
 */
public class AddAppointment extends Fragment {

    Global global;
    SharedPreferences session;
    int doctorId,clinicId,patientId;
    DatePicker datePicker;
    TimePicker timePicker;
    Button save,cancel,back;
    Spinner visitType,clinicSpinner;
    MyApi api;
    List<Clinic> clinicList;
    String[] arrayClinics;
    ArrayAdapter<String> clinicAdapter;
    private String loggedInUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_appointment,container,false);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = Integer.parseInt(session.getString("patientId", ""));
        doctorId = Integer.parseInt(session.getString("doctorId", "0"));
        loggedInUser = session.getString("id", "0");
        System.out.println("PatientId::::::"+patientId);
        System.out.println("DoctorId::::::"+doctorId);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker1);
        timePicker = (TimePicker)view.findViewById(R.id.timePicker1);
        save = (Button)view.findViewById(R.id.save_appointment);
        cancel = (Button)view.findViewById(R.id.cancel_appointment);
        visitType = (Spinner)view.findViewById(R.id.visitType);
        clinicSpinner = (Spinner)view.findViewById(R.id.clinicSpinner);
        back = (Button)getActivity().findViewById(R.id.back_button);
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
        api.getAllClinics(new PersonID( loggedInUser),new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinics, Response response) {
                clinicList = clinics;
                arrayClinics = new String[clinicList.size()];
                int i= 0;
                for(Clinic c : clinicList){
                    arrayClinics[i] = c.getClinicName();
                    i = i +1;
                }
                clinicAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayClinics);
                clinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                clinicSpinner.setAdapter(clinicAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = showMonth(datePicker.getMonth());
                int year = datePicker.getYear();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String dateString = year+"-"+month+"-"+day;
                String timeString = updateTime(hour,minute);
                String visitSelected = visitType.getSelectedItem().toString();
                System.out.println("dateString:::::::"+dateString);
                System.out.println("timeString:::::::"+timeString);
                System.out.println("Visit Type::::::"+visitSelected);
                clinicId = Integer.parseInt(clinicList.get(clinicSpinner.getSelectedItemPosition()).getIdClinic());
                ClinicAppointment clinicAppointment = new ClinicAppointment(doctorId, ""+patientId,
                        null, null,clinicId,timeString,dateString,"Occupied",visitSelected);
                api.saveClinicAppointment(clinicAppointment,new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        Toast.makeText(getActivity(), "Appointment Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new PatientVisitDatesView();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
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
                Fragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
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
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goBack()
    {
            Bundle bundle = getArguments();
            if(bundle != null){
                if(bundle.getString("fragment").equals("PatientAllAppointment")){
                    Bundle args = new Bundle();
                    args.putString("fragment","doctor_list");
                    Fragment fragment = new PatientAllAppointment();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }else if(bundle.getString("fragment").equals("doctorPatientListAdapter")){
                    Bundle args = new Bundle();
                    args.putString("fragment",bundle.getString("fragment"));
                    Fragment fragment = new PatientVisitDatesView();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }

            }else{
                Bundle args = new Bundle();
                args.putString("fragment","doctor_list");
                Fragment fragment = new PatientVisitDatesView();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }


    }
    public int showMonth(int month)
    {
        int showMonth = month;
        switch(showMonth)
        {
            case 0:
                showMonth = showMonth + 1;
                break;
            case 1:
                showMonth = showMonth + 1;
                break;
            case 2:
                showMonth = showMonth + 1;
                break;
            case 3:
                showMonth = showMonth + 1;
                break;
            case 4:
                showMonth = showMonth + 1;
                break;
            case 5:
                showMonth = showMonth + 1;
                break;
            case 6:
                showMonth = showMonth + 1;
                break;
            case 7:
                showMonth = showMonth + 1;
                break;
            case 8:
                showMonth = showMonth + 1;
                break;
            case 9:
                showMonth = showMonth + 1;
                break;
            case 10:
                showMonth = showMonth + 1;
                break;
            case 11:
                showMonth = showMonth + 1;
                break;

        }
        return showMonth;
    }
    private String updateTime(int hours, int mins)
    {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }
}
