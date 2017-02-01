package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.medico.model.AppointmentId1;
import com.medico.model.ProfileId;
import com.medico.view.ParentFragment;
import com.medico.view.PatientProfileListView;
import com.medico.view.PatientVisitDatesView;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.MedicineAdapter;
import Adapter.TestsAdapter;
import Model.AddDiagnosisTestRequest;
import Model.AlarmReminderVM;
import Model.Clinic;
import Model.ResponseCodeVerfication;
import Model.SummaryResponse;
import Model.SummaryResponse.MedicinePrescribed;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentSummary extends ParentFragment {

    TextView visitedDate,referedBy;
    Spinner visit;
    Button logout,drawar,reminderBtn,saveSummary;
    ImageView  prescribHistryBtn,testHistryBtn, addMedicineAndAlarm,addtestsBtn,diagnosisHistryBtn,symptomsHistryBtn;
    ListView alarmListView;
    ListView testsListView;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue,medicineValue,testPrescribedValue,clinicValue;
    ProgressDialog progress;
    TextView clinicName;
    MedicineAdapter adapter;
    TestsAdapter testAdapter;
    public SummaryResponse summaryResponse;
    Button timeBtn;
    private Spinner clinicSpinner;
//    private Toolbar toolbar;
    SlideDateTimePicker pickerDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container,false);
        alarmListView = (ListView)view.findViewById(R.id.alarm_list);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        testsListView = (ListView)view.findViewById(R.id.test_prescribed_list);
        addMedicineAndAlarm = (ImageView)view.findViewById(R.id.add_alarm);
        addtestsBtn = (ImageView)view.findViewById(R.id.addtestsBtn);
//        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
//        toolbar.setVisibility(View.VISIBLE);
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.save);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.replacementFragment);
//                if (f instanceof DoctorAppointmentSummary){
//                    saveData();
//                }
//                return true;
//            }
//        });

        clinicName = (TextView)view.findViewById(R.id.clinicName);
        visit = (Spinner) view.findViewById(R.id.visit);
        visitedDate = (TextView) view.findViewById(R.id.visitedDate);
        referedBy = (TextView) view.findViewById(R.id.referedBy);
        symptomsHistryBtn = (ImageView) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (ImageView) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (ImageView) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (ImageView) view.findViewById(R.id.testHistryBtn);
        reminderBtn = (Button) view.findViewById(R.id.reminderBtn);
        saveSummary = (Button) view.findViewById(R.id.saveSummary);
        String[] typeList =  getResources().getStringArray(R.array.visit_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType,  typeList);
        visit.setAdapter(adapter);
        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        medicineValue = (MultiAutoCompleteTextView)view.findViewById(R.id.medicineValue);
        clinicSpinner = (Spinner)view.findViewById(R.id.clinic_spinner);
        clinicValue= (MultiAutoCompleteTextView)view.findViewById(R.id.clinicValue);
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        symptomsValue.setThreshold(1);
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicineValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        timeBtn = (Button) view.findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }
        });
        if(summaryResponse!=null && summaryResponse.visitDate !=null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(summaryResponse.visitDate);
            visitedDate.setText(DateFormat.getInstance().format(calendar.getTime()));
        }
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
            public void onClick(View v)
            {
                MedicinePrescribed prescribed = null;
                if(prescribed != null )
                {
                    Bundle args = getActivity().getIntent().getExtras();
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
                Bundle args = getActivity().getIntent().getExtras();
                args.putString("visitedDate", visitedDate.getText().toString());
                args.putString("referedBy", referedBy.getText().toString());
                args.putInt("clinicId", summaryResponse.clinicId);
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
                args.putString("referedBy", referedBy.getText().toString());
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

//        final Button back = (Button)getActivity().findViewById(R.id.back_button);
//        back.setVisibility(View.VISIBLE);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBack();
//            }
//        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        int doctorId = bundle.getInt(DOCTOR_ID);
        int patientId = bundle.getInt(PATIENT_ID);
        int appointMentId = bundle.getInt(APPOINTMENT_ID);
        if(appointMentId > 0) {
            api.getPatientVisitSummary(new AppointmentId1(appointMentId), new Callback<SummaryResponse>() {
                @Override
                public void success(SummaryResponse summary, Response response) {
                    summaryResponse = summary;
                    progress.dismiss();
                    setPatientSummary();
                    setClinic(false);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    setClinic(true);
                }
            });
        }
        else
        {
            setClinic(true);
        }
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

    public void saveData()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        if(summaryResponse == null)
        {
            Bundle bundle1 = getActivity().getIntent().getExtras();
            Calendar calender = Calendar.getInstance();
            DateFormat.getInstance().parse(visitedDate.getText().toString());
            SummaryResponse createSummary = new SummaryResponse();
            createSummary.visitType = new Integer(visit.getSelectedItemPosition()).byteValue();
            createSummary.referredBy = (referedBy.getText().toString());
            createSummary.diagnosis = diagnosisValue.getText().toString();
            createSummary.symptoms = symptomsValue.getText().toString();
            createSummary.clinicId = new Integer(((Clinic)clinicSpinner.getAdapter().getItem(clinicSpinner.getSelectedItemPosition())).getIdClinic());
            createSummary.loggedinUserId = bundle.getInt(LOGGED_IN_ID);
            createSummary.doctorId = bundle1.getInt(DOCTOR_ID);
            createSummary.doctorId = bundle1.getInt(PATIENT_ID);
            createSummary.visitDate = calender.getTimeInMillis();
            createSummary.visitType = new Integer(visit.getSelectedItemPosition()).byteValue();
            createSummary.referredBy = referedBy.getText().toString();
            createSummary.treatmentPlanEnabled = 0;
            if(bundle.getInt(APPOINTMENT_ID) > 0)
                createSummary.appointmentId = bundle.getInt(APPOINTMENT_ID);
            createSummary(createSummary);
        }
        else
        {
            summaryResponse.diagnosis = diagnosisValue.getText().toString();
            summaryResponse.symptoms = symptomsValue.getText().toString();
            updateSummary(summaryResponse);
         }

    }
    public void setDate() {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                visitedDate.setText(format.format(date));
            }

        };
        pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .build();
        pickerDialog.show();
    }
//    public void setDate() {
//        final Calendar calendar = Calendar.getInstance();
//
//        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, monthOfYear);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                visitedDate.setText(calendar.get(Calendar.YEAR)+"-"+ UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
//            }
//
//        };
//        pickerDialog = new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        pickerDialog.show();
//    }

    public void getHistryData(final int histryString)
    {

//        VisitEditLogRequest req= new VisitEditLogRequest(summaryResponse.getAppointmentId(),histryString);
//        api.getPatientVisitEditLog(req, new Callback<VisitEditLogResponse>() {
//            @Override
//            public void success(VisitEditLogResponse summaryHistoryVMs, Response response) {
//
//                if (histryString == 1) {
//                    ShowHistryDialog show = ShowHistryDialog.newInstance();
//                    show.summaryHistoryVMs = summaryHistoryVMs;
//                    show.heading = "Symptoms History";
//                    show.show(getFragmentManager(), "Dialog");
//                    show.State=histryString;
//                }
//                if (histryString == 2) {
//                    ShowHistryDialog show = ShowHistryDialog.newInstance();
//                    show.summaryHistoryVMs = summaryHistoryVMs;
//                    show.heading = "Diagnosis History";
//                    show.show(getFragmentManager(), "Dialog");
//                    show.State=histryString;
//                }
//                if (histryString == 3) {
//                    ShowHistryDialog show = ShowHistryDialog.newInstance();
//                    show.summaryHistoryVMs = summaryHistoryVMs;
//                    show.heading = "Prescribed History";
//                    show.show(getFragmentManager(), "Dialog");
//                    show.State=histryString;
//                }
//                if (histryString == 4) {
//                    ShowHistryDialog show = ShowHistryDialog.newInstance();
//                    show.summaryHistoryVMs = summaryHistoryVMs;
//                    show.heading = "Test History";
//                    show.show(getFragmentManager(), "Dialog");
//                    show.State=histryString;
//                }
//                progress.dismiss();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//            }
//        });
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
            fragment = new PatientProfileListView();

        }else{
            fragment = new PatientVisitDatesView();
        }
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();

    }

    public void setPatientSummary()
    {
        System.out.println("In doctor Login ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(summaryResponse.getVisitDate());
        visitedDate.setText(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date(summaryResponse.getVisitDate())));
        referedBy.setText(summaryResponse.getReferredBy());
        clinicValue.setText(summaryResponse.clinicName);
        symptomsValue.setText(summaryResponse.getSymptoms());
        diagnosisValue.setText(summaryResponse.getDiagnosis());
        if(summaryResponse.getTestPrescribed() !=null) {
            testAdapter = new TestsAdapter(getActivity(), summaryResponse.getTestPrescribed(), getActivity().getIntent().getExtras().getInt(LOGGED_IN_ID));
            testsListView.setAdapter(testAdapter);
            testsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AddDiagnosisTestRequest addPatientMedicineSummary = new AddDiagnosisTestRequest();
                    List<AlarmReminderVM> alarms = new ArrayList<AlarmReminderVM>();
                    Bundle bun = getActivity().getIntent().getExtras();
                    Fragment fragment = new AddDiagnosticTest();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            });
        }
        if(summaryResponse.getMedicinePrescribed() != null) {
            adapter = new MedicineAdapter(getActivity(), summaryResponse.getMedicinePrescribed(), getActivity().getIntent().getExtras().getInt(LOGGED_IN_ID));
            alarmListView.setAdapter(adapter);
            alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle args = getActivity().getIntent().getExtras();
                    Fragment fragment = new PatientMedicinReminder();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            });
        }
    }

    public void createSummary(final SummaryResponse summaryToCreate)
    {

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.createPatientVisitSummary(summaryToCreate, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication reminderVM, Response response) {
                progress.dismiss();
                summaryResponse = summaryToCreate;
                summaryResponse.appointmentId = reminderVM.getAppointmentId();
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
    public void updateSummary(SummaryResponse summaryToSave)
    {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.updatePatientVisitSummary(summaryToSave, new Callback<ResponseCodeVerfication>() {
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

    }
    public class ClinicSpinner extends ArrayAdapter<Clinic> {
        Clinic[] strClinic;
        public ClinicSpinner(Context ctx, int txtViewResourceId, Clinic[] objects) {
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
            main_text.setText(strClinic[position].toString());
            return mySpinner;
        }


    }


    private void setClinic(boolean selectionRequired)
    {
        if(selectionRequired)
        {
           clinicSpinner.setVisibility(View.VISIBLE);
            clinicValue.setVisibility(View.GONE);
            Bundle bundle1 = getActivity().getIntent().getExtras();
            api.getAllClinics1(new ProfileId(bundle1.getInt(DOCTOR_ID)), new Callback<List<Clinic>>() {
                @Override
                public void success(List<Clinic> clinicsList, Response response) {
                    clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, (Clinic[])clinicsList.toArray()));
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
    }
}
