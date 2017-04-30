package com.medico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.AppointmentId1;
import com.medico.model.AppointmentResponse;
import com.medico.model.ClinicSlotDetails;
import com.medico.model.DoctorClinicId;
import com.medico.model.PatientAppointmentsVM;
import com.medico.model.Person;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.ClinicDoctorAppointmentView;
import com.medico.view.profile.DoctorDetailsView;
import com.medico.view.profile.FeedbackFragmentClinicAppointment;
import com.medico.view.profile.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientAppointmentListAdapter extends HomeAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    List<PatientAppointmentsVM.Appointments> appointments;
    private ProgressDialog progress;

    public PatientAppointmentListAdapter(Activity activity, List<PatientAppointmentsVM.Appointments> appointments)
    {
        super(activity);
        this.activity = activity;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {

        if(appointments==null)
            return 0;
        else
            return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
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
            convertView = inflater.inflate(R.layout.patient_appointment_list, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        TextView address = (TextView) convertView.findViewById(R.id.address);

        TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointment_value);
        TextView appointmentStatus = (TextView) convertView.findViewById(R.id.appointment_status_value);
        TextView appointmentType = (TextView) convertView.findViewById(R.id.appointment_type_value);

        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        Button cancel = (Button)convertView.findViewById(R.id.cancel_btn);
        Button reshedule = (Button)convertView.findViewById(R.id.change_btn);
        Button feedback = (Button)convertView.findViewById(R.id.feedback_btn);

        final PatientAppointmentsVM.Appointments doctorappointment = appointments.get(position);
        final Person doctor = doctorappointment.doctor;

        if(doctor.getImageUrl() != null && doctor.getImageUrl().trim().length() > 0)
            new ImageLoadTask(activity.getString(R.string.image_base_url) + doctor.getImageUrl(), viewImage).execute();

//        totalCount.setText("" + doctorappointment.app);

        doctorName.setText(doctor.getName());
        doctorSpeciality.setText(doctor.speciality);

        if (doctor.getAddress() != null) {
            if (doctor.getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(doctor.getAddress());

            }
        }

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
        appointmentDate.setText(format.format(new Date(doctorappointment.appointmentDate)));
        appointmentStatus.setText(activity.getResources().getStringArray(R.array.appointment_status)[doctorappointment.appointmentStatus]);
        appointmentType.setText(activity.getResources().getStringArray(R.array.visit_type_list)[doctorappointment.type]);
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, doctor.id);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new DoctorDetailsView();
                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, DoctorDetailsView.class.getName()).addToBackStack(DoctorDetailsView.class.getName()).commit();            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, doctor.id);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });

        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, doctor.id);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        rescheduleAppointment(reshedule,doctorappointment);
        feedbackAppointment(feedback,doctorappointment);
        cancelAppointment(cancel,doctorappointment);
        return convertView;

    }
    private void rescheduleAppointment(Button reschedule, final PatientAppointmentsVM.Appointments doctorappointment)
    {
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                api.getSlotDetail(new DoctorClinicId(doctorappointment.doctorClinicId,0), new Callback<ClinicSlotDetails>()
                {
                    @Override
                    public void success(ClinicSlotDetails details, Response response)
                    {
                        if(details != null )
                        {
                            Bundle bundle = activity.getIntent().getExtras();
                            DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                            String shiftDateTime = formatTime.format(details.timeToStart) +" - " + formatTime.format(details.TimeToStop);
                            bundle.putInt(PARAM.DOCTOR_ID,doctorappointment.doctor.getId());
                            bundle.putInt(PARAM.PATIENT_ID,doctorappointment.patientId);
                            bundle.putInt(PARAM.APPOINTMENT_ID,doctorappointment.appointmentId);
                            bundle.putString(PARAM.DAYS_OF_WEEK,details.daysOfWeek);
                            bundle.putLong(PARAM.APPOINTMENT_DATETIME, doctorappointment.appointmentDate);
                            bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, doctorappointment.sequenceNumber);
                            bundle.putInt(PARAM.CLINIC_ID, doctorappointment.clinicId);
                            bundle.putString(PARAM.CLINIC_NAME,details.clinicName);
                            bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
                            bundle.putInt(PARAM.DOCTOR_CLINIC_ID,doctorappointment.doctorClinicId);
                            bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
                            bundle.putLong(PARAM.SLOT_START_DATETIME,details.timeToStart);
                            bundle.putLong(PARAM.SLOT_END_DATETIME,details.TimeToStop);
                            activity.getIntent().putExtras(bundle);
                            ParentFragment fragment = new ClinicDoctorAppointmentView();
                            ((ParentActivity)activity).attachFragment(fragment);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManger = activity.getFragmentManager();
                            fragmentManger.beginTransaction().add(R.id.service,fragment,ClinicDoctorAppointmentView.class.getName()).addToBackStack(ClinicDoctorAppointmentView.class.getName()).commit();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                         error.printStackTrace();
                    }
                });


            }
        });
    }

    private void feedbackAppointment(Button feedback, final PatientAppointmentsVM.Appointments doctorappointment)
    {
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = activity.getIntent().getExtras();
                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                bundle.putInt(PARAM.APPOINTMENT_ID,doctorappointment.appointmentId);
                bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, doctorappointment.sequenceNumber);
                bundle.putInt(PARAM.CLINIC_ID, doctorappointment.clinicId);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,doctorappointment.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new FeedbackFragmentClinicAppointment();
                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service,fragment,FeedbackFragmentClinicAppointment.class.getName()).addToBackStack(FeedbackFragmentClinicAppointment.class.getName()).commit();


            }
        });
    }
    private void cancelAppointment(Button cancel, final PatientAppointmentsVM.Appointments doctorappointment)
    {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Close buttom clicked");
                new AlertDialog.Builder(activity)
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this appointment?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                                api.cancelAppointment(new AppointmentId1(doctorappointment.appointmentId), new Callback<AppointmentResponse>() {
                                    @Override
                                    public void success(AppointmentResponse result, Response response) {
                                        progress.dismiss();
//                                        if (result.getStatus().equalsIgnoreCase("1")) {
                                        Toast.makeText(activity, "Appointment is Cancelled!", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetInvalidated();
//                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        progress.dismiss();
                                        error.printStackTrace();
                                        Toast.makeText(activity, "Failed to remove medicine", Toast.LENGTH_SHORT).show();
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
        });
    }

}
