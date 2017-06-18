package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.DoctorClinicId;
import com.medicohealthcare.model.DoctorClinicQueue;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ServerResponseStatus;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.appointment.AppointmentQueueDetailedView;
import com.medicohealthcare.view.appointment.ClinicAppointmentScheduleView;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.ClinicDoctorAppointmentView;

import java.text.DateFormat;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorQueueListAdapter extends HomeAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorClinicQueue> personList;

    public DoctorQueueListAdapter(Activity activity, List<DoctorClinicQueue> personList)
    {
        super(activity);
        this.activity = activity;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
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
            convertView = inflater.inflate(R.layout.doctor_appointment_que, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        Button add_profile = (Button)convertView.findViewById(R.id.add_profile);

        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView downImg = (ImageView) convertView.findViewById(R.id.downImg);

        TextView doctorId = (TextView)convertView.findViewById(R.id.profile_id);
        TextView clinicName = (TextView)convertView.findViewById(R.id.clinicName);
        TextView location = (TextView)convertView.findViewById(R.id.clinicLocation);
        TextView clinicContact = (TextView)convertView.findViewById(R.id.clinicContact);
        final Bundle bundle = activity.getIntent().getExtras();
        int searchType = bundle.getInt(PARAM.SEARCH_TYPE);
        final int profileId = bundle.getInt(PARAM.PROFILE_ID);
        int loginrole = bundle.getInt(PARAM.PROFILE_ROLE);
        int searchRole = bundle.getInt(PARAM.SEARCH_ROLE);
        final DoctorClinicQueue slot = personList.get(position);
        downImg.setTag(slot);
        final Person doctor = slot.doctor;
        final Clinic1 clinic = slot.clinic;

        bookOnline.setText(slot.queueStatus.intValue()==0?"START":"Stop");
        doctorName.setText(doctor.getName());
        doctorSpeciality.setText(doctor.getSpeciality());
        doctorId.setText(doctor.getId().toString());
        totalCount.setText(slot.numberOfAppointments.toString());
        clinicName.setText(clinic.clinicName);
        location.setText(clinic.address);
        if(clinic.mobile != null && clinic.location != null)
        clinicContact.setText(" +" + clinic.location.toString() + " " + clinic.mobile.toString());
        addSlots(convertView,slot);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = activity.getIntent().getExtras();
                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                String shiftDateTime = formatTime.format(slot.timeToStart) +" - " + formatTime.format(slot.timeToStop);
                bundle.putInt(PARAM.CLINIC_ID, slot.clinic.idClinic);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,slot.doctorClinicId);
                bundle.putInt(PARAM.DOCTOR_ID,slot.doctor.id);
                bundle.putInt(PARAM.DOCTOR_ID,slot.doctor.id);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new ClinicAppointmentScheduleView();
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service,fragment,ClinicDoctorAppointmentView.class.getName()).addToBackStack(ClinicDoctorAppointmentView.class.getName()).commit();
            }
        });
        downImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorClinicQueue model = (DoctorClinicQueue)v.getTag();
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.CLINIC_ID,model.clinic.idClinic);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,model.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                AppointmentQueueDetailedView fragment = new AppointmentQueueDetailedView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, AppointmentQueueDetailedView.class.getName()).addToBackStack(AppointmentQueueDetailedView.class.getName()).commit();

            }
        });

//        viewImage.setBackground(null);

        String imageUrl = doctor.getImageUrl();
        if (imageUrl != null && imageUrl.trim().length() > 0) {
                new ImageLoadTask(imageUrl, viewImage).execute();
        }

        return convertView;

    }

    private void addSlots(View convertView, DoctorClinicQueue slot )
    {
        TextView shiftName = (TextView)convertView.findViewById(R.id.shift_name);
        TextView shiftDays = (TextView)convertView.findViewById(R.id.shiftDays);
        TextView shiftTime = (TextView)convertView.findViewById(R.id.shiftTime);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        shiftName.setText("Slot " + slot.slotNumber + " : ");
        shiftDays.setText(daysOfWeek(slot.daysOfWeek));
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(slot.timeToStart) +" - " + formatTime.format(slot.timeToStop);
        shiftTime.setText(shiftDateTime);
        bookOnline(bookOnline, slot);
    }
    private void bookOnline(final Button bookonline, final DoctorClinicQueue slot)
    {
        bookonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final int queuestatus = slot.queueStatus.intValue()==0?1:0;
                api.setDoctorClinicQueueStatus(new DoctorClinicId(slot.doctorClinicId,queuestatus,null), new Callback<ServerResponseStatus>()
                {
                    @Override
                    public void success(ServerResponseStatus queue, Response response)
                    {
                        if(queue != null && queue.status.intValue() == 1)
                        {
                            slot.queueStatus =queuestatus;
                            bookonline.setText(slot.queueStatus.intValue()==0?"START":"Stop");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        hideBusy();
                        new MedicoCustomErrorHandler(activity).handleError(error);
                    }
                });


            }
        });
    }
    private String daysOfWeek(String days)
    {
        String[] daysNumber = {"0,1,2,3,4,5,6","0,1,2,3,4,5","0,1,2,3,4","0,1,2,3","0,1,2","0,1","0",
                "1,2,3,4,5,6","1,2,3,4,5","1,2,3,4","1,2,3","1,2","1",
                "2,3,4,5,6","2,3,4,5","2,3,4","2,3","2",
                "3,4,5,6","3,4,5","3,4","3",
                "4,5,6","4,5","3",
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

}
