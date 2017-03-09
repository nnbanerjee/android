package com.medico.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.medico.adapter.ClinicSpinnerAdapter;
import com.medico.adapter.DiagnosticTestSpinnerAdapter;
import com.medico.model.Clinic1;
import com.medico.model.DiagnosticTest;
import com.medico.model.PatientDiagnostic;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SearchParameter;
import com.medico.util.AlarmService;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.PatientTestId;
import Model.PersonID;
import com.medico.util.PARAM;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class PatientDiagnosticTests extends ParentFragment {

    TextView startDateEdit;
    EditText doctorInstructionValue;
    ImageView calenderImg;
    Calendar calendar = Calendar.getInstance();
    CheckBox medicineReminderBtn;
    ArrayAdapter<String> scheduleAdapter;
    Spinner clinicName,referredBy,testName;
    PatientDiagnostic patientMedicine = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_diagnostic_test, container, false);
        setHasOptionsMenu(true);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Diagnostic Test Details");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        //Load Ui controls
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        startDateEdit = (TextView) view.findViewById(R.id.start_date);
        medicineReminderBtn = (CheckBox) view.findViewById(R.id.medicineReminderBtn);
        testName = (Spinner) view.findViewById(R.id.testValueEdit);
        clinicName = (Spinner) view.findViewById(R.id.clinicNames);
        referredBy = (Spinner)view.findViewById(R.id.referredByValue);
        doctorInstructionValue = (EditText) view.findViewById(R.id.editText2);
        String[] options = {};
        return view;
    }

    public void setAlarm(Calendar calendar) {
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmService.class);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);

        long trigerTime = calendar.getTimeInMillis();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) trigerTime,
                intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, trigerTime, pendingIntent);
    }

    public Calendar getStringToTime(String time, Calendar calendar) {
        String[] timeValue;
        timeValue = time.split(":");
        int hour1 = Integer.parseInt(timeValue[0].trim());
        int min1 = Integer.parseInt(timeValue[1].trim().split("[a-zA-Z ]+")[0]);
        calendar.set(Calendar.HOUR, hour1);
        calendar.set(Calendar.MINUTE, min1);

        String strAM_PM = timeValue[1].replaceAll("[0-9]", "");
        if (strAM_PM.equals("AM")) {
            calendar.set(Calendar.AM_PM, 0);
        } else {
            calendar.set(Calendar.AM_PM, 1);
        }
        return calendar;
    }

    public Date getStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = null;
        try {
            dateValue = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateValue;
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



    //Declaration
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final int doctorId = bundle.getInt(DOCTOR_ID);
        final int patientId = bundle.getInt(PATIENT_ID);
        final Integer testId = bundle.getInt(PARAM.DIAGNOSTIC_TEST_ID);
        final Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);
        if(testId > 0) {
            api.getTestDetails1(new PatientTestId(testId.toString()), new Callback<PatientDiagnostic>() {
                @Override
                public void success(PatientDiagnostic testReminder, Response response)
                {
                    patientMedicine = testReminder;
                    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                    startDateEdit.setText(format.format(testReminder.datetime));
                    doctorInstructionValue.setText(testReminder.getDoctorInstruction());
                    doctorInstructionValue.setText(testReminder.getDoctorInstruction());
                    medicineReminderBtn.setChecked(testReminder.getReminder().byteValue() ==1);
                    patientMedicine.setLoggedinUserId(new Integer(logged_in_id));
                    patientMedicine.setDoctorId(doctorId);
//                    patientMedicine.setAppointmentId(appointMentId);
                    patientMedicine.setPatientId(patientId);
//                    patientMedicine.setLoggedinUserId(logged_in_id);
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
            patientMedicine = new PatientDiagnostic();
            patientMedicine.setStartDate(new Date().getTime());
            patientMedicine.setPatientId(patientId);
            patientMedicine.setDoctorId(doctorId);
            patientMedicine.setReminder(Byte.parseByte("1"));
            patientMedicine.setAppointmentId(appointMentId);
            patientMedicine.setLoggedinUserId(logged_in_id);
            patientMedicine.setClinicId(clinicId);

            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            startDateEdit.setText(format.format(patientMedicine.getStartDate()));
            doctorInstructionValue.setText(patientMedicine.getDoctorInstruction());
            doctorInstructionValue.setText(patientMedicine.getDoctorInstruction());
            medicineReminderBtn.setChecked(true);
        }

        api.getAllClinics1(new PersonID(new Integer(doctorId).toString()), new Callback<List<Clinic1>>() {
            @Override
            public void success(List<Clinic1> clinicsList, Response response) {

                ClinicSpinnerAdapter adapter = new ClinicSpinnerAdapter(getActivity(), R.layout.customize_spinner, clinicsList);
                clinicName.setAdapter(adapter);
                if(patientMedicine != null && patientMedicine.clinic != null)
                    clinicName.setSelection(adapter.getPositionId(patientMedicine.clinic.idClinic));
                else
                    clinicName.setSelection(((ClinicSpinnerAdapter)clinicName.getAdapter()).getPositionId(clinicId));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });
        api.searchAutoFillDiagnostic(new SearchParameter("", 0, 1, 100, 4), new Callback<List<DiagnosticTest>>() {
            @Override
            public void success(List<DiagnosticTest> medicineList, Response response)
            {
                DiagnosticTestSpinnerAdapter adapter = new DiagnosticTestSpinnerAdapter(getActivity(), R.layout.customize_spinner, medicineList);
                testName.setAdapter(adapter);
                if(patientMedicine != null && patientMedicine.test != null)
                    testName.setSelection(adapter.getPositionId(patientMedicine.getDiagnosticTest().testId));
                else
                    testName.setSelection(0);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });



    }

    public void savePatientReminderData(PatientDiagnostic saveReminderVM) {

        if (saveReminderVM.getPatientTestId() == null || saveReminderVM.getPatientTestId().intValue() == 0 )
        {
            api.addPatientDiagnosticTest1(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
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
            System.out.println("I am Update Conidtion::::::::::::::::::::");
            api.updatePatientDiagnosticTest1(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                 }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        }
    }


    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                dateField.setText(format.format(date));
                patientMedicine.setStartDate(date.getTime());
            }

        };
        Date date = null;
        if(dateField.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            try {
                date = format.parse(dateField.getText().toString());
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.save);
    }

    @Override
    public boolean isChanged()
    {
        return patientMedicine.isChanged();
    }
    @Override
    protected void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        patientMedicine.setDiagnosticTest((DiagnosticTest)testName.getSelectedItem());
        patientMedicine.setDoctorInstruction(doctorInstructionValue.getText().toString());
        patientMedicine.setReminder(medicineReminderBtn.isChecked()? new Integer(1).byteValue():new Integer(0).byteValue());
        patientMedicine.setClinicId(((Clinic1)clinicName.getSelectedItem()).idClinic);
        if(referredBy.getSelectedItemPosition()==0)
            patientMedicine.setReferredId(bundle1.getInt(PATIENT_ID));
        else
            patientMedicine.setReferredId(bundle1.getInt(DOCTOR_ID));
    }
    @Override
    protected boolean save()
    {
        if(patientMedicine.canBeSaved())
        {
            savePatientReminderData(patientMedicine);
            return true;
        }
        return false;
    }
    @Override
    protected boolean canBeSaved()
    {
        return patientMedicine.canBeSaved();
    }
    @Override
    protected void setEditable(boolean editable)
    {
//        testName.setEnabled(editable);
//        calenderImg.setEnabled(editable);
//        doctorInstructionValue.setEnabled(editable);
//        medicineReminderBtn.setEnabled(editable);

    }

}
