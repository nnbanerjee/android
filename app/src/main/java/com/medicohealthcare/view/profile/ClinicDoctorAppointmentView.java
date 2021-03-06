package com.medicohealthcare.view.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.AppointmentResponse;
import com.medicohealthcare.model.DoctorAppointment;
import com.medicohealthcare.model.DoctorAppointmentGridViewAdapter;
import com.medicohealthcare.model.DoctorClinicId;
import com.medicohealthcare.model.DoctorHoliday;
import com.medicohealthcare.model.DoctorSlotBookings;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */


public class ClinicDoctorAppointmentView extends ParentFragment {

    GridView timeTeableList;
    TextView slotTime,slotName,clinicName,dateValue;
    Button timeBtn;

    Spinner visitType;
    String userId,appointmentTime,appointmentDate;

    DoctorSlotBookings doctorSlotBookings = new DoctorSlotBookings();
    List<DoctorHoliday> doctorholidayList = new ArrayList<>();

    DoctorAppointment doctorAppointment = new DoctorAppointment();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_appointment_fragment, container,false);
        setHasOptionsMenu(true);

        clinicName = (TextView) view.findViewById(R.id.clinicName);
        slotName = (TextView) view.findViewById(R.id.shiftName);
        slotTime = (TextView) view.findViewById(R.id.shiftValue);
        dateValue = (TextView) view.findViewById(R.id.dateValue);
        timeBtn = (Button) view.findViewById(R.id.timeBtn);
        timeTeableList = (GridView) view.findViewById(R.id.timeTeableList);

        visitType = (Spinner) view.findViewById(R.id.visitType);

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(dateValue);
            }
        });
        timeTeableList.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
        Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        clinicName.setText(bundle.getString(CLINIC_NAME));
        slotTime.setText(bundle.getString(SLOT_TIME));
        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
        Long datetime = bundle.getLong(APPOINTMENT_DATETIME);
        Integer appointmentId = bundle.getInt(APPOINTMENT_ID);
        if(appointmentId != null && appointmentId.intValue() > 0)
            getAppointment(appointmentId);
        else
            doctorAppointment = new DoctorAppointment();
        Date date;
        if(datetime == null || datetime.intValue() == 0)
            date = new Date();
        else
            date = new Date(datetime);

        setAdapter(date);
        setHasOptionsMenu(true);
    }

    public void saveAppointment( DoctorAppointment appointment) {
        showBusy();
        if(appointment.appointmentId == null || appointment.appointmentId.intValue() == 0) {

            api.createAppointment(appointment, new Callback<AppointmentResponse>() {
                @Override
                public void success(AppointmentResponse s, Response response) {
                    Toast.makeText(getActivity(), "Appointment Created Successful!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
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
        else
        {
            api.updateAppointment(appointment, new Callback<AppointmentResponse>() {
                @Override
                public void success(AppointmentResponse s, Response response) {
                    Toast.makeText(getActivity(), "Appointment update Successful!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                dateField.setText(format.format(date));
//                doctorAppointment.setAppointmentDate(date.getTime());
                setAdapter(date);
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
        String days = getActivity().getIntent().getExtras().getString(DAYS_OF_WEEK);
        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .setMode(SlideDateTimePicker.ONLY_CALENDAR)
                .setWorkingDays(getWorkingDays(days))
                .build();
        pickerDialog.show();
    }

    private void setAdapter(final Date date)
    {
        final Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        clinicName.setText(bundle.getString(CLINIC_NAME));
        slotTime.setText(bundle.getString(SLOT_TIME));
//        Long time = bundle.getLong(APPOINTMENT_DATETIME);
        final DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
        dateValue.setText(format.format(date));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY,0);
        calendar1.set(Calendar.MINUTE,00);
        calendar1.set(Calendar.SECOND,00);
        final Date date1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.set(Calendar.HOUR_OF_DAY,23);
        calendar2.set(Calendar.MINUTE,59);
        calendar2.set(Calendar.SECOND,59);
        final Date date2 = calendar2.getTime();
        Integer doctorId = bundle.getInt(DOCTOR_ID);
        Integer slotId = bundle.getInt(DOCTOR_CLINIC_ID);
        Long startTime = bundle.getLong(SLOT_START_DATETIME);
        Long endTime = bundle.getLong(SLOT_END_DATETIME);
        Integer vistDuration = bundle.getInt(SLOT_VISIT_DURATION);
        int numberOfPatients = Math.round((endTime - startTime)/(vistDuration * 60 * 1000));

        api.getDoctorHolidayList(new DoctorHoliday(doctorId,calendar1.getTimeInMillis(),calendar2.getTimeInMillis(), new Integer(2).byteValue()), new Callback<List<DoctorHoliday>>(){
            @Override
            public void success(List<DoctorHoliday> holidayList, Response response) {
                doctorholidayList = holidayList;
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity(),false).handleError(error);
                doctorholidayList = null;
            }
        });

        api.getClinicSlotBookingByDoctor(new DoctorClinicId(slotId,calendar1.getTimeInMillis(),calendar2.getTimeInMillis()), new Callback<List<DoctorSlotBookings>>(){
            @Override
            public void success(List<DoctorSlotBookings> slotBookingses, Response response) {
                if(slotBookingses != null && slotBookingses.size() > 0) {
                    doctorSlotBookings = slotBookingses.get(0);
                    DoctorAppointmentGridViewAdapter adapter = new DoctorAppointmentGridViewAdapter(activity, doctorSlotBookings, doctorholidayList,date);
                    timeTeableList.setAdapter(adapter);
                }
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                doctorSlotBookings = null;
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);

                DoctorAppointmentGridViewAdapter adapter = new DoctorAppointmentGridViewAdapter(activity, doctorSlotBookings, doctorholidayList,date);
                timeTeableList.setAdapter(adapter);
                error.printStackTrace();
            }
        });
    }
    private void getAppointment(Integer appointmentId)
    {
        api.getPatientAppointment(new AppointmentId1(appointmentId), new Callback<DoctorAppointment>(){
            @Override
            public void success(DoctorAppointment appointment, Response response) {
                if(appointment != null ) {
                    doctorAppointment = appointment;
                }
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                doctorAppointment = new DoctorAppointment();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(null);
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointmentId = bundle.getInt(APPOINTMENT_ID);
        if(appointmentId == null || appointmentId.intValue() > 0 == false)
            menuItem.setTitle("CREATE");
        else
            menuItem.setTitle("UPDATE");
    }

    @Override
    public boolean isChanged()
    {
        return doctorAppointment.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        Integer patientId = bundle1.getInt(PATIENT_ID);
        Integer doctorId = bundle1.getInt(DOCTOR_ID);
        Integer loggedinUserId = bundle1.getInt(LOGGED_IN_ID);
        Integer clinicId = bundle1.getInt(CLINIC_ID);
        Integer doctorClinicId = bundle1.getInt(DOCTOR_CLINIC_ID);

        if(doctorId.intValue() == loggedinUserId.intValue())
            doctorAppointment.appointmentStatus = APPOINTMENT_CONFIRMED;
        else
            doctorAppointment.appointmentStatus = APPOINTMENT_TENTATIVE;


        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
        Date date = null;
        try {
            date = format.parse(dateValue.getText().toString());
        }
        catch (ParseException e)
        {

        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);

        Long time = ((DoctorAppointmentGridViewAdapter)timeTeableList.getAdapter()).getSelectedAppointmentTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time);
        calendar2.set(Calendar.YEAR,calendar1.get(Calendar.YEAR));
        calendar2.set(Calendar.MONTH,calendar1.get(Calendar.MONTH));
        calendar2.set(Calendar.DAY_OF_MONTH,calendar1.get(Calendar.DAY_OF_MONTH));
        doctorAppointment.patientId = patientId;
        doctorAppointment.clinicId = clinicId;
        doctorAppointment.doctorId = doctorId;
        doctorAppointment.doctorClinicId = doctorClinicId;
        doctorAppointment.setSequenceNumber(((DoctorAppointmentGridViewAdapter)timeTeableList.getAdapter()).getSelectedSequenceNumber());
        doctorAppointment.setAppointmentDate(calendar2.getTime().getTime());
        doctorAppointment.setVisitType(new Integer(visitType.getSelectedItemPosition()).byteValue());
        doctorAppointment.visitStatus = 2;
        doctorAppointment.type  = 0;
    }
    @Override
    public boolean save()
    {
        if(doctorAppointment.canBeSaved())
        {
            saveAppointment(doctorAppointment);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        if(((DoctorAppointmentGridViewAdapter)timeTeableList.getAdapter()).getSelectedAppointmentTime() == null)
            return false;
        return doctorAppointment.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {


    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
//                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
//                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }

            }
            break;
            case R.id.home: {

            }
            break;

        }
        return true;
    }

    private int[] getWorkingDays(String days)
    {
        StringTokenizer tokenizer = new StringTokenizer(days,",");
        int workingdays[] = new int[tokenizer.countTokens()];
        for(int i = 0; i < workingdays.length;i++)
        {
            String token = tokenizer.nextToken();
            switch (token)
            {
                case "0":
                    workingdays[i] = Calendar.MONDAY;
                    break;
                case "1":
                    workingdays[i] = Calendar.TUESDAY;
                    break;
                case "2":
                    workingdays[i] = Calendar.WEDNESDAY;
                    break;
                case "3":
                    workingdays[i] = Calendar.THURSDAY;
                    break;
                case "4":
                    workingdays[i] = Calendar.FRIDAY;
                    break;
                case "5":
                    workingdays[i] = Calendar.SATURDAY;
                    break;
                case "6":
                    workingdays[i] = Calendar.SUNDAY;
                    break;
            }

        }
        return workingdays;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setHasOptionsMenu(true);
        setTitle("Appointment");
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
}
