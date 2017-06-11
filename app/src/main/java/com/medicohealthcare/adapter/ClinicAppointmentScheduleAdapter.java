package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.AppointmentResponse;
import com.medicohealthcare.model.AppointmentStatusRequest;
import com.medicohealthcare.model.DoctorAppointment;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.DoctorHoliday;
import com.medicohealthcare.model.DoctorSlotBookings;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.ClinicDoctorAppointmentView;
import com.medicohealthcare.view.profile.DoctorAppointmentInformation;
import com.medicohealthcare.view.profile.FeedbackFragmentClinicAppointment;
import com.medicohealthcare.view.profile.PatientDetailsView;
import com.medicohealthcare.view.search.PersonSearchView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicAppointmentScheduleAdapter extends HomeAdapter  {


    private Activity activity;
    private LayoutInflater inflater;
    DoctorClinicDetails details;
    DoctorClinicDetails.ClinicSlots model;
    List<DoctorSlotBookings> slotBookingses;
    List<DoctorHoliday> holidayList;
    List<AppointmentHolder> holders;
    Date date;

    String[] emptyAppointment = {"","Add Appointment"};
    String[] filledAppointment = {"","Reschedule Appointment","Cancel Appointment","Appointment Feedback"};

    public ClinicAppointmentScheduleAdapter(Activity activity, DoctorClinicDetails.ClinicSlots model, DoctorClinicDetails details,List<DoctorSlotBookings> slotBookingses, List<DoctorHoliday> holidayList, Date date) {
        super(activity);
        this.details = details;
        this.activity = activity;
        this.slotBookingses = slotBookingses;
        this.holidayList = holidayList;
        this.model = model;
        this.date = date;
        holders = createAppointmentHolders( model, slotBookingses,holidayList, date);
    }

    @Override
    public int getCount() {

        return holders.size();
    }

    @Override
    public Object getItem(int position) {
        return holders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.patient_appointment, null);

        final AppointmentHolder holder = holders.get(position);
        //Appointment headings
        TextView appointment_number = (TextView)convertView.findViewById(R.id.appointment_number);
        TextView appointment_time = (TextView)convertView.findViewById(R.id.appointment_time);
        final Spinner appointment_status = (Spinner)convertView.findViewById(R.id.appointment_status);
        final Spinner appointment_type = (Spinner)convertView.findViewById(R.id.appointment_type);
        final Spinner appointment_visit_status = (Spinner)convertView.findViewById(R.id.appointment_visit_status);
        TextView patient_name = (TextView) convertView.findViewById(R.id.patient_name);
        TextView speciality = (TextView) convertView.findViewById(R.id.speciality);
        ImageView patient_image = (ImageView) convertView.findViewById(R.id.patient_image);
        LinearLayout appointmentHeader = (LinearLayout) convertView.findViewById(R.id.appointment_header);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView upcomingappointmentDate = (TextView) convertView.findViewById(R.id.review_value);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        totalCount.setVisibility(View.GONE);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        TextView totalAppointment = (TextView) convertView.findViewById(R.id.total_appointment);
        final Spinner appointment_menu = (Spinner)convertView.findViewById(R.id.appointment_menu);
        totalAppointment.setVisibility(View.GONE);
        RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.profile);
        final RelativeLayout parentLayout = (RelativeLayout)convertView.findViewById(R.id.layout);
        patient_image.setBackgroundResource(R.drawable.patient_default);
        appointment_number.setText(new Integer(holder.sequenceNumber).toString());
        appointment_time.setText(holder.getTime());
        appointment_visit_status.setSelection(holder.getVisitStatus());
        appointment_menu.setTag(holder);
        String[] menuArray = holder.patient != null? filledAppointment: emptyAppointment;
        appointment_menu.setAdapter(new ArrayAdapter<String>(activity,R.layout.appointment_spinner_layout, menuArray)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ImageView view = new ImageView(activity);
                view.setImageResource(R.drawable.ic_reorder_black_24dp);
                return view;
            }
        });
        appointment_menu.setTag(holder);

        appointment_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   if(parent.getSelectedItem().equals(emptyAppointment[1])) //new appointment
                   {
                       bookOnline(holder);
                   }
                   else if (parent.getSelectedItem().equals(filledAppointment[1])) //Reschedule
                   {
                       rescheduleAppointment(holder);
                   }
                   else if (parent.getSelectedItem().equals(filledAppointment[2])) //Cancel
                   {
                        cancelAppointment(holder);
                   }

                   else if (parent.getSelectedItem().equals(filledAppointment[3])) //Feedback
                   {
                        feedbackAppointment(holder);
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

        if(holder.patient != null)
        {
            layout.setVisibility(View.VISIBLE);
            DoctorSlotBookings.PersonBooking booking = holder.patient;
            final Person patient = booking.patient;
            if(patient.getStatus()==PARAM.VISIT_STATUS_VISITED)
                rightButton.setVisibility(View.VISIBLE);
            else
                rightButton.setVisibility(View.GONE);
            if(patient.getImageUrl() != null)
                new ImageLoadTask( patient.getImageUrl(), patient_image).execute();

            totalCount.setText(booking.numberOfVisits.toString());

            if (patient.getAddress() != null)
                    address.setText(patient.getAddress());

            if (booking.upcomingAppointments != null) {
                    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
                upcomingappointmentDate.setText(format.format(new Date(booking.upcomingAppointments)));

            }
            else
                upcomingappointmentDate.setText("None");

            if (booking.lastAppointment != null) {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
                lastVisitedValue.setText(format.format(new Date(booking.lastAppointment)));

            }
            else
                lastVisitedValue.setText("None");

            patient_name.setText(patient.getName());

            speciality.setText(patient.speciality);

            downImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParentActivity parentactivity = (ParentActivity)activity;
                    Bundle bundle = activity.getIntent().getExtras();
                    bundle.putInt(PARAM.PATIENT_ID, patient.getId());
                    parentactivity.getIntent().putExtras(bundle);
                    ParentFragment fragment = new PatientDetailsView();
//                    parentactivity.attachFragment(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.service, fragment, PatientDetailsView.class.getName()).addToBackStack(PatientDetailsView.class.getName()).commit();
                }
            });


            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bun = activity.getIntent().getExtras();
                    bun.putInt(PARAM.APPOINTMENT_ID, holder.patient.appointmentId);
                    bun.putLong(PARAM.APPOINTMENT_DATETIME, holder.date.getTime());
                    bun.putString(PARAM.CLINIC_NAME, holder.details.clinic.clinicName);
                    bun.putInt(PARAM.CLINIC_ID, holder.details.clinic.idClinic);
                    activity.getIntent().putExtras(bun);
                    ParentFragment fragment = new DoctorAppointmentInformation();
//                    ((ParentActivity)activity).attachFragment(fragment);
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, DoctorAppointmentInformation.class.getName()).addToBackStack(DoctorAppointmentInformation.class.getName()).commit();
                }
            });
            appointment_status.setAdapter(new ArrayAdapter<String>(activity,R.layout.appointment_spinner_layout,activity.getResources().getStringArray(R.array.appointment_status)));
            appointment_status.setSelection(holder.getAppointmentStatus(),false);
            appointment_type.setVisibility(View.VISIBLE);
            appointment_type.setAdapter(new ArrayAdapter<String>(activity,R.layout.appointment_spinner_layout,activity.getResources().getStringArray(R.array.visit_type_list)));
            appointment_visit_status.setVisibility(View.VISIBLE);
            appointment_visit_status.setAdapter(new ArrayAdapter<String>(activity,R.layout.appointment_spinner_layout,activity.getResources().getStringArray(R.array.visit_status)));
            appointment_type.setSelection(holder.getVisitType(),false);
            appointment_visit_status.setSelection(holder.getVisitStatus(),false);
            appointment_status.setTag(holder.getAppointmentStatus());
            appointment_type.setTag(holder.getVisitType());
            appointment_visit_status.setTag(holder.getVisitStatus());
        }
        else
        {
            layout.setVisibility(View.GONE);
            appointment_type.setVisibility(View.GONE);
            appointment_visit_status.setVisibility(View.GONE);
            if(holder.isHoliday)
            {
                appointmentHeader.setBackgroundResource(R.color.medico_absence_heading);
                parentLayout.setBackgroundResource(R.color.medico_absence);
                appointment_menu.setEnabled(false);
            }
            else
            {
                appointmentHeader.setBackgroundResource(R.color.medico_green1);
                parentLayout.setBackgroundColor(Color.WHITE);
                appointment_menu.setEnabled(true);
            }
            appointment_status.setAdapter(new ArrayAdapter<String>(activity,R.layout.appointment_spinner_layout,activity.getResources().getStringArray(R.array.no_appointment_status)));
            appointment_status.setSelection(holder.isHoliday?1:0,false);
            appointment_status.setTag(holder.isHoliday?1:0);

        }
        appointment_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(appointment_status.getTag().equals(position))
                    return;
                if(holder.patient != null)
                {
                    api.setAppointmentStatus(new AppointmentStatusRequest(holder.patient.appointmentId,position,AppointmentStatusRequest.APPOINTMENT_STATUS), new Callback<ServerResponse>() {
                        @Override
                        public void success(ServerResponse responseCodeVerfication, Response response) {
                            Toast.makeText(activity, "Appointment Status update is Successful!!", Toast.LENGTH_LONG).show();
                            appointment_status.setTag(position);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            hideBusy();
                            new MedicoCustomErrorHandler(activity).handleError(error);
                        }
                    });
                }
                else
                {
                    if(position == 1) {
                        if(!holder.isHoliday) {
                            final Bundle bundle = activity.getIntent().getExtras();
                            api.addDoctorHoliday(new DoctorHoliday(bundle.getInt(PARAM.DOCTOR_ID), holder.date.getTime(), holder.date.getTime(), new Integer(0).byteValue(), holder.sequenceNumber, holder.details.clinic.idClinic,holder.model.doctorClinicId), new Callback<ServerResponse>() {
                                @Override
                                public void success(ServerResponse responseCodeVerfication, Response response) {
                                    Toast.makeText(activity, "Add doctor holiday is Successful!!", Toast.LENGTH_LONG).show();
                                    appointment_status.setTag(1);
                                    holder.setHoliday(true);
                                    notifyDataSetInvalidated();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    hideBusy();
                                    new MedicoCustomErrorHandler(activity).handleError(error);
                                }
                            });
                        }
                    }
                    else
                    {
                        DoctorHoliday holiday = holder.getHolidayForAppointmentSequence();
                        if(holder.isHoliday && holiday != null) {
                            final Bundle bundle = activity.getIntent().getExtras();
                            api.removeDoctorHoliday(new DoctorHoliday(holiday.idHoliday,holiday.sequenceNo), new Callback<ServerResponse>() {
                                @Override
                                public void success(ServerResponse responseCodeVerfication, Response response) {
                                    Toast.makeText(activity, "Remove doctor holiday is Successful!!", Toast.LENGTH_LONG).show();
                                    appointment_status.setTag(0);
                                    holder.setHoliday(false);
                                    notifyDataSetInvalidated();

                                }

                                @Override
                                public void failure(RetrofitError error)
                                {
                                    hideBusy();
                                    new MedicoCustomErrorHandler(activity).handleError(error);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        appointment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(appointment_type.getTag().equals(position))
                    return;
                if(holder.patient != null)
                {
                    api.setAppointmentVisitType(new AppointmentStatusRequest(holder.patient.appointmentId,position,AppointmentStatusRequest.VISIT_TYPE), new Callback<ServerResponse>() {
                        @Override
                        public void success(ServerResponse responseCodeVerfication, Response response) {
                            Toast.makeText(activity, "Set Appointment Visit Type is Successful!!", Toast.LENGTH_LONG).show();
                            appointment_type.setTag(position);
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            hideBusy();
                            new MedicoCustomErrorHandler(activity).handleError(error);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        appointment_visit_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(appointment_visit_status.getTag().equals(position))
                    return;
                if(holder.patient != null)
                {
                    api.setAppointmentVisitStatus(new AppointmentStatusRequest(holder.patient.appointmentId,position,AppointmentStatusRequest.VISIT_STATUS), new Callback<ServerResponse>() {
                        @Override
                        public void success(ServerResponse responseCodeVerfication, Response response) {
                            Toast.makeText(activity, "Set Appointment Visit Status is Successful!!", Toast.LENGTH_LONG).show();
                            appointment_visit_status.setTag(position);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            hideBusy();
                            new MedicoCustomErrorHandler(activity).handleError(error);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return convertView;

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

    class AppointmentHolder
    {
        int position = 0;
        int sequenceNumber;
        boolean isHoliday = false;
        DoctorClinicDetails details;
        DoctorSlotBookings.PersonBooking patient;
        DoctorClinicDetails.ClinicSlots model;
        Date date;

        public AppointmentHolder(int i, DoctorClinicDetails.ClinicSlots model, DoctorClinicDetails details, DoctorSlotBookings.PersonBooking personBooking,List<DoctorHoliday> doctorHolidays , Date date)
        {
            sequenceNumber = i+1;
            this.model = model;
            this.details = details;
            this.patient = personBooking;
            setAppointmentDateTime(i, date, model.startTime, model.endTime, model.visitDuration);
            setDoctorHoliday(doctorHolidays);
        }

        private void setAppointmentDateTime(int i, Date date, long startTime, long endTime, int visitDuration)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            long appointmentTime = startTime + i * visitDuration * 60 * 1000;
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(appointmentTime);
            calendar2.set(Calendar.YEAR,calendar1.get(Calendar.YEAR));
            calendar2.set(Calendar.MONTH,calendar1.get(Calendar.MONTH));
            calendar2.set(Calendar.DAY_OF_MONTH,calendar1.get(Calendar.DAY_OF_MONTH));
            this.date = calendar2.getTime();
        }
        private void setDoctorHoliday(List<DoctorHoliday> holidays)
        {
            if(holidays != null && !holidays.isEmpty())
            {
                for(DoctorHoliday holiday:holidays)
                {
                    switch (holiday.type) {
                        case 2:
                            isHoliday = true;
                            break;
                        case 1:
                            isHoliday = true;
                            break;
                        case 0:
                            if (sequenceNumber == holiday.sequenceNo) {
                                isHoliday = true;
                            }
                            break;
                        default:
                            isHoliday = false;
                    }
                }
            }
        }
        public String getTime()
        {
            DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
            return format.format(date);
        }
        public int getAppointmentStatus()
        {
            if(patient == null)
                return PARAM.APPOINTMENT_AVAILABLE;
            else
                return patient.appointmentStatus;
        }
        public int getVisitStatus()
        {
            if(patient == null)
                return PARAM.VISIT_STATUS_EMPTY;
            else
                return patient.visitStatus;
        }

        public int getVisitType()
        {
            if(patient == null)
                return PARAM.VISIT_TYPE_NONE;
            else
                return patient.visitType;
        }
        public boolean isHoliday()
        {
            return isHoliday;
        }
        public void setHoliday(boolean holiday)
        {
            isHoliday = holiday;
        }
        public DoctorHoliday getHolidayForAppointmentSequence()
        {
            if(holidayList != null && !holidayList.isEmpty())
            {
                for (DoctorHoliday holiday : holidayList) {
                    if (holiday.type == 0 && holiday.sequenceNo == sequenceNumber)
                        return holiday;
                }
            }
            return null;
        }
    }

    private List<AppointmentHolder> createAppointmentHolders( DoctorClinicDetails.ClinicSlots model, List<DoctorSlotBookings> slotBookingses, List<DoctorHoliday> holidayList, Date date)
    {
        List<DoctorSlotBookings.PersonBooking> bookings = null;
        List<DoctorHoliday> doctorHolidays = null;

        if(slotBookingses != null && slotBookingses.size() > 0) {
            for (DoctorSlotBookings booking : slotBookingses) {
                if (booking.doctorClinicSlotId.intValue() == model.doctorClinicId.intValue())
                    bookings = booking.bookings;
            }
        }
        if(holidayList != null && holidayList.size() > 0) {
            for (DoctorHoliday holiday : holidayList) {
                if (holiday.doctorClinicId.intValue() == model.doctorClinicId.intValue()) {
                    if (doctorHolidays == null)
                        doctorHolidays = new ArrayList<>();
                    doctorHolidays.add(holiday);
                }
            }
        }
        List<AppointmentHolder> holders = new ArrayList<>();

        for(int i = 0; i < model.numberOfPatients;i++)
        {
            if(bookings != null)
            {
                boolean found = false;
                for (DoctorSlotBookings.PersonBooking bookings1:bookings)
                {
                    if(bookings1.sequenceNo.intValue() == i+1)
                    {
                        found = true;
                        holders.add(new AppointmentHolder(i,model, details,bookings1,doctorHolidays, date));
                        break;
                    }
                }
                if(found == false)
                    holders.add(new AppointmentHolder(i,model, details, null ,doctorHolidays, date));
            }
            else
                holders.add(new AppointmentHolder(i,model,details,null,doctorHolidays, date));
        }
        return holders;
    }

    private void bookOnline(AppointmentHolder holder)
    {
        Bundle bundle = activity.getIntent().getExtras(); 
        bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.PATIENT_SETTING_VIEW);
        bundle.putInt(PARAM.SEARCH_TYPE,PARAM.APPOINTMENT_BOOKING);
        bundle.putInt(PARAM.SEARCH_ROLE,PARAM.PATIENT);
        activity.getIntent().putExtras(bundle);
        PersonSearchView fragment = new PersonSearchView();
        fragment.setAdapter(this,holder);
//        ((ParentActivity)activity).attachFragment(fragment);
        fragment.setArguments(bundle); 
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service,fragment,PersonSearchView.class.getName()).addToBackStack(PersonSearchView.class.getName()).commit();
    }
    public void callBack(int id, final Object source, Object parameter)
    {
        Bundle bundle = activity.getIntent().getExtras();
        final AppointmentHolder holder = (AppointmentHolder)parameter;
        final DoctorAppointment request = new DoctorAppointment();
        request.doctorId = bundle.getInt(PARAM.PROFILE_ID);
        request.patientId = id;
        request.clinicId = holder.details.clinic.idClinic;
        request.appointmentDate = holder.date.getTime();
        request.type = holder.details.clinic.type;
        request.sequenceNumber = holder.sequenceNumber;
        request.appointmentStatus = PARAM.APPOINTMENT_CONFIRMED;
        request.visitType = PARAM.VISIT_TYPE_NEWCASE;
        request.visitStatus = PARAM.VISIT_STATUS_UNKNOWN;
        request.doctorClinicId = holder.model.doctorClinicId;

        api.createAppointment(request, new Callback<AppointmentResponse>() {
            @Override
            public void success(AppointmentResponse s, Response response) {
                Toast.makeText(activity, "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                request.appointmentId = s.appointmentId;
//                holder.update(request, source);

            }
            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(activity).handleError(error);
            }
        });

    }

    private void rescheduleAppointment(AppointmentHolder holder)
    {

        Bundle bundle = activity.getIntent().getExtras();
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(holder.model.startTime) +" - " + formatTime.format(holder.model.endTime);
        bundle.putInt(PARAM.PATIENT_ID,holder.patient.patient.getId());
        bundle.putInt(PARAM.APPOINTMENT_ID,holder.patient.appointmentId);
        bundle.putLong(PARAM.APPOINTMENT_DATETIME, holder.date.getTime());
        bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, holder.patient.sequenceNo);
        bundle.putInt(PARAM.CLINIC_ID, holder.details.clinic.idClinic);
        bundle.putString(PARAM.CLINIC_NAME,holder.details.clinic.clinicName);
        bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
        bundle.putInt(PARAM.DOCTOR_CLINIC_ID,holder.model.doctorClinicId);
        bundle.putInt(PARAM.SLOT_VISIT_DURATION,model.visitDuration);
        bundle.putLong(PARAM.SLOT_START_DATETIME,model.startTime);
        bundle.putLong(PARAM.SLOT_END_DATETIME,model.endTime);
        activity.getIntent().putExtras(bundle);
        ParentFragment fragment = new ClinicDoctorAppointmentView();
//        ((ParentActivity)activity).attachFragment(fragment);
        fragment.setArguments(bundle);
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service,fragment,ClinicDoctorAppointmentView.class.getName()).addToBackStack(ClinicDoctorAppointmentView.class.getName()).commit();
    }

    private void feedbackAppointment(AppointmentHolder holder)
    {
        Bundle bundle = activity.getIntent().getExtras();
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(holder.model.startTime) +" - " + formatTime.format(holder.model.endTime);
//        bundle.putInt(PARAM.DOCTOR_ID, bundle.getInt(PARAM.LOGGED_IN_ID));
//        public Long visitDate;
//        public Byte visitType;
//        public String referredBy;
//        public Integer clinicId;
//        public String clinicName;
        bundle.putLong(PARAM.APPOINTMENT_DATETIME,holder.date.getTime());
        bundle.putInt(PARAM.VISIT_TYPE,holder.patient.visitType);
        bundle.putString(PARAM.CLINIC_NAME,holder.details.clinic.clinicName);
        bundle.putInt(PARAM.PATIENT_ID,holder.patient.patient.getId());
        bundle.putInt(PARAM.APPOINTMENT_ID,holder.patient.appointmentId);
        bundle.putLong(PARAM.APPOINTMENT_DATETIME, holder.date.getTime());
        bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, holder.sequenceNumber);
        bundle.putInt(PARAM.CLINIC_ID, holder.details.clinic.idClinic);
        bundle.putString(PARAM.CLINIC_NAME,holder.details.clinic.clinicName);
        bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
        bundle.putInt(PARAM.DOCTOR_CLINIC_ID,holder.model.doctorClinicId);
        bundle.putInt(PARAM.SLOT_VISIT_DURATION,model.visitDuration);
        bundle.putLong(PARAM.SLOT_START_DATETIME,model.startTime);
        bundle.putLong(PARAM.SLOT_END_DATETIME,model.endTime);
        activity.getIntent().putExtras(bundle);
        ParentFragment fragment = new FeedbackFragmentClinicAppointment();
//        ((ParentActivity)activity).attachFragment(fragment);
        fragment.setArguments(bundle);
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service,fragment,FeedbackFragmentClinicAppointment.class.getName()).addToBackStack(FeedbackFragmentClinicAppointment.class.getName()).commit();
    }
    private void cancelAppointment(final AppointmentHolder holder)
    {
        showBusy();
        new AlertDialog.Builder(activity)
                .setTitle("Delete Appointment")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        api.cancelAppointment(new AppointmentId1(holder.patient.appointmentId), new Callback<AppointmentResponse>() {
                            @Override
                            public void success(AppointmentResponse result, Response response) {
                                hideBusy();
                                Toast.makeText(activity, "Medicine Removed!", Toast.LENGTH_SHORT).show();
                                holder.patient = null;
                                adapter.notifyDataSetChanged();
//                                        }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                hideBusy();
                                new MedicoCustomErrorHandler(activity).handleError(error);
                            }
                        });
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


}
