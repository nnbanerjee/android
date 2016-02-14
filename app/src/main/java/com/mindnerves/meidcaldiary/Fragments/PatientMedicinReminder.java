package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mindnerves.meidcaldiary.AlarmService;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapter.HorizontalListAdapter;
import Application.MyApi;
import Model.AlarmReminderVM;

import Model.ReminderDate;
import Model.ReminderVM;
import Model.TimeReminder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class PatientMedicinReminder extends Fragment {

    Integer doctorId;
    MyApi api;
    SharedPreferences session;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    int count = 0;
    boolean checkStartDate = false;
    TextView dateValue,timeValue,endDateValue,daysText,durationText;
    EditText duration,doctorInstructionValue;
    ImageView calenderImg,endDateImg;
    Calendar calendar = Calendar.getInstance();
    Spinner numberDoses,scheduleDate;
    HorizontalListView horizontalList;
    CheckBox medicineReminderBtn;
    List<AlarmReminderVM> medicinReminderTables;
    List<AlarmReminderVM> alarmList;
    String medicinName = "";
    Global global;
    String[] scheduleList;
    String[] dosesList;
    String[] medicin_list = null;
    String type = null;
    Button saveTimeTable;
    String appointmentDate,appointmentTime,patientId;
    ArrayList<ReminderDate> reminderDate;
    ArrayList<String> startList,endList;
    Bundle saveToBundle = new Bundle();
    ArrayAdapter<String> scheduleAdapter;
    ArrayAdapter<String> dosesAdapter;
    MultiAutoCompleteTextView medicine;
    int addNewFlag = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_medicine_reminder, container, false);

        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        scheduleList = getResources().getStringArray(R.array.scheduleDateList);
        dosesList = getResources().getStringArray(R.array.numberDosesList);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        type = session.getString("type",null);
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        endDateImg = (ImageView) view.findViewById(R.id.endDateImg);
        numberDoses = (Spinner) view.findViewById(R.id.numberDoses);
        scheduleDate = (Spinner) view.findViewById(R.id.scheduleDate);
        dateValue = (TextView) view.findViewById(R.id.dateValue);
        endDateValue = (TextView) view.findViewById(R.id.endDateValue);
        horizontalList = (HorizontalListView) view.findViewById(R.id.horizontalList);
        medicineReminderBtn = (CheckBox) view.findViewById(R.id.medicineReminderBtn);
        medicine = (MultiAutoCompleteTextView)view.findViewById(R.id.medicineValueEdit);
        doctorInstructionValue = (EditText) view.findViewById(R.id.doctorInstructionValue);
        saveTimeTable = (Button) view.findViewById(R.id.saveTimeTable);
        daysText = (TextView)view.findViewById(R.id.schedule_text);
        durationText = (TextView)view.findViewById(R.id.duration_text);
        final Bundle args = getArguments();
        medicinName = args.getString("medicinName");
        duration = (EditText) view.findViewById(R.id.duration_days);
        scheduleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, scheduleList);
        scheduleDate.setAdapter(scheduleAdapter);
        dosesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, dosesList);
        numberDoses.setAdapter(dosesAdapter);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String timeString = updateTimeString(hour, minute);
        String dateString = cal.get(Calendar.YEAR) + "-" + showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        startList = new ArrayList<String>();
        startList.add(dateString);
        startList.add(timeString);
        dateValue.setText(timeString + "  " + dateString);
        medicin_list = getResources().getStringArray(R.array.medicin_list);
        medicine.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, medicin_list));
        medicine.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicine.setText(medicinName);
        medicineReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    if (duration.getText().toString().equals("") && endDateValue.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please select Total Duration or End Date", Toast.LENGTH_LONG).show();
                    } else {
                        scheduleMedicineReminder();
                    }
                }
            }
        });
        if(args.getString("argument") != null){
            if(args.getString("argument").equals("NewMedicine")){
                addNewFlag = 1;
            }
        }
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Fragment fragment = new StartTimePicker();
                FragmentManager fragmentManger = getFragmentManager();
                saveToBundle = args;
                saveToBundle.putString("duration", duration.getText().toString());
                saveToBundle.putString("startTime",dateValue.getText().toString());
                fragment.setArguments(saveToBundle);
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });

        if(global.getDateString()!= null)
        {
            dateValue.setText(global.getDateString());
            String durationText = args.get("duration").toString();
            if(durationText!= null)
            {
                duration.setText(durationText);
                String scheduleType = scheduleDate.getSelectedItem().toString();
                int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                System.out.println("ScheduleTYpe:::::::::" + scheduleType);
                if (scheduleType.equals("Daily")) {
                    showScheduleDay(doses);
                } else if (scheduleType.equals("Weekly")) {
                    showScheduleWeek(doses);
                } else if (scheduleType.equals("Monthly")) {
                    showScheduleMonth(doses);
                }
            }

        }

        if(global.getEndDateString()!= null)
        {
            endDateValue.setText(global.getEndDateString());
            String durationText = args.get("duration").toString();
            if(durationText!=null)
            {
                duration.setText(durationText);

                 String scheduleType = scheduleDate.getSelectedItem().toString();
                 int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                 System.out.println("ScheduleTYpe:::::::::" + scheduleType);
                 if (scheduleType.equals("Daily")) {
                       showScheduleDay(doses);
                    } else if (scheduleType.equals("Weekly")) {
                        showScheduleWeek(doses);
                    } else if (scheduleType.equals("Monthly")) {
                        showScheduleMonth(doses);
                    }

            }
        }

        endDateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new EndTimePicker();
                FragmentManager fragmentManger = getFragmentManager();
                saveToBundle = args;
                saveToBundle.putString("duration", duration.getText().toString());
                saveToBundle.putString("endTime",endDateValue.getText().toString());
                fragment.setArguments(saveToBundle);
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });

        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endList = new ArrayList<String>();
                createDateArray();
                if (!duration.getText().toString().equals("")) {
                    int index = scheduleDate.getSelectedItemPosition() + 1;
                    int days = Integer.parseInt(duration.getText().toString().trim());
                    Calendar cal = Calendar.getInstance();
                    Date startDuration = getStringToDate(startList.get(1));
                    cal.setTime(startDuration);
                    if (index == 1) {
                        cal.add(Calendar.DAY_OF_MONTH, days);
                    } else if (index == 2) {
                        cal.add(Calendar.WEEK_OF_MONTH, days);
                    } else if (index == 3) {
                        cal.add(Calendar.MONTH, days);
                    }
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    String timeString = updateTimeString(hour, minute);
                    endList.add(cal.get(Calendar.YEAR) + "-" + showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
                    endList.add(timeString);
                    endDateValue.setText(timeString + "  " + cal.get(Calendar.YEAR) + "-" + showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
                    System.out.println(":::::::OnClick::::::");
                    String scheduleType = scheduleDate.getSelectedItem().toString();
                    int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                    System.out.println("ScheduleTYpe:::::::::" + scheduleType);
                    if (scheduleType.equals("Daily")) {
                        showScheduleDay(doses);
                    } else if (scheduleType.equals("Weekly")) {
                        showScheduleWeek(doses);
                    } else if (scheduleType.equals("Monthly")) {
                        showScheduleMonth(doses);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });

        scheduleDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = position + 1;
                System.out.println("Index::::::"+index);
                endList = new ArrayList<String>();
                if (!duration.getText().toString().equals(""))
                {
                    int days = Integer.parseInt(duration.getText().toString().trim());
                    Calendar cal = Calendar.getInstance();
                    if (index == 1) {
                        cal.add(Calendar.DAY_OF_MONTH, days);
                    } else if (index == 2) {
                        cal.add(Calendar.WEEK_OF_MONTH, days);
                    } else if (index == 3) {
                        cal.add(Calendar.MONTH, days);
                    }

                    System.out.println("Calculated Date:::::"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.WEEK_OF_MONTH)+"/"+cal.get(Calendar.YEAR));
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    String timeString = updateTimeString(hour, minute);
                    endDateValue.setText(timeString + "  " + cal.get(Calendar.YEAR) + "-" + showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
                    String scheduleType = scheduleDate.getSelectedItem().toString();
                    int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                    System.out.println("ScheduleTYpe:::::::::" + scheduleType);
                    if (scheduleType.equals("Daily")) {
                        showScheduleDay(doses);
                        daysText.setText("Daily");
                        durationText.setText("Days");
                    } else if (scheduleType.equals("Weekly")) {
                        showScheduleWeek(doses);
                        daysText.setText("Weekly");
                        durationText.setText("Weeks");
                    } else if (scheduleType.equals("Monthly")) {
                        showScheduleMonth(doses);
                        daysText.setText("Monthly");
                        durationText.setText("Months");
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Please Enter Duration",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        numberDoses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medicineReminderBtn.setChecked(false);
                if (count > 0) {
                    String eDate = endDateValue.getText().toString();
                    if (!eDate.equals("")) {
                        int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                        String scheduleType = scheduleDate.getSelectedItem().toString();
                        System.out.println("ScheduleTYpe:::::::::" + scheduleType);
                        if (scheduleType.equals("Daily")) {
                            showScheduleDay(doses);

                        } else if (scheduleType.equals("Weekly")) {
                            showScheduleWeek(doses);

                        } else if (scheduleType.equals("Monthly")) {
                            showScheduleMonth(doses);

                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter Duration", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    count = count + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        System.out.println("Condition of Reminder::::::::" + (global.getReminderVM() != null));
        if (global.getReminderVM() != null) {
            ReminderVM reminderVM = global.getReminderVM();
            if (reminderVM.startDate != null) {
                for (int i = 0; i < scheduleList.length; i++) {
                    if (reminderVM.schedule.equals(scheduleList[i])) {
                        scheduleDate.setSelection(i);
                    }
                }
                if(addNewFlag == 1){
                    dateValue.setText(reminderVM.startDate);
                }else {
                    dateValue.setText(reminderVM.alarmReminderVMList.get(0).startDate);
                    endDateValue.setText(reminderVM.alarmReminderVMList.get(0).endDate);
                    doctorInstructionValue.setText(reminderVM.alarmReminderVMList.get(0).doctorInstruction);
                    System.out.println("Duration::::::" + reminderVM.duration);
                    duration.setText("" + reminderVM.duration);
                    medicinReminderTables = reminderVM.alarmReminderVMList;
                    System.out.println("Size::::::::" + medicinReminderTables.size());
                    HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
                    horizontalList.setAdapter(hrAdapter);
                }
            }
        }


        saveTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderVM saveReminderVM = new ReminderVM();
                List<AlarmReminderVM> alarms = new ArrayList<AlarmReminderVM>();
                alarms = alarmList;
                System.out.println("MedicineReminders::::::::::" + medicinReminderTables.size());
                if (alarms == null)
                {
                    alarms = medicinReminderTables;
                }
                System.out.println("AlarmList::::::::::" + alarms.size());
                doctorId = Integer.parseInt(session.getString("doctorId", "0"));
                if(type.equalsIgnoreCase("Patient")){
                    patientId = session.getString("sessionID", null);
                }else{
                    patientId = session.getString("doctor_patientEmail", null);
                }

                appointmentTime = global.getAppointmentTime();
                appointmentDate = global.getAppointmentDate();
                System.out.println("Appointment Date:::::::" + appointmentDate);

                Bundle args = getArguments();
                String visitedDate = args.getString("visitedDate");
                String visitType = args.getString("visit");
                String referedBy = args.getString("referedBy");
                String symptomsValue = args.getString("symptomsValue");
                String diagnosisValue = args.getString("diagnosisValue");
                String testPrescribedValue = args.getString("testPrescribedValue");

                if (global.getReminderVM() != null) {
                    saveReminderVM.id = global.getReminderVM().id;
                } else {
                    saveReminderVM.id = null;
                }
                System.out.println("id::::::::::" + saveReminderVM.id);
                saveReminderVM.doctorId = doctorId;
                saveReminderVM.patientId = patientId;
                saveReminderVM.appointmentDate = appointmentDate;
                saveReminderVM.appointmentTime = appointmentTime;

                saveReminderVM.medicinName = medicinName;
                saveReminderVM.startDate = dateValue.getText().toString();
                saveReminderVM.endDate = endDateValue.getText().toString();
                if (!(duration.getText().toString().equals(""))) {
                    saveReminderVM.duration = Integer.parseInt(duration.getText().toString());
                } else {
                    saveReminderVM.duration = 0;
                }
                saveReminderVM.numberOfDoses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                saveReminderVM.schedule = scheduleDate.getSelectedItem().toString();
                saveReminderVM.doctorInstruction = doctorInstructionValue.getText().toString();
                saveReminderVM.visitDate = visitedDate;
                saveReminderVM.visitType = visitType;
                saveReminderVM.referredBy = referedBy;
                saveReminderVM.symptoms = symptomsValue;
                saveReminderVM.diagnosis = diagnosisValue;
                saveReminderVM.testsPrescribed = testPrescribedValue;
                System.out.println("medicinReminderTables = " + alarms.size());
                for(AlarmReminderVM vm : alarms){
                    vm.startDate = saveReminderVM.startDate;
                    vm.endDate = saveReminderVM.endDate;
                    vm.doses = saveReminderVM.numberOfDoses;
                    vm.duration = saveReminderVM.duration;
                    vm.doctorInstruction = saveReminderVM.doctorInstruction;
                }
                saveReminderVM.alarmReminderVMList = alarms;
                savePatientReminderData(saveReminderVM);

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


    public void scheduleMedicineReminder()
    {
        alarmList = global.getAlarmTime();
        System.out.println("Alarm List::::::"+alarmList.size());
        for(AlarmReminderVM vm : alarmList)
        {
            System.out.println("Date:::::::"+vm.getAlarmDate());
            System.out.println("Time1::::::"+vm.getTime1());
            String time = vm.getTime1().toString();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }
            time = vm.getTime2();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }
            time = vm.getTime3();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }
            time = vm.getTime4();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }
            time = vm.getTime5();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }
            time = vm.getTime6();
            if(time!=null)
            {
                Date alarmDate = getStringToDate(vm.getAlarmDate());
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(alarmDate);
                calendarDate = getStringToTime(time, calendarDate);
                System.out.println("Calendar Object::::::" + calendarDate.toString());
                setAlarm(calendarDate);
            }

        }

    }
    public void showScheduleMonth(int doses)
    {
        if(medicine.getText().equals("")){
            Toast.makeText(getActivity(),"Please Enter Medicine Name",Toast.LENGTH_SHORT).show();
        }else{
            String medicineName = medicine.getText().toString();
            createDateArray();
            Date startDate = getStringToDate(startList.get(1));
            Date endDate = getStringToDate(endList.get(1));
            System.out.println("endDate" + endDate);
            long diff = endDate.getTime() - startDate.getTime();
            if (diff < 0) {
                // Toast.makeText(getActivity(),"Please Select Proper Date",Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Calendar startCalenderDate = Calendar.getInstance();
                startCalenderDate.setTime(startDate);
                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
                System.out.println("StartDate:::::" + startCalenderDate.toString());
                Calendar endCalenderDate = Calendar.getInstance();
                endCalenderDate.setTime(endDate);
                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
                System.out.println("EndDate:::::" + endCalenderDate.toString());
                System.out.println("EndDate:::::" + endCalenderDate.toString());
                System.out.println("DOses::::::" + doses);
                Calendar startDateMain = startCalenderDate;
                Calendar currentDate = Calendar.getInstance();
                reminderDate = new ArrayList<ReminderDate>();
                if (doses == 1) {
                    int i = 30;
                    Calendar startDateLoop = startDateMain;
                    while (i <= dayDifference) {
                        ReminderDate rDate1 = new ReminderDate();
                        ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                        boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                        System.out.println("Condition:::::" + sameDay);
                        if (sameDay == true) {
                            startDateLoop = currentDate;
                        }
                        rDate1.setDate(startDateLoop.getTime());
                        String timeHHMM = null;
                        timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                        if (startDateLoop.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                        } else {
                            timeHHMM = timeHHMM + "PM";
                        }
                        TimeReminder t1 = new TimeReminder();
                        t1.setTime(timeHHMM);
                        timeReminder.add(t1);
                        rDate1.setTimes(timeReminder);
                        reminderDate.add(rDate1);
                        startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                        startDateLoop.set(Calendar.MINUTE, 0);
                        startDateLoop.set(Calendar.SECOND, 0);
                        startDateLoop.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }
                if (doses == 2) {
                    int i = 30;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 2; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_MONTH, 15);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }

                if (doses == 3) {
                    int i = 30;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 3; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 10);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }
                if (doses == 4) {
                    int i = 30;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 4; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 7);
                            startDateLoop.add(Calendar.MINUTE, 30);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }
                if (doses == 5) {
                    int i = 30;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 5; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 6);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }
                if (doses == 6) {
                    int i = 30;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 6; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 5);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
                        i = i + 30;
                    }
                }
                int switchCaseCount = 0;
                medicinReminderTables = new ArrayList<AlarmReminderVM>();
                System.out.println("Reminder Date::::::" + reminderDate.size());
                for (ReminderDate rDate : reminderDate) {
                    Date date = rDate.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = format.format(date);
                    switch (rDate.getTimes().size()) {
                        case 1:
                            switchCaseCount = 1;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), null, null, null, null, null,medicineName));
                            break;
                        case 2:
                            switchCaseCount = 2;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), null, null, null, null,medicineName));
                            break;
                        case 3:
                            switchCaseCount = 3;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), null, null, null,medicineName));
                            break;
                        case 4:
                            switchCaseCount = 4;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), null, null,medicineName));
                            break;
                        case 5:
                            switchCaseCount = 5;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), null,medicineName));
                            break;
                        case 6:
                            switchCaseCount = 6;
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), rDate.getTimes().get(5).getTime(),medicineName));
                            break;
                    }
                }
                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
                horizontalList.setAdapter(hrAdapter);

            }
        }
    }
    public void showScheduleWeek(int doses)
    {
        if(medicine.getText().equals("")){
            Toast.makeText(getActivity(),"Please Enter Medicine Name",Toast.LENGTH_SHORT).show();
        }else {
            String medicineName = medicine.getText().toString();
            createDateArray();
            Date startDate = getStringToDate(startList.get(1));
            Date endDate = getStringToDate(endList.get(1));
            System.out.println("endDate" + endDate);
            long diff = endDate.getTime() - startDate.getTime();
            if (diff < 0) {
                //  Toast.makeText(getActivity(),"Please Select Proper Date",Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Calendar startCalenderDate = Calendar.getInstance();
                startCalenderDate.setTime(startDate);
                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
                System.out.println("StartDate:::::" + startCalenderDate.toString());
                Calendar endCalenderDate = Calendar.getInstance();
                endCalenderDate.setTime(endDate);
                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
                System.out.println("EndDate:::::" + endCalenderDate.toString());
                System.out.println("DOses::::::" + doses);
                Calendar startDateMain = startCalenderDate;
                Calendar currentDate = Calendar.getInstance();
                reminderDate = new ArrayList<ReminderDate>();
                if (doses == 1) {
                    int i = 7;
                    Calendar startDateLoop = startDateMain;
                    while (i <= dayDifference) {
                        ReminderDate rDate1 = new ReminderDate();
                        ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                        boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                        System.out.println("Condition:::::" + sameDay);
                        if (sameDay == true) {
                            startDateLoop = currentDate;
                        }
                        rDate1.setDate(startDateLoop.getTime());
                        String timeHHMM = null;
                        timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                        if (startDateLoop.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                        } else {
                            timeHHMM = timeHHMM + "PM";
                        }
                        TimeReminder t1 = new TimeReminder();
                        t1.setTime(timeHHMM);
                        timeReminder.add(t1);
                        rDate1.setTimes(timeReminder);
                        reminderDate.add(rDate1);
                        startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                        startDateLoop.set(Calendar.MINUTE, 0);
                        startDateLoop.set(Calendar.SECOND, 0);
                        startDateLoop.add(Calendar.DAY_OF_MONTH, 7);
                        i = i + 7;
                    }
                }
                if (doses == 2) {
                    int i = 7;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 2; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 3);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
                        i = i + 7;
                    }
                }

                if (doses == 3) {
                    int i = 7;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 3; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 2);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
                        i = i + 7;
                    }
                }
                if (doses == 4) {
                    int i = 7;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 4; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
                            startDateLoop.add(Calendar.MINUTE, 75);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 6);
                        i = i + 7;
                    }
                }
                if (doses == 5) {
                    int i = 7;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 5; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
                            startDateLoop.add(Calendar.MINUTE, 40);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
                        i = i + 7;
                    }
                }
                if (doses == 6) {
                    int i = 7;
                    while (i <= dayDifference) {
                        Calendar startDateLoop = startDateMain;
                        for (int j = 1; j <= 6; j++) {
                            ReminderDate rDate1 = new ReminderDate();
                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
                            System.out.println("Condition:::::" + sameDay);
                            if (sameDay == true) {
                                startDateLoop = currentDate;
                            }
                            rDate1.setDate(startDateLoop.getTime());
                            String timeHHMM = null;
                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                            } else {
                                timeHHMM = timeHHMM + "PM";
                            }
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);
                            rDate1.setTimes(timeReminder);
                            reminderDate.add(rDate1);
                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
                            startDateLoop.set(Calendar.MINUTE, 0);
                            startDateLoop.set(Calendar.SECOND, 0);
                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
                        }
                        startDateMain.add(Calendar.DAY_OF_MONTH, 6);
                        i = i + 7;
                    }
                }
                medicinReminderTables = new ArrayList<AlarmReminderVM>();
                System.out.println("Reminder Date::::::" + reminderDate.size());
                for (ReminderDate rDate : reminderDate) {
                    Date date = rDate.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = format.format(date);
                    switch (rDate.getTimes().size()) {
                        case 1:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), null, null, null, null, null,medicineName));
                            break;
                        case 2:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), null, null, null, null,medicineName));
                            break;
                        case 3:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), null, null, null,medicineName));
                            break;
                        case 4:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), null, null,medicineName));
                            break;
                        case 5:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), null,medicineName));
                            break;
                        case 6:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), rDate.getTimes().get(5).getTime(),medicineName));
                            break;
                    }
                }
                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
                horizontalList.setAdapter(hrAdapter);
            }
        }
    }

    public void createDateArray()
    {
        System.out.println("Start Date::::::"+dateValue.getText().toString());
        System.out.println("End Date::::::"+endDateValue.getText().toString());
        String startString = dateValue.getText().toString();
        String endString = endDateValue.getText().toString();
        if((startString!=null)&&(endString!= null))
        {
            startList = new ArrayList<String>();
            endList = new ArrayList<String>();
            String[] startArray = dateValue.getText().toString().split("  ");
            String[] endArray = endDateValue.getText().toString().split("  ");
            int len = startArray.length;
            for (int i = 0; i < len; i++) {
                System.out.println("Start Date Array:::::" + startArray[i]);
                startList.add(startArray[i]);
            }
            len = endArray.length;
            for (int i = 0; i < len; i++) {
                System.out.println("End Date Array:::::" + endArray[i]);
                endList.add(endArray[i]);
            }

        }
        else
        {
            //Toast.makeText(getActivity(), "Please Select Proper Date", Toast.LENGTH_SHORT).show();
        }
    }



    public void showScheduleDay(int doses)
    {
        if(medicine.getText().equals("")){
            Toast.makeText(getActivity(),"Please Enter Medicine Name",Toast.LENGTH_SHORT).show();
        }else {
            String medicineName = medicine.getText().toString();
            createDateArray();
            Date startDate = getStringToDate(startList.get(1));
            Date endDate = getStringToDate(endList.get(1));
            System.out.println("endDate" + endDate);
            long diff = endDate.getTime() - startDate.getTime();
            if (diff < 0) {
                // Toast.makeText(getActivity(), "Please Select Proper Date", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("StartList:::::::" + startList.size());
                System.out.println("EndList:::::::" + endList.size());
                for (String s : startList) {
                    System.out.println("StartList:::::::" + s);
                }
                for (String s : endList) {
                    System.out.println("EndList:::::::" + s);
                }
                Calendar startCalenderDate = Calendar.getInstance();
                startCalenderDate.setTime(startDate);
                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
                System.out.println("StartDate:::::" + startCalenderDate.toString());
                Calendar endCalenderDate = Calendar.getInstance();
                endCalenderDate.setTime(endDate);
                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
                System.out.println("EndDate:::::" + endCalenderDate.toString());
                System.out.println("DOses::::::" + doses);
                Calendar startDateMain = startCalenderDate;
                reminderDate = new ArrayList<ReminderDate>();
                for (int i = 1; i <= dayDifference; i++) {
                    ReminderDate rDate1 = new ReminderDate();
                    System.out.println("Start Date::::::" + startDateMain.getTime().toString());
                    Calendar startDateLoop = startDateMain;
                    rDate1.setDate(startDateLoop.getTime());
                    ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();

                    if (doses == 1) {
                        for (int j = 1; j <= 1; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {

                                System.out.println("Time::::::" + timeHHMM);
                                TimeReminder t1 = new TimeReminder();
                                t1.setTime(timeHHMM);
                                timeReminder.add(t1);

                            } else {

                                break;
                            }

                            startDateLoop.add(Calendar.HOUR_OF_DAY, 12);

                        }
                    }
                    if (doses == 2) {
                        for (int j = 1; j <= 2; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
                                System.out.println("Time::::::" + timeHHMM);
                                TimeReminder t1 = new TimeReminder();
                                t1.setTime(timeHHMM);
                                timeReminder.add(t1);
                            } else {

                                break;
                            }
                            startDateLoop.add(Calendar.HOUR_OF_DAY, 6);

                        }
                    }
                    if (doses == 3) {
                        for (int j = 1; j <= 3; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
                                System.out.println("Time::::::" + timeHHMM);
                                TimeReminder t1 = new TimeReminder();
                                t1.setTime(timeHHMM);
                                timeReminder.add(t1);
                            } else {

                                break;
                            }
                            startDateLoop.add(Calendar.HOUR_OF_DAY, 4);

                        }
                    }
                    if (doses == 4) {
                        for (int j = 1; j <= 4; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
                                System.out.println("Time::::::" + timeHHMM);
                                TimeReminder t1 = new TimeReminder();
                                t1.setTime(timeHHMM);
                                timeReminder.add(t1);
                            } else {

                                break;
                            }
                            startDateLoop.add(Calendar.HOUR_OF_DAY, 3);

                        }
                    }
                    if (doses == 5) {
                        for (int j = 1; j <= 5; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 20) {
                                System.out.println("Time::::::" + timeHHMM);
                            } else {

                                break;
                            }
                            startDateLoop.add(Calendar.HOUR_OF_DAY, 2);
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);

                        }
                    }

                    if (doses == 6) {
                        for (int j = 1; j <= 6; j++) {
                            String timeHHMM = null;
                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
                                System.out.println("Time::::::" + timeHHMM);
                            } else {

                                break;
                            }
                            startDateLoop.add(Calendar.HOUR_OF_DAY, 2);
                            TimeReminder t1 = new TimeReminder();
                            t1.setTime(timeHHMM);
                            timeReminder.add(t1);

                        }
                    }
                    rDate1.setTimes(timeReminder);
                    reminderDate.add(rDate1);
                    startDateMain.add(Calendar.HOUR_OF_DAY, 12);
                }
                medicinReminderTables = new ArrayList<AlarmReminderVM>();
                System.out.println("Reminder Date::::::" + reminderDate.size());
                for (ReminderDate rDate : reminderDate) {
                    Date date = rDate.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = format.format(date);
                    switch (rDate.getTimes().size()) {
                        case 1:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), null, null, null, null, null,medicineName));
                            break;
                        case 2:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), null, null, null, null,medicineName));
                            break;
                        case 3:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), null, null, null,medicineName));
                            break;
                        case 4:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), null, null,medicineName));
                            break;
                        case 5:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), null,medicineName));
                            break;
                        case 6:
                            medicinReminderTables.add(new AlarmReminderVM(null, dateStr, rDate.getTimes().get(0).getTime(), rDate.getTimes().get(1).getTime(), rDate.getTimes().get(2).getTime(), rDate.getTimes().get(3).getTime(), rDate.getTimes().get(4).getTime(), rDate.getTimes().get(5).getTime(),medicineName));
                            break;
                    }
                }
                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
                horizontalList.setAdapter(hrAdapter);
            }
        }
    }


    public void setAlarm(Calendar calendar){
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmService.class);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,1);

        long trigerTime = calendar.getTimeInMillis();

        System.out.println("trigerTime = "+trigerTime);
        System.out.println("trigerTime current = "+Calendar.getInstance().getTimeInMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) trigerTime,
                intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, trigerTime, pendingIntent);
    }

    public Calendar getStringToTime(String time, Calendar calendar){
        String[] timeValue;
        timeValue = time.split(":");
        int hour1 = Integer.parseInt(timeValue[0].trim());
        int min1 = Integer.parseInt(timeValue[1].trim().split("[a-zA-Z ]+")[0]);
        calendar.set(Calendar.HOUR,hour1);
        calendar.set(Calendar.MINUTE, min1);

        String strAM_PM = timeValue[1].replaceAll("[0-9]","");
        if(strAM_PM.equals("AM")){
            calendar.set(Calendar.AM_PM, 0);
        }else{
            calendar.set(Calendar.AM_PM, 1);
        }
        return calendar;
    }

    public Date getStringToDate( String date){
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }


    public void  goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Medical Diary");

        if(type.equalsIgnoreCase("doctor"))
        {
            Fragment fragment = new DoctorAppointmentSummary();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
        else if(type.equalsIgnoreCase("patient"))
        {
            Fragment fragment = new PatientAppointmentInformation();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate();
        }

    };

    //Declaration


    public void updatedate(){
        if(checkStartDate){
            endDateValue.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        }else{
            dateValue.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    public void setDate(){

        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setTime(){
        new TimePickerDialog(getActivity(), timePickerListener, hour, minute,false).show();
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

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;
            updateTime(hour,minute);
        }
    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

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

        if(timeValue == null){

        }else{
            timeValue.setText(aTime);
        }
    }


    public void savePatientReminderData(ReminderVM saveReminderVM){

        System.out.println("saveReminderVM = "+saveReminderVM.visitDate);
        System.out.println("saveReminderVM.doctorId = "+saveReminderVM.doctorId);
        global.setReminderVM(saveReminderVM);
        if(addNewFlag == 1) {
            api.savePatientReminderDetails(saveReminderVM, new Callback<ReminderVM>() {
                @Override
                public void success(ReminderVM jsonObject, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    if(type.equalsIgnoreCase("Patient")){
                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
                        Fragment fragment = new PatientAppointmentInformation();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                    else {
                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
                        Fragment fragment = new DoctorAppointmentSummary();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        }else{
            System.out.println("I am Update Conidtion::::::::::::::::::::");
            api.updatePatientReminder(saveReminderVM,new Callback<ReminderVM>() {
                @Override
                public void success(ReminderVM reminderVM, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                    if(type.equalsIgnoreCase("Patient")){
                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
                        Fragment fragment = new PatientAppointmentInformation();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                    else {
                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
                        Fragment fragment = new DoctorAppointmentSummary();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
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


}
