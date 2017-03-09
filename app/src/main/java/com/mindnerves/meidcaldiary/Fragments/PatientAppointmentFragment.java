package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.ClinicTimeTableAdapter;
import com.medico.application.MyApi;
import Model.ClinicAppointment;
import Model.ClinicDetailVm;
import Model.TimeInterval;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientAppointmentFragment extends Fragment {

    MyApi api;
    String clinicId,clinicShift,doctorId,doctorEmail;
    ProgressDialog progress;
    Calendar calendar = Calendar.getInstance();
    List<ClinicDetailVm> clinicDetailVmList;
    ClinicDetailVm clinicVm;
    TextView dateValue;
    GridView timeTeableList;
    Spinner visitType;
    TextView shiftValue,shiftName,doctor_email;
    Button bookNowBtn,timeBtn,cancelBtn;
    Global global;
    String userId,appointmentTime,appointmentDate;
    public SharedPreferences session;
    List<ClinicAppointment> clinicAppointments;
    Date currentDateAppointment = new Date();
    Date currentDate = new Date();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_appointment_fragment, container,false);
        TextView clinicName = (TextView) view.findViewById(R.id.clinicName);
        shiftName = (TextView) view.findViewById(R.id.shiftName);
        shiftValue = (TextView) view.findViewById(R.id.shiftValue);
        dateValue = (TextView) view.findViewById(R.id.dateValue);
        timeBtn = (Button) view.findViewById(R.id.timeBtn);
        bookNowBtn = (Button) view.findViewById(R.id.bookNowBtn);
        visitType = (Spinner) view.findViewById(R.id.visitType);
        timeTeableList = (GridView) view.findViewById(R.id.timeTeableList);
        Button backBtn = (Button) getActivity().findViewById(R.id.back_button);
        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        global = (Global) getActivity().getApplicationContext();
        System.out.println("Patient Appointment Fragment:::::::::");
        Bundle args = getArguments();
        clinicId = args.getString("clinicId");
        clinicShift = args.getString("clinicShift");
        appointmentTime = args.getString("appointmentTime");
        appointmentDate = args.getString("appointmentDate");
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId =  session.getString("sessionID", "");
        doctorId = session.getString("patient_DoctorId", "");
        doctorEmail = session.getString("patient_DoctorEmail", "");
        global.clinicDetailsData.setClinicId(Integer.parseInt(clinicId));
        global.clinicDetailsData.setShift(clinicShift);
        global.clinicDetailsData.setPatientId(userId);

        clinicDetailVmList = global.getClinicDetailVm();
        updatedate();
        System.out.println("Patient APpointment Fragment====");
        if(appointmentTime != null){
            cancelBtn.setVisibility(View.VISIBLE);
        }else{
            cancelBtn.setVisibility(View.GONE);
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("appController.clinicDetailsData  = "+doctorId);
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getPatientId());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getClinicId());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getShift());

                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete appointment?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                cancelClinicsAppointmentData(Integer.parseInt(doctorId), session.getString("patient_email", null),
                                        global.clinicDetailsData.getClinicId(), global.clinicDetailsData.getShift());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                getAllClinicsAppointmentDataFromCalendarButton();
            }
        });

        bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("patient_email appointment  = "+userId);
                global.clinicDetailsData.setTimeSlot(shiftValue.getText().toString());
                global.clinicDetailsData.setAppointmentDate(dateValue.getText().toString());
                //appController.clinicDetailsData
                System.out.println("appController.clinicDetailsData  = "+doctorId);
                System.out.println("appController.clinicDetailsData  = "+userId);
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getTimeSlot());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getShift());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getClinicId());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getBookTime());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getAppointmentDate());
                System.out.println("appController.clinicDetailsData  = "+global.clinicDetailsData.getStatus());

                ClinicAppointment clinicAppointment = new ClinicAppointment(Integer.parseInt(doctorId), userId,
                           global.clinicDetailsData.getTimeSlot(), global.clinicDetailsData.getShift(),global.clinicDetailsData.getClinicId(),
                        global.clinicDetailsData.getBookTime(),global.clinicDetailsData.getAppointmentDate(),global.clinicDetailsData.getStatus(), visitType.getSelectedItem().toString());

                saveClinicsAppointmentData(clinicAppointment);
            }
        });

        for(ClinicDetailVm clinicDetailVm : clinicDetailVmList){
            if(clinicDetailVm.getClinicId().equals(clinicId)){
                clinicVm = clinicDetailVm;
            }
        }

        getAllClinicsAppointmentData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBackWindows();
            }
        });

        clinicName.setText(clinicVm.getClinicName());
        global.clinicDetailsData.setDoctorId(Integer.parseInt(doctorId));
        global.clinicDetailsData.setAppointmentDate(dateValue.getText().toString());

        timeTeableList.setOnTouchListener(new ListView.OnTouchListener() {
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
                v.onTouchEvent(event);
                return true;
            }
        });

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        updatedate();
       // getAllClinicsDetails();
        return view;
    }

    public Calendar getStringToCal(String str){

        String[] time = str.split(":");
        String[] timeMin = time[1].split(" ");
        int hr = Integer.parseInt(time[0]);
        int min =  Integer.parseInt(timeMin[0]);
        String am = timeMin[1];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR, hr);
        if(am.equals("AM")){
            cal.set(Calendar.AM_PM,Calendar.AM);
        }else{
            cal.set(Calendar.AM_PM,Calendar.PM);
        }
        //cal.setTime(date);
        return cal;
    }

    public void getAllClinicsAppointmentDataFromCalendarButton(){
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                        //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
            @Override
            public void success(List<ClinicAppointment> clinicAppointments, Response response) {
                System.out.println("calendar date::::"+calendar.get(Calendar.DAY_OF_MONTH));
                loadAppointmentData(clinicAppointments,calendar.getTime());
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
    public void updatedate(){
        dateValue.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        if(appointmentDate != null){

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


        }
    }

    public void setDate(){
        appointmentDate = null;
        appointmentTime = null;
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
            getAllClinicsAppointmentDataFromCalendarButton();

        }

    };

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


    public void getAllClinicsAppointmentData(){
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        System.out.println("doctorId = "+doctorId);
        System.out.println("ClinicId = "+clinicId);
        System.out.println("Clinic Shift = "+clinicShift);
        System.out.println("date = "+dateValue.getText().toString());
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        api1.getAllClinicsAppointment(doctorId, clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
            @Override
            public void success(List<ClinicAppointment> clinicAppointments, Response response) {

                System.out.println("Clinic Appointments:::::::"+clinicAppointments.size());
                loadAppointmentData(clinicAppointments,currentDate);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void saveClinicsAppointmentData(ClinicAppointment clinicAppointment){
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        api1.saveClinicsAppointmentData(clinicAppointment, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Toast.makeText(getActivity(), "Request Send Successfully !!!", Toast.LENGTH_SHORT).show();
               // getBackWindows();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void cancelClinicsAppointmentData(Integer doctorId, String patientId, Integer clinicId, String shift){
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        api1.cancelClinicsAppointment(doctorId, patientId, clinicId, shift, new Callback<String>() {
            @Override
            public void success(String jsonObject, Response response) {
                Toast.makeText(getActivity(), "Deleted Successfully !!!", Toast.LENGTH_SHORT).show();
                getBackWindows();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
    public String convert24To12Hour(String time)
    {
        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
    public void loadAppointmentData(List<ClinicAppointment> clinicAppointments,Date curDate){
        String curDay = "";
        int flagDay = 0;
        Calendar startTime = null;
        Calendar endTime = null;
        System.out.println("Condition shift1::::"+clinicShift.equals("shift1"));
        System.out.println("Value:::::::::"+clinicShift);
        Calendar curCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        System.out.println(simpleDateformat.format(curDate));
        curDay = simpleDateformat.format(curDate);
        SimpleDateFormat simpleDateformatTime = new SimpleDateFormat("HH:mm");

        if(clinicShift.equals("shift1")){
            if(clinicVm.getSlot1().getStartTimes() == null){
                shiftValue.setText("No Time Scheduled");
            }else{
                if(clinicVm.getSlot1().getDays().contains(curDay)) {
                    flagDay = 1;
                }
                String timestr = clinicVm.getSlot1().getStartTimes() +" to "+ clinicVm.getSlot1().getEndTimes();
                shiftName.setText("Shift 1 : ");
                shiftValue.setText(timestr);
                startTime = getStringToCal(clinicVm.getSlot1().getStartTimes());
                endTime = getStringToCal(clinicVm.getSlot1().getEndTimes());
                System.out.println("Start Time= "+startTime.getTimeInMillis());
                System.out.println("End Time= "+endTime.getTimeInMillis());
            }
        }else if(clinicShift.equals("shift2")){
            if(clinicVm.getSlot2().getStartTimes() == null){
                shiftValue.setText("No Time Scheduled");
            }else{
                if(clinicVm.getSlot2().getDays().contains(curDay)) {
                    flagDay = 1;
                }
                String timestr = clinicVm.getSlot2().getStartTimes() +" to "+ clinicVm.getSlot2().getEndTimes();
                shiftName.setText("Shift 2 : ");
                shiftValue.setText(timestr);
                startTime = getStringToCal(clinicVm.getSlot2().getStartTimes());
                endTime = getStringToCal(clinicVm.getSlot2().getEndTimes());
            }

        }else if(clinicShift.equals("shift3")){
            if(clinicVm.getSlot3().getStartTimes() == null){
                shiftValue.setText("No Time Scheduled");
            }else{
                if(clinicVm.getSlot3().getDays().contains(curDay)) {
                    flagDay = 1;
                }
                String timestr = clinicVm.getSlot3().getStartTimes() +" to "+ clinicVm.getSlot3().getEndTimes();
                shiftName.setText("Shift 3 : ");
                shiftValue.setText(timestr);
                startTime = getStringToCal(clinicVm.getSlot3().getStartTimes());
                endTime = getStringToCal(clinicVm.getSlot3().getEndTimes());
            }
        }
        List<TimeInterval> timeList = new ArrayList<TimeInterval>();
        if(flagDay == 1)
        {
            curCalendar.setTime(curDate);
            currentCalendar.setTime(currentDate);
            System.out.println("Condition::::::::::::"+currentCalendar.getTimeInMillis() +" "+curCalendar.getTimeInMillis());
            if(currentCalendar.getTimeInMillis() < curCalendar.getTimeInMillis())
            {
                System.out.println("Day condition true:::::::::::::");
                System.out.println("Time::::::"+simpleDateformatTime.format(curDate));
                System.out.println("Time function::::::::"+convert24To12Hour(simpleDateformatTime.format(curDate)));
                while (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {

                    if (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {
                        String timeHHMM = null;
                        timeHHMM = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                        TimeInterval timeInterval = null;

                        if (startTime.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        } else {
                            timeHHMM = timeHHMM + "PM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        }

                        //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                        for (ClinicAppointment clinicAppointment : clinicAppointments) {
                            //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                            if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                break;
                            }
                        }
                        timeList.add(timeInterval);
                        startTime.add(Calendar.MINUTE, 15);
                    }
                }

            }
            else if(currentCalendar.getTimeInMillis() == curCalendar.getTimeInMillis())
            {
                int flagStartTime = 0;
                int flagNormal = 0;
                int flagAbnormal = 0;
                System.out.println("Equal condition true:::::::::::::");
                String currentTime = convert24To12Hour(simpleDateformatTime.format(currentDate));
                currentCalendar = getStringToCal(currentTime);
                System.out.println("Equal Condition startTime= "+startTime.getTimeInMillis());
                System.out.println("Equal Condition currentCalendar= "+currentCalendar.getTimeInMillis());
                System.out.println("Equal Condition endTime= "+endTime.getTimeInMillis());
                if((startTime.getTimeInMillis() <= currentCalendar.getTimeInMillis()) &&(currentCalendar.getTimeInMillis() <= endTime.getTimeInMillis()))
                {
                    flagStartTime = 1;
                }
                else if(startTime.getTimeInMillis() >= currentCalendar.getTimeInMillis())
                {
                    flagNormal = 1;
                }
                else if(currentCalendar.getTimeInMillis() > endTime.getTimeInMillis())
                {
                    flagAbnormal = 1;
                }

                System.out.println("FlagStartTime::::::"+flagStartTime);
                System.out.println("flagNormal::::::"+flagNormal);
                System.out.println("flagAbnormal::::::"+flagAbnormal);
                if(flagStartTime == 1)
                {
                    while (endTime.getTimeInMillis() > currentCalendar.getTimeInMillis()) {

                        if (endTime.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = currentCalendar.get(Calendar.HOUR) + ":" + currentCalendar.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (currentCalendar.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            } else {
                                timeHHMM = timeHHMM + "PM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                            for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }
                            timeList.add(timeInterval);
                            currentCalendar.add(Calendar.MINUTE, 15);
                        }
                    }
                }

                if(flagNormal == 1)
                {
                    while (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {

                        if (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (startTime.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            } else {
                                timeHHMM = timeHHMM + "PM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                            for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }
                            timeList.add(timeInterval);
                            startTime.add(Calendar.MINUTE, 15);
                        }
                    }
                }

                if(flagAbnormal == 1)
                {
                    while (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {

                        if (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (startTime.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + "AM";
                                timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                            } else {
                                timeHHMM = timeHHMM + "PM";
                                timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                            for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }
                            timeList.add(timeInterval);
                            startTime.add(Calendar.MINUTE, 15);
                        }
                    }
                }
            }
            else
            {
                while (endTime.getTimeInMillis() > startTime.getTimeInMillis())
                {
                    if (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {
                        String timeHHMM = null;
                        timeHHMM = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                        TimeInterval timeInterval = null;

                        if (startTime.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + "AM";
                            timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                        } else {
                            timeHHMM = timeHHMM + "PM";
                            timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                        }

                        //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                        for (ClinicAppointment clinicAppointment : clinicAppointments) {
                            //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                            if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                break;
                            }
                        }
                        timeList.add(timeInterval);
                        startTime.add(Calendar.MINUTE, 15);
                    }
                }
            }
        }
        else
        {
            while (endTime.getTimeInMillis() > startTime.getTimeInMillis())
            {
                if (endTime.getTimeInMillis() > startTime.getTimeInMillis()) {
                    String timeHHMM = null;
                    timeHHMM = startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE);
                    TimeInterval timeInterval = null;

                    if (startTime.get(Calendar.AM_PM) == 0) {
                        timeHHMM = timeHHMM + "AM";
                        timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                    } else {
                        timeHHMM = timeHHMM + "PM";
                        timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                    }

                    //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTime.get(Calendar.AM_PM), "Available", false);
                    for (ClinicAppointment clinicAppointment : clinicAppointments) {
                        //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                        if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                            timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                            break;
                        }
                    }
                    timeList.add(timeInterval);
                    startTime.add(Calendar.MINUTE, 15);
                }
            }
        }

        System.out.println("timeList = "+timeList.size());

        ClinicTimeTableAdapter clinicTimeTableAdapter = new ClinicTimeTableAdapter(getActivity(), timeList,timeTeableList,appointmentTime);
        timeTeableList.setAdapter(clinicTimeTableAdapter);
    }

    public void getBackWindows(){
            System.out.println("I am here:::::::::::::");
            Bundle appointmentArgs = getArguments();
            if(appointmentArgs.get("fragment") != null){
                if(appointmentArgs.get("fragment").equals("appointmentDate")){
                    Fragment fragment = new PatientAllDoctors();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }

            }else {
                Bundle args = new Bundle();
                args.putString("doctorId", session.getString("patient_DoctorEmail", ""));
                System.out.println("Doctor Email::::::::::::" + session.getString("patient_DoctorEmail", ""));
                args.putString("fragment", "patientAllDoctor");
                //Fragment fragment = new ClinicAllPatientFragment();
                Fragment fragment = new DoctorDetailsFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }

    }

}
