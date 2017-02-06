package com.medico.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.medico.model.MedicineId;
import com.medico.model.PatientMedicine;
import com.mindnerves.meidcaldiary.AlarmService;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.AlarmReminderVM;
import Model.ReminderDate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class PatientMedicinReminder extends ParentFragment {

//    Integer doctorId;
//    MyApi api;
//    SharedPreferences session;
//    private int year;
//    private int month;
//    private int day;
//    private int hour;
//    private int minute;
//    int count = 0;
//    boolean checkStartDate = false;
    TextView dateValue, timeValue, endDateValue, daysText, durationText;
//    private long calStartDate, calEndDate;
    EditText duration, doctorInstructionValue,numberDoses, doses;
    ImageView calenderImg, endDateImg;
    Calendar calendar = Calendar.getInstance();
    Spinner  scheduleDate;
//    HorizontalListView horizontalList;

    TableLayout medicineSchedule;

    CheckBox medicineReminderBtn;
    CheckBox autoScheduleBtn;
    List<AlarmReminderVM> medicinReminderTables;
    List<AlarmReminderVM> alarmList;
//    String medicinName = "";
//    Global global;
    String[] scheduleList;
    String[] dosesList;
    String[] medicin_list = null;
    String type = null;
//    Button saveTimeTable;
//    String appointmentDate, appointmentTime, patientId;
    ArrayList<ReminderDate> reminderDate;
    ArrayList<String> startList, endList;
//    Bundle saveToBundle = new Bundle();
    ArrayAdapter<String> scheduleAdapter;
   //ArrayAdapter<String> dosesAdapter;
    public MultiAutoCompleteTextView medicineName;
    PatientMedicine patientMedicine = null;
//    AddPatientMedicineSummary patientMedicine;
//    private String appointMentId;
//    boolean addNewFlag = false;
//    private String state;
//    private String medicineName, medicineId, medicineStartDate, medicineEndDate, medicineReminder;
//    Button  back;

//    TextView show_global_tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_medicine_reminder, container, false);

//        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
//        back = (Button)getActivity().findViewById(R.id.back_button);
//
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBack();
//
//            }
//        });

//        show_global_tv.setText("Add Medicine");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//
//        scheduleList = getResources().getStringArray(R.array.scheduleDateList);
//        dosesList = getResources().getStringArray(R.array.numberDosesList);
//        global = (Global) getActivity().getApplicationContext();
//        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //Retrofit Initialization
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
        // type = session.getString("type", null);
//        type = session.getString("loginType", null);
//        appointMentId = session.getString("appointmentId", "");
        //Load Ui controls
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        endDateImg = (ImageView) view.findViewById(R.id.endDateImg);
//        numberDoses = (EditText) view.findViewById(R.id.no_of_doses_edit_box);
        doses = (EditText) view.findViewById(R.id.no_of_doses_edit_box);
//        numberDoses.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        scheduleDate = (Spinner) view.findViewById(R.id.scheduleDate);
        dateValue = (TextView) view.findViewById(R.id.dateValue);
        endDateValue = (TextView) view.findViewById(R.id.endDateValue);
//        horizontalList = (HorizontalListView) view.findViewById(R.id.horizontalList);
        medicineReminderBtn = (CheckBox) view.findViewById(R.id.medicineReminderBtn);
        autoScheduleBtn = (CheckBox) view.findViewById(R.id.auto_scheduleBtn);
        medicineName = (MultiAutoCompleteTextView) view.findViewById(R.id.medicineValueEdit);
        doctorInstructionValue = (EditText) view.findViewById(R.id.editText2);
//        saveTimeTable = (Button) view.findViewById(R.id.saveTimeTable);
        daysText = (TextView) view.findViewById(R.id.schedule_text);
        durationText = (TextView) view.findViewById(R.id.duration_text);
        duration = (EditText) view.findViewById(R.id.duration_days);

        medicineSchedule = (TableLayout)view.findViewById(R.id.displayLinear);

        //Read Arguments
//        final Bundle args = getArguments();
////        state = args.getString("state");
////        medicinName = args.getString("medicinName");
//        medicineId = args.getString("medicineId");
//        if(state!=null && !state.equalsIgnoreCase("") && state.equalsIgnoreCase("EDIT")){
//
//            getMedicineDetails(medicineId);
//        }
        //medicineName,medicineId,medicineStartDate,medicineEndDate,medicineReminder;
        // medicineName=args.getString("medicinName", "");
//        medicineEndDate = args.getString("medicineEndDate");
//
//        medicineStartDate = args.getString("medicineStartDate");
//        medicineReminder = args.getString("medicineReminder");
//        //check if new medicine
//        if (args.getString("argument") != null) {
//            if (args.getString("argument").equals("NewMedicine")) {
//                addNewFlag = true;
//            }
//        }

//        scheduleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, scheduleList);
//        scheduleDate.setAdapter(scheduleAdapter);
        /*dosesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, dosesList);
        numberDoses.setAdapter(dosesAdapter);*/

//        Calendar cal = Calendar.getInstance();
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int minute = cal.get(Calendar.MINUTE);
//        String timeString = updateTimeString(hour, minute);
//        String dateString = cal.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH);
//
//        startList = new ArrayList<String>();
//        startList.add(dateString);
//        startList.add(timeString);
//        dateValue.setText(timeString + "  " + dateString);
//        // dateValue.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(""+cal.getTimeInMillis()));
//        calStartDate = cal.getTimeInMillis();
//
//        medicin_list = getResources().getStringArray(R.array.medicin_list);
//        medicine.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, medicin_list));
//        medicine.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//        medicine.setText(medicinName);


//        medicineReminderBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((CheckBox) v).isChecked()) {
//                    if (duration.getText().toString().equals("") && endDateValue.getText().toString().equals("")) {
//                        Toast.makeText(getActivity(), "Please select Total Duration or End Date", Toast.LENGTH_LONG).show();
//                    } else {
//                        scheduleMedicineReminder();
//                    }
//                }
//            }
//        });
//
//        //not necessary
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(dateValue);
            }
        });

//        if (global.getDateString() != null) {
//            dateValue.setText(global.getDateString());
//            String durationText="";
//            if(args.get("duration")!=null)
//              durationText = args.get("duration").toString();
//            if (durationText != null) {
//                duration.setText(durationText);
//                String scheduleType = scheduleDate.getSelectedItem().toString();
//                int doses = Integer.parseInt(numberDoses.getText().toString());
//                System.out.println("ScheduleTYpe:::::::::" + scheduleType);
//                if (scheduleType.equals("Daily")) {
//                    showScheduleDay(doses);
//                } else if (scheduleType.equals("Weekly")) {
//                    showScheduleWeek(doses);
//                } else if (scheduleType.equals("Monthly")) {
//                    showScheduleMonth(doses);
//                }
//            }
//
//        }
//
//        if (global.getEndDateString() != null) {
//            endDateValue.setText(global.getEndDateString());
//            String durationText = args.get("duration").toString();
//            if (durationText != null) {
//                duration.setText(durationText);
//
//                String scheduleType = scheduleDate.getSelectedItem().toString();
//                int doses = Integer.parseInt(numberDoses.getText().toString());
//                System.out.println("ScheduleTYpe:::::::::" + scheduleType);
//                if (scheduleType.equals("Daily")) {
//                    showScheduleDay(doses);
//                } else if (scheduleType.equals("Weekly")) {
//                    showScheduleWeek(doses);
//                } else if (scheduleType.equals("Monthly")) {
//                    showScheduleMonth(doses);
//                }
//
//            }
//        }

        endDateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate(endDateValue);
            }
        });
//
//        duration.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                endList = new ArrayList<String>();
////                createDateArray();
//                if (!duration.getText().toString().equals("")) {
//                    int index = scheduleDate.getSelectedItemPosition() + 1;
//
//                    int days = Integer.parseInt(duration.getText().toString().trim());
//                    Calendar cal = Calendar.getInstance();
//                    Date startDuration = getStringToDate(startList.get(1));
//                    cal.setTime(startDuration);
//                    if (index == 1) {
//                        cal.add(Calendar.DAY_OF_MONTH, days);
//                    } else if (index == 2) {
//                        cal.add(Calendar.WEEK_OF_MONTH, days);
//                    } else if (index == 3) {
//                        cal.add(Calendar.MONTH, days);
//                    }
//                    int hour = cal.get(Calendar.HOUR_OF_DAY);
//                    int minute = cal.get(Calendar.MINUTE);
//                    String timeString = updateTimeString(hour, minute);
//                    endList.add(cal.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
//                    endList.add(timeString);
//                    endDateValue.setText(timeString + "  " + cal.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
////                    calEndDate = cal.getTimeInMillis();
//                    System.out.println(":::::::OnClick::::::");
//                    String scheduleType = scheduleDate.getSelectedItem().toString();
//                    int doses = Integer.parseInt(numberDoses.getText().toString());
//                    System.out.println("ScheduleTYpe:::::::::" + scheduleType);
////                    if (scheduleType.equals("Daily")) {
////                        showScheduleDay(doses);
////                    } else if (scheduleType.equals("Weekly")) {
////                        showScheduleWeek(doses);
////                    } else if (scheduleType.equals("Monthly")) {
////                        showScheduleMonth(doses);
////                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        scheduleDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int index = position + 1;
//                System.out.println("Index::::::" + index);
//                endList = new ArrayList<String>();
//                if (!duration.getText().toString().equals("")) {
//                    int days = Integer.parseInt(duration.getText().toString().trim());
//                    Calendar cal = Calendar.getInstance();
//                    if (index == 1) {
//                        cal.add(Calendar.DAY_OF_MONTH, days);
//                    } else if (index == 2) {
//                        cal.add(Calendar.WEEK_OF_MONTH, days);
//                    } else if (index == 3) {
//                        cal.add(Calendar.MONTH, days);
//                    }
//
//                    System.out.println("Calculated Date:::::" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.WEEK_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
//                    int hour = cal.get(Calendar.HOUR_OF_DAY);
//                    int minute = cal.get(Calendar.MINUTE);
//                    String timeString = updateTimeString(hour, minute);
//                    endDateValue.setText(timeString + "  " + cal.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH));
//                    String scheduleType = scheduleDate.getSelectedItem().toString();
//                    int doses = Integer.parseInt(numberDoses.getText().toString());
//                    System.out.println("ScheduleTYpe:::::::::" + scheduleType);
//                    if (scheduleType.equals("Daily")) {
//                        showScheduleDay(doses);
//                        daysText.setText("/ Day");
//                        durationText.setText("Days");
//                    } else if (scheduleType.equals("Weekly")) {
//                        showScheduleWeek(doses);
//                        daysText.setText("/ Week");
//                        durationText.setText("Weeks");
//                    } else if (scheduleType.equals("Monthly")) {
//                        showScheduleMonth(doses);
//                        daysText.setText("/ Month");
//                        durationText.setText("Months");
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Please Enter Duration", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        numberDoses.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                medicineReminderBtn.setChecked(false);
////                if (count > 0) {
//                    String eDate = endDateValue.getText().toString();
//                    if (!eDate.equals("")&& numberDoses.getText()!=null && !numberDoses.getText().toString().equalsIgnoreCase("")) {
//                        int doses = Integer.parseInt(numberDoses.getText().toString());
//                        String scheduleType = scheduleDate.getSelectedItem().toString();
//                        System.out.println("ScheduleTYpe:::::::::" + scheduleType);
////                        if (scheduleType.equals("Daily")) {
////                            showScheduleDay(doses);
////
////                        } else if (scheduleType.equals("Weekly")) {
////                            showScheduleWeek(doses);
////
////                        } else if (scheduleType.equals("Monthly")) {
////                            showScheduleMonth(doses);
////
////                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Please Enter Duration", Toast.LENGTH_SHORT).show();
//                    }
////                } else {
////                    count = count + 1;
////                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

       /* numberDoses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/

//        System.out.println("Condition of Reminder::::::::" + (global.getReminderVM() != null));
//        if (global.getReminderVM() != null) {
//            ReminderVM reminderVM = global.getReminderVM();
//            if (reminderVM.startDate != null) {
//                for (int i = 0; i < scheduleList.length; i++) {
//                    if (reminderVM.schedule.equals(scheduleList[i])) {
//                        scheduleDate.setSelection(i);
//                    }
//                }
//                if (addNewFlag) {
//                    dateValue.setText(reminderVM.startDate);
//                } else {
//                    dateValue.setText(reminderVM.alarmReminderVMList.get(0).startDate);
//                    endDateValue.setText(reminderVM.alarmReminderVMList.get(0).endDate);
//                doctorInstructionValue.setText(reminderVM.alarmReminderVMList.get(0).doctorInstruction);
//                    System.out.println("Duration::::::" + reminderVM.duration);
//                    duration.setText("" + reminderVM.duration);
//                    medicinReminderTables = reminderVM.alarmReminderVMList;
//                    System.out.println("Size::::::::" + medicinReminderTables.size());
//                    HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
//                    horizontalList.setAdapter(hrAdapter);
//                    horizontalList.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
//                }
//            }
//        }


//        saveTimeTable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // ReminderVM saveReminderVM = new ReminderVM();
////                patientMedicine = new AddPatientMedicineSummary();
//                List<AlarmReminderVM> alarms = new ArrayList<AlarmReminderVM>();
//                alarms = alarmList;
//                System.out.println("MedicineReminders::::::::::" + medicinReminderTables.size());
//                if (alarms == null) {
//                    alarms = medicinReminderTables;
//                }
//                System.out.println("AlarmList::::::::::" + alarms.size());
////                doctorId = Integer.parseInt(session.getString("id", "0"));
////                patientId = session.getString("patientId", "");
//               /* if(type.equalsIgnoreCase("Patient")){
//                    patientId = session.getString("sessionID", null);
//                }else{
//                    patientId = session.getString("doctor_patientEmail", null);
//                }*/
//
////                appointmentTime = global.getAppointmentTime();
////                appointmentDate = global.getAppointmentDate();
////                System.out.println("Appointment Date:::::::" + appointmentDate);
//
////                Bundle args = getArguments();
////                String visitedDate = args.getString("visitedDate");
////                String visitType = args.getString("visit");
////                String referedBy = args.getString("referedBy");
////                String symptomsValue = args.getString("symptomsValue");
////                String diagnosisValue = args.getString("diagnosisValue");
////                String testPrescribedValue = args.getString("testPrescribedValue");
//
//             /*   if (global.getReminderVM() != null) {
//                    saveReminderVM.id = global.getReminderVM().id;
//                } else {
//                    saveReminderVM.id = null;
//                }*/
//                //System.out.println("id::::::::::" + saveReminderVM.id);
//                /*
//                {"autoSchedule":1,"doctorInstruction":"morning-evening","dosesPerSchedule":2,"durationSchedule":4,"endDate":1457548200000,"medicinName":"crocine",
//                "reminder":1,"schedule":0,"startDate":1457164317533,"medicineSchedule":[{"scheduleTime":1457116200000},{"scheduleTime":1457116200000},
//                {"scheduleTime":1457375400000}],"appointmentId":584,"patientId":7,"loggedinUserId":111,"userType":1}
//                 */
//                //patientMedicine.doctorId = doctorId;
////                patientMedicine.setPatientId(patientId);
//                // patientMedicine.setAppointmentId(appointMentID); = appointmentDate;
//                //  patientMedicine.appointmentTime = appointmentTime;
//
//                patientMedicine.setMedicinName(medicineName.getText().toString());
//                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
//                try {
//                    patientMedicine.setStartDate(format.parse(dateValue.getText().toString()).getTime());
//                    patientMedicine.setEndDate(format.parse(endDateValue.getText().toString()).getTime());
//                }
//                catch(ParseException e) {
//                    e.printStackTrace();
//                }
//                patientMedicine.setDurationSchedule(Integer.parseInt(duration.getText().toString()));
//                patientMedicine.setDosesPerSchedule(Integer.parseInt(numberDoses.getText().toString()));
//                patientMedicine.setSchedule((byte)scheduleDate.getSelectedItemPosition());
//                patientMedicine.setDoctorInstruction(doctorInstructionValue.getText().toString());
//                patientMedicine.setAutoSchedule(new Integer(1).byteValue());
//                if (autoScheduleBtn.isChecked())
//                    patientMedicine.setAutoSchedule(new Integer(1).byteValue());
//                else
//                    patientMedicine.setAutoSchedule(new Integer(0).byteValue());
//                if (medicineReminderBtn.isChecked())
//                    patientMedicine.setReminder(new Integer(1).byteValue());
//                else
//                    patientMedicine.setReminder(new Integer(0).byteValue());
////                patientMedicine.setAppointmentId(appointMentId);
////                patientMedicine.setLoggedinUserId("" + doctorId);
////                patientMedicine.setUserType(UtilSingleInstance.getUserType(type));
////                patientMedicine.setPatientId(patientId);
//
//
//                System.out.println("medicinReminderTables = " + alarms.size());
//                List<PatientMedicine.MedicineSchedule> scheduleArray = new ArrayList<PatientMedicine.MedicineSchedule>();
////                for (AlarmReminderVM vm : alarms) {
////                    vm.startDate = patientMedicine.getStartDate();
////                    vm.endDate = patientMedicine.getEndDate();
////                    vm.doses = Integer.parseInt(patientMedicine.getDosesPerSchedule());
////                    vm.duration = Integer.parseInt(patientMedicine.getDurationSchedule());
////                    vm.doctorInstruction = patientMedicine.getDoctorInstruction();
////
////
////                }
//                if (reminderDate != null)
//                    for (int i = 0; i < reminderDate.size(); i++) {
//                        ReminderDate rm = reminderDate.get(i);
//                        scheduleArray.add(new PatientMedicine.MedicineSchedule(rm.getDate().getTime()));
//                    }
//
//                patientMedicine.setMedicineSchedule(scheduleArray);
////                savePatientReminderData(patientMedicine);
//
//            }
//        });
//
//
//        return view;
//    }

//    private String updateTimeString(int hours, int mins) {
//
//        String timeSet = "";
//        if (hours > 12) {
//            hours -= 12;
//            timeSet = "PM";
//        } else if (hours == 0) {
//            hours += 12;
//            timeSet = "AM";
//        } else if (hours == 12)
//            timeSet = "PM";
//        else
//            timeSet = "AM";
//
//        String minutes = "";
//        if (mins < 10)
//            minutes = "0" + mins;
//        else
//            minutes = String.valueOf(mins);
//        // Append in a StringBuilder
//        String aTime = new StringBuilder().append(hours).append(':')
//                .append(minutes).append(" ").append(timeSet).toString();
//
//        return aTime;
//    }
//
//
//    public void scheduleMedicineReminder() {
////        alarmList = global.getAlarmTime();
//        System.out.println("Alarm List::::::" + alarmList.size());
//        for (AlarmReminderVM vm : alarmList) {
//            System.out.println("Date:::::::" + vm.getAlarmDate());
//            System.out.println("Time1::::::" + vm.getTime1());
//            String time = vm.getTime1().toString();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//            time = vm.getTime2();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//            time = vm.getTime3();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//            time = vm.getTime4();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//            time = vm.getTime5();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//            time = vm.getTime6();
//            if (time != null) {
//                Date alarmDate = getStringToDate(vm.getAlarmDate());
//                Calendar calendarDate = Calendar.getInstance();
//                calendarDate.setTime(alarmDate);
//                calendarDate = getStringToTime(time, calendarDate);
//                System.out.println("Calendar Object::::::" + calendarDate.toString());
//                setAlarm(calendarDate);
//            }
//
//        }
//
//    }

//    public void showScheduleMonth(int doses) {
//        if (medicine.getText().equals("")) {
//            Toast.makeText(getActivity(), "Please Enter Medicine Name", Toast.LENGTH_SHORT).show();
//        } else {
//            String medicineName = medicine.getText().toString();
//            createDateArray();
//            Date startDate = getStringToDate(startList.get(1));
//            Date endDate = getStringToDate(endList.get(1));
//            System.out.println("endDate" + endDate);
//            long diff = endDate.getTime() - startDate.getTime();
//            if (diff < 0) {
//                // Toast.makeText(getActivity(),"Please Select Proper Date",Toast.LENGTH_SHORT).show();
//            } else {
//                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
//                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//                Calendar startCalenderDate = Calendar.getInstance();
//                startCalenderDate.setTime(startDate);
//                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
//                System.out.println("StartDate:::::" + startCalenderDate.toString());
//                Calendar endCalenderDate = Calendar.getInstance();
//                endCalenderDate.setTime(endDate);
//                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
//                System.out.println("EndDate:::::" + endCalenderDate.toString());
//                System.out.println("EndDate:::::" + endCalenderDate.toString());
//                System.out.println("DOses::::::" + doses);
//                Calendar startDateMain = startCalenderDate;
//                Calendar currentDate = Calendar.getInstance();
//                reminderDate = new ArrayList<ReminderDate>();
//
//                    int i = 30;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= doses; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            int numDays = calendar.getActualMaximum(Calendar.DATE);
//                            startDateLoop.add(Calendar.DAY_OF_MONTH,Math.round(numDays/doses) );
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 30);
//                        i = i + 30;
//                    }
//              //  }
//
//
//                int switchCaseCount = 0;
//                medicinReminderTables = new ArrayList<AlarmReminderVM>();
//                System.out.println("Reminder Date::::::" + reminderDate.size());
//                for (ReminderDate rDate : reminderDate) {
//                    Date date = rDate.getDate();
//                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
//                    String dateStr = format.format(date);
//                    List<String> timeList= new ArrayList<String>();
//                    for(int k=0; k< rDate.getTimes().size();k++){
//                        timeList.add( rDate.getTimes().get(k).getTime());
//
//                    }
//                    medicinReminderTables.add(new AlarmReminderVM(null, dateStr,timeList, medicineName));
//
//                }
//                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
//                horizontalList.setAdapter(hrAdapter);
//
//                Integer listSize = 50;
//
//                listSize = medicinReminderTables.size() * 50;
//                horizontalList.setLayoutParams(new RelativeLayout.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, listSize));
//
//            }
//        }
//    }
//
//    public void showScheduleWeek(int doses) {
//        if (medicine.getText().equals("")) {
//            Toast.makeText(getActivity(), "Please Enter Medicine Name", Toast.LENGTH_SHORT).show();
//        } else {
//            String medicineName = medicine.getText().toString();
//            createDateArray();
//            Date startDate = getStringToDate(startList.get(1));
//            Date endDate = getStringToDate(endList.get(1));
//            System.out.println("endDate" + endDate);
//            long diff = endDate.getTime() - startDate.getTime();
//            if (diff < 0) {
//                //  Toast.makeText(getActivity(),"Please Select Proper Date",Toast.LENGTH_SHORT).show();
//            } else {
//                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
//                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//                Calendar startCalenderDate = Calendar.getInstance();
//                startCalenderDate.setTime(startDate);
//                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
//                System.out.println("StartDate:::::" + startCalenderDate.toString());
//                Calendar endCalenderDate = Calendar.getInstance();
//                endCalenderDate.setTime(endDate);
//                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
//                System.out.println("EndDate:::::" + endCalenderDate.toString());
//                System.out.println("DOses::::::" + doses);
//                Calendar startDateMain = startCalenderDate;
//                Calendar currentDate = Calendar.getInstance();
//                reminderDate = new ArrayList<ReminderDate>();
//         /*       if (doses == 1) {
//                    int i = 7;
//                    Calendar startDateLoop = startDateMain;
//                    while (i <= dayDifference) {
//                        ReminderDate rDate1 = new ReminderDate();
//                        ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                        boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                        System.out.println("Condition:::::" + sameDay);
//                        if (sameDay == true) {
//                            startDateLoop = currentDate;
//                        }
//                        rDate1.setDate(startDateLoop.getTime());
//                        String timeHHMM = null;
//                        timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                        if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                            timeHHMM = timeHHMM + "AM";
//                        } else {
//                            timeHHMM = timeHHMM + "PM";
//                        }
//                        TimeReminder t1 = new TimeReminder();
//                        t1.setTime(timeHHMM);
//                        timeReminder.add(t1);
//                        rDate1.setTimes(timeReminder);
//                        reminderDate.add(rDate1);
//                        startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                        startDateLoop.set(Calendar.MINUTE, 0);
//                        startDateLoop.set(Calendar.SECOND, 0);
//                        startDateLoop.add(Calendar.DAY_OF_MONTH, 7);
//                        i = i + 7;
//                    }
//                }else{*/
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= doses; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                           // startDateLoop.add(Calendar.DAY_OF_WEEK, 3);
//                            int numDays = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK,Math.round(numDays/doses) );
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
//                        i = i + 7;
//                    }
//               // }
//              /*  if (doses == 2) {
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= 2; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK, 3);
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
//                        i = i + 7;
//                    }
//                }
//
//                if (doses == 3) {
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= 3; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK, 2);
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
//                        i = i + 7;
//                    }
//                }
//                if (doses == 4) {
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= 4; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
//                            startDateLoop.add(Calendar.MINUTE, 75);
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 6);
//                        i = i + 7;
//                    }
//                }
//                if (doses == 5) {
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= 5; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
//                            startDateLoop.add(Calendar.MINUTE, 40);
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 7);
//                        i = i + 7;
//                    }
//                }
//                if (doses == 6) {
//                    int i = 7;
//                    while (i <= dayDifference) {
//                        Calendar startDateLoop = startDateMain;
//                        for (int j = 1; j <= 6; j++) {
//                            ReminderDate rDate1 = new ReminderDate();
//                            ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                            boolean sameDay = currentDate.get(Calendar.YEAR) == startDateLoop.get(Calendar.YEAR) &&
//                                    currentDate.get(Calendar.DAY_OF_YEAR) == startDateLoop.get(Calendar.DAY_OF_YEAR);
//                            System.out.println("Condition:::::" + sameDay);
//                            if (sameDay == true) {
//                                startDateLoop = currentDate;
//                            }
//                            rDate1.setDate(startDateLoop.getTime());
//                            String timeHHMM = null;
//                            timeHHMM = startDateLoop.get(Calendar.HOUR) + ":" + startDateLoop.get(Calendar.MINUTE);
//                            if (startDateLoop.get(Calendar.AM_PM) == 0) {
//                                timeHHMM = timeHHMM + "AM";
//                            } else {
//                                timeHHMM = timeHHMM + "PM";
//                            }
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//                            rDate1.setTimes(timeReminder);
//                            reminderDate.add(rDate1);
//                            startDateLoop.set(Calendar.HOUR_OF_DAY, 9);
//                            startDateLoop.set(Calendar.MINUTE, 0);
//                            startDateLoop.set(Calendar.SECOND, 0);
//                            startDateLoop.add(Calendar.DAY_OF_WEEK, 1);
//                        }
//                        startDateMain.add(Calendar.DAY_OF_MONTH, 6);
//                        i = i + 7;
//                    }
//                }*/
//                medicinReminderTables = new ArrayList<AlarmReminderVM>();
//                System.out.println("Reminder Date::::::" + reminderDate.size());
//                for (ReminderDate rDate : reminderDate) {
//                    Date date = rDate.getDate();
//                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
//                    String dateStr = format.format(date);
//                    List<String> timeList= new ArrayList<String>();
//                    for(int k=0; k < rDate.getTimes().size();k++){
//                        timeList.add( rDate.getTimes().get(k).getTime());
//
//                    }
//                    medicinReminderTables.add(new AlarmReminderVM(null, dateStr,timeList, medicineName));
//                }
//                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
//                horizontalList.setAdapter(hrAdapter);
//            }
//        }
//    }
//
//    public void createDateArray() {
//        System.out.println("Start Date::::::" + dateValue.getText().toString());
//        System.out.println("End Date::::::" + endDateValue.getText().toString());
//        String startString = dateValue.getText().toString();
//        String endString = endDateValue.getText().toString();
//        if ((startString != null) && (endString != null)) {
//            startList = new ArrayList<String>();
//            endList = new ArrayList<String>();
//            String[] startArray = dateValue.getText().toString().split("  ");
//            String[] endArray = endDateValue.getText().toString().split("  ");
//            int len = startArray.length;
//            for (int i = 0; i < len; i++) {
//                System.out.println("Start Date Array:::::" + startArray[i]);
//                startList.add(startArray[i]);
//            }
//            len = endArray.length;
//            for (int i = 0; i < len; i++) {
//                System.out.println("End Date Array:::::" + endArray[i]);
//                endList.add(endArray[i]);
//            }
//
//        } else {
//            //Toast.makeText(getActivity(), "Please Select Proper Date", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void showScheduleDay(int doses)
//    {
////            String medicineName = medicine.getText().toString();
//            createDateArray();//can bre removed no need to use arrays.
//               if(startList!=null && startList.size()>=2 && endList!=null && endList.size()>=2){
//            Date startDate = getStringToDate(startList.get(1));
//            Date endDate = getStringToDate(endList.get(1));
//            System.out.println("endDate" + endDate);
//            long diff = endDate.getTime() - startDate.getTime();
//            if (diff < 0) {
//                // Toast.makeText(getActivity(), "Please Select Proper Date", Toast.LENGTH_SHORT).show();
//            } else {
//                System.out.println("Difference:::::::" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
//                int dayDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//                System.out.println("StartList:::::::" + startList.size());
//                System.out.println("EndList:::::::" + endList.size());
//                for (String s : startList) {
//                    System.out.println("StartList:::::::" + s);
//                }
//                for (String s : endList) {
//                    System.out.println("EndList:::::::" + s);
//                }
//                Calendar startCalenderDate = Calendar.getInstance();
//                startCalenderDate.setTime(startDate);
//                startCalenderDate = getStringToTime(startList.get(0), startCalenderDate);
//                System.out.println("StartDate:::::" + startCalenderDate.toString());
//                Calendar endCalenderDate = Calendar.getInstance();
//                endCalenderDate.setTime(endDate);
//                endCalenderDate = getStringToTime(endList.get(0), endCalenderDate);
//                System.out.println("EndDate:::::" + endCalenderDate.toString());
//                System.out.println("DOses::::::" + doses);
//                Calendar startDateMain = startCalenderDate;
//                reminderDate = new ArrayList<ReminderDate>();
//                for (int i = 1; i <= dayDifference; i++) {
//                    ReminderDate rDate1 = new ReminderDate();
//                    System.out.println("Start Date::::::" + startDateMain.getTime().toString());
//                    Calendar startDateLoop = startDateMain;
//                    rDate1.setDate(startDateLoop.getTime());
//                    ArrayList<TimeReminder> timeReminder = new ArrayList<TimeReminder>();
//
//                 /*   if (doses == 1) {
//                        for (int j = 1; j <= 1; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//
//                                System.out.println("Time::::::" + timeHHMM);
//                                TimeReminder t1 = new TimeReminder();
//                                t1.setTime(timeHHMM);
//                                timeReminder.add(t1);
//
//                            } else {
//
//                                break;
//                            }
//
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 12);
//
//                        }
//                    }else{*/
//                        for (int j = 1; j <= doses; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//                                System.out.println("Time::::::" + timeHHMM);
//                                TimeReminder t1 = new TimeReminder();
//                                t1.setTime(timeHHMM);
//                                timeReminder.add(t1);
//                            } else {
//
//                                break;
//                            }
//                           // startDateLoop.add(Calendar.HOUR_OF_DAY, 6);
//                            int numDays = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
//                            startDateLoop.add(Calendar.HOUR_OF_DAY,Math.round(12/doses) );
//
//                        }
//                   // }
//                   /* if (doses == 2) {
//                        for (int j = 1; j <= 2; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//                                System.out.println("Time::::::" + timeHHMM);
//                                TimeReminder t1 = new TimeReminder();
//                                t1.setTime(timeHHMM);
//                                timeReminder.add(t1);
//                            } else {
//
//                                break;
//                            }
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 6);
//
//                        }
//                    }
//                    if (doses == 3) {
//                        for (int j = 1; j <= 3; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//                                System.out.println("Time::::::" + timeHHMM);
//                                TimeReminder t1 = new TimeReminder();
//                                t1.setTime(timeHHMM);
//                                timeReminder.add(t1);
//                            } else {
//
//                                break;
//                            }
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 4);
//
//                        }
//                    }
//                    if (doses == 4) {
//                        for (int j = 1; j <= 4; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//                                System.out.println("Time::::::" + timeHHMM);
//                                TimeReminder t1 = new TimeReminder();
//                                t1.setTime(timeHHMM);
//                                timeReminder.add(t1);
//                            } else {
//
//                                break;
//                            }
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 3);
//
//                        }
//                    }
//                    if (doses == 5) {
//                        for (int j = 1; j <= 5; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 20) {
//                                System.out.println("Time::::::" + timeHHMM);
//                            } else {
//
//                                break;
//                            }
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 2);
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//
//                        }
//                    }
//
//                    if (doses == 6) {
//                        for (int j = 1; j <= 6; j++) {
//                            String timeHHMM = null;
//                            timeHHMM = updateTimeString(startDateLoop.get(Calendar.HOUR_OF_DAY), startDateLoop.get(Calendar.MINUTE));
//                            if (startDateLoop.get(Calendar.HOUR_OF_DAY) <= 21) {
//                                System.out.println("Time::::::" + timeHHMM);
//                            } else {
//
//                                break;
//                            }
//                            startDateLoop.add(Calendar.HOUR_OF_DAY, 2);
//                            TimeReminder t1 = new TimeReminder();
//                            t1.setTime(timeHHMM);
//                            timeReminder.add(t1);
//
//                        }
//                    }*/
//                    rDate1.setTimes(timeReminder);
//                    reminderDate.add(rDate1);
//                    startDateMain.add(Calendar.HOUR_OF_DAY, 12);
//                }
//                medicinReminderTables = new ArrayList<AlarmReminderVM>();
//                System.out.println("Reminder Date::::::" + reminderDate.size());
//                for (ReminderDate rDate : reminderDate) {
//                    Date date = rDate.getDate();
//                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
//                    String dateStr = format.format(date);
//                    List<String> timeList= new ArrayList<String>();
//                    for(int i=0; i < rDate.getTimes().size();i++){
//                        timeList.add( rDate.getTimes().get(i).getTime());
//
//                    }
//                    medicinReminderTables.add(new AlarmReminderVM(null, dateStr,timeList, medicineName));
//                }
//                HorizontalListAdapter hrAdapter = new HorizontalListAdapter(getActivity(), medicinReminderTables);
//                horizontalList.setAdapter(hrAdapter);
//            }
//        }else{
//                   Toast.makeText(getActivity(), "Select duration first !!!", Toast.LENGTH_LONG).show();
//        }
//    }
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


//    public void goToBack() {
//        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
//        globalTv.setText(getActivity().getResources().getString(R.string.app_name));
//
//        if (type.equalsIgnoreCase("doctor")) {
//            Fragment fragment = new DoctorAppointmentInformation();
//            FragmentManager fragmentManger = getFragmentManager();
//            fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//
//
//           /* final Button back = (Button) getActivity().findViewById(R.id.back_button);
//            back.setVisibility(View.INVISIBLE);*/
//        } else if (type.equalsIgnoreCase("patient")) {
//            Fragment fragment = new PatientAppointmentInformation();
//            FragmentManager fragmentManger = getFragmentManager();
//            fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            final Button back = (Button) getActivity().findViewById(R.id.back_button);
//            back.setVisibility(View.INVISIBLE);
//        }
//    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updatedate();
        }

    };

    //Declaration
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        int doctorId = bundle.getInt(DOCTOR_ID);
        int patientId = bundle.getInt(PATIENT_ID);
        Integer medicineId = bundle.getInt("medicineId");
        int appointMentId = bundle.getInt(APPOINTMENT_ID);
        if(medicineId > 0) {
            api.getPatientMedicine(new MedicineId(medicineId), new Callback<PatientMedicine>() {
                @Override
                public void success(PatientMedicine medicine, Response response)
                {
                    patientMedicine = medicine;
                    medicineName.setText(medicine.getMedicinName());
                    doses.setText("" + medicine.dosesPerSchedule);
                    scheduleDate.setSelection(medicine.getSchedule());
                    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                    dateValue.setText(format.format(medicine.getStartDate()));
                    endDateValue.setText(format.format(medicine.getEndDate()));
                    doctorInstructionValue.setText(medicine.getDoctorInstruction());
                    duration.setText("" + medicine.getDurationSchedule());
                    doctorInstructionValue.setText(medicine.getDoctorInstruction());
                    medicineReminderBtn.setChecked(medicine.getReminder().byteValue() ==1);
                    autoScheduleBtn.setChecked(medicine.getReminder().byteValue()==1);
                    List<PatientMedicine.MedicineSchedule> schedule = medicine.getMedicineSchedule();
                    setDailySchedule(patientMedicine);

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

        }
    }

    private void setDailySchedule(PatientMedicine medicine)
    {

        long timeBetweenTwoDoses = 24 * 60 * 60 * 1000 / medicine.getDosesPerSchedule();
        long startTime = medicine.getStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        calendar.add(Calendar.HOUR,3);
//        calendar.set(Calendar.AM_PM, Calendar.PM);
        startTime = calendar.getTimeInMillis();
        patientMedicine.setStartDate(startTime);

        long[] schedule = new long[ medicine.dosesPerSchedule * medicine.getDurationSchedule()];
        for(int i = 0; i < schedule.length; i++)
        {
            schedule[i] = startTime + i * timeBetweenTwoDoses;
        }
        long endTine = startTime + (schedule.length - 1 ) * timeBetweenTwoDoses;
        String dateString = null;
        List<Long> dates = new ArrayList<Long>();
        for(int i = 0; i < schedule.length; i++)
        {
            DateFormat dateFormat = DateFormat.getDateInstance();
            String tempDate = dateFormat.format(new Date(schedule[i]));
            DateFormat dateFormat1 = DateFormat.getDateInstance();
            String tempDate1 = dateFormat1.format(new Date(schedule[i]));
            if(dateString == null )
            {
                dates.add(new Long(schedule[i]));
                dateString = tempDate;
            }
            else if (dateString.equalsIgnoreCase(tempDate) == false)
            {
                dates.add(new Long(schedule[i]));
                dateString = tempDate;
            }
            else
                dates.set(dates.size()-1, schedule[i]);
        }

        long date[] = new long[dates.size()];
        int i = 0;
        for(Long list : dates)
        {
            date[i] = list.longValue();
            i++;
        }
        createTable(date, schedule, timeBetweenTwoDoses);
    }
    private void setWeeklySchedule()
    {

    }
    private void setMontlySchedule()
    {

    }
    private void setYearlySchedule()
    {

    }

    private void createTable(long date[], long schedule[], long durationBetweenDoses)
    {
        Activity activity = getActivity();
        TableRow row= new TableRow(activity);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat datetime = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        for(int i = 0; i < date.length; i++)
        {
            TextView textView = new TextView(activity);
            textView.setText(dateFormat.format(date[i]));
            textView.setBackgroundResource(R.drawable.medicine_schedule);
            row.addView(textView,i,lp);
        }
        medicineSchedule.addView(row);
        medicineSchedule.setStretchAllColumns(true);
        TableRow medicineSch[] = new TableRow[patientMedicine.dosesPerSchedule];

        long startDateTime = patientMedicine.startDate;
        Calendar startDateEndTime = Calendar.getInstance();
        startDateEndTime.setTime(new Date(startDateTime));
        startDateEndTime.set(Calendar.HOUR_OF_DAY,23);
        startDateEndTime.set(Calendar.MINUTE, 59);
        startDateEndTime.set(Calendar.SECOND, 59);
        long startDateEndTIme = startDateEndTime.getTime().getTime();
        long delta = startDateEndTIme - startDateTime;
        String StartDateEndTimeString = datetime.format(startDateEndTIme);
        String startDateTimeString = datetime.format(startDateTime);
        int StartDayFirstIndex = medicineSch.length - Math.round(delta/durationBetweenDoses) - 1;
        for(int i = 0; i < medicineSch.length; i++)
        {
            medicineSch[i] = new TableRow(activity);
            medicineSchedule.addView(medicineSch[i]);
            if(i < StartDayFirstIndex)
                medicineSch[i].addView(new TextView(activity));
        }
        int column = 0;
        DateFormat dateFor = DateFormat.getTimeInstance(DateFormat.SHORT);
        for(int i = 0; i < schedule.length; i++)
        {
            TextView textView = new TextView(activity);
            textView.setText(dateFor.format(schedule[i]));
//            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.medicine_schedule);
            medicineSch[StartDayFirstIndex].addView(textView);
            StartDayFirstIndex++;

            if(StartDayFirstIndex >= patientMedicine.dosesPerSchedule) {
                StartDayFirstIndex = 0;
                column++;
            }
        }
        medicineSchedule.requestLayout();
    }



//    public void updatedate() {
//        if (checkStartDate) {
//            calStartDate = calendar.getTimeInMillis();
//            endDateValue.setText(calendar.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
//        } else {
//            calEndDate = calendar.getTimeInMillis();
//            dateValue.setText(calendar.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
//        }
//    }

//    public void setDate() {
//
//        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    public void setTime() {
//        new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false).show();
//    }


//    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
//
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
//            // TODO Auto-generated method stub
//            hour = hourOfDay;
//            minute = minutes;
//            updateTime(hour, minute);
//        }
//    };

//    private static String utilTime(int value) {
//
//        if (value < 10)
//            return "0" + String.valueOf(value);
//        else
//            return String.valueOf(value);
//    }

    // Used to convert 24hr format to 12hr format with AM/PM values
//    private void updateTime(int hours, int mins) {
//
//        String timeSet = "";
//        if (hours > 12) {
//            hours -= 12;
//            timeSet = "PM";
//        } else if (hours == 0) {
//            hours += 12;
//            timeSet = "AM";
//        } else if (hours == 12)
//            timeSet = "PM";
//        else
//            timeSet = "AM";
//
//        String minutes = "";
//        if (mins < 10)
//            minutes = "0" + mins;
//        else
//            minutes = String.valueOf(mins);
//        // Append in a StringBuilder
//        String aTime = new StringBuilder().append(hours).append(':')
//                .append(minutes).append(" ").append(timeSet).toString();
//
//        if (timeValue == null) {
//
//        } else {
//            timeValue.setText(aTime);
//        }
//    }


//    public void savePatientReminderData(AddPatientMedicineSummary saveReminderVM) {
//
//        // System.out.println("saveReminderVM = "+saveReminderVM.visitDate);
//        // System.out.println("saveReminderVM.doctorId = "+saveReminderVM.doctorId);
//        // global.setReminderVM(saveReminderVM);
//
//
//        if (addNewFlag) {
//            api.addPatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
//                @Override
//                public void success(ResponseCodeVerfication jsonObject, Response response) {
//                    System.out.println("Response::::::" + response.getStatus());
//                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
//                    if (type.equalsIgnoreCase("Patient")) {
//                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
//                        Fragment fragment = new PatientAppointmentInformation();
//                        FragmentManager fragmentManger = getFragmentManager();
//                        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    } else {
//                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
//                        Fragment fragment = new DoctorAppointmentSummary();
//                        FragmentManager fragmentManger = getFragmentManager();
//                        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                    error.printStackTrace();
//                }
//            });
//        } else {
//            System.out.println("I am Update Conidtion::::::::::::::::::::");
//            api.updatePatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
//                @Override
//                public void success(ResponseCodeVerfication reminderVM, Response response) {
//                    System.out.println("Response::::::" + response.getStatus());
//                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
//                    if (type.equalsIgnoreCase("Patient")) {
//                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
//                        Fragment fragment = new PatientAppointmentInformation();
//                        FragmentManager fragmentManger = getFragmentManager();
//                        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    } else {
//                        getFragmentManager().beginTransaction().remove(PatientMedicinReminder.this).commit();
//                        Fragment fragment = new DoctorAppointmentSummary();
//                        FragmentManager fragmentManger = getFragmentManager();
//                        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                    error.printStackTrace();
//                }
//            });
//        }
//    }
//
//    public void getMedicineDetails(String medicineId){
////MedicineId medicineId, Callback<ResponseCodeVerfication> response
//        MedicineId medicinei=new MedicineId(medicineId);
//        api.getMedicineDetails(medicinei, new Callback<AddPatientMedicineSummary>() {
//            @Override
//            public void success(AddPatientMedicineSummary clinics, Response response) {
//
//                //get medicine details and show on screen
//
//                // {"autoSchedule":1,"doctorInstruction":"evening","dosesPerSchedule":1,"durationSchedule":2,"endDate":1457721000000,
//                // "medicinName":"Paracitamal, ","reminder":1,"schedule":0,"startDate":1457616528000,"medicineSchedule":[],
//                // "medicineId":19,"appointmentId":null,"patientId":null,"loggedinUserId":null,"userType":0}
//                medicine.setText(clinics.getMedicinName());
//
//                numberDoses.setSelection(Integer.parseInt(clinics.getDosesPerSchedule()));
//                scheduleDate.setSelection(Integer.parseInt(clinics.getSchedule()));
//                dateValue.setText(UtilSingleInstance.getDateFormattedInStringFormatUsingLongForMedicinDetails(clinics.getStartDate()));
//                endDateValue.setText(UtilSingleInstance.getDateFormattedInStringFormatUsingLongForMedicinDetails(clinics.getEndDate()));
//                //horizontalList = (HorizontalListView) view.findViewById(R.id.horizontalList);
//
//                doctorInstructionValue.setText(clinics.getDoctorInstruction());
//                if(clinics!=null && clinics.getDurationSchedule()!=null)
//                duration.setText(""+Integer.parseInt(clinics.getDurationSchedule()));
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                error.printStackTrace();
//            }
//        });
//    }

    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                dateField.setText(format.format(date));
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

}
