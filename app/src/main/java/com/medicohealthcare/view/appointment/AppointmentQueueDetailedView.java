package com.medicohealthcare.view.appointment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.DoctorClinicId;
import com.medicohealthcare.model.DoctorClinicQueue;
import com.medicohealthcare.model.ServerResponseStatus;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AppointmentQueueDetailedView extends ParentFragment {

//    ProgressDialog progress;
    SharedPreferences session;
    TextView doctorName,doctorSpeciality, address,totalCount,doctorId,clinicName,location,clinicContact;
    Button appointmentsBtn,profileBtn,bookOnline;
    ImageView rightButton,viewImage,downImg;
    TableLayout tableLayout;
    TableRow dateRow, appointRow;
    AppointmentQueueStatusView profile;
    DoctorClinicQueue model;
    ClinicSlotView appointment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.doctor_appointment_que1, container, false);
        doctorName = (TextView) view.findViewById(R.id.doctor_name);
        doctorSpeciality = (TextView) view.findViewById(R.id.speciality);
        viewImage = (ImageView) view.findViewById(R.id.doctor_image);
        rightButton = (ImageView) view.findViewById(R.id.nextBtn);
        bookOnline = (Button)view.findViewById(R.id.bookOnline);
        totalCount = (TextView) view.findViewById(R.id.totalCount);
        downImg = (ImageView) view.findViewById(R.id.downImg);
        appointmentsBtn = (Button)view.findViewById(R.id.appointment);
        profileBtn = (Button)view.findViewById(R.id.profile);
        doctorId = (TextView)view.findViewById(R.id.profile_id);
        clinicName = (TextView)view.findViewById(R.id.clinicName);
        location = (TextView)view.findViewById(R.id.clinicLocation);
        clinicContact = (TextView)view.findViewById(R.id.clinicContact);

        view.findViewById(R.id.document_type).setVisibility(View.VISIBLE);
        Activity activity = getActivity();
        final Bundle bundle = activity.getIntent().getExtras();
        int searchType = bundle.getInt(SEARCH_TYPE);
        final int profileId = bundle.getInt(PROFILE_ID);
        int loginrole = bundle.getInt(PROFILE_ROLE);
        int searchRole = bundle.getInt(SEARCH_ROLE);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = getActivity();
                appointmentsBtn.setBackgroundResource(R.drawable.page_selected);
                profileBtn.setBackgroundResource(R.drawable.page_default);
                    appointment = new ClinicSlotView();
                appointment.setModel(model);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.que_status1, appointment, "Doctor Consultations").commit();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                appointmentsBtn.setBackgroundResource(R.drawable.page_default);
                profileBtn.setBackgroundResource(R.drawable.page_selected);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putBoolean("profileview",true);
                profile = new AppointmentQueueStatusView();
                profile.setModel(model);
                getActivity().getIntent().putExtras(bundle);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.que_status1, profile, "Doctor Consultations").commit();


            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity activity = (ParentActivity)getActivity();
                Bundle bundle = getActivity().getIntent().getExtras();
                 bundle.putInt(CLINIC_ID, model.clinic.idClinic);
                bundle.putInt(DOCTOR_CLINIC_ID, model.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, AppointmentQueueDetailedView.class.getName()).addToBackStack(AppointmentQueueDetailedView.class.getName()).commit();
             }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity activity = (ParentActivity)getActivity();
                Bundle bundle = getActivity().getIntent().getExtras();
               bundle.putInt(CLINIC_ID, model.clinic.idClinic);
                bundle.putInt(DOCTOR_CLINIC_ID, model.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, AppointmentQueueDetailedView.class.getName()).addToBackStack(AppointmentQueueDetailedView.class.getName()).commit();

            }
        });
        bookOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final int queuestatus = model.queueStatus.intValue()==0?1:0;
                api.setDoctorClinicQueueStatus(new DoctorClinicId(model.doctorClinicId,queuestatus,null), new Callback<ServerResponseStatus>()
                {
                    @Override
                    public void success(ServerResponseStatus queue, Response response)
                    {
                        if(queue != null && queue.status.intValue() == 1)
                        {
                            model.queueStatus =queuestatus;
                            bookOnline.setText(model.queueStatus.intValue()==0?"START":"Stop");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        hideBusy();
                        new MedicoCustomErrorHandler(getActivity()).handleError(error);
                    }
                });


            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final Integer doctorclinicId = bundle.getInt(DOCTOR_CLINIC_ID);
        api.getClinicQueueDetails(new DoctorClinicId(doctorclinicId), new Callback<DoctorClinicQueue>() {
            @Override
            public void success(DoctorClinicQueue queue, Response response)
            {
                if(queue != null && queue.doctor != null && queue.clinic != null)
                {

                    model = queue;
                    doctorName.setText(model.doctor.getName());
                    doctorSpeciality.setText(model.doctor.getSpeciality());
                    doctorId.setText(model.doctor.getId().toString());
                    clinicName.setText(model.clinic.clinicName);
                    totalCount.setText(queue.numberOfAppointments.toString());
                    bookOnline.setText(queue.queueStatus.intValue()==0?"START":"Stop");
                    location.setText(model.clinic.address);
                    if (model.clinic.mobile != null && model.clinic.location != null)
                        clinicContact.setText(" +" + model.clinic.location.toString() + " " + model.clinic.mobile.toString());
                    String imageUrl = model.doctor.getImageUrl();
                    if (imageUrl != null && imageUrl.trim().length() > 0)
                    {
                        new ImageLoadTask(imageUrl, viewImage).execute();
                    }
                    appointmentsBtn.callOnClick();

                }

            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
     }

    private void setAppointmentDates(DoctorClinicDetails details)
    {
        Activity activity = getActivity();
        tableLayout.setStretchAllColumns(true);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        dateRow.setLayoutParams(lp);
        dateRow.removeAllViews();
        appointRow.removeAllViews();
        List<DoctorClinicDetails.AppointmentCounts> counts = details.datecounts;
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM");

        int i = 0;
        for(DoctorClinicDetails.AppointmentCounts count:counts)
        {
            TextView dateView = new TextView(activity);
            dateView.setTextSize(10);
            dateView.setBackgroundResource(R.drawable.medicine_schedule_header);
            dateView.setTextColor(Color.WHITE);
            TextView countView = new TextView(activity);
            dateView.setText(format.format(new Date(count.date)));
            dateView.setPadding(3,3,3,3);
            dateView.setGravity(1);
            dateRow.addView(dateView,i,lp);
            countView.setText(new Integer(count.counts).toString());
            countView.setBackgroundResource(R.drawable.medicine_schedule);
            countView.setTextColor(activity.getResources().getColor(R.color.medico_blue));
            countView.setPadding(3,3,3,3);
            countView.setGravity(1);
            countView.setTextSize(10);
            appointRow.addView(countView,i,lp);

        }
        tableLayout.requestLayout();
    }

}
