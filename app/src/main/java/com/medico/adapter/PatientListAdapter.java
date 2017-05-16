package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.PatientProfileList;
import com.medico.model.PatientShortProfile;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.PatientDetailsView;
import com.medico.view.profile.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    PatientProfileList allPatients;
    private ProgressDialog progress;

    public PatientListAdapter(Activity activity, PatientProfileList allPatients)
    {
        super(activity);
        this.activity = activity;
        this.allPatients = allPatients;
    }

    @Override
    public int getCount() {
        return allPatients.getPatientlist().size();
    }

    @Override
    public Object getItem(int position) {
        return allPatients.getPatientlist().get(position);
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
            convertView = inflater.inflate(R.layout.doctor_patient_profile_list, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);

        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView appointmentDate = (TextView) convertView.findViewById(R.id.review_value);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        PatientShortProfile profile = allPatients.getPatientlist().get(position);
        viewImage.setBackgroundResource(R.drawable.patient_default);
        if(profile.getImageUrl() != null && profile.getImageUrl().length() > 0)
            new ImageLoadTask(activity.getString(R.string.image_base_url) + profile.getImageUrl(), viewImage).execute();
        totalCount.setText("" + profile.getNumberOfVisits());

        if (profile.getAddress() != null) {
            if (profile.getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(profile.getAddress());

            }
        }
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
        if (profile.getUpcomingVisit() != null) {
            if (profile.getUpcomingVisit().equals("")) {
                appointmentDate.setText("None");

            } else {
                appointmentDate.setText(format.format(new Date(profile.getUpcomingVisit())));

            }
        }
        if (profile.getLastVisit() == null || profile.getLastVisit().equals("")) {
            lastAppointment.setText("None");

        } else {

            lastAppointment.setText(format.format(new Date(profile.getLastVisit())));
        }
        doctorName.setText(profile.getName());
        doctorSpeciality.setText(profile.getProfession());



        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, allPatients.getPatientlist().get(position).getPatientId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsView();
//                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, PatientDetailsView.class.getName()).addToBackStack(PatientDetailsView.class.getName()).commit();
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, allPatients.getPatientlist().get(position).getPatientId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, allPatients.getPatientlist().get(position).getPatientId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        return convertView;

    }


}
