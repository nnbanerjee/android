package com.medico.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.medico.model.DoctorAppointmentGridViewAdapter;
import com.medico.model.DoctorClinicId;
import com.medico.model.DoctorSlotBookings;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */


public class ClinicDoctorAppointmentFragment extends ParentFragment {

    ProgressDialog progress;

    GridView timeTeableList;
    TextView slotTime,slotName,clinicName,dateValue;
    Button timeBtn;

    Spinner visitType;
    String userId,appointmentTime,appointmentDate;

    DoctorSlotBookings doctorSlotBookings = new DoctorSlotBookings();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_appointment_fragment, container,false);

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
//        timeTeableList.setOnTouchListener(new ListView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
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
//                        break;
//                }
//                v.onTouchEvent(event);
//                return true;
//            }
//        });


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        clinicName.setText(bundle.getString(CLINIC_NAME));
        slotTime.setText(bundle.getString(SLOT_TIME));
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Long datetime = bundle.getLong(APPOINTMENT_DATE);
        Date date;
        if(datetime == null || datetime.intValue() == 0)
            date = new Date();
        else
            date = new Date(datetime);
        setAdapter(date);
//
//        api1.getAllClinicsAppointment(doctorId, clinicId, clinicShift, dateValue.getText().toString(), new Callback<List<ClinicAppointment>>() {
//            @Override
//            public void success(List<ClinicAppointment> clinicAppointments, Response response) {
//
//                System.out.println("clinicAppointments = "+clinicAppointments.size());
//                loadAppointmentData(clinicAppointments);
//
//                progress.dismiss();
//            }
//
//            @Override
//
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                error.printStackTrace();
//            }
//        });
        progress.dismiss();

    }

//    public void savePatientReminderData(PatientMedicine saveReminderVM) {
//
//        if (saveReminderVM.getMedicineId()== null || saveReminderVM.getMedicineId().intValue() == 0 )
//        {
//            api.addPatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
//                @Override
//                public void success(ResponseCodeVerfication jsonObject, Response response) {
//                    System.out.println("Response::::::" + response.getStatus());
//                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
//                    getActivity().onBackPressed();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                    error.printStackTrace();
//                }
//            });
//        }
//        else
//        {
//            System.out.println("I am Update Conidtion::::::::::::::::::::");
//            api.updatePatientMedicine(saveReminderVM, new Callback<ResponseCodeVerfication>() {
//                @Override
//                public void success(ResponseCodeVerfication reminderVM, Response response) {
//                    System.out.println("Response::::::" + response.getStatus());
//                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
//                    getActivity().onBackPressed();
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
//
    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                dateField.setText(format.format(date));
//                doctorSlotBookings.date = (date.getTime());
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

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .build();
        pickerDialog.show();
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        menu.clear();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.add);
//        menuItem.setIcon(R.drawable.save);
//    }
//
//    @Override
//    public boolean isChanged()
//    {
//        return patientMedicine.isChanged();
//    }
//    @Override
//    protected void update()
//    {
//        Bundle bundle1 = getActivity().getIntent().getExtras();
//        patientMedicine.setMedicinName(medicineName.getText().toString());
//        patientMedicine.setDosesPerSchedule(new Integer(numberOfDosesPerSchedule.getText().toString()));
//        patientMedicine.setSchedule(new Integer(scheduleDate.getSelectedItemPosition()).byteValue());
//        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
////      patientMedicine.setStartDate(format.parse(startDateEdit.getText().toString()).getTime());
//        endDateEdit.setText(format.format(patientMedicine.getEndDate()));
//        patientMedicine.setDoctorInstruction(doctorInstructionValue.getText().toString());
//        patientMedicine.setDurationSchedule(new Integer(duration.getText().toString()));
//        patientMedicine.setReminder(medicineReminderBtn.isChecked()? new Integer(1).byteValue():new Integer(0).byteValue());
//        patientMedicine.setAutoSchedule(autoScheduleBtn.isChecked()?new Integer(1).byteValue():new Integer(0).byteValue());
//        patientMedicine.setType(1);
//        long[] schedule = patientMedicine.getAlarmSchedule();
//        List<PatientMedicine.MedicineSchedule> scheduleList = new ArrayList<>();
//        for(int i = 0; i < schedule.length; i++)
//        {
//            scheduleList.add(new PatientMedicine.MedicineSchedule(schedule[i]));
//        }
//        patientMedicine.setMedicineSchedule(scheduleList);
//    }
//    @Override
//    protected boolean save()
//    {
//        if(patientMedicine.canBeSaved())
//        {
//            savePatientReminderData(patientMedicine);
//            return true;
//        }
//        return false;
//    }
//    @Override
//    protected boolean canBeSaved()
//    {
//        return patientMedicine.canBeSaved();
//    }
//    @Override
//    protected void setEditable(boolean editable)
//    {
//        medicineName.setEnabled(editable);
//        numberOfDosesPerSchedule.setEnabled(editable);
//        scheduleDate.setEnabled(editable);
//        calenderImg.setEnabled(editable);
//        doctorInstructionValue.setEnabled(editable);
//        duration.setEnabled(editable);
//        medicineReminderBtn.setEnabled(editable);
//        autoScheduleBtn.setEnabled(editable);
//
//    }
    private void setAdapter(Date date)
    {
        final Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        clinicName.setText(bundle.getString(CLINIC_NAME));
        slotTime.setText(bundle.getString(SLOT_TIME));
        final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY,0);
        calendar1.set(Calendar.MINUTE,1);
        calendar1.set(Calendar.SECOND,0);
        final Date date1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.set(Calendar.HOUR_OF_DAY,23);
        calendar2.set(Calendar.MINUTE,59);
        calendar2.set(Calendar.SECOND,0);
        final Date date2 = calendar2.getTime();
        Integer slotId = bundle.getInt(DOCTOR_CLINIC_ID);
        Long startTime = bundle.getLong(SLOT_START_DATETIME);
        Long endTime = bundle.getLong(SLOT_END_DATETIME);
        Integer vistDuration = bundle.getInt(SLOT_VISIT_DURATION);
        int numberOfPatients = Math.round((endTime - startTime)/(vistDuration * 60 * 1000));

        api.getClinicSlotBookingByDoctor(new DoctorClinicId(slotId,calendar1.getTimeInMillis(),calendar2.getTimeInMillis()), new Callback<List<DoctorSlotBookings>>(){
            @Override
            public void success(List<DoctorSlotBookings> slotBookingses, Response response) {
                if(slotBookingses != null && slotBookingses.size() > 0) {
                    doctorSlotBookings = slotBookingses.get(0);
                    DoctorAppointmentGridViewAdapter adapter = new DoctorAppointmentGridViewAdapter(activity, doctorSlotBookings, null);
                    timeTeableList.setAdapter(adapter);
                }
                Toast.makeText(getActivity(), "Request Send Successfully !!!" + format.format(date1)+" "+ format.format(date2), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Request Send FAILED " + format.format(date1)+ " "+ format.format(date2), Toast.LENGTH_LONG).show();
                doctorSlotBookings = null;
                DoctorAppointmentGridViewAdapter adapter = new DoctorAppointmentGridViewAdapter(activity, doctorSlotBookings, null);
                timeTeableList.setAdapter(adapter);
                error.printStackTrace();
            }
        });
    }
}
