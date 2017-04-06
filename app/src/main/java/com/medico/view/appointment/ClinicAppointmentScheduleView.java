package com.medico.view.appointment;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.ClinicAppointmentScheduleAdapter;
import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.AppointmentResponse;
import com.medico.model.ClinicByDoctorRequest;
import com.medico.model.DoctorAppointment;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.DoctorClinicId;
import com.medico.model.DoctorHoliday;
import com.medico.model.DoctorSlotBookings;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicAppointmentScheduleView extends ParentFragment {
    float slideActive = 0f;
    int[] daysOfWeek = {2,3,4,5,6,7,1};
    Date activateDate = new Date();
    Date[] activatedDateRange;
//    TextView slot_name;
    Spinner holidayList;
    TableLayout date_value;
    TableRow dateRow, dayRow;
    ListView appointment_schedule;
    DoctorClinicDetails doctorClinicDetails;
    DoctorClinicDetails.ClinicSlots model;
    List<DoctorHoliday> doctorholidayList;
    List<DoctorSlotBookings> doctorSlotBookings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appointment_schedule_list, container, false);
        setHasOptionsMenu(true);
//        holidayList = (Spinner) view.findViewById(R.id.holidayList);
//        slot_name = (TextView) view.findViewById(R.id.slot_name_for_clinic);
        date_value = (TableLayout) view.findViewById(R.id.date_value);
        appointment_schedule = (ListView)view.findViewById(R.id.appointment_schedule);
//        holidayList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(holidayList.getSelectedItemPosition() == 0) {
//                    holidayList.setBackgroundColor(Color.GREEN);
//                    slot_name.setBackgroundColor(Color.GREEN);
//                }
//                else {
//                    holidayList.setBackgroundColor(Color.RED);
//                    slot_name.setBackgroundColor(Color.RED);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        } );

        date_value.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()  ) {
                    case MotionEvent.ACTION_DOWN:
                        slideActive = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float finalx = event.getX();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(activateDate);

                        if(finalx - slideActive > 0) {

                            calendar.add(Calendar.DATE, -7);
                        }
                        else
                        {
                            calendar.add(Calendar.DATE, 7);

                        }
                        setDateAndDateRange(calendar.getTime());
                        return true;
                    default:
                        return false;
                }

            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorSlotId = bundle.getInt(DOCTOR_CLINIC_ID);
        final Integer doctorId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        api.getClinicByDoctor(new ClinicByDoctorRequest(doctorId, clinicId), new Callback<DoctorClinicDetails>() {
            @Override
            public void success(DoctorClinicDetails clinicDetailsreturn, Response response) {
                doctorClinicDetails = clinicDetailsreturn;
                model = doctorClinicDetails.getSlot(doctorSlotId);
                DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
//                slot_name.setText(model.name + " " + daysOfWeek(model.daysOfWeek) + format.format(model.startTime) + " - " + format.format(model.endTime));
                setDateAndDateRange(activateDate);
                //slot and appointments
                setAdapter(activateDate);
            }

            @Override
            public void failure(RetrofitError error) {
//                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

//    private void setWeekDays(DoctorClinicDetails.ClinicSlots slot)
//    {
//        if(slot == null ) return;
//        Activity activity = getActivity();
//        date_value.setStretchAllColumns(true);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//        dateRow = new TableRow(getActivity());
//        dayRow = new TableRow(getActivity());
//        dateRow.setLayoutParams(lp);
//        dateRow.removeAllViews();
//        dayRow.removeAllViews();
//        List<DoctorClinicDetails.AppointmentCounts> counts = slot.counts;
//        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
//
//        int i = 0;
//        for(DoctorClinicDetails.AppointmentCounts count:counts)
//        {
//            final TextView dateView = new TextView(activity);
//            dateView.setTag(new Date(count.date));
//            dateView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setSelection(dateView);
//                    DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
//                    Date date = (Date)v.getTag();
//                    TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
//                    textviewTitle.setText(format.format(date));
//                    setAdapter(date);
//                }
//            });
//            TextView countView = new TextView(activity);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(new Date(count.date));
//            dateView.setText(new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString());
//            dateView.setBackgroundResource(R.drawable.medicine_schedule);
//            dateView.setLeft(10);
//            dateView.setTop(10);
//            dateView.setRight(10);
//            dateView.setBottom(10);
//            dateRow.addView(dateView,i,lp);
////            dateRow.setTag(new Date(count.date));
//            countView.setText(getDayString(cal.get(Calendar.DAY_OF_WEEK)));
//            countView.setBackgroundResource(R.drawable.medicine_schedule);
//            countView.setLeft(10);
//            countView.setTop(10);
//            countView.setRight(10);
//            countView.setBottom(10);
//            dayRow.addView(countView,i,lp);
//            i++;
//        }
//        date_value.addView(dayRow);
//        date_value.addView(dateRow);
//        date_value.requestLayout();
//    }
    private void setWeekDays()
    {
        date_value.removeAllViews();
        Date[] weekdays = activatedDateRange;
        Activity activity = getActivity();
        date_value.setStretchAllColumns(true);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        dateRow = new TableRow(getActivity());
        dayRow = new TableRow(getActivity());
        dateRow.setLayoutParams(lp);
        dayRow.setLayoutParams(lp);
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        for(int i = 0; i < weekdays.length; i++)
        {
            final TextView dateView = new TextView(activity);
            dateView.setBackgroundColor(Color.WHITE);
            dateView.setTag(weekdays[i]);
            dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activateDate = (Date)v.getTag();
                    setSelection();
                }
            });
            TextView countView = new TextView(activity);
            countView.setBackgroundColor(Color.WHITE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(weekdays[i]);
            dateView.setText(new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString());
            dateView.setBackgroundResource(R.drawable.medicine_schedule);
            dateView.setLeft(10);
            dateView.setTop(10);
            dateView.setRight(10);
            dateView.setBottom(10);
            dateView.setEnabled(isValid(cal.get(Calendar.DAY_OF_WEEK)));
            dateRow.addView(dateView,i,lp);
            countView.setText(getDayString(cal.get(Calendar.DAY_OF_WEEK)));
            countView.setBackgroundResource(R.drawable.medicine_schedule);
            countView.setLeft(10);
            countView.setTop(10);
            countView.setRight(10);
            countView.setBottom(10);
            dayRow.addView(countView,i,lp);
        }
        date_value.addView(dayRow);
        date_value.addView(dateRow);
        date_value.requestLayout();
    }
    private String getDayString(int dayOfWeek)
    {
        switch (dayOfWeek)
        {
            case 1: return "SUN";
            case 2: return "MON";
            case 3: return "TUE";
            case 4: return "WED";
            case 5: return "THU";
            case 6: return "FRI";
            case 7: return "SAT";
        }

        return null;
    }

    public void saveAppointment( DoctorAppointment appointment) {

        if(appointment.appointmentId == null || appointment.appointmentId.intValue() == 0) {

            api.createAppointment(appointment, new Callback<AppointmentResponse>() {
                @Override
                public void success(AppointmentResponse s, Response response) {
                    Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
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
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Appointment update failed!!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void setDate()
    {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                setDateAndDateRange(date);
            }

        };

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(activateDate)
                .build();
        pickerDialog.show();

    }

    private void setAdapter(final Date date)
    {
        final Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        final DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
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
        Integer doctorId = bundle.getInt(PROFILE_ID);
        int numberOfPatients = Math.round((model.endTime - model.startTime)/(model.visitDuration * 60 * 1000));

        api.getDoctorHolidayList(new DoctorHoliday(doctorId,calendar1.getTimeInMillis(),calendar2.getTimeInMillis(), new Integer(2).byteValue()), new Callback<List<DoctorHoliday>>(){
            @Override
            public void success(List<DoctorHoliday> holidayList, Response response) {
                doctorholidayList = holidayList;
//                Toast.makeText(getActivity(), "Holiday Request Send Successfully !!!" + format.format(date1)+" "+ format.format(date2), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "Holiday Request Send FAILED " + format.format(date1)+ " "+ format.format(date2), Toast.LENGTH_LONG).show();
                doctorholidayList = null;
            }
        });

        api.getClinicSlotBookingByDoctor(new DoctorClinicId(model.doctorClinicId,calendar1.getTimeInMillis(),calendar2.getTimeInMillis()), new Callback<List<DoctorSlotBookings>>(){
            @Override
            public void success(List<DoctorSlotBookings> slotBookingses, Response response) {
                if(slotBookingses != null && slotBookingses.size() > 0) {
                    doctorSlotBookings = slotBookingses;
                    ClinicAppointmentScheduleAdapter adapter = new ClinicAppointmentScheduleAdapter(activity, model, doctorClinicDetails,doctorSlotBookings, doctorholidayList, date1);
                    appointment_schedule.setAdapter(adapter);
                }
//                Toast.makeText(getActivity(), "Request Send Successfully !!!" + format.format(date1)+" "+ format.format(date2), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "Request Send FAILED " + format.format(date1)+ " "+ format.format(date2), Toast.LENGTH_LONG).show();
                doctorSlotBookings = null;
                ClinicAppointmentScheduleAdapter adapter = new ClinicAppointmentScheduleAdapter(activity, model, doctorClinicDetails,doctorSlotBookings, doctorholidayList, date1);
                appointment_schedule.setAdapter(adapter);
                error.printStackTrace();
            }
        });
    }

    private String daysOfWeek(String days)
    {
        String[] daysNumber = {"0,1,2,3,4,5,6","0,1,2,3,4,5","0,1,2,3,4","0,1,2,3","0,1,2","0,1","0",
                "1,2,3,4,5,6","1,2,3,4,5","1,2,3,4","1,2,3","1,2","1",
                "2,3,4,5,6","2,3,4,5","2,3,4","2,3","2",
                "3,4,5,6","3,4,5","3,4","3",
                "4,5,6","4,5","4",
                "5,6","5",
                "6"};
        String[] daysWord = {"MON-SUN","MON-SAT","MON-FRI","MON-THU","MON-WED","MON-TUE","MON",
                "TUE-SUN","TUE-SAT","TUE-FRI","TUE-THU","TUE-WED","TUE",
                "WED-SUN","WED-SAT","WED-FRI","WED-THU","WED",
                "THU-SUN","THU-SAT","THU-FRI","THU",
                "FRI-SUN","FRI-SAT","FRI",
                "SAT-SUN","SAT",
                "SUN"};

        for(int i = 0; i < daysNumber.length;i++)
        {
            days = days.replace(daysNumber[i],daysWord[i]);
        }

        return days;

    }
    private void setSelection()
    {
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(format.format(activateDate));
        setAdapter(activateDate);
        for(int i = 0; i < dateRow.getChildCount();i++)
        {
            TextView textview = (TextView)dateRow.getChildAt(i);
            Date date = (Date)textview.getTag();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());

            if(calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                    && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                    && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
            {
                textview.setTextColor(Color.GREEN);
            }
            else if(textview.isEnabled())
                textview.setTextColor(Color.BLACK);
            else
                textview.setTextColor(Color.LTGRAY);

            if(date.equals(activateDate)) {
                textview.setTextColor(Color.BLUE);
            }
        }
    }

    private boolean isValid(int dayOfWeek)
    {
        boolean isValid = false;
        StringTokenizer tokenizer = new StringTokenizer(model.daysOfWeek,",");
        for(;tokenizer.hasMoreTokens();)
        {
            Integer day = new Integer(tokenizer.nextToken());
            if(daysOfWeek[day.intValue()] == dayOfWeek )
                return true;
        }

        return isValid;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        inflater.inflate(R.menu.doctor_manage_appointment_schedule, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setChecked(true);
        menuItem.setIcon(R.drawable.calendar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
            {
                setDate();

            }
            break;
            case R.id.addFilter:
            {
                if(item.isChecked())
                {
                    setDateAndDateRange(activateDate);
                    item.setChecked(false);
                }
                else
                {
                    filterDateAndDateRange();
                    item.setChecked(true);
                }


            }
            break;
        }
        return true;
    }

    public Date[] daysOfTheWeek(Date date)
    {
        Calendar calendar = GregorianCalendar.getInstance();//Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(date.getTime());
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        Date[] weekDays = new Date[7];
        for(int i = 0; i < weekDays.length; i++ )
        {
            calendar.add(Calendar.DATE, i==0?i:1);
            weekDays[i] = new Date(calendar.getTime().getTime());
        }
        return weekDays;
    }

//    public Date getFirstValidDayOfWeek(Date date)
//    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        Date[] weekDays = getFirstDatyOfWeek(date);
//        for(int i = 0; i < weekDays.length; i++ )
//        {
//            if(isValid(calendar.get(Calendar.DAY_OF_WEEK)))
//                return date;
//        }
//        return null;
//    }

    public void setDateAndDateRange(Date date)
    {
        activatedDateRange = daysOfTheWeek(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isValid(calendar.get(Calendar.DAY_OF_WEEK)))
            activateDate = date;
        else
        {
            for(int i = 0; i < activatedDateRange.length; i++)
            {
                calendar.clear();
                calendar.setTime(activatedDateRange[i]);
                if(isValid(calendar.get(Calendar.DAY_OF_WEEK))) {
                    activateDate = activatedDateRange[i];
                    break;
                }
            }
        }
        setWeekDays();
        setSelection();
    }
    public void filterDateAndDateRange()
    {
        List<DoctorClinicDetails.AppointmentCounts> counts = model.counts;
        if(counts != null && counts.size() > 1) {
            activatedDateRange = new Date[counts.size()];
            int i = 0;
            for(DoctorClinicDetails.AppointmentCounts date:counts)
            {
                activatedDateRange[i] = new Date(date.date);
                i++;
            }
            activateDate = activatedDateRange[0];
            setWeekDays();
            setSelection();
        }
    }
}
