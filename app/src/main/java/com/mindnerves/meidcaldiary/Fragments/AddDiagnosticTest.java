package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
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
import Model.AddDiagnosisTestRequest;
import Model.AddPatientMedicineSummary;
import Model.AlarmReminderVM;
import Model.Clinic;
import Model.MedicineId;
import Model.MedicineSchedule;
import Model.PatientTestId;
import Model.PersonID;
import Model.ReminderDate;
import Model.ReminderVM;
import Model.ResponseCodeVerfication;
import Model.TestDetails;
import Model.TimeReminder;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 07-Apr-15.
 */
//add test
public class AddDiagnosticTest extends Fragment {

    MyApi api;
    SharedPreferences session;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    int count = 0;
    boolean checkStartDate = false;
    TextView dateValue, timeValue, endDateValue, daysText, durationText;
    private long calStartDate, calEndDate;
    EditText duration, doctorInstructionValue;
    ImageView calenderImg, endDateImg;
    Calendar calendar = Calendar.getInstance();
    Spinner numberDoses, scheduleDate;
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
    String appointmentDate, appointmentTime, patientId;
    ArrayList<ReminderDate> reminderDate;
    ArrayList<String> startList, endList;
    Bundle saveToBundle = new Bundle();
    ArrayAdapter<String> scheduleAdapter;
    ArrayAdapter<String> dosesAdapter;
    public MultiAutoCompleteTextView medicine, textNameEdit;
    AddDiagnosisTestRequest addPatientMedicineSummary;
    private String appointMentId;
    boolean addNewFlag = false;
    private String state, testId, clinicId, doctorId;
    private String medicineName, medicineId, medicineStartDate, medicineEndDate, medicineReminder, referredBy;
    TextView show_global_tv;//,refferredby;
    Button  back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_diagnostic_test, container, false);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        show_global_tv.setText("Add Dignostic Test");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });


        scheduleList = getResources().getStringArray(R.array.scheduleDateList);
        //  dosesList = getResources().getStringArray(R.array.numberDosesList);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        // type = session.getString("type", null);
        type = session.getString("loginType", null);
        doctorId = session.getString("id", "0");
        appointMentId = session.getString("appointmentId", "");
        //Load Ui controls
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        endDateImg = (ImageView) view.findViewById(R.id.endDateImg);
        numberDoses = (Spinner) view.findViewById(R.id.numberDoses);
        //refferredby = (TextView) view.findViewById(R.id.refferredby);
        scheduleDate = (Spinner) view.findViewById(R.id.scheduleDate);
        dateValue = (TextView) view.findViewById(R.id.dateValue);
        endDateValue = (TextView) view.findViewById(R.id.endDateValue);
        horizontalList = (HorizontalListView) view.findViewById(R.id.horizontalList);
        medicineReminderBtn = (CheckBox) view.findViewById(R.id.medicineReminderBtn);
        medicine = (MultiAutoCompleteTextView) view.findViewById(R.id.medicineValueEdit);
        doctorInstructionValue = (EditText) view.findViewById(R.id.doctorInstructionValue);
        textNameEdit = (MultiAutoCompleteTextView) view.findViewById(R.id.testnameEdit);

        saveTimeTable = (Button) view.findViewById(R.id.saveTimeTable);
        daysText = (TextView) view.findViewById(R.id.schedule_text);
        durationText = (TextView) view.findViewById(R.id.duration_text);
        duration = (EditText) view.findViewById(R.id.duration_days);

        //Read Arguments
        final Bundle args = getArguments();
        state = args.getString("state");
        medicinName = args.getString("medicinName");
        medicineId = args.getString("medicineId");
        referredBy = args.getString("referedBy");
        dosesList = new String[]{referredBy};
        //dosesList[0]=referredBy
        // refferredby.setText(referredBy);
        testId = args.getString("testId");
        clinicId = args.getString("clinicId");

        //medicineName,medicineId,medicineStartDate,medicineEndDate,medicineReminder;
        // medicineName=args.getString("medicinName", "");
        medicineEndDate = args.getString("medicineEndDate");

        medicineStartDate = args.getString("medicineStartDate");
        medicineReminder = args.getString("medicineReminder");

        //check if new medicine
        if (args.getString("argument") != null) {
            if (args.getString("argument").equals("NewTest")) {
                addNewFlag = true;
            } else
                addNewFlag = false;
        }
        if (state != null && !state.equalsIgnoreCase("") && state.equalsIgnoreCase("EDIT") && !addNewFlag) {
            getTestDetails(testId);
        }else{
            dosesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, dosesList);
            numberDoses.setAdapter(dosesAdapter);
            getAllClinics();
        }


        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String timeString = updateTimeString(hour, minute);
        String dateString = cal.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.DAY_OF_MONTH);

        startList = new ArrayList<String>();
        startList.add(dateString);
        startList.add(timeString);
        dateValue.setText(timeString + "  " + dateString);

        calStartDate = cal.getTimeInMillis();

        medicin_list = getResources().getStringArray(R.array.medicin_list);
        medicine.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, medicin_list));
        medicine.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        medicine.setText(medicinName);



        medicineReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    if (duration.getText().toString().equals("") && endDateValue.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please select Total Duration or End Date", Toast.LENGTH_LONG).show();
                    } else {
                        // scheduleMedicineReminder();
                    }
                }
            }
        });

        //not necessary
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StartTimePicker();
                FragmentManager fragmentManger = getFragmentManager();
                saveToBundle = args;
                saveToBundle.putString("duration", duration.getText().toString());
                saveToBundle.putString("startTime", dateValue.getText().toString());
                fragment.setArguments(saveToBundle);
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });

        if (global.getDateString() != null) {
            dateValue.setText(global.getDateString());
            String durationText = args.get("duration").toString();
            if (durationText != null) {
                duration.setText(durationText);
                String scheduleType = scheduleDate.getSelectedItem().toString();
                //  int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                System.out.println("ScheduleTYpe:::::::::" + scheduleType);

            }

        }

        if (global.getEndDateString() != null) {
            endDateValue.setText(global.getEndDateString());
            String durationText = args.get("duration").toString();
            if (durationText != null) {
                duration.setText(durationText);

                String scheduleType = scheduleDate.getSelectedItem().toString();
                int doses = Integer.parseInt(numberDoses.getSelectedItem().toString());
                System.out.println("ScheduleTYpe:::::::::" + scheduleType);
              /*  if (scheduleType.equals("Daily")) {
                    showScheduleDay(doses);
                } else if (scheduleType.equals("Weekly")) {
                    showScheduleWeek(doses);
                } else if (scheduleType.equals("Monthly")) {
                    showScheduleMonth(doses);
                }*/

            }
        }

        endDateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new EndTimePicker();
                FragmentManager fragmentManger = getFragmentManager();
                saveToBundle = args;
                saveToBundle.putString("duration", duration.getText().toString());
                saveToBundle.putString("endTime", endDateValue.getText().toString());
                fragment.setArguments(saveToBundle);
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });


        saveTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ReminderVM saveReminderVM = new ReminderVM();
                addPatientMedicineSummary = new AddDiagnosisTestRequest();
                List<AlarmReminderVM> alarms = new ArrayList<AlarmReminderVM>();
                alarms = alarmList;
                // System.out.println("MedicineReminders::::::::::" + medicinReminderTables.size());
                if (alarms == null) {
                    alarms = medicinReminderTables;
                }
                patientId = session.getString("patientId", "");
                appointmentTime = global.getAppointmentTime();
                appointmentDate = global.getAppointmentDate();
                System.out.println("Appointment Date:::::::" + appointmentDate);
                addPatientMedicineSummary.setDoctorInstruction(doctorInstructionValue.getText().toString());
                if (medicineReminderBtn.isChecked())
                    addPatientMedicineSummary.setReminder("1");
                else
                    addPatientMedicineSummary.setReminder("0");
                addPatientMedicineSummary.setTestName(textNameEdit.getText().toString());
                addPatientMedicineSummary.setDatetime("" + calStartDate);
                addPatientMedicineSummary.setAppointmentId(appointMentId);
                addPatientMedicineSummary.setReferredId(referredBy);
                if (!addNewFlag)
                    addPatientMedicineSummary.setTestId(testId);
                else
                    addPatientMedicineSummary.setTestId("1");
                addPatientMedicineSummary.setClinicId(clinicIds[scheduleDate.getSelectedItemPosition()]);
                addPatientMedicineSummary.setLoggedinUserId(doctorId);
                addPatientMedicineSummary.setUserType(UtilSingleInstance.getUserType(type));


                //addPatientMedicineSummary.setMedicineSchedule(scheduleArray);
                savePatientReminderData(addPatientMedicineSummary);

            }
        });


        return view;
    }

    String[] clinicIds;
    String[] clinics;

    public void getAllClinics() {
        api.getAllClinics(new PersonID(doctorId), new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinicsList, Response response) {
                //clinicsListwithIDs=clinicsList;
                clinics = new String[clinicsList.size()];
                clinicIds = new String[clinicsList.size()];

                int count = 0;
                for (Clinic vm : clinicsList) {
                    clinics[count] = vm.getClinicName();
                    clinicIds[count] = vm.getIdClinic();
                    count = count + 1;
                }
                scheduleDate.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinics));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });
    }

    private String updateTimeString(int hours, int mins) {

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
        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText(getActivity().getResources().getString(R.string.app_name));

        if (type.equalsIgnoreCase("doctor")) {
            Fragment fragment = new DoctorAppointmentInformation();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            /*final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);*/
        } else if (type.equalsIgnoreCase("patient")) {
            Fragment fragment = new PatientAppointmentInformation();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatedate();
        }

    };

    //Declaration


    public void updatedate() {
        if (checkStartDate) {
            calStartDate = calendar.getTimeInMillis();
            endDateValue.setText(calendar.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            calEndDate = calendar.getTimeInMillis();
            dateValue.setText(calendar.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    public void setDate() {

        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setTime() {
        new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false).show();
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTime(hour, minute);
        }
    };



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

        if (timeValue == null) {

        } else {
            timeValue.setText(aTime);
        }
    }


    public void savePatientReminderData(AddDiagnosisTestRequest saveReminderVM) {

        if (addNewFlag) {
            api.addPatientDiagnosticTest(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    if (type.equalsIgnoreCase("Patient")) {
                        getFragmentManager().beginTransaction().remove(AddDiagnosticTest.this).commit();
                        Fragment fragment = new PatientAppointmentInformation();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    } else {
                        getFragmentManager().beginTransaction().remove(AddDiagnosticTest.this).commit();
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
        } else {
            System.out.println("I am Update Conidtion::::::::::::::::::::");
            api.updatePatientDiagnosticTest(saveReminderVM, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                    if (type.equalsIgnoreCase("Patient")) {
                        getFragmentManager().beginTransaction().remove(AddDiagnosticTest.this).commit();
                        Fragment fragment = new PatientAppointmentInformation();
                        FragmentManager fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    } else {
                        getFragmentManager().beginTransaction().remove(AddDiagnosticTest.this).commit();
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

    public void getTestDetails(String testId) {
        //MedicineId medicineId, Callback<ResponseCodeVerfication> response
        PatientTestId test = new PatientTestId(testId);
        api.getTestDetails(test, new Callback<TestDetails>() {
            @Override
            public void success(TestDetails testDetail, Response response) {

                medicine.setText(testDetail.getTest().getType());
                textNameEdit.setText(testDetail.getTest().getName());
                // numberDoses.setSelection(Integer.parseInt(testDetail.get.getDosesPerSchedule()));

                clinicIds = new String[]{testDetail.getClinic().getIdClinic()};
                clinics = new String[]{testDetail.getClinic().getClinicName()};
                scheduleDate.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinics));
                 if(testDetail.getReferredId()!=null && !testDetail.getReferredId().equalsIgnoreCase(""))
                    dosesList = new String[]{testDetail.getReferredId()};
                if(dosesList!=null && dosesList.length>0) {
                    dosesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_type, R.id.visitType, dosesList);
                    numberDoses.setAdapter(dosesAdapter);
                }
                dateValue.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(testDetail.getDatetime()));
                // endDateValue.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(testDetail.getEndDate()));
                //horizontalList = (HorizontalListView) view.findViewById(R.id.horizontalList);
                doctorInstructionValue.setText(testDetail.getDoctorInstruction());
                //duration.setText(Integer.parseInt(testDetail.getDurationSchedule()));


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public class ClinicSpinner extends ArrayAdapter<String> {
        String[] strClinic;

        public ClinicSpinner(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
            strClinic = objects;
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
