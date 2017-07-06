package com.medicohealthcare.view.profile;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.DiagnosticTestAdapter;
import com.medicohealthcare.adapter.MedicineAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.Diagnosis;
import com.medicohealthcare.model.PersonID;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.SummaryResponse;
import com.medicohealthcare.model.Symptom;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentSummary extends ParentFragment {

    EditText visit_date_value,refered_by_value;
    Button calendar_button;
    Spinner visit_type_value,clinic_spinner;
    ImageView  prescribHistryBtn,testHistryBtn, addMedicine,addtestsBtn,diagnosisHistryBtn,symptomsHistryBtn;
    ListView medicineListView;
    ListView testsListView;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue;
    MedicineAdapter adapter;
    DiagnosticTestAdapter testAdapter;
    public SummaryResponse summaryResponse = new SummaryResponse();
    private Spinner clinicSpinner;
    boolean isChanged = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_summary, container,false);
        setHasOptionsMenu(true);
        summaryResponse = new SummaryResponse();
        medicineListView = (ListView)view.findViewById(R.id.alarm_list);
        testsListView = (ListView)view.findViewById(R.id.test_prescribed_list);
        addMedicine = (ImageView)view.findViewById(R.id.add_alarm);
        addtestsBtn = (ImageView)view.findViewById(R.id.addtestsBtn);
        visit_date_value = (EditText) view.findViewById(R.id.visit_date_value);
        refered_by_value = (EditText) view.findViewById(R.id.refered_by_value);
        visit_type_value = (Spinner) view.findViewById(R.id.visit_type_value);
        clinic_spinner = (Spinner) view.findViewById(R.id.clinic_spinner);

        symptomsHistryBtn = (ImageView) view.findViewById(R.id.symptomsHistryBtn);
        diagnosisHistryBtn = (ImageView) view.findViewById(R.id.diagnosisHistryBtn);
        prescribHistryBtn = (ImageView) view.findViewById(R.id.prescribHistryBtn);
        testHistryBtn = (ImageView) view.findViewById(R.id.testHistryBtn);
        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
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
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1).trim();
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSymptom(new SearchParameter(searchText, 1, 1, 10, 1), new Callback<List<Symptom>>() {
                        @Override
                        public void success(List<Symptom> symptomList, Response response)
                        {
                            Symptom[] options = new Symptom[symptomList.size()];
                            symptomList.toArray(options);
                            ArrayAdapter<Symptom> symptomAdapter = new ArrayAdapter<Symptom>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
                            symptomAdapter.setNotifyOnChange(true);
                            symptomsValue.setAdapter(symptomAdapter);
                            symptomsValue.setOnTouchListener(new View.OnTouchListener() {

                                @Override
                                public boolean onTouch(View v, MotionEvent event)
                                {
                                    symptomsValue.showDropDown();
                                    if(symptomsValue.hasFocus()==false)
                                        symptomsValue.requestFocus();
                                    return false;
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
//                            error.printStackTrace();
                        }
                    });
                }

            }
        });
//        symptomsValue.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        // Disallow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        // Allow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//
//                        break;
//                }
//
//                // Handle ListView touch events.
//                v.onTouchEvent(event);
//                return true;
//            }
//        });
        symptomsValue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "selected", Toast.LENGTH_LONG).show();

            }
        });
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
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
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1).trim();
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillDiagnosis(new SearchParameter(searchText, 1, 1, 10, 2), new Callback<List<Diagnosis>>() {
                        @Override
                        public void success(List<Diagnosis> diagnosisList, Response response)
                        {
                            Diagnosis[] options = new Diagnosis[diagnosisList.size()];
                            diagnosisList.toArray(options);
                            ArrayAdapter<Diagnosis> diagnosisAdapter = new ArrayAdapter<Diagnosis>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
                            diagnosisAdapter.setNotifyOnChange(true);
                            diagnosisValue.setAdapter(diagnosisAdapter);
                            diagnosisValue.setOnTouchListener(new View.OnTouchListener() {

                                @Override
                                public boolean onTouch(View v, MotionEvent event)
                                {
                                    diagnosisValue.showDropDown();
                                    return false;
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
//                            error.printStackTrace();
                        }
                    });
                }

            }
        });

        clinicSpinner = (Spinner)view.findViewById(R.id.clinic_spinner);
        calendar_button = (Button) view.findViewById(R.id.calendar_button);
        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }
        });
        symptomsHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistryData(1);

            }
        });
        diagnosisHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistryData(2);

            }
        });

        prescribHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistryData(3);

            }
        });
        testHistryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistryData(4);

            }
        });
        addtestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getActivity().getIntent().getExtras();
                args.putInt(DIAGNOSTIC_TEST_ID,0);
                getActivity().getIntent().putExtras(args);
                ParentFragment fragment = new PatientDiagnosticTests();
//                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientDiagnosticTests.class.getName()).addToBackStack(PatientDiagnosticTests.class.getName()).commit();

            }
        });
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getActivity().getIntent().getExtras();
                args.putInt(MEDICINE_ID,0);
                getActivity().getIntent().putExtras(args);
                ParentFragment fragment = new PatientMedicinReminder();
//                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientMedicinReminder.class.getName()).addToBackStack(PatientMedicinReminder.class.getName()).commit();
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
        showBusy();
        final Bundle bundle = getActivity().getIntent().getExtras();
        final int doctorId = bundle.getInt(DOCTOR_ID);
        final int patientId = bundle.getInt(PATIENT_ID);
        final int appointMentId = bundle.getInt(APPOINTMENT_ID);
        final int loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        final int clinicId = bundle.getInt(CLINIC_ID);
        final long date = bundle.getLong(APPOINTMENT_DATETIME);
        final int visitType = bundle.getInt(VISIT_TYPE);
        final String clinicName = bundle.getString(CLINIC_NAME);
        if(appointMentId > 0) {
            api.getPatientVisitSummary(new AppointmentId1(appointMentId), new Callback<SummaryResponse>() {
                @Override
                public void success(SummaryResponse summary, Response response) {

                    if(summary.status == 0 || summary.errorCode != null)
                    {
                        summaryResponse = new SummaryResponse();
                        summaryResponse.status = 0;
                        summaryResponse.setDoctorId(doctorId);
                        summaryResponse.setPatientId(patientId);
                        summaryResponse.setLoggedinUserId(loggedInUserId);
                        summaryResponse.setAppointmentId(appointMentId);
                        summaryResponse.setClinicId(clinicId);
                        summaryResponse.setClinicName(clinicName);
                        summaryResponse.setVisitType(new Integer(visitType).byteValue());
                        summaryResponse.setVisitDate(date);
                    }
                    else
                        summaryResponse = summary;
                    summaryResponse.setLoggedinUserId(loggedInUserId);
                    setPatientSummary();
                    setClinic(false);
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
            hideBusy();
        }
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {   menu.clear();
        inflater.inflate(R.menu.patient_visist_summary, menu);
        super.onCreateOptionsMenu(menu,inflater);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.save_summary:
            {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            case R.id.add_invoice:
            {
                ((DoctorAppointmentInvoices)fragment).addInvoice();
                return true;
            }
            case R.id.add_payment:
            {
                ((DoctorAppointmentInvoices)fragment).addPayment();
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return false;
            }
            default:
            {
                return false;
            }

        }
    }

    public void setDate() {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                visit_date_value.setText(format.format(date));
                summaryResponse.setVisitDate(date.getTime());
                isChanged = true;
            }

        };
        Date date = null;
        if(visit_date_value.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            try {
                date = format.parse(visit_date_value.getText().toString());
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

    public void getHistryData(int histryType)
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        bundle.putInt("history_type", histryType);
        bundle.putInt(APPOINTMENT_ID,summaryResponse.getAppointmentId());
        ParentFragment fragment = new HistryDialogView();
//        ((ParentActivity)getActivity()).attachFragment(fragment);
        getActivity().getIntent().putExtras(bundle);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, HistryDialogView.class.getName()).addToBackStack(HistryDialogView.class.getName()).commit();

    }


    public void setPatientSummary()
    {
        System.out.println("In doctor Login ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(summaryResponse.getVisitDate());
        visit_date_value.setText(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date(summaryResponse.getVisitDate())));
        refered_by_value.setText(summaryResponse.getReferredBy());
        visit_date_value.setEnabled(false);
        visit_type_value.setEnabled(false);
        calendar_button.setEnabled(false);

        symptomsValue.setText(summaryResponse.getSymptoms());
        diagnosisValue.setText(summaryResponse.getDiagnosis());
        Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        int profileId = bundle.getInt(PROFILE_ID);
        if(summaryResponse.getTestPrescribed() !=null) {
            testAdapter = new DiagnosticTestAdapter(getActivity(), summaryResponse.getTestPrescribed(),profileId );
            testsListView.setAdapter(testAdapter);
        }
        if(summaryResponse.getMedicinePrescribed() != null) {
            adapter = new MedicineAdapter(getActivity(), summaryResponse.getMedicinePrescribed(), getActivity().getIntent().getExtras().getInt(LOGGED_IN_ID));
            medicineListView.setAdapter(adapter);

        }
    }

    public void createSummary(final SummaryResponse summaryToCreate)
    {
        showBusy();
        api.createPatientVisitSummary(summaryToCreate, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication reminderVM, Response response) {
                summaryResponse = summaryToCreate;
                if( reminderVM.getAppointmentId() != null && reminderVM.getAppointmentId().intValue() > 0 )
                    summaryResponse.appointmentId = reminderVM.getAppointmentId();
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
    }
    public void updateSummary(SummaryResponse summaryToSave)
    {
        showBusy();
        api.updatePatientVisitSummary(summaryToSave, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication reminderVM, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
            Bundle bundle1 = getActivity().getIntent().getExtras();
            Integer doctorId = bundle1.getInt(DOCTOR_ID);
            api.getAllClinics(new PersonID(doctorId), new Callback<List<Clinic1>>() {
                @Override
                public void success(List<Clinic1> clinicsList, Response response) {
                    clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinicsList));
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);

                }
            });
        }
        else
        {
            Clinic1 clinic = new Clinic1();
            clinic.clinicName = summaryResponse.clinicName;
            clinic.idClinic = summaryResponse.clinicId;
            List<Clinic1> clinicsList = new ArrayList<>();
            clinicsList.add(clinic);
            clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinicsList));
            clinicSpinner.setEnabled(false);
        }
    }
    @Override
    public boolean isChanged()
    {
        return summaryResponse.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        if(bundle1.getInt(APPOINTMENT_ID) == 0)
        {
            summaryResponse.setClinicId(((Clinic1)clinicSpinner.getSelectedItem()).idClinic);
            summaryResponse.setVisitType(new Integer(visit_type_value.getSelectedItemPosition()).byteValue());
            summaryResponse.setReferredBy(refered_by_value.getText().toString());
        }
        summaryResponse.setSymptoms(symptomsValue.getText().toString());
        summaryResponse.setDiagnosis(diagnosisValue.getText().toString());
    }
    @Override
    public boolean save()
    {
        if(summaryResponse.canBeSaved())
        {
            if(summaryResponse.appointmentId == null || summaryResponse.status == 0)
                createSummary(summaryResponse);
            else
                updateSummary(summaryResponse);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return summaryResponse.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {
            if (summaryResponse.getAppointmentId() != null && summaryResponse.getAppointmentId().intValue() > 0)
            {
                calendar_button.setEnabled(false);
                clinicSpinner.setEnabled(false);
                visit_type_value.setEnabled(false);
                refered_by_value.setEnabled(editable);
                symptomsValue.setEnabled(editable);
                diagnosisValue.setEnabled(editable);
            }
            else
            {
                calendar_button.setEnabled(editable);
                clinicSpinner.setEnabled(editable);
                visit_type_value.setEnabled(editable);
                refered_by_value.setEnabled(editable);
                symptomsValue.setEnabled(editable);
                diagnosisValue.setEnabled(editable);
            }
     }

}
