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
import com.medico.model.DoctorProfileList;
import com.medico.model.DoctorShortProfile;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.DoctorDetailsView;
import com.medico.view.profile.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    DoctorProfileList allPatients;
    private ProgressDialog progress;

    public DoctorListAdapter(Activity activity, DoctorProfileList allPatients)
    {
        super(activity);
        this.activity = activity;
        this.allPatients = allPatients;
    }

    @Override
    public int getCount() {
        return allPatients.getDoctorList().size();
    }

    @Override
    public Object getItem(int position) {
        return allPatients.getDoctorList().get(position);
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
        TextView upcomingAppointment = (TextView) convertView.findViewById(R.id.review_value);
        TextView lastVisited = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
//        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        DoctorShortProfile profile = allPatients.getDoctorList().get(position);
        if(profile.getImageUrl() != null && profile.getImageUrl().length() > 0)
            new ImageLoadTask( profile.getImageUrl().toString(), viewImage).execute();
        totalCount.setText("" + profile.getNumberOfVisits());

        if (profile.getAddress() != null) {
            if (profile.getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(profile.getAddress());

            }
        }
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
        if (profile.getUpcomingVisit() == null)
        {
            upcomingAppointment.setText("None");

        }
        else
        {
            upcomingAppointment.setText(format.format(new Date(profile.getUpcomingVisit())));
        }
        if (profile.getLastVisit() == null )
        {
            lastVisited.setText("None");

        }
        else
        {

            lastVisited.setText(format.format(new Date(profile.getLastVisit())));
        }
        doctorName.setText(profile.getName());
        doctorSpeciality.setText(profile.getProfession());



        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, allPatients.getDoctorList().get(position).getDoctorId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new DoctorDetailsView();
                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, DoctorDetailsView.class.getName()).addToBackStack(DoctorDetailsView.class.getName()).commit();
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, allPatients.getDoctorList().get(position).getDoctorId());
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
                bundle.putInt(PARAM.DOCTOR_ID, allPatients.getDoctorList().get(position).getDoctorId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        return convertView;

    }


}
