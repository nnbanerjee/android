package com.medico.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.DoctorHoliday;
import com.medico.model.DoctorSlotBookings;
import com.medico.model.Person;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.ManagePatientProfile;
import com.medico.view.ParentFragment;
import com.medico.view.PatientDetailsFragment;
import com.medico.view.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicAppointmentScheduleAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    DoctorClinicDetails.ClinicSlots model;
    List<DoctorSlotBookings> slotBookingses;
    List<DoctorHoliday> holidayList;
    List<AppointmentHolder> holders;
    Date date;

    public ClinicAppointmentScheduleAdapter(Activity activity, DoctorClinicDetails.ClinicSlots model, List<DoctorSlotBookings> slotBookingses, List<DoctorHoliday> holidayList, Date date) {
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MyApi api = restAdapter.create(MyApi.class);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.patient_appointment, null);

        AppointmentHolder holder = holders.get(position);
        //Appointment headings
        TextView appointment_number = (TextView)convertView.findViewById(R.id.appointment_number);
        TextView appointment_time = (TextView)convertView.findViewById(R.id.appointment_time);
        Spinner appointment_status = (Spinner)convertView.findViewById(R.id.appointment_status);
        Spinner appointment_type = (Spinner)convertView.findViewById(R.id.appointment_type);
        Spinner appointment_visit_status = (Spinner)convertView.findViewById(R.id.appointment_visit_status);
//        global = (Global) activity.getApplicationContext();
        TextView patient_name = (TextView) convertView.findViewById(R.id.patient_name);
        TextView speciality = (TextView) convertView.findViewById(R.id.speciality);
        ImageView patient_image = (ImageView) convertView.findViewById(R.id.patient_image);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView upcomingappointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        TextView totalAppointment = (TextView) convertView.findViewById(R.id.total_appointment);
        Spinner appointment_menu = (Spinner)convertView.findViewById(R.id.appointment_menu);
        appointment_menu.setAdapter(new AppointmentMenuAdapter(activity,holder));
        totalAppointment.setVisibility(View.GONE);
        RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.profile);

        patient_image.setBackgroundResource(R.drawable.patient);

        appointment_number.setText(new Integer(holder.sequenceNumber).toString());
        appointment_time.setText(holder.getTime());
        appointment_status.setSelection(holder.getAppointmentStatus());
        appointment_type.setSelection(holder.getVisitType());
        appointment_visit_status.setSelection(holder.getVisitStatus());

        if(holder.patient != null)
        {
            layout.setVisibility(View.VISIBLE);
            appointment_type.setVisibility(View.VISIBLE);
            appointment_visit_status.setVisibility(View.VISIBLE);
            appointment_type.setSelection(holder.getVisitType());
            appointment_visit_status.setSelection(holder.getVisitStatus());
            DoctorSlotBookings.PersonBooking booking = holder.patient;
            final Person patient = booking.patient;

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

                    ManagePatientProfile parentactivity = (ManagePatientProfile)activity;
                    Bundle bundle = activity.getIntent().getExtras();
                    bundle.putInt(PARAM.PATIENT_ID, patient.getId());
                    parentactivity.getIntent().putExtras(bundle);
                    ParentFragment fragment = new PatientDetailsFragment();
                    parentactivity.fragmentList.add(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            });


            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = activity.getIntent().getExtras();
                    bundle.putInt(PARAM.PATIENT_ID, patient.getId());
                    Fragment fragment = new PatientVisitDatesView();
                    fragment.setArguments(bundle);
                    activity.getIntent().putExtras(bundle);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            });
//            appointment_menu.setAdapter(new AppointmentAdapter(activity));
        }
        else {
            layout.setVisibility(View.GONE);
            appointment_type.setVisibility(View.GONE);
            appointment_visit_status.setVisibility(View.GONE);
        }

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
        int sequenceNumber;
        Integer duration;
        Integer doctorId;
        Integer clinicSlotId;
        boolean isHoliday = false;
        DoctorSlotBookings.PersonBooking patient;
        DoctorClinicDetails.ClinicSlots model;
        Date date;

//        public AppointmentHolder(int i, DoctorClinicDetails.ClinicSlots model)
//        {
//            sequenceNumber = i+1;
//            this.model = model;
//        }
//        public AppointmentHolder(int i, DoctorClinicDetails.ClinicSlots model, DoctorSlotBookings.PersonBooking personBooking)
//        {
//            sequenceNumber = i+1;
//            this.model = model;
//            this.patient = personBooking;
//        }
        public AppointmentHolder(int i, DoctorClinicDetails.ClinicSlots model, DoctorSlotBookings.PersonBooking personBooking,List<DoctorHoliday> doctorHolidays , Date date)
        {
            sequenceNumber = i+1;
            this.model = model;
            this.patient = personBooking;
            setAppointmentDateTime(i, date, model.startTime, model.endTime, model.visitDuration);
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
                        holders.add(new AppointmentHolder(i,model, bookings1,doctorHolidays, date));
                        break;
                    }
                }
                if(found == false)
                    holders.add(new AppointmentHolder(i,model, null ,doctorHolidays, date));
            }
            else
                holders.add(new AppointmentHolder(i,model, null,doctorHolidays, date));
        }
        return holders;
    }
}
