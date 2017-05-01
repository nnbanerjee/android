package com.medico.view.profile;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.AlarmReminderVM;
import com.medico.model.Medicine;
import com.medico.model.MedicineId;
import com.medico.model.PatientMedicine;
import com.medico.model.ReminderDate;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SearchParameter;
import com.medico.util.AlarmService;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import Model.AlarmReminderVM;
//import Model.ReminderDate;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class PatientMedicinReminder extends ParentFragment {

    TextView timeValue,daysText, durationText;
    EditText duration, doctorInstructionValue,numberOfDosesPerSchedule,startDateEdit,endDateEdit;
    ImageView calenderImg;
    Calendar calendar = Calendar.getInstance();
    Spinner  scheduleDate;

    TableLayout medicineSchedule;

    CheckBox medicineReminderBtn;
    CheckBox autoScheduleBtn;
    List<AlarmReminderVM> medicinReminderTables;
    List<AlarmReminderVM> alarmList;
    ArrayList<ReminderDate> reminderDate;
    ArrayList<String> startList, endList;
    ArrayAdapter<String> scheduleAdapter;
    public MultiAutoCompleteTextView medicineName;
    PatientMedicine patientMedicine = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_medicine_reminder, container, false);
        setHasOptionsMenu(true);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Medicine Details");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        //Load Ui controls
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        numberOfDosesPerSchedule = (EditText) view.findViewById(R.id.no_of_doses_edit_box);
        numberOfDosesPerSchedule.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(numberOfDosesPerSchedule.getText().length() != 0) {
                    int number = Integer.parseInt(numberOfDosesPerSchedule.getText().toString());
                    patientMedicine.setDosesPerSchedule(number);
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                    endDateEdit.setText(dateFormat.format(patientMedicine.getEndDate()));
                    setSchedule(patientMedicine);
                }
            }
        });
        scheduleDate = (Spinner) view.findViewById(R.id.scheduleDate);
        startDateEdit = (EditText) view.findViewById(R.id.start_date);
        endDateEdit = (EditText) view.findViewById(R.id.end_date);
        medicineReminderBtn = (CheckBox) view.findViewById(R.id.medicineReminderBtn);
        autoScheduleBtn = (CheckBox) view.findViewById(R.id.auto_scheduleBtn);
        autoScheduleBtn.setEnabled(false);
        medicineName = (MultiAutoCompleteTextView) view.findViewById(R.id.medicineValueEdit);
        doctorInstructionValue = (EditText) view.findViewById(R.id.editText2);
        daysText = (TextView) view.findViewById(R.id.schedule_text);
        durationText = (TextView) view.findViewById(R.id.duration_text);
        duration = (EditText) view.findViewById(R.id.duration_days);
        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(duration.getText().length() != 0) {
                    int number = Integer.parseInt(duration.getText().toString());
                    patientMedicine.setDurationSchedule(number);
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                    endDateEdit.setText(dateFormat.format(patientMedicine.getEndDate()));
                    setSchedule(patientMedicine);
                }
            }
        });
        String[] options = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
        medicineName.setAdapter(adapter);
        medicineName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(medicineName.getText().toString().trim().length() > 0 )
                {
                    api.searchAutoFill(new SearchParameter(medicineName.getText().toString(), 1, 1, 10, 3), new Callback<List<Medicine>>() {
                        @Override
                        public void success(List<Medicine> medicineList, Response response)
                        {
                            ArrayAdapter array = (ArrayAdapter<String>)medicineName.getAdapter();
                            array.clear();
                            array.addAll(medicineList);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        }
                    });
                }
            }
        });
        medicineSchedule = (TableLayout)view.findViewById(R.id.displayLinear);
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(startDateEdit);

            }
        });
        scheduleDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(patientMedicine != null) {

                    patientMedicine.setSchedule(new Integer(position).byteValue());
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                    endDateEdit.setText(dateFormat.format(patientMedicine.getEndDate()));
                    setSchedule(patientMedicine);
                    switch(position)
                    {
                        case PARAM.DAY:
                            daysText.setText(" / Day");
                            durationText.setText(" Days");
                            break;
                        case PARAM.WEEK:
                            daysText.setText(" / Week");
                            durationText.setText(" Weeks");
                            break;
                        case PARAM.MONTH:
                            daysText.setText(" / Month");
                            durationText.setText(" Months");
                            break;
                        case PARAM.YEAR:
                            daysText.setText(" / Year");
                            durationText.setText(" Years");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void setAlarm(Calendar calendar) {
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmService.class);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);

        long trigerTime = calendar.getTimeInMillis();

        System.out.println("trigerTime = " + trigerTime);
        System.out.println("trigerTime current = " + Calendar.getInstance().getTimeInMillis());

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
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
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
        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        int doctorId = bundle.getInt(DOCTOR_ID);
        int patientId = bundle.getInt(PATIENT_ID);
        Integer medicineId = bundle.getInt(MEDICINE_ID);
        int appointMentId = bundle.getInt(APPOINTMENT_ID);
        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);
        if(medicineId > 0) {
            api.getPatientMedicine(new MedicineId(medicineId), new Callback<PatientMedicine>() {
                @Override
                public void success(PatientMedicine medicine, Response response)
                {
                    patientMedicine = medicine;
                    medicineName.setText(medicine.getMedicinName());
                    numberOfDosesPerSchedule.setText("" + medicine.getDosesPerSchedule());
                    scheduleDate.setSelection(medicine.getSchedule());
                    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                    startDateEdit.setText(format.format(medicine.getStartDate()));
                    endDateEdit.setText(format.format(medicine.getEndDate()));
                    doctorInstructionValue.setText(medicine.getDoctorInstruction());
                    duration.setText("" + medicine.getDurationSchedule());
                    doctorInstructionValue.setText(medicine.getDoctorInstruction());
                    medicineReminderBtn.setChecked(medicine.getReminder().byteValue() ==1);
                    autoScheduleBtn.setChecked(medicine.getAutoSchedule().byteValue()==1);
                    List<PatientMedicine.MedicineSchedule> schedule = medicine.getMedicineSchedule();
                    patientMedicine.setLoggedinUserId(new Integer(logged_in_id));
                    setSchedule(patientMedicine);
                    hideBusy();

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    hideBusy();

                }
            });
        }
        else
        {
            patientMedicine = new PatientMedicine();
            patientMedicine.setDurationSchedule(1);
            patientMedicine.setDosesPerSchedule(1);
            patientMedicine.setStartDate(new Date().getTime());
            patientMedicine.setSchedule(new Integer(0).byteValue());
            patientMedicine.setPatientId(patientId);
            patientMedicine.setDoctorId(doctorId);
            patientMedicine.setReminder(Byte.parseByte("1"));
            patientMedicine.setAutoSchedule(Byte.parseByte("1"));
            patientMedicine.setAppointmentId(appointMentId);
            patientMedicine.setLoggedinUserId(logged_in_id);

            medicineName.setText(patientMedicine.getMedicinName());
            numberOfDosesPerSchedule.setText("" + patientMedicine.getDosesPerSchedule());
            scheduleDate.setSelection(patientMedicine.getSchedule());
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            startDateEdit.setText(format.format(patientMedicine.getStartDate()));
            endDateEdit.setText(format.format(patientMedicine.getEndDate()));
            doctorInstructionValue.setText(patientMedicine.getDoctorInstruction());
            duration.setText("" + patientMedicine.getDurationSchedule());
            doctorInstructionValue.setText(patientMedicine.getDoctorInstruction());
            medicineReminderBtn.setChecked(true);
            autoScheduleBtn.setChecked(true);
            List<PatientMedicine.MedicineSchedule> schedule = patientMedicine.getMedicineSchedule();
            setSchedule(patientMedicine);
            hideBusy();

        }
    }

    private void setSchedule(PatientMedicine medicine)
    {

        //create dates
        List<Long> dates = new ArrayList<Long>();
        long schedule[] = medicine.getAlarmSchedule();
        String dateString = null;
        for(int i = 0; i < schedule.length; i++)
        {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String tempDate = dateFormat.format(new Date(schedule[i]));
            DateFormat dateFormat1 = DateFormat.getDateInstance();
            String tempDate1 = dateFormat1.format(new Date(schedule[i]));
            if(dateString == null || dateString.equalsIgnoreCase(tempDate) == false)
            {
                dates.add(new Long(schedule[i]));
                dateString = tempDate;
            }
        }

        long date[] = new long[dates.size()];
        int j = 0;
        for(Long list : dates)
        {
            date[j] = list.longValue();
            j++;
        }

        //Create table
        medicineSchedule.removeAllViews();
        Activity activity = getActivity();
        TableRow row= new TableRow(activity);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        for(int i = 0; i < date.length; i++)
        {
            TextView textView = new TextView(activity);
            textView.setText(dateFormat.format(date[i]));
            textView.setBackgroundResource(R.drawable.medicine_schedule_header);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(3,3,3,3);
            textView.setGravity(1);
            row.addView(textView,i,lp);
        }
        medicineSchedule.addView(row);
        medicineSchedule.setStretchAllColumns(true);
        DateFormat dateFor = DateFormat.getTimeInstance(DateFormat.SHORT);
        if(medicine.getSchedule() == PARAM.DAY)
        {
            TableRow[] medicineSch = new TableRow[medicine.getDosesPerSchedule()];
            int dosesPerday = medicine.getDosesPerSchedule();

            long startDateTime = medicine.getStartDate();
            Calendar startDateEndTime = Calendar.getInstance();
            startDateEndTime.setTime(new Date(startDateTime));
            startDateEndTime.set(Calendar.HOUR_OF_DAY,23);
            startDateEndTime.set(Calendar.MINUTE, 59);
            startDateEndTime.set(Calendar.SECOND, 59);
            long startDateEndTIme = startDateEndTime.getTime().getTime();
            long delta = startDateEndTIme - startDateTime;
            int StartDayFirstIndex = medicineSch.length - Math.round(delta/medicine.durationBetweenDoses()) - 1;
            for(int i = 0; i < medicineSch.length; i++)
            {
                medicineSch[i] = new TableRow(activity);
                medicineSchedule.addView(medicineSch[i]);
                if(i < StartDayFirstIndex)
                    medicineSch[i].addView(new TextView(activity));
            }

            for(int i = 0; i < schedule.length; i++)
            {
                TextView textView = new TextView(activity);
                textView.setPadding(3,3,3,3);
                textView.setGravity(1);
                textView.setText(dateFor.format(schedule[i]));
                textView.setBackgroundResource(R.drawable.medicine_schedule);
                medicineSch[StartDayFirstIndex].addView(textView);
                StartDayFirstIndex++;

                if(StartDayFirstIndex >= dosesPerday) {
                    StartDayFirstIndex = 0;
                }
            }
        }
        else
        {
            int dosesPerday = 1;
            TableRow medicineSch = new TableRow(activity);
            for(int i = 0; i < schedule.length; i++)
            {
                TextView textView = new TextView(activity);
                textView.setPadding(3,3,3,3);
                textView.setGravity(1);
                textView.setText(dateFor.format(schedule[i]));
                textView.setBackgroundResource(R.drawable.medicine_schedule);
                medicineSch.addView(textView);
            }
            medicineSchedule.addView(medicineSch);

        }

        medicineSchedule.requestLayout();
    }




    public void savePatientReminderData(PatientMedicine saveReminderVM)
    {
        showBusy();
        if (saveReminderVM.getMedicineId()== null || saveReminderVM.getMedicineId().intValue() == 0 )
        {
            api.addPatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    hideBusy();
                }
            });
        }
        else
        {
            System.out.println("I am Update Conidtion::::::::::::::::::::");
            api.updatePatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    hideBusy();
                 }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    hideBusy();
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
                patientMedicine.setEndDate(patientMedicine.getEndDate());
                endDateEdit.setText(format.format(patientMedicine.getEndDate()));
                setSchedule(patientMedicine);
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
        inflater.inflate(R.menu.medicine_menu, menu);
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer medicineId = bundle.getInt(MEDICINE_ID);
        MenuItem menuItem = menu.findItem(R.id.add_medicine);
        if(medicineId != null && medicineId.intValue() > 0)
        {
            menuItem.setTitle("SAVE");
        }
        else
            menuItem.setTitle("CREATE");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add_medicine: {
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
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean isChanged()
    {
        return patientMedicine.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        patientMedicine.setMedicinName(medicineName.getText().toString());
        patientMedicine.setDosesPerSchedule(new Integer(numberOfDosesPerSchedule.getText().toString()));
        patientMedicine.setSchedule(new Integer(scheduleDate.getSelectedItemPosition()).byteValue());
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
        endDateEdit.setText(format.format(patientMedicine.getEndDate()));
        patientMedicine.setDoctorInstruction(doctorInstructionValue.getText().toString());
        patientMedicine.setDurationSchedule(new Integer(duration.getText().toString()));
        patientMedicine.setReminder(medicineReminderBtn.isChecked()? new Integer(1).byteValue():new Integer(0).byteValue());
        patientMedicine.setAutoSchedule(autoScheduleBtn.isChecked()?new Integer(1).byteValue():new Integer(0).byteValue());
        patientMedicine.setType(1);
        long[] schedule = patientMedicine.getAlarmSchedule();
        List<PatientMedicine.MedicineSchedule> scheduleList = new ArrayList<>();
        for(int i = 0; i < schedule.length; i++)
        {
            scheduleList.add(new PatientMedicine.MedicineSchedule(schedule[i]));
        }
        patientMedicine.setMedicineSchedule(scheduleList);
    }
    @Override
    public boolean save()
    {
        if(patientMedicine.canBeSaved())
        {
            savePatientReminderData(patientMedicine);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return patientMedicine.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {
        medicineName.setEnabled(editable);
        numberOfDosesPerSchedule.setEnabled(editable);
        scheduleDate.setEnabled(editable);
        calenderImg.setEnabled(editable);
        doctorInstructionValue.setEnabled(editable);
        duration.setEnabled(editable);
        medicineReminderBtn.setEnabled(editable);
        autoScheduleBtn.setEnabled(editable);

    }

}
