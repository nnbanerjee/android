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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medico.view.PatientProfileListView;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.ClinicTimeTableAdapter;
import Application.MyApi;
import Model.AppointmentId;
import Model.ClinicDetailVm;
import Model.ClinicList;
import Model.DoctorAppointmentsResponse;
import Model.DoctorClinicAppointments;
import Model.DoctorCreatesAppoinementResponse;
import Model.DoctorCreatesAppointment;
import Model.ResponseCodeVerfication;
import Model.Slot;
import Model.TimeInterval;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicAppointmentFragment extends Fragment {

    MyApi api;
    String clinicId, clinicShift;
    ProgressDialog progress;
    Calendar calendar = Calendar.getInstance();
    List<ClinicDetailVm> clinicDetailVmList;
    ClinicDetailVm clinicVm;
    TextView dateValue;
    GridView timeTeableList;
    Spinner visitType;
    TextView shiftValue, shiftName, doctor_email;
    Button bookNowBtn, timeBtn, cancelBtn, back;
    Global global;
    String userId, appointmentTime, appointmentDate;
    public SharedPreferences session;
    Date currentDate = new Date();
    TextView globalTv;
    String type;
    Date date;
    private String doctorId;
    private String doctorClinicId;
    private String dateLong, strClinicName;
    int selectedSlotNo;
    Slot SelectedSlotDetailsWithClinic;
    Calendar startTimeFromSelection, endTimeFromSelection;
    String patientId;
    ClinicList selectedClinic;
    ClinicTimeTableAdapter clinicTimeTableAdapter;
    boolean reschedule = false;
    Toolbar toolbar;
    DoctorAppointmentsResponse originalDataOfClinicAppointments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_appointment_fragment, container, false);

        //Hide top layout from screen.. no needed now
        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.layout);
        if (layout != null)
            layout.setVisibility(View.GONE);
        RelativeLayout layout2 = (RelativeLayout) getActivity().findViewById(R.id.layout2);
        if (layout2 != null)
            layout2.setVisibility(View.GONE);


        TextView clinicName = (TextView) view.findViewById(R.id.clinicName);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinic Appointment");
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
        Bundle args = getArguments();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType", null);


        clinicId = args.getString("clinicId");
        clinicShift = args.getString("clinicShift");
        appointmentTime = args.getString("appointmentTime");
        appointmentDate = args.getString("appointmentDate");


        reschedule = args.getBoolean("reschedule");

        toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        if (reschedule)
            toolbar.inflateMenu(R.menu.book_now_with_cancel);

        else
            toolbar.inflateMenu(R.menu.book_now);
        // doctorClinicId= args.getString("doctorClinicId");


        patientId = session.getString("patientId", null);
        System.out.println("patientId from session sharedPreferance-->" + patientId);

        strClinicName = args.getString("clinicName");
       /* startTimeFromSelection= Long.parseLong(args.getString("startTime")) ;
        endTimeFromSelection=args.getString("endTime");*/
        startTimeFromSelection = Calendar.getInstance();
        startTimeFromSelection.setTimeInMillis(Long.parseLong(args.getString("startTime")));
        endTimeFromSelection = Calendar.getInstance();
        endTimeFromSelection.setTimeInMillis(Long.parseLong(args.getString("endTime")));

        //New Data`
        Gson gson = new Gson();
        String json = session.getString("SelectedSlotDetailsWithClinic", "");
        SelectedSlotDetailsWithClinic = gson.fromJson(json, Slot.class);
        selectedSlotNo = session.getInt("selectedSlotNo", 0);
        String jsonSelectedClinic = session.getString("selectedClinic", "");
        selectedClinic = gson.fromJson(jsonSelectedClinic, ClinicList.class);

        doctorId = session.getString("id", null);
        doctorClinicId = SelectedSlotDetailsWithClinic.getDoctorClinicId();
        dateLong = "" + Calendar.getInstance().getTimeInMillis();


        // global.clinicDetailsData.setClinicId(Integer.parseInt(clinicId));
        // global.clinicDetailsData.setShift(clinicShift);
        // global.clinicDetailsData.setPatientId(userId);
        if (type != null && type.equalsIgnoreCase("Patient")) {
            userId = session.getString("sessionID", "");
        } else if (type != null && type.equalsIgnoreCase("Doctor")) {
            userId = session.getString("Doctor_patientEmail", "");
        }
        System.out.println("Appointment Date= " + appointmentDate);
        clinicDetailVmList = global.getClinicDetailVm();
        updatedate();

        if (reschedule) {
            cancelBtn.setVisibility(View.VISIBLE);
            bookNowBtn.setText("Update");
        } else {
            bookNowBtn.setText("Book Now");
            cancelBtn.setVisibility(View.GONE);
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText()
                /*System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getDoctorId());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getPatientId());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getClinicId());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getShift());*/

                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete appointment?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                cancelClinicsAppointmentData(selectedClinic.getAppointments().get(0).getAppointmentId());// global.clinicDetailsData.getDoctorId(), session.getString("patient_email", null),
                                // global.clinicDetailsData.getClinicId(), global.clinicDetailsData.getShift());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



                /* cancelClinicsAppointmentData(global.clinicDetailsData.getDoctorId(), global.clinicDetailsData.getId(),
                        global.clinicDetailsData.getIdClinic(), global.clinicDetailsData.getShift());*/
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

                System.out.println("patient_email appointment  = " + userId);
                global.clinicDetailsData.setTimeSlot(shiftValue.getText().toString());
                global.clinicDetailsData.setAppointmentDate(dateValue.getText().toString());
                //appController.clinicDetailsData
              /*  System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getDoctorId());
                System.out.println("appController.clinicDetailsData  = " + userId);
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getTimeSlot());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getShift());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getClinicId());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getBookTime());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getAppointmentDate());
                System.out.println("appController.clinicDetailsData  = " + global.clinicDetailsData.getStatus());*/


                DoctorCreatesAppointment newAppointment = new DoctorCreatesAppointment("" + (ClinicTimeTableAdapter.selectedTimeSlotSequenceNo + 1), doctorId, patientId,
                        UtilSingleInstance.getUserType(type), clinicId, "" + UtilSingleInstance.getStringToCalWithTodaysDate(clinicTimeTableAdapter.getSelectedTime()), doctorClinicId, "0", "0", "" + visitType.getSelectedItemPosition(), "", "2", "", "", "");

              /*  ClinicAppointment clinicAppointment = new ClinicAppointment(global.clinicDetailsData.getDoctorId(), userId,"",,

                        global.clinicDetailsData.getTimeSlot(), global.clinicDetailsData.getShift(), global.clinicDetailsData.getClinicId(),
                        global.clinicDetailsData.getBookTime(), global.clinicDetailsData.getAppointmentDate(), global.clinicDetailsData.getStatus(), visitType.getSelectedItem().toString());
*/
                saveClinicsAppointmentData(newAppointment);
            }
        });

       /* for (ClinicDetailVm clinicDetailVm : clinicDetailVmList) {
            if (clinicDetailVm.getClinicId().equals(clinicId)) {
                clinicVm = clinicDetailVm;
            }
        }*/

        //getAllClinicsAppointmentData();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Back Clicked:::::::::::::::::::::");
                getBackWindows();
            }
        });

        clinicName.setText(strClinicName);
        /*global.clinicDetailsData.setDoctorId(Integer.parseInt(clinicVm.getDoctorId()));
        global.clinicDetailsData.setAppointmentDate(dateValue.getText().toString());*/

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
        getAllClinicsAppointmentData();
        return view;
    }


    public void updatedate() {
        dateValue.setText(calendar.get(Calendar.YEAR) + "-" + UtilSingleInstance.showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

        if (appointmentDate != null) {

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                date = formatter.parse(appointmentDate);
                System.out.println(date);
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println(formatter2.format(date));
                dateValue.setText(formatter2.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    public void setDate() {
        appointmentDate = null;
        appointmentTime = null;
        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatedate();
            progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
            getAllClinicsAppointmentDataFromCalendarButton();

        }

    };


    public void getAllClinicsAppointmentData() {
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                        //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        // DoctorClinicAppointments param = new DoctorClinicAppointments(doctorId, doctorClinicId, ""+ calendar.getTimeInMillis());
        DoctorClinicAppointments param = new DoctorClinicAppointments(doctorClinicId, "" + startTimeFromSelection.getTimeInMillis(), "" + endTimeFromSelection.getTimeInMillis());
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        api1.getClinicSlotAvailabilityByDoctor(param, new Callback<DoctorAppointmentsResponse>() {
            @Override
            public void success(DoctorAppointmentsResponse clinicAppointments, Response response) {
                if (appointmentDate != null) {
                    System.out.println("Appointment Date Not Null");
                    loadAppointmentData(clinicAppointments, date);
                } else {
                    System.out.println("Appointment Date Null");
                    loadAppointmentData(clinicAppointments, currentDate);
                }
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void getAllClinicsAppointmentDataFromCalendarButton() {
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                        //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);

        // DoctorClinicAppointments param = new DoctorClinicAppointments(doctorId, doctorClinicId, dateLong);
        DoctorClinicAppointments param = new DoctorClinicAppointments(doctorClinicId, "" + startTimeFromSelection.getTimeInMillis(), "" + endTimeFromSelection.getTimeInMillis());
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        api1.getClinicSlotAvailabilityByDoctor(param, new Callback<DoctorAppointmentsResponse>() {
            @Override
            public void success(DoctorAppointmentsResponse clinicAppointments, Response response) {
                System.out.println("calendar date::::" + calendar.get(Calendar.DAY_OF_MONTH));

                loadAppointmentData(clinicAppointments, calendar.getTime());
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void saveClinicsAppointmentData(DoctorCreatesAppointment clinicAppointment) {
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                        //.setEndpoint("http://192.168.1.19:9000/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        progress = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        //api1.getAllClinicsAppointment(clinicVm.getDoctorId(), clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
        // api1.saveClinicsAppointmentData(clinicAppointment, new Callback<JsonObject>() {

        if (reschedule) {
            clinicAppointment.setAppointmentId(selectedClinic.getAppointments().get(0).getAppointmentId());
            api1.updateAppointment(clinicAppointment, new Callback<DoctorCreatesAppoinementResponse>() {
                @Override
                public void success(DoctorCreatesAppoinementResponse jsonObject, Response response) {
                    Toast.makeText(getActivity(), R.string.request_success, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                   // int lastbooked=1;

                  //  if(originalDataOfClinicAppointments!=null&& originalDataOfClinicAppointments.getsequence_num()!=null)
                     //lastbooked=Integer.parseInt(originalDataOfClinicAppointments.getsequence_num().get(0)) ;
                    for(int i=0; i<timeList.size();i++){
                       if( timeList.get(i).getIsAvailable().equalsIgnoreCase("Occupied"))
                           timeList.get(i).setIsAvailable("Available");
                    }
                    timeList.get(ClinicTimeTableAdapter.selectedTimeSlotSequenceNo).setIsAvailable("Occupied");
                    //timeList.get(lastbooked).setIsAvailable("Available");
                    clinicTimeTableAdapter.notifyDataSetChanged();
                    clinicTimeTableAdapter.notifyDataSetInvalidated();
                  //  getAllClinicsAppointmentData();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        } else {

            api1.createAppointment(clinicAppointment, new Callback<DoctorCreatesAppoinementResponse>() {
                @Override
                public void success(DoctorCreatesAppoinementResponse jsonObject, Response response) {
                    Toast.makeText(getActivity(), R.string.request_success, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    timeList.get(ClinicTimeTableAdapter.selectedTimeSlotSequenceNo).setIsAvailable("Occupied");

                    clinicTimeTableAdapter.notifyDataSetChanged();
                    clinicTimeTableAdapter.notifyDataSetInvalidated();
                   /* clinicTimeTableAdapter = new ClinicTimeTableAdapter(getActivity(), timeList, timeTeableList, appointmentTime);
                    timeTeableList.setAdapter(clinicTimeTableAdapter);*/

                    // ClinicTimeTableAdapter.selectedTimeSlotSequenceNo + 1
                    //update red color
                    // getAllClinicsAppointmentData();
                    //update the current selection with red
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }

    }

    public void cancelClinicsAppointmentData(String appointmentId) {//Integer doctorId, String patientId, Integer clinicId, String shift) {
        MyApi api1;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api1 = restAdapter.create(MyApi.class);
        api1.cancelAppointment(new AppointmentId(appointmentId), new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication jsonObject, Response response) {
                progress.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully cancelled Appointment!", Toast.LENGTH_LONG).show();
                getBackWindows();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public String convert24To12Hour(String time) {
        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    List<TimeInterval> timeList = new ArrayList<TimeInterval>();

    public void loadAppointmentData(DoctorAppointmentsResponse clinicAppointments, Date curDate) {
        originalDataOfClinicAppointments=clinicAppointments;
        String curDay = "";
        int flagDay = 0;
        Calendar endTime = null;
        Calendar curCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        System.out.println("cur Date::::" + simpleDateformat.format(curDate));//todays date = WED
        curDay = simpleDateformat.format(curDate);
        SimpleDateFormat simpleDateformatTime = new SimpleDateFormat("hh:mm");

        if (startTimeFromSelection == null) {
            shiftValue.setText("No Time Scheduled");
        } else {
            System.out.println("Condition= " + SelectedSlotDetailsWithClinic.getDaysOfWeek().contains(curDay));
            System.out.println("Days= " + SelectedSlotDetailsWithClinic.getDaysOfWeek());
            if (SelectedSlotDetailsWithClinic.getDaysOfWeek().contains(curDay)) {
                flagDay = 1;
            }
            String timestr = UtilSingleInstance.getTimeFromLongDate(SelectedSlotDetailsWithClinic.getStartTime()) + " to " + UtilSingleInstance.getTimeFromLongDate(SelectedSlotDetailsWithClinic.getEndTime());
            shiftName.setText("Slot " + SelectedSlotDetailsWithClinic.getSlotNumber() + " : ");
            shiftValue.setText(timestr);

            startTimeFromSelection = Calendar.getInstance();
            startTimeFromSelection.setTimeInMillis(Long.parseLong(SelectedSlotDetailsWithClinic.getStartTime()));
            endTimeFromSelection = Calendar.getInstance();
            endTimeFromSelection.setTimeInMillis(Long.parseLong(SelectedSlotDetailsWithClinic.getEndTime()));
            //startTimeFromSelection = getStringToCal(SelectedSlotDetailsWithClinic.getStartTime());
            // endTime = getStringToCal(SelectedSlotDetailsWithClinic.getEndTime());

        }


        //if todays date.
        if (flagDay == 1) {
            curCalendar.setTime(curDate);
            currentCalendar.setTime(currentDate);
            System.out.println("Condition::::::::::::" + currentCalendar.getTimeInMillis() + " " + curCalendar.getTimeInMillis());
            if (currentCalendar.getTimeInMillis() < curCalendar.getTimeInMillis()) {
                System.out.println("Day condition true:::::::::::::");
                System.out.println("Time::::::" + simpleDateformatTime.format(curDate));
                System.out.println("Time function::::::::" + convert24To12Hour(simpleDateformatTime.format(curDate)));
                while (endTime.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {

                    if (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                        String timeHHMM = null;
                        timeHHMM = startTimeFromSelection.get(Calendar.HOUR) + ":" + startTimeFromSelection.get(Calendar.MINUTE);
                        TimeInterval timeInterval = null;

                        if (startTimeFromSelection.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + " AM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        } else {
                            timeHHMM = timeHHMM + " PM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        }

                        //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                        String size = "" + (timeList.size() + 1);//get the current rendering slot no.
                        for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                            if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                break;
                            } else
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        }

                        timeList.add(timeInterval);
                        startTimeFromSelection.add(Calendar.MINUTE, 15);
                    }
                }

            } else if (currentCalendar.getTimeInMillis() == curCalendar.getTimeInMillis()) {//this may be initial cond
                int flagStartTime = 0;
                int flagNormal = 0;
                int flagAbnormal = 0;
                System.out.println("Equal condition true:::::::::::::");
                System.out.println("condition normal:::::" + (startTimeFromSelection.getTimeInMillis() >= currentCalendar.getTimeInMillis()));
                System.out.println("start Time:::::::" + startTimeFromSelection.getTimeInMillis() + "current TIme::::::" + currentCalendar.getTimeInMillis());
                String currentTime = convert24To12Hour(simpleDateformatTime.format(currentDate));
                currentCalendar = UtilSingleInstance.getStringToCal(currentTime);
                if ((startTimeFromSelection.getTimeInMillis() <= currentCalendar.getTimeInMillis()) && (currentCalendar.getTimeInMillis() <= endTimeFromSelection.getTimeInMillis())) {
                    flagStartTime = 1;
                } else if (startTimeFromSelection.getTimeInMillis() >= currentCalendar.getTimeInMillis()) {
                    flagNormal = 1;
                } else if (currentCalendar.getTimeInMillis() > endTimeFromSelection.getTimeInMillis()) {
                    flagAbnormal = 1;
                }

                System.out.println("FlagStartTime::::::" + flagStartTime);
                System.out.println("flagNormal::::::" + flagNormal);
                System.out.println("flagAbnormal::::::" + flagAbnormal);
                if (flagStartTime == 1) {
                    while (endTimeFromSelection.getTimeInMillis() > currentCalendar.getTimeInMillis()) {

                        if (endTimeFromSelection.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = currentCalendar.get(Calendar.HOUR) + ":" + currentCalendar.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (currentCalendar.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + " AM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            } else {
                                timeHHMM = timeHHMM + " PM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                            /* for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }*/

                            String size = "" + (timeList.size() + 1);
                            for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                                if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                    // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                    timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                    break;
                                } else
                                    timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }
                           /* for (int i = 0; i < 10; i++) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());

                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                                break;

                            }*/
                            timeList.add(timeInterval);
                            currentCalendar.add(Calendar.MINUTE, 15);
                        }
                    }
                }

                if (flagNormal == 1) {
                    while (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {

                        if (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = startTimeFromSelection.get(Calendar.HOUR) + ":" + startTimeFromSelection.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (startTimeFromSelection.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + " AM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            } else {
                                timeHHMM = timeHHMM + " PM";
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                           /* for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }*/
                            /*for (int i = 0; i < 10; i++) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());

                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                                break;

                            }*/
                            String size = "" + (timeList.size() + 1);
                            for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                                if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                    // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                    timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                    break;
                                } else
                                    timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }
                            timeList.add(timeInterval);
                            startTimeFromSelection.add(Calendar.MINUTE, 15);
                        }
                    }
                }

                if (flagAbnormal == 1) {
                    while (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {

                        if (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                            String timeHHMM = null;
                            timeHHMM = startTimeFromSelection.get(Calendar.HOUR) + ":" + startTimeFromSelection.get(Calendar.MINUTE);
                            TimeInterval timeInterval = null;

                            if (startTimeFromSelection.get(Calendar.AM_PM) == 0) {
                                timeHHMM = timeHHMM + " AM";
                                timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                            } else {
                                timeHHMM = timeHHMM + " PM";
                                timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                            }

                            //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                         /*   for (ClinicAppointment clinicAppointment : clinicAppointments) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                                if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                    timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                    break;
                                }
                            }*/
                           /* for (int i = 0; i < 10; i++) {
                                //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());

                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                                break;

                            }*/
                            String size = "" + (timeList.size() + 1);
                            for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                                if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                    // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                    timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                    break;
                                } else
                                    timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }
                            timeList.add(timeInterval);
                            startTimeFromSelection.add(Calendar.MINUTE, 15);
                        }
                    }
                }
            } else {
                while (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                    if (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                        String timeHHMM = null;
                        timeHHMM = startTimeFromSelection.get(Calendar.HOUR) + ":" + startTimeFromSelection.get(Calendar.MINUTE);
                        TimeInterval timeInterval = null;

                        if (startTimeFromSelection.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + " AM";
                            timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                        } else {
                            timeHHMM = timeHHMM + " PM";
                            timeInterval = new TimeInterval(timeHHMM, "Not Available", false);
                        }

                        //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                    /*    for (ClinicAppointment clinicAppointment : clinicAppointments) {
                            //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                            if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                                timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                                break;
                            }
                        }*/

                        String size = "" + (timeList.size() + 1);
                        for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                            if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                break;
                            } else
                                timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        }
                        timeList.add(timeInterval);
                        startTimeFromSelection.add(Calendar.MINUTE, 15);
                    }
                }
            }
        } else {
            System.out.println("startTimeFromSelection-------->" + startTimeFromSelection);
            System.out.println("endTimeFromSelection-------->" + endTimeFromSelection);
            if (endTimeFromSelection != null && startTimeFromSelection != null) {
                while (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                    if (endTimeFromSelection.getTimeInMillis() > startTimeFromSelection.getTimeInMillis()) {
                        String timeHHMM = null;
                        timeHHMM = startTimeFromSelection.get(Calendar.HOUR) + ":" + startTimeFromSelection.get(Calendar.MINUTE);
                        TimeInterval timeInterval = null;

                        if (startTimeFromSelection.get(Calendar.AM_PM) == 0) {
                            timeHHMM = timeHHMM + " AM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        } else {
                            timeHHMM = timeHHMM + " PM";
                            timeInterval = new TimeInterval(timeHHMM, "Available", false);
                        }

                        //TimeInterval timeInterval = new TimeInterval(timeHHMM + startTimeFromSelection.get(Calendar.AM_PM), "Available", false);
                   /* for (ClinicAppointment clinicAppointment : clinicAppointments) {
                        //System.out.println(" timeHHMM = "+timeHHMM+"  clinicAppointment = "+clinicAppointment.getBookTime());
                        if (clinicAppointment.getBookTime().equals(timeHHMM)) {
                            timeInterval = new TimeInterval(timeHHMM, clinicAppointment.getStatus(), false);
                            break;
                        }
                    }*/
                        if (clinicAppointments != null && clinicAppointments.getsequence_num() != null) {
                            String size = "" + (timeList.size() + 1);
                            for (int i = 0; i < clinicAppointments.getsequence_num().size(); i++) {

                                if (size.equalsIgnoreCase(clinicAppointments.getsequence_num().get(i))) {
                                    // if(clinicAppointments.getBookedSlots().get(i).equalsIgnoreCase(""+i)){
                                    timeInterval = new TimeInterval(timeHHMM, "Occupied", false);
                                    break;
                                } else
                                    timeInterval = new TimeInterval(timeHHMM, "Available", false);
                            }
                        }


                        timeList.add(timeInterval);
                        startTimeFromSelection.add(Calendar.MINUTE, 15);
                    }
                }
            }
        }
        //int count = 0;


        System.out.println("timeList = " + timeList.size());

        clinicTimeTableAdapter = new ClinicTimeTableAdapter(getActivity(), timeList, timeTeableList, appointmentTime);
        timeTeableList.setAdapter(clinicTimeTableAdapter);

    }

    public void getBackWindows() {
        Bundle args = getArguments();
        System.out.println("Clinic Appointment Fragment Back:::::::::");
        //  args.putString("doctorId", clinicVm.getDoctorId());
        //Fragment fragment = new ClinicAllPatientFragment();
        if (args != null) {
            if (args.getString("fragment").equals("ClinicPatientAdapter")) {
                Fragment fragment = new PatientDetailsFragment();
                FragmentManager fragmentManger = getFragmentManager();
                //fragmentManger.popBackStack();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            } else if (args.getString("fragment").equals("DoctorPatientAdapter")) {
                Fragment fragment = new PatientProfileListView();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        } else {

            Fragment fragment = new ClinicAllDoctorFragment();
            fragment.setArguments(args);
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
        }

    }

}
