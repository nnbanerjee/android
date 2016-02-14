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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Adapter.MedicineAdapter;
import Application.MyApi;
import Model.AlarmReminderVM;
import Model.Clinic;
import Model.Histry;
import Model.MedicineVM;
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
public class DoctorAppointmentSummary extends Fragment {

    MyApi api;
    public SharedPreferences session;
    String doctor_email,appointmentDate,appointmentTime,patientId,clinicId;
    Integer doctorId;
    TextView visitedDate,referedBy;
    Spinner visit;
    Button logout,drawar,reminderBtn,saveSummary,
            symptomsHistryBtn,diagnosisHistryBtn,
            prescribHistryBtn,testHistryBtn,addAlarm;
    List<AlarmReminderVM> alarms;
    ListView alarmListView;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue,medicineValue,testPrescribedValue;
    String[] symptoms_item,medicin_list = null;
    String[] typeList;
    Global global;
    ProgressDialog progress;
    TextView clinicName;
    MedicineAdapter adapter;
    Set<String> medicineSet;
    List<MedicineVM> medicineVMs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container,false);
        alarmListView = (ListView)view.findViewById(R.id.alarm_list);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctor_email = session.getString("patient_doctor_email", null);
        appointmentDate  = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        clinicId = session.getString("clinicId",null);
        System.out.println("Clinic Id:::::::"+clinicId);
        doctorId = Integer.parseInt(session.getString("doctorId","0"));
        patientId = session.getString("doctor_patientEmail", null);
        addAlarm = (Button)view.findViewById(R.id.add_alarm);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        clinicName = (TextView)view.findViewById(R.id.clinicName);
        api.getAllClinics(new Callback<List<Clinic>>() {
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        //Retrofit Initialization][;
        visit = (Spinner) view.findViewById(R.id.visit);
        visitedDate = (TextView) view.findViewById(R.id.visitedDate);
        referedBy = (TextView) view.findViewById(R.id.referedBy);
        //documentationBtn = (Button) view.findViewById(R.id.documentationBtn);

        symptomsHistryBtn = (Button) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (Button) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (Button) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (Button) view.findViewById(R.id.testHistryBtn);

        reminderBtn = (Button) view.findViewById(R.id.reminderBtn);
        saveSummary = (Button) view.findViewById(R.id.saveSummary);
        symptoms_item = getResources().getStringArray(R.array.symptoms);
        medicin_list = getResources().getStringArray(R.array.medicin_list);
        typeList =  getResources().getStringArray(R.array.visit_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType,  typeList);
        visit.setAdapter(adapter);
        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        medicineValue = (MultiAutoCompleteTextView)view.findViewById(R.id.medicineValue);
        testPrescribedValue = (MultiAutoCompleteTextView)view.findViewById(R.id.testPrescribedValue);
        symptomsValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, symptoms_item));
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        symptomsValue.setThreshold(1);
        diagnosisValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, symptoms_item));
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicineValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, medicin_list));
        medicineValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        testPrescribedValue.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, symptoms_item));
        testPrescribedValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        visitedDate.setText(appointmentDate);
        symptomsHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                getHistryData("systoms");

            }
        });
        diagnosisHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                getHistryData("diagnosis");

            }
        });

        prescribHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                getHistryData("prescribHistry");

            }
        });
        testHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                getHistryData("testHistry");

            }
        });
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicinName = medicineValue.getText().toString();
                if(medicinName == null || medicinName.equals("")){
                    Toast.makeText(getActivity(), "Please enter medicin name", Toast.LENGTH_SHORT).show();
                }else{

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
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
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
        saveSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderVM reminderVM = new ReminderVM();
                if(global.getReminderVM() != null){
                    reminderVM.id = global.getReminderVM().id;
                    reminderVM.startDate = global.getReminderVM().getStartDate();
                    reminderVM.endDate = global.getReminderVM().getEndDate();
                    reminderVM.duration = global.getReminderVM().getDuration();
                    reminderVM.doctorInstruction = global.getReminderVM().getDoctorInstruction();
                    reminderVM.numberOfDoses = global.getReminderVM().getNumberOfDoses();
                    reminderVM.schedule = global.getReminderVM().getSchedule();
                }else{
                    reminderVM.id = null;
                }
                reminderVM.doctorId = doctorId;
                reminderVM.patientId = patientId;
                reminderVM.ownerType = "Doctor";
                reminderVM.appointmentDate = appointmentDate;
                reminderVM.appointmentTime = appointmentTime;
                reminderVM.medicinName = medicineValue.getText().toString();
                reminderVM.visitDate = visitedDate.getText().toString();
                reminderVM.visitType = visit.getSelectedItem().toString();
                reminderVM.referredBy = referedBy.getText().toString();
                reminderVM.symptoms = symptomsValue.getText().toString();
                reminderVM.diagnosis = diagnosisValue.getText().toString();
                reminderVM.testsPrescribed = testPrescribedValue.getText().toString();
                global.setTestPrescribed(reminderVM.testsPrescribed);
                reminderVM.alarmReminderVMList = alarms;
                savePatientReminderData(reminderVM);
             }
        });
        alarmListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        getAllPatientSummary();
        //getAllMedicine();
        return view;
    }
    public void getAllMedicine()
    {
        List<MedicineVM> medicines = new ArrayList<MedicineVM>();
        MedicineVM med1 = new MedicineVM();
        med1.medicineName = "Crocin1";
        med1.alarms = null;
        medicines.add(med1);
        MedicineVM med2 = new MedicineVM();
        med2.medicineName = "Crocin2";
        med2.alarms = null;
        medicines.add(med2);
        MedicineVM med3 = new MedicineVM();
        med3.medicineName = "Crocin3";
        med3.alarms = null;
        medicines.add(med3);
        MedicineVM med4 = new MedicineVM();
        med4.medicineName = "Crocin4";
        med4.alarms = null;
        medicines.add(med4);
      /*  adapter = new MedicineAdapter(getActivity(),medicines);
        alarmListView.setAdapter(adapter);*/
    }
    public void getHistryData(final String histryString){
        api.getHistry(doctor_email,patientId,appointmentDate,appointmentTime,new Callback<List<SummaryHistoryVM>>() {
            @Override
            public void success(List<SummaryHistoryVM> summaryHistoryVMs, Response response) {
                System.out.println("historyVMList.size()  = "+summaryHistoryVMs.size());
                for(SummaryHistoryVM summaryHistoryVM : summaryHistoryVMs){
                    System.out.println("summaryHistoryVM.curDate before = "+summaryHistoryVM.curDate);
                }
                //Collections.sort(summaryHistoryVMs, new SortListComparator());
                for(SummaryHistoryVM summaryHistoryVM : summaryHistoryVMs){
                    System.out.println("summaryHistoryVM.curDate after = "+summaryHistoryVM.curDate);
                }
                if(histryString.equalsIgnoreCase("systoms"))
                {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Symptoms Histry";
                    show.show(getFragmentManager(),"Dialog");
                }
                else if(histryString.equalsIgnoreCase("diagnosis"))
                {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Diagnosis Histry";
                    show.show(getFragmentManager(),"Dialog");
                }
                else if(histryString.equalsIgnoreCase("prescribHistry"))
                {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Prescribed Histry";
                    show.show(getFragmentManager(),"Dialog");
                }
                else if(histryString.equalsIgnoreCase("testHistry"))
                {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Test Histry";
                    show.show(getFragmentManager(),"Dialog");
                }
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void  goToBack(){
        String fragmentCall = "";
        Fragment fragment;
        Bundle bun = getArguments();
        if(bun != null){
            if(bun.get("fragment") != null){
               fragmentCall = bun.getString("fragment");
            }
        }
        if(fragmentCall.equalsIgnoreCase("DoctorPatientAdapter")){
            fragment = new AllDoctorsPatient();

        }else{
            fragment = new AllDoctorPatientAppointment();
        }
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();

    }

    public void getAllPatientSummary(){
        System.out.println("In doctor Login ");
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getPatientReminderData(doctorId, patientId, appointmentDate, appointmentTime , new Callback<ReminderVM>() {
        //api.getPatientReminderData(1, "aa@gmail.com", "22-05-2015", "09:00AM" , new Callback<ReminderVM>() {
            @Override
            public void success(ReminderVM reminderVM, Response response) {
                medicineSet = new HashSet<String>();
                if(reminderVM.id != null ){
                    global.setReminderVM(reminderVM);
                    for(int i = 0; i < typeList.length; i++){
                        if(reminderVM.visitType.equals(typeList[i])){
                            visit.setSelection(i);
                        }
                    }
                    System.out.println("reminderVM id = "+reminderVM.id);
                    System.out.println("in getAllPatientSummary = ");
                    referedBy.setText(reminderVM.referredBy);
                    symptomsValue.setText(reminderVM.symptoms);
                    diagnosisValue.setText(reminderVM.diagnosis);
                    medicineValue.setText(reminderVM.medicinName);
                    testPrescribedValue.setText(reminderVM.testsPrescribed);
                    global.setTestPrescribed(reminderVM.testsPrescribed);
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

                    adapter = new MedicineAdapter(getActivity(),medicineVMs,reminderVM);
                    alarmListView.setAdapter(adapter);
                    global.setReminderVM(reminderVM);
                    progress.dismiss();

                }else{
                    global.setReminderVM(null);
                    medicineVMs = new ArrayList<MedicineVM>();
                    MedicineVM vm = new MedicineVM();
                    vm.medicineName = "No Medicine";
                    vm.alarms = null;
                    medicineVMs.add(vm);
                    adapter = new MedicineAdapter(getActivity(),medicineVMs,reminderVM);
                    alarmListView.setAdapter(adapter);
                    progress.dismiss();
                }
            }
            @Override
            public void failure(RetrofitError error) {
               // Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });

    }
    static class SortListComparator implements Comparator<SummaryHistoryVM>
    {
        public int compare(SummaryHistoryVM c1, SummaryHistoryVM c2)
        {
            //"curDate": "Tue Sep 29 16:43:39 IST 2015",
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date1 =  null;
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
    public void savePatientReminderData(ReminderVM reminderVM)
    {
        api.savePatientReminderDetails(reminderVM, new Callback<ReminderVM>() {
            @Override
            public void success(ReminderVM reminderVM, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                if(reminderVM.id != null ){
                    System.out.println("save reminderVM = "+reminderVM.id);
                    global.setReminderVM(reminderVM);
                    for(int i = 0; i < typeList.length; i++){
                        if(reminderVM.visitType.equals(typeList[i])){
                            visit.setSelection(i);
                        }
                    }
                    System.out.println("reminderVM id = "+reminderVM.id);
                    referedBy.setText(reminderVM.referredBy);
                    symptomsValue.setText(reminderVM.symptoms);
                    diagnosisValue.setText(reminderVM.diagnosis);
                    medicineValue.setText(reminderVM.medicinName);
                    testPrescribedValue.setText(reminderVM.testsPrescribed);

                }else{
                    System.out.println("save reminderVM null ");
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

}
