package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.AlarmService;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Application.MyApi;
import DB.DatabaseHandler;
import Model.AppointmentDB;
import com.medico.model.Clinic;
import Model.ClinicAppointment;
import Model.DoctorSearchResponse;
import Model.PersonID;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 10-10-2015.
 */
public class DiagnosisTestReminder extends Fragment {
    ImageView dateTimePicker;
    EditText date;
    Spinner clinicSpineer,typeSpineer,doctorSpinner;
    Button Alarm;
    Global global;
    public MyApi api;
    List<Clinic> clinicDetailVm;
    String[] clinics,types,doctors;
    List<DoctorSearchResponse> doctorList;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int clinicPosition = 0;
    int doctorPosition = 0;
    SharedPreferences session;
    private String loggedInUSer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diagnostic_test_reminder, container, false);
        dateTimePicker = (ImageView)view.findViewById(R.id.date_time_picker);
        date = (EditText)view.findViewById(R.id.date_diagnosis);
        doctorSpinner = (Spinner)view.findViewById(R.id.doctor_list);
        clinicSpineer = (Spinner)view.findViewById(R.id.clinic_list);
        typeSpineer = (Spinner)view.findViewById(R.id.type_list);
        Alarm = (Button)view.findViewById(R.id.alarm_set);
        global = (Global) getActivity().getApplicationContext();
        types = getResources().getStringArray(R.array.visit_type_list);
        typeSpineer.setAdapter(new TypeSpinner(getActivity(), R.layout.customize_spinner,types));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggedInUSer =  session.getString("id", "0") ;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getAllClinics(new PersonID( loggedInUSer), new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinicList, Response response) {
                clinicDetailVm = clinicList;
                clinics = new String[clinicList.size()];
                int count = 0;
                for(Clinic vm: clinicDetailVm)
                {
                    clinics[count] = vm.getClinicName();
                    count = count + 1;
                }
                clinicSpineer.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner,clinics));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        clinicSpineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clinicPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(global.getDoctorSearchResponses() != null){
            doctorList = global.getDoctorSearchResponses();
            doctors = new String[doctorList.size()];
            int i = 0;
            for(DoctorSearchResponse doctor : doctorList){
                doctors[i] = doctor.getName();
                i++;
            }
            doctorSpinner.setAdapter(new DoctorSpinner(getActivity(), R.layout.customize_spinner,doctors));
        }
        if(global.diagnosisTestTime != null){
            date.setText(global.diagnosisTestTime);
        }else {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String timeString = updateTimeString(hour, minute);
            String dateString = cal.get(Calendar.YEAR) + "-" + showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH);
            date.setText(timeString + "  " + dateString);
        }
        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DiagnosticTimePicker();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.frame_layout, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateSet = null;
                String[] timeValue;
                String[] dateTimeArray = date.getText().toString().split("  ");
                try{
                    dateSet = formatter.parse(dateTimeArray[1]);
                    Calendar calOne = Calendar.getInstance();
                    calOne.setTime(dateSet);
                    timeValue = dateTimeArray[0].split(":");
                    int hour1 = Integer.parseInt(timeValue[0].trim());
                    int min1 = Integer.parseInt(timeValue[1].trim().split(
                            "[a-zA-Z ]+")[0]);
                    calOne.set(Calendar.HOUR_OF_DAY, hour1);
                    calOne.set(Calendar.MINUTE, min1);
                    calOne.set(Calendar.MINUTE,-15);
                    String strAM_PM = timeValue[1].replaceAll("[0-9]", "");
                    if (strAM_PM.equals("AM")) {
                        calOne.set(Calendar.AM_PM, 0);
                    } else {
                        calOne.set(Calendar.AM_PM, 1);
                    }
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
                    Intent intent = new Intent(getActivity(), AlarmService.class);
                    long trigerTime = calOne.getTimeInMillis();
                    System.out.println("trigerTime = "+trigerTime);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) trigerTime,
                            intent, PendingIntent.FLAG_ONE_SHOT);
                    am.set(AlarmManager.RTC_WAKEUP, trigerTime, pendingIntent);
                    int doctorId = Integer.parseInt(doctorList.get(doctorPosition).getDoctorId().toString());
                    String patientId = session.getString("sessionID", null);
                    int clinicId = Integer.parseInt(clinicDetailVm.get(clinicPosition).getIdClinic().toString());
                    String type = typeSpineer.getSelectedItem().toString();
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    AppointmentDB appointment = new AppointmentDB();
                    appointment.setPatientId(patientId);
                    appointment.setAppointment_status("true");
                    appointment.setAppointment_date(dateTimeArray[0]);
                    appointment.setAppointment_time(dateTimeArray[1]);
                    appointment.setAlarm_id(""+trigerTime);
                    appointment.setAppointment_type(type);
                    Boolean value = databaseHandler.savePatientAppointment(appointment);
                    System.out.println("Table status= "+value);
                    ClinicAppointment clinicAppointment = new ClinicAppointment(doctorId, ""+patientId,
                            null, null,clinicId,dateTimeArray[0],dateTimeArray[1],"Occupied",type);
                    api.saveClinicAppointment(clinicAppointment,new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Appointment Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new PatientAppointmentStatus();
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
                catch(Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
        return view;
    }
    private String updateTimeString(int hours, int mins)
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
    public class ClinicSpinner extends ArrayAdapter<String>
    {
        public ClinicSpinner(Context ctx, int txtViewResourceId, String[] objects)
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
            main_text.setText(clinics[position]);
            return mySpinner;
        }


    }
    public class DoctorSpinner extends ArrayAdapter<String>
    {
        public DoctorSpinner(Context ctx, int txtViewResourceId, String[] objects)
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
            View mySpinner = inflater.inflate(R.layout.customize_doctor_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(doctors[position]);
            return mySpinner;
        }


    }
    public class TypeSpinner extends ArrayAdapter<String>
    {
        public TypeSpinner(Context ctx, int txtViewResourceId, String[] objects)
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
            main_text.setText(types[position]);
            return mySpinner;
        }


    }
}
