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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Application.MyApi;
import Model.Appointment;
import com.medico.model.Clinic;
import Model.ClinicAppointment;
import Model.ModeVM;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 6/30/15.
 */
public class AddDoctorAppointment extends Fragment {

    String[] slots,times,patients;
    ArrayList<Patient> addPatients;
    Spinner slotSpinner,timeSpinner,patientSpinner,markAsSpinner,selectedSlotSpinner,selectedTimeSpinner;
    ArrayAdapter<String> slotAdapter,timeAdapter,patientAdapter,selectedTimeAdapter;
    ArrayAdapter<CharSequence> statusAdapter;
    Global global;
    SharedPreferences session;
    int doctorId,clinicId;
    Appointment appointment;
    String shift = "";
    RadioButton createAppointment,markAs,allSlot,selectedSlot,wholeWeek,selectTime;
    RadioGroup radioGroup;
    MyApi api;
    Button save;
    TextView dateTv;
    Date currentDate = new Date();
    RadioButton radio;
    String id = "";
    Calendar startTimeSlot = null;
    Calendar endTimeSlot = null;
    ArrayList<String> selectedTimeSlots = new ArrayList<String>();
    int markAsFlag = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.doctor_appointment_add,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id = session.getString("sessionID",null);
        slotSpinner = (Spinner)view.findViewById(R.id.slot_spinner);
        timeSpinner = (Spinner)view.findViewById(R.id.time_spinner);
        patientSpinner = (Spinner)view.findViewById(R.id.patient_spinner);
        markAsSpinner = (Spinner)view.findViewById(R.id.mark_as_spinner);
        selectedSlotSpinner = (Spinner)view.findViewById(R.id.slot_select_spinner);
        save = (Button)view.findViewById(R.id.done);
        statusAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.mark_as, android.R.layout.simple_spinner_item);
        markAsSpinner.setAdapter(statusAdapter);
        createAppointment = (RadioButton)view.findViewById(R.id.create_appointment_text);
        markAs = (RadioButton)view.findViewById(R.id.Mark_as);
        radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        allSlot = (RadioButton)view.findViewById(R.id.all_slots_radio);
        selectedSlot = (RadioButton)view.findViewById(R.id.selected_slot_radio);
        wholeWeek = (RadioButton)view.findViewById(R.id.whole_week_radio);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radio = (RadioButton) view.findViewById(radioId);
        dateTv = (TextView)view.findViewById(R.id.date_text);
        selectedTimeSpinner = (Spinner)view.findViewById(R.id.selected_time_spineer);
        selectTime = (RadioButton)view.findViewById(R.id.select_time);
        global = (Global) getActivity().getApplicationContext();
        Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Back Button clicked/////////////////////////////");
                goBack();
            }
        });
        appointment = global.getAppointment();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        System.out.println("DoctorId:::::::::"+id);
        api.searchPatients("",new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> patient, Response response) {
                addPatients = new ArrayList<Patient>();
                addPatients = patient;
                patients = new String[patient.size()];
                int i = 0;
                for(Patient pat: patient)
                {
                    patients[i] = pat.getName();
                    i++;
                }
                patientAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, patients);
                patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                patientSpinner.setAdapter(patientAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        if(appointment != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateTv.setText(dateFormat.format(currentDate));
            createAppointment.setChecked(true);
            markAs.setChecked(false);
            radioGroup.setEnabled(false);
            markAsSpinner.setEnabled(false);
            allSlot.setEnabled(false);
            selectedSlot.setEnabled(false);
            wholeWeek.setEnabled(false);
            selectTime.setEnabled(false);
            selectedSlotSpinner.setEnabled(false);
            selectedTimeSpinner.setEnabled(false);
            clinicId = Integer.parseInt(session.getString("patient_clinicId", ""));
            doctorId = Integer.parseInt(session.getString("clinic_doctorId", ""));
            if (appointment.getSlot().equalsIgnoreCase("slot 1")) {
                shift = "shift1";
            } else if (appointment.getSlot().equalsIgnoreCase("slot 2")) {
                shift = "shift2";
            } else {
                shift = "shift3";
            }

            slots = new String[1];
            slots[0] = new String(appointment.getStartTime() + " - " + appointment.getEndTime());
            times = new String[1];
            times[0] = new String(appointment.getAppointmentTime());
            slotAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, slots);
            slotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            slotSpinner.setAdapter(slotAdapter);
            selectedSlotSpinner.setAdapter(slotAdapter);
            timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, times);
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSpinner.setAdapter(timeAdapter);
            startTimeSlot = getStringToCal(appointment.getStartTime());
            endTimeSlot = getStringToCal(appointment.getEndTime());
            int count = 0;
            while (endTimeSlot.getTimeInMillis() > startTimeSlot.getTimeInMillis()) {
                count = count + 1;
                String timeHHMM = null;
                timeHHMM = startTimeSlot.get(Calendar.HOUR) + ":" + startTimeSlot.get(Calendar.MINUTE);
                if (startTimeSlot.get(Calendar.AM_PM) == 0) {
                    timeHHMM = timeHHMM + "AM";
                } else {
                    timeHHMM = timeHHMM + "PM";
                }
                selectedTimeSlots.add(timeHHMM);
                startTimeSlot.add(Calendar.MINUTE, 15);
            }
            selectedTimeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,selectedTimeSlots);
            selectedTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectedTimeSpinner.setAdapter(selectedTimeAdapter);
        }

        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment.setChecked(true);
                markAs.setChecked(false);
                slotSpinner.setEnabled(true);
                timeSpinner.setEnabled(true);
                patientSpinner.setEnabled(true);
                radioGroup.setClickable(false);
                markAsSpinner.setEnabled(false);
                allSlot.setEnabled(false);
                selectedSlot.setEnabled(false);
                wholeWeek.setEnabled(false);
                selectedSlotSpinner.setEnabled(false);
                selectedTimeSpinner.setEnabled(false);
                selectTime.setEnabled(false);
            }
        });
        markAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAs.setChecked(true);
                createAppointment.setChecked(false);
                slotSpinner.setEnabled(false);
                timeSpinner.setEnabled(false);
                patientSpinner.setEnabled(false);
                radioGroup.setClickable(true);
                markAsSpinner.setEnabled(true);
                allSlot.setEnabled(true);
                allSlot.setChecked(true);
                selectedSlot.setEnabled(true);
                wholeWeek.setEnabled(true);
                selectedSlotSpinner.setEnabled(true);
                selectedTimeSpinner.setEnabled(true);
                selectTime.setEnabled(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createAppointment.isChecked())
                {
                    System.out.println("Create Appointment Checked///////////////////");
                    int position = patientSpinner.getSelectedItemPosition();
                    Patient patient = addPatients.get(position);
                    System.out.println("Patient Name::::::" + patient.getName());
                    ClinicAppointment clinicAppointment = new ClinicAppointment(doctorId, patient.getEmailID(),
                            appointment.getStartTime() + " to " + appointment.getEndTime(), shift, clinicId,
                            appointment.getAppointmentTime(), dateTv.getText().toString(), "Occupied","New Profile");
                    api.saveClinicsAppointmentData(clinicAppointment,new Callback<JsonObject>() {
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
                if(markAs.isChecked())
                {
                    ModeVM vm = new ModeVM();
                    vm.doctorId = id;
                    vm.clinicId = clinicId;
                    vm.shift = shift;
                    vm.availability = markAsSpinner.getSelectedItem().toString();
                    vm.date = currentDate.toString();
                    System.out.println("Mark As Checked/////////////////////////////");
                    String radioStatus = radio.getText().toString();
                    System.out.println("Status::::::"+radioStatus);
                    if(radioStatus.equals("Selected Slot"))
                    {
                        String slot = selectedSlotSpinner.getSelectedItem().toString();
                        System.out.println("Slot Text::::::::"+slot);
                        vm.markAs = "Selected Slot";
                        markAsFlag = 0;
                    }
                    else if(radioStatus.equals("All Slots"))
                    {
                        vm.markAs = "All Slots";
                        markAsFlag = 0;
                    }
                    else if(radioStatus.equals("Select Time"))
                    {
                        markAsFlag = 1;
                    }
                    else
                    {
                        vm.markAs = "Whole week";
                        markAsFlag = 0;
                    }
                    if(markAsFlag == 0) {
                        api.setStatusSlot(vm, new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Toast.makeText(getActivity(), "Mode Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                                Date currentDate = new Date();
                                String doctor = session.getString("sessionID", null);
                                api.getAllDoctorClinics(doctor, currentDate, new Callback<List<Clinic>>() {
                                    @Override
                                    public void success(List<Clinic> clinics, Response response) {
                                        global.setAllClinicsList(clinics);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                });

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
                    else
                    {
                        ClinicAppointment clinicAppointment = new ClinicAppointment(doctorId, "0",
                            appointment.getStartTime() + " to " + appointment.getEndTime(), shift, clinicId,
                            appointment.getAppointmentTime(), dateTv.getText().toString(), vm.availability,"New Profile");
                        api.saveDoctorAppointment(clinicAppointment,new Callback<JsonObject>() {
                            @Override
                            public void success(JsonObject jsonObject, Response response) {
                                Toast.makeText(getActivity(), "Appointment Mode Saved !!!", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                radio = (RadioButton) view.findViewById(radioId);
            }
        });
        return view;
    }
    public Calendar getStringToCal(String str){

        String[] time = str.split(":");
        String[] timeMin = time[1].split(" ");
        int hr = Integer.parseInt(time[0].trim());
        int min =  Integer.parseInt(timeMin[0].trim());
        String am = timeMin[1].trim();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR, hr);
        if(am.equals("AM")){
            cal.set(Calendar.AM_PM,Calendar.AM);
        }else{
            cal.set(Calendar.AM_PM,Calendar.PM);
        }
        //cal.setTime(date);
        return cal;
    }
    public void goBack()
    {
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinic");
        Fragment fragmentMain = new DoctorClinicFragment();
        FragmentManager fragmentMangerMain = getActivity().getFragmentManager();
        fragmentMangerMain.beginTransaction().replace(R.id.content_frame,fragmentMain,"Doctor Consultations").addToBackStack(null).commit();
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
}
