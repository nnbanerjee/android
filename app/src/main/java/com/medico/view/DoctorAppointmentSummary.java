package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.medico.adapter.MedicineAdapter;
import com.medico.adapter.TestsAdapter;
import com.medico.model.AppointmentId1;
import com.medico.model.Clinic1;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SearchParameter;
import com.medico.model.SummaryResponse;
import com.medico.model.SummaryResponse.MedicinePrescribed;
import com.medico.model.Symptom;
import com.medico.model.VisitEditLogRequest;
import com.medico.model.VisitEditLogResponse;
import com.mindnerves.meidcaldiary.Fragments.AddDiagnosticTest;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.AddDiagnosisTestRequest;
import Model.AlarmReminderVM;
import Model.PersonID;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentSummary extends ParentFragment {

    TextView visitedDate,referedBy;
    Spinner visit;
    Button medicineBtn, selectDateBtn;
    ImageView  prescribHistryBtn,testHistryBtn, addMedicine,addtestsBtn,diagnosisHistryBtn,symptomsHistryBtn;
    ListView medicineListView;
    ListView testsListView;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue,medicineValue,testPrescribedValue;
    ProgressDialog progress;
    TextView clinicName;
    MedicineAdapter adapter;
    TestsAdapter testAdapter;
    public SummaryResponse summaryResponse;
    private Spinner clinicSpinner;
    boolean isChanged = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container,false);
        summaryResponse = new SummaryResponse();
        medicineListView = (ListView)view.findViewById(R.id.alarm_list);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        testsListView = (ListView)view.findViewById(R.id.test_prescribed_list);
        addMedicine = (ImageView)view.findViewById(R.id.add_alarm);
        addtestsBtn = (ImageView)view.findViewById(R.id.addtestsBtn);
        clinicName = (TextView)view.findViewById(R.id.clinicName);
        visit = (Spinner) view.findViewById(R.id.visit);
        visitedDate = (TextView) view.findViewById(R.id.visitedDate);
        referedBy = (TextView) view.findViewById(R.id.referedBy);

        symptomsHistryBtn = (ImageView) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (ImageView) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (ImageView) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (ImageView) view.findViewById(R.id.testHistryBtn);
        medicineBtn = (Button) view.findViewById(R.id.reminderBtn);
        String[] typeList =  getResources().getStringArray(R.array.visit_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType,  typeList);
        visit.setAdapter(adapter);

        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        Symptom[] options = {};
        ArrayAdapter<Symptom> symptomAdapter = new ArrayAdapter<Symptom>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
        symptomsValue.setAdapter(symptomAdapter);
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        symptomsValue.setThreshold(1);
        symptomsValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1);
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSymptom(new SearchParameter(searchText, 1, 1, 10, 1), new Callback<List<Symptom>>() {
                        @Override
                        public void success(List<Symptom> symptomList, Response response)
                        {
                            ArrayAdapter array = (ArrayAdapter<Symptom>)symptomsValue.getAdapter();
                            array.clear();
                            array.addAll(symptomList);
                            array.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            error.printStackTrace();
                        }
                    });
                }

            }
        });
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        Symptom[] options1 = {};
        ArrayAdapter<Symptom> diagnosticAdapter = new ArrayAdapter<Symptom>(getActivity(), android.R.layout.simple_dropdown_item_1line,options1);
        diagnosisValue.setAdapter(diagnosticAdapter);
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        diagnosisValue.setThreshold(1);
        diagnosisValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1);
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSymptom(new SearchParameter(searchText, 1, 1, 10, 2), new Callback<List<Symptom>>() {
                        @Override
                        public void success(List<Symptom> symptomList, Response response)
                        {
                            ArrayAdapter array = (ArrayAdapter<Symptom>)diagnosisValue.getAdapter();
                            array.clear();
                            array.addAll(symptomList);
                            array.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            error.printStackTrace();
                        }
                    });
                }

            }
        });
        medicineValue = (MultiAutoCompleteTextView)view.findViewById(R.id.medicineValue);
        clinicSpinner = (Spinner)view.findViewById(R.id.clinic_spinner);

//        clinicValue= (MultiAutoCompleteTextView)view.findViewById(R.id.clinicValue);
//        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        medicineValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        selectDateBtn = (Button) view.findViewById(R.id.timeBtn);
        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }
        });
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
        medicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MedicinePrescribed prescribed = null;
                if(prescribed != null )
                {
                    Bundle args = getActivity().getIntent().getExtras();
                    ParentFragment fragment = new PatientMedicinReminder();
                    ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
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
                fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ParentFragment fragment = new PatientMedicinReminder();
//                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
//                FragmentManager fragmentManger = getActivity().getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
                Bundle args = getActivity().getIntent().getExtras();
                args.remove(MEDICINE_ID);
                getActivity().getIntent().putExtras(args);
                ParentFragment fragment = new PatientMedicinReminder();
                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        medicineListView.setOnTouchListener(new View.OnTouchListener() {
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

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        final Bundle bundle = getActivity().getIntent().getExtras();
        final int doctorId = bundle.getInt(DOCTOR_ID);
        final int patientId = bundle.getInt(PATIENT_ID);
        final int appointMentId = bundle.getInt(APPOINTMENT_ID);
        final int loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        if(appointMentId > 0) {
            api.getPatientVisitSummary(new AppointmentId1(appointMentId), new Callback<SummaryResponse>() {
                @Override
                public void success(SummaryResponse summary, Response response) {
                    summaryResponse = summary;
                    summary.setLoggedinUserId(loggedInUserId);
                    progress.dismiss();
                    setPatientSummary();
                    setClinic(false);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    setClinic(true);
                    progress.dismiss();
                }
            });
        }
        else
        {
            summaryResponse = new SummaryResponse();
            summaryResponse.setDoctorId(doctorId);
            summaryResponse.setPatientId(patientId);
            summaryResponse.setLoggedinUserId(loggedInUserId);
            setClinic(true);

            progress.dismiss();
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
//                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

//    public void saveData()
//    {
//        Bundle bundle = getActivity().getIntent().getExtras();
//        if(summaryResponse == null)
//        {
//            Bundle bundle1 = getActivity().getIntent().getExtras();
//            Calendar calender = Calendar.getInstance();
//
//            SummaryResponse createSummary = new SummaryResponse();
//            createSummary.visitType = new Integer(visit.getSelectedItemPosition()).byteValue();
//            createSummary.referredBy = (referedBy.getText().toString());
//            createSummary.diagnosis = diagnosisValue.getText().toString();
//            createSummary.symptoms = symptomsValue.getText().toString();
//            createSummary.clinicId = new Integer(((Clinic)clinicSpinner.getAdapter().getItem(clinicSpinner.getSelectedItemPosition())).getIdClinic());
//            createSummary.loggedinUserId = bundle.getInt(LOGGED_IN_ID);
//            createSummary.doctorId = bundle1.getInt(DOCTOR_ID);
//            createSummary.doctorId = bundle1.getInt(PATIENT_ID);
//            createSummary.visitDate = calender.getTimeInMillis();
//            createSummary.visitType = new Integer(visit.getSelectedItemPosition()).byteValue();
//            createSummary.referredBy = referedBy.getText().toString();
//            createSummary.treatmentPlanEnabled = 0;
//            try {
//                Date date = DateFormat.getInstance().parse(visitedDate.getText().toString());
//                createSummary.setVisitDate(date.getTime());
//            }
//            catch (ParseException e)
//            {
//                e.printStackTrace();
//            }
//
//            if(bundle.getInt(APPOINTMENT_ID) > 0)
//                createSummary.appointmentId = bundle.getInt(APPOINTMENT_ID);
//            createSummary(createSummary);
//        }
//        else
//        {
//            summaryResponse.diagnosis = diagnosisValue.getText().toString();
//            summaryResponse.symptoms = symptomsValue.getText().toString();
//            updateSummary(summaryResponse);
//         }
//
//    }
    public void setDate() {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                visitedDate.setText(format.format(date));
                summaryResponse.setVisitDate(date.getTime());
                isChanged = true;
            }

        };
        Date date = null;
        if(visitedDate.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            try {
                date = format.parse(visitedDate.getText().toString());
            }
            catch(ParseException e)
            {
                date = new Date();
            }

        }

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .build();
        pickerDialog.show();
    }

    public void getHistryData(final int histryString)
    {

        VisitEditLogRequest req= new VisitEditLogRequest(summaryResponse.getAppointmentId(),histryString);
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


    public void setPatientSummary()
    {
        System.out.println("In doctor Login ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(summaryResponse.getVisitDate());
        visitedDate.setText(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date(summaryResponse.getVisitDate())));
        referedBy.setText(summaryResponse.getReferredBy());
//        clinicValue.setText(summaryResponse.clinicName);
        visitedDate.setEnabled(false);
        visit.setEnabled(false);
//        clinicValue.setEnabled(false);
        selectDateBtn.setEnabled(false);

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
                    fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            });
        }
        if(summaryResponse.getMedicinePrescribed() != null) {
            adapter = new MedicineAdapter(getActivity(), summaryResponse.getMedicinePrescribed(), getActivity().getIntent().getExtras().getInt(LOGGED_IN_ID));
            medicineListView.setAdapter(adapter);
            medicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle args = getActivity().getIntent().getExtras();
                    MedicinePrescribed medicinePrescribed = (MedicinePrescribed)adapterView.getAdapter().getItem(position);
                    args.putInt(MEDICINE_ID, medicinePrescribed.medicineId);
                    getActivity().getIntent().putExtras(args);
                    ParentFragment fragment = new PatientMedicinReminder();
                    ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
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
                if( reminderVM.getAppointmentId() != null && reminderVM.getAppointmentId().intValue() > 0 )
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
    public class ClinicSpinner extends ArrayAdapter<Clinic1> {
        List<Clinic1> strClinic;
        public ClinicSpinner(Context ctx, int txtViewResourceId, List<Clinic1> objects) {
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
            main_text.setText(strClinic.get(position).toString());
            return mySpinner;
        }


    }


    private void setClinic(boolean selectionRequired)
    {
        if(selectionRequired)
        {
           clinicSpinner.setVisibility(View.VISIBLE);
//            clinicValue.setVisibility(View.GONE);
            Bundle bundle1 = getActivity().getIntent().getExtras();
            Integer doctorId = bundle1.getInt(DOCTOR_ID);
            api.getAllClinics1(new PersonID(doctorId.toString()), new Callback<List<Clinic1>>() {
                @Override
                public void success(List<Clinic1> clinicsList, Response response) {
                    clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinicsList));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            });
        }else{
//            clinicSpinner.setVisibility(View.GONE);
//            clinicValue.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean isChanged()
    {
        return summaryResponse.isChanged();
    }
    @Override
    protected void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        if(bundle1.getInt(APPOINTMENT_ID) == 0)
        {
            summaryResponse.setClinicId(((Clinic1)clinicSpinner.getSelectedItem()).idClinic);
            summaryResponse.setVisitType(new Integer(visit.getSelectedItemPosition()).byteValue());
            summaryResponse.setReferredBy(referedBy.getText().toString());
        }
        summaryResponse.setSymptoms(symptomsValue.getText().toString());
        summaryResponse.setDiagnosis(diagnosisValue.getText().toString());
    }
    @Override
    protected boolean save()
    {
        if(summaryResponse.canBeSaved())
        {
            if(summaryResponse.appointmentId == null)
                createSummary(summaryResponse);
            else
                updateSummary(summaryResponse);
            return true;
        }
        return false;
    }
    @Override
    protected boolean canBeSaved()
    {
        return summaryResponse.canBeSaved();
    }
    @Override
    protected void setEditable(boolean editable)
    {
            if (summaryResponse.getAppointmentId() != null && summaryResponse.getAppointmentId().intValue() > 0)
            {
                selectDateBtn.setEnabled(false);
                clinicSpinner.setEnabled(false);
                visit.setEnabled(false);
                referedBy.setEnabled(editable);
                symptomsValue.setEnabled(editable);
                diagnosisValue.setEnabled(editable);
            }
            else
            {
                selectDateBtn.setEnabled(editable);
                clinicSpinner.setEnabled(editable);
                visit.setEnabled(editable);
                referedBy.setEnabled(editable);
                symptomsValue.setEnabled(editable);
                diagnosisValue.setEnabled(editable);
            }
     }

}
