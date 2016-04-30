package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Adapter.MedicineAdapter;
import Adapter.TestsAdapter;
import Application.MyApi;
import Model.AddDiagnosisTestRequest;
import Model.AlarmReminderVM;
import Model.Clinic;
import Model.CreateSummary;
import Model.MedicineId;
import Model.MedicinePrescribed;
import Model.MedicineVM;
import Model.PersonID;
import Model.ReminderVM;
import Model.ResponseCodeVerfication;
import Model.SummaryHistoryVM;
import Model.SummaryRequest;
import Model.SummaryResponse;
import Model.TestPrescribed;
import Model.VisitEditLogRequest;
import Model.VisitEditLogResponse;
import Utils.UtilSingleInstance;
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
    String doctor_email,appointmentDate,appointmentTime, clinicId;
    String doctorId;
    TextView visitedDate,referedBy;
    Spinner visit;
    Button logout,drawar,reminderBtn,saveSummary;


    ImageView  prescribHistryBtn,testHistryBtn, addMedicineAndAlarm,addtestsBtn,diagnosisHistryBtn,symptomsHistryBtn;
    List<AlarmReminderVM> alarms;
    ListView alarmListView;
    ListView testsListView;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue,medicineValue,testPrescribedValue,clinicValue;
    String[] symptoms_item,medicin_list = null;
    String[] typeList;
    Global global;
    ProgressDialog progress;
    TextView clinicName;
    MedicineAdapter adapter;
    TestsAdapter testAdapter;
    Set<String> medicineSet;
    List<MedicinePrescribed> medicineVMs;
    List<TestPrescribed> testPrescribedList;
    private String appointMentId,patientId;
    Calendar calendar = Calendar.getInstance();
    public SummaryResponse summaryResponse;
    RestAdapter restAdapter;
    Button timeBtn;
    private Spinner clinicSpinner;
    private String newOrOldSummary;
    List<Clinic> clinicsListwithIDs;
    private Toolbar toolbar;
    TextView show_global_tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container,false);
        alarmListView = (ListView)view.findViewById(R.id.alarm_list);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        testsListView = (ListView)view.findViewById(R.id.test_prescribed_list);


        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        show_global_tv.setText("<  1 / 5  >");
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctor_email = session.getString("patient_doctor_email", null);
        appointmentDate  = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        clinicId = session.getString("clinicId", null);
        System.out.println("Clinic Id:::::::"+clinicId);
        doctorId =  session.getString("id", "0") ;
        newOrOldSummary=session.getString("Summary", "");

        appointMentId= session.getString("appointmentId", "");
        patientId=session.getString( "patientId","");
        System.out.println("appointmentId Id:::::::"+appointMentId);
        System.out.println("patientId Id:::::::"+patientId);

        addMedicineAndAlarm = (ImageView)view.findViewById(R.id.add_alarm);
        addtestsBtn = (ImageView)view.findViewById(R.id.addtestsBtn);
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.replacementFragment);
                if (f instanceof DoctorAppointmentSummary){
                    saveData();
                }

                return true;
            }
        });

        clinicName = (TextView)view.findViewById(R.id.clinicName);

        SummaryRequest summary = new SummaryRequest( patientId,appointMentId);
        api.getPatientVisitSummary(summary, new Callback<SummaryResponse>() {
            @Override
            public void success(SummaryResponse clinics, Response response) {
                summaryResponse=clinics;
                progress.dismiss();
                getAllPatientSummary();
               // summaryResponse.getMedicinePrescribed().get(0).getMedicineId()

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

        symptomsHistryBtn = (ImageView) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (ImageView) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (ImageView) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (ImageView) view.findViewById(R.id.testHistryBtn);

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
        clinicSpinner = (Spinner)view.findViewById(R.id.clinic_spinner);
        clinicValue= (MultiAutoCompleteTextView)view.findViewById(R.id.clinicValue);
       // testPrescribedValue = (MultiAutoCompleteTextView)view.findViewById(R.id.testPrescribedValue);
        symptomsValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        symptomsValue.setThreshold(1);
        diagnosisValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicineValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, medicin_list));
        medicineValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
       /* testPrescribedValue.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, symptoms_item));
        testPrescribedValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());*/


        if(newOrOldSummary.equalsIgnoreCase("newsummary")){
            clinicSpinner.setVisibility(View.VISIBLE);
            clinicValue.setVisibility(View.GONE);

            api.getAllClinics(new PersonID(doctorId), new Callback<List<Clinic>>() {
                @Override
                public void success(List<Clinic> clinicsList, Response response) {
                    clinicsListwithIDs=clinicsList;
                    String[] clinics = new String[clinicsList.size()];
                    int count = 0;
                    for (Clinic vm : clinicsList) {
                        clinics[count] = vm.getClinicName();
                        count = count + 1;
                    }
                    clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinics));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            });
        }else{
            clinicSpinner.setVisibility(View.GONE);
            clinicValue.setVisibility(View.VISIBLE);
        }

        timeBtn = (Button) view.findViewById(R.id.timeBtn);

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }
        });
        if(summaryResponse!=null && summaryResponse.getVisitDate()!=null)
            visitedDate.setText(summaryResponse.getVisitDate());
        symptomsHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData(1);

            }
        });
        diagnosisHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData(2);

            }
        });

        prescribHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData(3);

            }
        });
        testHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                getHistryData(4);

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
                    //args.putString("testPrescribedValue", testPrescribedValue.getText().toString());
                    args.putString("medicinName", medicineValue.getText().toString());

                    Fragment fragment = new PatientMedicinReminder();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });
        addtestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("visitedDate", visitedDate.getText().toString());
                args.putString("visit", visit.getSelectedItem().toString());
                args.putString("referedBy", referedBy.getText().toString());
                args.putString("symptomsValue", symptomsValue.getText().toString());
                args.putString("diagnosisValue", diagnosisValue.getText().toString());
                //args.putString("testPrescribedValue", testPrescribedValue.getText().toString());
                args.putString("medicinName", medicineValue.getText().toString());
                args.putString("clinicId", summaryResponse.getClinicId().toString());
                args.putString("argument", "NewTest");
                Fragment fragment = new AddDiagnosticTest();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        addMedicineAndAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("visitedDate", visitedDate.getText().toString());
                args.putString("visit", visit.getSelectedItem().toString());
                args.putString("referedBy", referedBy.getText().toString());
                args.putString("symptomsValue", symptomsValue.getText().toString());
                args.putString("diagnosisValue", diagnosisValue.getText().toString());
                //args.putString("testPrescribedValue", testPrescribedValue.getText().toString());
                args.putString("medicinName", medicineValue.getText().toString());
                args.putString("argument", "NewMedicine");
                Fragment fragment = new PatientMedicinReminder();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        saveSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           saveData();
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

        testsListView.setOnTouchListener(new View.OnTouchListener() {
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
        //getAllPatientSummary();
        //getAllMedicine();
        return view;
    }

    public void saveData(){
        //ReminderVM reminderVM = new ReminderVM();
        CreateSummary createSummary= new CreateSummary();


        if(appointMentId!=null && !appointMentId.equalsIgnoreCase("") ){
            createSummary.setAppointmentId(appointMentId);
            createSummary.setVisitType("" + visit.getSelectedItemPosition());
            createSummary.setReferred_by(referedBy.getText().toString());
            createSummary.setDiagnosis(diagnosisValue.getText().toString());
            createSummary.setSymptoms(symptomsValue.getText().toString());
            createSummary.setUserType("1");//for Doctor
            createSummary.setLoggedinUserId(doctorId);


        }else{
            createSummary.setDoctorId(doctorId);
            createSummary.setPatientId(patientId);

            createSummary.setType("0");
            createSummary.setSequenceNumber("");
            createSummary.setAppointmentStatus("");
            createSummary.setVisitType("" + visit.getSelectedItemPosition());
            createSummary.setVisitStatus("1");
            createSummary.setRating("1");
            createSummary.setDoctorClinicId("");
            createSummary.setReferred_by(referedBy.getText().toString());
            createSummary.setReviews("Good");
            createSummary.setDiagnosis(diagnosisValue.getText().toString());
            createSummary.setSymptoms(symptomsValue.getText().toString());
            createSummary.setUserType("1");
            createSummary.setLoggedinUserId(doctorId);
            createSummary.setAppointmentDate(""+calendar.getTimeInMillis());
            if(newOrOldSummary.equalsIgnoreCase("newsummary")){

                createSummary.setClinicId(clinicsListwithIDs.get(clinicSpinner.getSelectedItemPosition()).getIdClinic());
            }

        }



        savePatientReminderData(createSummary);
    }
    public void setDate(){
        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){




        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate();
            //progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
           // getAllClinicsAppointmentDataFromCalendarButton();

        }

    };
    public void updatedate(){
        visitedDate.setText(calendar.get(Calendar.YEAR)+"-"+ UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        /*if(appointmentDate != null){

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date date = formatter.parse(appointmentDate);
                System.out.println(date);
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println(formatter2.format(date));
                dateValue.setText(formatter2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }*/
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
    public void getHistryData(final int histryString){

        VisitEditLogRequest req= new VisitEditLogRequest(appointMentId,histryString);
        api.getPatientVisitEditLog(req, new Callback<VisitEditLogResponse>() {
            @Override
            public void success(VisitEditLogResponse summaryHistoryVMs, Response response) {

                if (histryString == 1) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Symptoms History";
                    show.show(getFragmentManager(), "Dialog");
                    show.State=histryString;
                }
                if (histryString == 2) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Diagnosis History";
                    show.show(getFragmentManager(), "Dialog");
                    show.State=histryString;
                }
                if (histryString == 3) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Prescribed History";
                    show.show(getFragmentManager(), "Dialog");
                    show.State=histryString;
                }
                if (histryString == 4) {
                    ShowHistryDialog show = ShowHistryDialog.newInstance();
                    show.summaryHistoryVMs = summaryHistoryVMs;
                    show.heading = "Test History";
                    show.show(getFragmentManager(), "Dialog");
                    show.State=histryString;
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
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
        if(fragmentCall.equalsIgnoreCase("DoctorPatientAdapter")||fragmentCall.equalsIgnoreCase("doctorPatientListAdapter") ){
            fragment = new AllDoctorsPatient();

        }else{
            fragment = new AllDoctorPatientAppointment();
        }
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();

    }

    public void getAllPatientSummary(){
        System.out.println("In doctor Login ");
        referedBy.setText(summaryResponse.getReferredBy());

        symptomsValue.setText(summaryResponse.getSymptoms());
        diagnosisValue.setText(summaryResponse.getDiagnosis());
       // medicineValue.setText(summaryResponse.getMedicinePrescribed());
        //testPrescribedValue.setText(summaryResponse.getTestPrescribed());
        //clinicName.setText(summaryResponse.getClinicName());
        clinicValue.setText(summaryResponse.getClinicName());
        if(summaryResponse!=null && summaryResponse.getMedicinePrescribed()!=null && summaryResponse.getMedicinePrescribed().size() != 0){
            medicineVMs = new ArrayList<MedicinePrescribed>();
            medicineVMs=summaryResponse.getMedicinePrescribed();

        }else{
            medicineVMs = new ArrayList<MedicinePrescribed>();
            MedicinePrescribed vm = new MedicinePrescribed();
            vm.setMedicineName(  "No Medicine");
            medicineVMs.add(vm);
        }
        if(summaryResponse!=null && summaryResponse.getTestPrescribed()!=null && summaryResponse.getTestPrescribed().size() != 0){
            testPrescribedList = new ArrayList<TestPrescribed>();
            testPrescribedList=summaryResponse.getTestPrescribed();

        }else{
            testPrescribedList = new ArrayList<TestPrescribed>();
            TestPrescribed vm = new TestPrescribed();
            vm.setTestName("No Tests");
            testPrescribedList.add(vm);
        }

        testAdapter= new TestsAdapter(getActivity(),testPrescribedList,null);
        testsListView.setAdapter(testAdapter);
        testsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddDiagnosisTestRequest addPatientMedicineSummary = new AddDiagnosisTestRequest();
                List<AlarmReminderVM> alarms = new ArrayList<AlarmReminderVM>();
                int doctorId = Integer.parseInt(session.getString("id", "0"));
                patientId = session.getString("patientId", "");


                if (!testPrescribedList.get(i).getTestName().equalsIgnoreCase("No Tests")) {
                    Bundle args = new Bundle();
                    args.putString("state", "EDIT");
                    args.putString("argument", "EDIT");

                    args.putString("referedBy", referedBy.getText().toString());
                    args.putString("testId", testPrescribedList.get(i).getTestId());

                    args.putString("visitedDate", visitedDate.getText().toString());
                    args.putString("visit", visit.getSelectedItem().toString());
                    args.putString("referedBy", referedBy.getText().toString());
                    args.putString("symptomsValue", symptomsValue.getText().toString());
                    args.putString("diagnosisValue", diagnosisValue.getText().toString());
                    args.putString("medicinName", medicineValue.getText().toString());
                    args.putString("clinicId", summaryResponse.getClinicId().toString());

                    Fragment fragment = new AddDiagnosticTest();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }


            }
        });
        adapter = new MedicineAdapter(getActivity(),medicineVMs,null);
        alarmListView.setAdapter(adapter);
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MedicinePrescribed medicineVm = medicineVMs.get(position);
                if (!medicineVm.getMedicineName().equals("No Medicine")) {
                    Bundle args = new Bundle();
                    args.putString("state", "EDIT");

                    args.putString("medicinName", medicineVm.getMedicineName());
                    args.putString("medicineEndDate", medicineVm.getEndDateTime());
                    args.putString("medicineId", medicineVm.getMedicineId());
                    args.putString("medicineStartDate", medicineVm.getStartDateTime());
                    args.putString("medicineReminder", medicineVm.getReminder());


                    Fragment fragment = new PatientMedicinReminder();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });
       // global.setReminderVM(reminderVM);
        //progress.dismiss();


      /*
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
        });*/

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
    public void savePatientReminderData(CreateSummary reminderVM)
    {

        if(summaryResponse!=null && summaryResponse.getAppointmentId()!=null){
            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
            api.updatePatientVisitSummary(reminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });


        }else{
            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
            api.createPatientVisitSummary(reminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });

        }

    }
    public class ClinicSpinner extends ArrayAdapter<String> {
        String[] strClinic;
        public ClinicSpinner(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
            strClinic=objects;
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
            main_text.setText(strClinic[position]);
            return mySpinner;
        }


    }
}
