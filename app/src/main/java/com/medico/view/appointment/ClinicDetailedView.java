package com.medico.view.appointment;

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
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.ClinicByDoctorRequest;
import com.medico.model.DoctorClinicDetails;
import com.medico.util.ImageLoadTask;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.settings.ClinicProfileEditView;

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
public class ClinicDetailedView extends ParentFragment {

//    ProgressDialog progress;
    SharedPreferences session;
    TextView clinicName,speciality, address,totalCount;
    Button appointmentsBtn,profileBtn;
    ImageView rightButton,viewImage,downImage;
    TableLayout tableLayout;
    TableRow dateRow, appointRow;
    ClinicProfileEditView profile;
    ClinicSlotListView appointment;

    DoctorClinicDetails model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appointment_clinic_slot_list, container, false);
        clinicName = (TextView) view.findViewById(R.id.doctor_name);
        speciality = (TextView) view.findViewById(R.id.speciality);
        viewImage = (ImageView) view.findViewById(R.id.doctor_image);

        address = (TextView) view.findViewById(R.id.address);
        downImage = (ImageView) view.findViewById(R.id.down_arrow);
        totalCount = (TextView) view.findViewById(R.id.total_count);
        rightButton = (ImageView) view.findViewById(R.id.nextBtn);

        tableLayout = (TableLayout)view.findViewById(R.id.upcoming_appointment);
        dateRow = (TableRow)view.findViewById(R.id.date_row);
        appointRow = (TableRow)view.findViewById(R.id.nr_row);

//        RelativeLayout layout21 = (RelativeLayout)view.findViewById(R.id.layout21);
//        layout21.setVisibility(View.VISIBLE);

        appointmentsBtn = (Button) view.findViewById(R.id.appointment);
        profileBtn = (Button) view.findViewById(R.id.profile);

        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
             }
        });
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // patientId = session.getString("patientId", null);
            }
        });

        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ManageDoctorAppointment activity = (ManageDoctorAppointment)getActivity();
                if(profile != null)
                    activity.detachFragment(profile);
                appointmentsBtn.setBackgroundResource(R.drawable.page_selected);
                profileBtn.setBackgroundResource(R.drawable.page_default);
                appointment = new ClinicSlotListView();
                activity.attachFragment(appointment);
                appointment.setModel(model);
                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_details1, appointment, "Doctor Consultations").addToBackStack(null).commit();
                fragmentManger.beginTransaction().replace(R.id.content_details1, appointment, "Doctor Consultations").commit();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageDoctorAppointment activity = (ManageDoctorAppointment)getActivity();
                if(appointment != null)
                activity.detachFragment(appointment);
                appointmentsBtn.setBackgroundResource(R.drawable.page_default);
                profileBtn.setBackgroundResource(R.drawable.page_selected);
                profile = new ClinicProfileEditView();
                activity.attachFragment(profile);
                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_details1, profile, "Doctor Consultations").addToBackStack(null).commit();
                fragmentManger.beginTransaction().replace(R.id.content_details1, profile, "Doctor Consultations").commit();


            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity activity = (ParentActivity)getActivity();
                Bundle bundle = getActivity().getIntent().getExtras();
                if(model.slots.size() > 0)
                {
                    bundle.putInt(DOCTOR_CLINIC_ID, model.slots.get(0).doctorClinicId);
                    activity.getIntent().putExtras(bundle);
                    ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
                    activity.attachFragment(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicDetailedView.class.getName()).addToBackStack(ClinicDetailedView.class.getName()).commit();
                }
            }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity activity = (ParentActivity)getActivity();
                Bundle bundle = getActivity().getIntent().getExtras();
                if(model.slots.size() > 0)
                {
                    bundle.putInt(DOCTOR_CLINIC_ID, model.slots.get(0).doctorClinicId);
                    activity.getIntent().putExtras(bundle);
                    ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
                    activity.attachFragment(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicDetailedView.class.getName()).addToBackStack(ClinicDetailedView.class.getName()).commit();
                }
            }
        });
        return view;
    }


//    public void setModel(DoctorClinicDetails model)
//    {
//        this.model = model;
//    }

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        api.getClinicByDoctor(new ClinicByDoctorRequest(doctorId,clinicId), new Callback<DoctorClinicDetails>() {
            @Override
            public void success(DoctorClinicDetails clinicDetailsreturn, Response response) {
                model = clinicDetailsreturn;
                if(model != null)
                    new ImageLoadTask(model.clinic.imageUrl, viewImage).execute();
                address.setText(model.clinic.address);
                clinicName.setText(model.clinic.clinicName);
                speciality.setText(model.clinic.speciality);
                if(model.datecounts != null)
                    totalCount.setText(new Integer(model.datecounts.size()).toString());
                else
                    totalCount.setText(new Integer(0).toString());
                setAppointmentDates(model);
                appointmentsBtn.callOnClick();
            }

            @Override
            public void failure(RetrofitError error) {
//                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
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
