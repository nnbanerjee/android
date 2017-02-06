package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.PatientMedicinReminder;
import com.medico.view.PatientMenusManage;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.medico.adapter.MedicineAdapter;
import Application.MyApi;
import Model.AlarmReminderVM;
import com.medico.model.Clinic;
import Model.MedicineVM;
import Model.PersonTemp;
import Model.PersonID;
import Model.ReminderVM;
import Model.SummaryHistoryVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class PatientAppointmentSummary extends Fragment {

    MyApi api;
    public SharedPreferences session;
    String doctor_email, appointmentDate, appointmentTime, patientId,clinicId;
    Integer doctorId;
    TextView visitedDate, referedBy,clinicName;
    Spinner visit;
    Button reminderBtn, saveSummary;
    MultiAutoCompleteTextView symptomsValue, diagnosisValue, medicineValue, testPrescribedValue;
    String[] symptoms_item, medicin_list = null;
    String[] typeList;
    Global global;
    String type = "";
    ProgressDialog progress;

    Button logout, drawar, symptomsHistryBtn, diagnosisHistryBtn,
            prescribHistryBtn, testHistryBtn;
    ImageView addAlarm;
    MedicineAdapter adapter;
    Set<String> medicineSet;
    List<MedicineVM> medicineVMs;
    ListView alarmListView;
    List<AlarmReminderVM> alarms;
    private String loggedInUSer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container, false);
        global = (Global) getActivity().getApplicationContext();
        alarmListView = (ListView)view.findViewById(R.id.alarm_list);
        clinicName = (TextView)view.findViewById(R.id.clinicName);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctor_email = session.getString("patient_doctor_email", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        doctorId = Integer.parseInt(session.getString("doctorId", "0"));
        clinicId = session.getString("patient_clinicId", null);
        patientId = session.getString("sessionID", null);
        type =  session.getString("loginType",null);

        loggedInUSer =  session.getString("id", "0") ;
        System.out.println("Type::::::"+type);
        //Retrofit Initialization
        addAlarm = (ImageView)view.findViewById(R.id.add_alarm);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getAllClinics( new PersonID(loggedInUSer),new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinics, Response response) {
                for(Clinic c:clinics)
                {
                    if(c.getIdClinic().equals(clinicId))
                    {
                        clinicName.setText(c.getClinicName());
                        break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });
        visit = (Spinner) view.findViewById(R.id.visit);
        visitedDate = (TextView) view.findViewById(R.id.visitedDate);
        referedBy = (TextView) view.findViewById(R.id.referedBy);
        reminderBtn = (Button) view.findViewById(R.id.reminderBtn);

        saveSummary = (Button) view.findViewById(R.id.saveSummary);

        symptomsHistryBtn = (Button) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (Button) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (Button) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (Button) view.findViewById(R.id.testHistryBtn);

        symptoms_item = getResources().getStringArray(R.array.symptoms);
        medicin_list = getResources().getStringArray(R.array.medicin_list);
        typeList = getResources().getStringArray(R.array.visit_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, typeList);
        visit.setAdapter(adapter);

        symptomsValue = (MultiAutoCompleteTextView) view.findViewById(R.id.symptomsValue);
        diagnosisValue = (MultiAutoCompleteTextView) view.findViewById(R.id.diagnosisValue);
        medicineValue = (MultiAutoCompleteTextView) view.findViewById(R.id.medicineValue);
        testPrescribedValue = (MultiAutoCompleteTextView) view.findViewById(R.id.testPrescribedValue);

        symptomsValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        diagnosisValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        medicineValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, medicin_list));
        medicineValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        testPrescribedValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        testPrescribedValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        visitedDate.setText(appointmentDate);

        symptomsHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData("systoms");
            }
        });

        diagnosisHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData("diagnosis");
            }
        });

        prescribHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData("prescribHistry");

            }
        });

        testHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData("testHistry");

            }
        });
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("visitedDate", visitedDate.getText().toString());
                args.putString("visit", visit.getSelectedItem().toString());
                args.putString("referedBy", referedBy.getText().toString());
                args.putString("symptomsValue", symptomsValue.getText().toString());
                args.putString("diagnosisValue", diagnosisValue.getText().toString());
                args.putString("testPrescribedValue", testPrescribedValue.getText().toString());
                args.putString("medicinName", medicineValue.getText().toString());
                args.putString("argument","NewMedicine");
                Fragment fragment = new PatientMedicinReminder();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicinName = medicineValue.getText().toString();
                if (medicinName == null || medicinName.equals("")) {
                    Toast.makeText(getActivity(), "Please enter medicin name", Toast.LENGTH_SHORT).show();
                } else {

                    Bundle args = new Bundle();
                    args.putString("visitedDate", visitedDate.getText().toString());
                    args.putString("visit", visit.getSelectedItem().toString());
                    args.putString("referedBy", referedBy.getText().toString());
                    args.putString("symptomsValue", symptomsValue.getText().toString());
                    args.putString("diagnosisValue", diagnosisValue.getText().toString());
                    args.putString("testPrescribedValue", testPrescribedValue.getText().toString());
                    args.putString("medicinName", medicineValue.getText().toString());

                    Fragment fragment = new PatientMedicinReminder();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });

        saveSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderVM reminderVM = new ReminderVM();
                if (global.getReminderVM() != null) {
                    reminderVM.id = global.getReminderVM().id;
                } else {
                    reminderVM.id = null;
                }
                reminderVM.doctorId = doctorId;
                reminderVM.patientId = patientId;
                reminderVM.ownerType = "Patient";
                reminderVM.appointmentDate = appointmentDate;
                reminderVM.appointmentTime = appointmentTime;
                reminderVM.medicinName = medicineValue.getText().toString();
                reminderVM.visitDate = visitedDate.getText().toString();
                reminderVM.visitType = visit.getSelectedItem().toString();
                reminderVM.referredBy = referedBy.getText().toString();
                reminderVM.symptoms = symptomsValue.getText().toString();
                reminderVM.diagnosis = diagnosisValue.getText().toString();
                reminderVM.testsPrescribed = testPrescribedValue.getText().toString();
                reminderVM.alarmReminderVMList = null;

                savePatientReminderData(reminderVM);
            }
        });


        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        //getAllPatientAppointment();
        getAllPatientSummary();

        return view;
    }

    public void getHistryData(final String histryString) {
        api.getHistry(doctor_email, patientId, appointmentDate, appointmentTime, new Callback<List<SummaryHistoryVM>>() {
            @Override
            public void success(List<SummaryHistoryVM> historyVMList, Response response) {

                System.out.println("historyVMList.size()  = " + historyVMList.size());
                for (SummaryHistoryVM summaryHistoryVM : historyVMList) {
                    System.out.println("summaryHistoryVM.curDate before = " + summaryHistoryVM.curDate);
                }

                // Collections.sort(historyVMList, new SortListComparator());

                for (SummaryHistoryVM summaryHistoryVM : historyVMList) {
                    System.out.println("summaryHistoryVM.curDate after = " + summaryHistoryVM.curDate);
                }
                if (histryString.equalsIgnoreCase("systoms")) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    //show.summaryHistoryVMs = historyVMList;
                    show.heading = "Symptoms Histry";
                    show.show(getFragmentManager(), "Dialog");
                } else if (histryString.equalsIgnoreCase("diagnosis")) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    //show.summaryHistoryVMs = historyVMList;
                    show.heading = "Diagnosis Histry";
                    show.show(getFragmentManager(), "Dialog");
                } else if (histryString.equalsIgnoreCase("prescribHistry")) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                   // show.summaryHistoryVMs = historyVMList;
                    show.heading = "Prescribed Histry";
                    show.show(getFragmentManager(), "Dialog");
                } else if (histryString.equalsIgnoreCase("testHistry")) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                   // show.summaryHistoryVMs = historyVMList;
                    show.heading = "Test Histry";
                    show.show(getFragmentManager(), "Dialog");
                }

                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goToBack() {
        Bundle bunDoctor = getArguments();
        if (global.getSummaryJump() != null) {
            if(global.getSummaryJump() == true) {
                global.setSummaryJump(false);
                TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment1 = new PatientMenusManage();
                FragmentManager fragmentManger1 = getFragmentManager();
                fragmentManger1.beginTransaction().replace(R.id.content_frame, fragment1, "Patients Information").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
                final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
                final RelativeLayout profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
                drawar = (Button) getActivity().findViewById(R.id.drawar_button);
                logout = (Button) getActivity().findViewById(R.id.logout);
                logout.setBackgroundResource(R.drawable.logout);
                drawar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);

                profileLayout.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
                api.getProfilePatient(patientId, new Callback<PersonTemp>() {
                    @Override
                    public void success(PersonTemp person, Response response) {
                        new ImageLoadTask(getResources().getString(R.string.image_base_url) + person.getId(), profilePicture).execute();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(bunDoctor != null){
                if((bunDoctor.getString("fragment") != null)) {
                    if(bunDoctor.getString("fragment").equalsIgnoreCase("clinicListAdapter")){
                        Fragment fragment = new PatientAllClinics();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                    }else if(bunDoctor.getString("fragment").equalsIgnoreCase("clinicAppointmentListAdapter")){
                        Fragment fragment = new PatientAppointmentStatus();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                    }else {
                        Fragment fragment = new PatientAllDoctors();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }else if(bunDoctor.getString("appointmentPatient") != null){
                    Bundle bun = new Bundle();
                    bun.putString("fragment", "PatientAppointmentSummary");
                    Fragment fragment = new PatientAllAppointment();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    final Button back = (Button) getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                }else if(bunDoctor.getString("patientAllAppointment") != null){
                    Bundle bun = new Bundle();
                    bun.putString("fragment", "PatientAppointmentSummary");
                    Fragment fragment = new PatientAllAppointment();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    final Button back = (Button) getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                }
            }
            else if (type.equalsIgnoreCase("Patient")) {
                Fragment fragment = new AppointmentsPatient();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }else{
                System.out.println("I am PatientAppointment Summary::::::::::::::::::");
                Bundle bun = new Bundle();
                bun.putString("fragment", "PatientAppointmentSummary");
                Fragment fragment = new PatientAllAppointment();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);
            }
        }else if(bunDoctor != null){
            System.out.println("I am here::::::::::::::::::");
            if((bunDoctor.getString("fragment") != null)) {
                if(bunDoctor.getString("fragment").equalsIgnoreCase("clinicAppointmentListAdapter")){
                    Fragment fragment = new PatientAppointmentStatus();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }else {
                    Fragment fragment = new PatientAllDoctors();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            }else if(bunDoctor.getString("appointmnetPatient") != null){
                Bundle bun = new Bundle();
                bun.putString("fragment", "PatientAppointmentSummary");
                Fragment fragment = new PatientAllAppointment();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);
            }else if(bunDoctor.getString("patientAllAppointent") != null){
                Bundle bun = new Bundle();
                bun.putString("fragment", "PatientAppointmentSummary");
                Fragment fragment = new PatientAllAppointment();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);
            }
        }else if(bunDoctor.getString("patientAllAppointment") != null){
            Bundle bun = new Bundle();
            bun.putString("fragment", "PatientAppointmentSummary");
            Fragment fragment = new PatientAllAppointment();
            fragment.setArguments(bun);
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }else if(bunDoctor.getString("fragment") != null){
            if(bunDoctor.getString("fragment").equalsIgnoreCase("clinicListAdapter")){
                Fragment fragment = new PatientAllClinics();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }else {
                Fragment fragment = new PatientAllDoctors();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        }
        else if (type.equalsIgnoreCase("Patient")) {
            System.out.println("I am PatientAppointment Summary::::::::::::::::::");
            Fragment fragment = new AppointmentsPatient();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
        }else if(bunDoctor.getString("appointmentPatient") != null){
            Bundle bun = new Bundle();
            bun.putString("fragment", "PatientAppointmentSummary");
            Fragment fragment = new PatientAllAppointment();
            fragment.setArguments(bun);
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
        else {
            Bundle bun = new Bundle();
            bun.putString("fragment", "PatientAppointmentSummary");
            Fragment fragment = new PatientAllAppointment();
            fragment.setArguments(bun);
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
    }


    public void getAllPatientSummary() {
        System.out.println("In Patient Login ");
        api.getPatientReminderData(doctorId, patientId, appointmentDate, appointmentTime, new Callback<ReminderVM>() {
            //api.getPatientReminderData(1, "aa@gmail.com", "22-05-2015", "09:00AM" , new Callback<ReminderVM>() {
            @Override
            public void success(ReminderVM reminderVM, Response response) {
                System.out.println("reminderVM " + reminderVM);
                medicineSet = new HashSet<String>();
                if (reminderVM.id != null) {
                    global.setReminderVM(reminderVM);
                    for (int i = 0; i < typeList.length; i++) {
                        if (reminderVM.visitType.equals(typeList[i])) {
                            visit.setSelection(i);
                        }
                    }
                    referedBy.setText(reminderVM.referredBy);
                    symptomsValue.setText(reminderVM.symptoms);
                    diagnosisValue.setText(reminderVM.diagnosis);
                    medicineValue.setText(reminderVM.medicinName);
                    testPrescribedValue.setText(reminderVM.testsPrescribed);
                    alarms = reminderVM.getAlarmReminderVMList();
                    System.out.println("Alarms size::::::"+alarms.size());
                    for(AlarmReminderVM vm : alarms){
                        medicineSet.add(vm.medicineName);
                    }
                    if(medicineSet.size() != 0){
                        medicineVMs = new ArrayList<MedicineVM>();
                        for(String medicineName : medicineSet){
                            MedicineVM vm = new MedicineVM();
                            vm.medicineName = medicineName;
                            List<AlarmReminderVM> alarmList = new ArrayList<AlarmReminderVM>();
                            for(AlarmReminderVM alarmVm : alarms){
                                if(alarmVm.medicineName.equals(medicineName)){
                                    alarmList.add(alarmVm);
                                }
                            }
                            vm.alarms = alarmList;
                            medicineVMs.add(vm);
                        }
                    }else{
                        medicineVMs = new ArrayList<MedicineVM>();
                        MedicineVM vm = new MedicineVM();
                        vm.medicineName = "No Medicine";
                        vm.alarms = null;
                        medicineVMs.add(vm);
                    }
                   // adapter = new MedicineAdapter(getActivity(),medicineVMs,reminderVM);
                    alarmListView.setAdapter(adapter);
                } else {
                    global.setReminderVM(null);
                    medicineVMs = new ArrayList<MedicineVM>();
                    MedicineVM vm = new MedicineVM();
                    vm.medicineName = "No Medicine";
                    vm.alarms = null;
                    medicineVMs.add(vm);
                   // adapter = new MedicineAdapter(getActivity(),medicineVMs,reminderVM);
                    alarmListView.setAdapter(adapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void savePatientReminderData(ReminderVM reminderVM) {
        api.savePatientReminderDetails(reminderVM, new Callback<ReminderVM>() {
            @Override
            public void success(ReminderVM reminderVM, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                if (reminderVM.id != null) {

                    global.setReminderVM(reminderVM);
                    for (int i = 0; i < typeList.length; i++) {
                        if (reminderVM.visitType.equals(typeList[i])) {
                            visit.setSelection(i);
                        }
                    }

                    System.out.println("reminderVM id = " + reminderVM.id);
                    referedBy.setText(reminderVM.referredBy);
                    symptomsValue.setText(reminderVM.symptoms);
                    diagnosisValue.setText(reminderVM.diagnosis);
                    medicineValue.setText(reminderVM.medicinName);
                    testPrescribedValue.setText(reminderVM.testsPrescribed);
                } else {
                    global.setReminderVM(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


    static class SortListComparator implements Comparator<SummaryHistoryVM> {
        public int compare(SummaryHistoryVM c1, SummaryHistoryVM c2) {
            //"curDate": "Tue Sep 29 16:43:39 IST 2015",
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date1 = null;
            Date date2 = null;

            try {
                date1 = format.parse(c1.curDate);
                date2 = format.parse(c2.curDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date1.before(date2)) {
                return 1;
            } else if (date1.after(date2)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}


