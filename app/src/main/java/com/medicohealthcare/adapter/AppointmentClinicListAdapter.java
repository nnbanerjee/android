package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.appointment.ClinicAppointmentScheduleView;
import com.medicohealthcare.view.appointment.ClinicDetailedView;
import com.medicohealthcare.view.appointment.ManageDoctorAppointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class AppointmentClinicListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorClinicDetails> clinicDetails;

    public AppointmentClinicListAdapter(Activity activity, List<DoctorClinicDetails> clinicDetails)
    {
        super(activity);
        this.activity = activity;
        this.clinicDetails = clinicDetails;
    }

    @Override
    public int getCount() {
        return clinicDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicDetails.get(position);
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
            convertView = inflater.inflate(R.layout.appointment_clinic_list, null);
        TextView clinicName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView speciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);

        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.down_arrow);
        TextView totalCount = (TextView) convertView.findViewById(R.id.total_count);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        TableLayout tableLayout = (TableLayout)convertView.findViewById(R.id.upcoming_appointment);
        TableRow dateRow = (TableRow)convertView.findViewById(R.id.date_row);
        TableRow appointRow = (TableRow)convertView.findViewById(R.id.nr_row);

        if(clinicDetails.get(position).clinic.imageUrl != null)
            new ImageLoadTask(clinicDetails.get(position).clinic.imageUrl, viewImage).execute();
        address.setText(clinicDetails.get(position).clinic.address);
        clinicName.setText(clinicDetails.get(position).clinic.clinicName);
        speciality.setText(clinicDetails.get(position).clinic.speciality);
        if(clinicDetails.get(position).datecounts != null)
            totalCount.setText(new Integer(clinicDetails.get(position).getAppointmentCounts()).toString());
        else
            totalCount.setText(new Integer(0).toString());
        setAppointmentDates(convertView, clinicDetails.get(position));
        viewImage.setTag(clinicDetails.get(position));
        downImage.setTag(clinicDetails.get(position));
        rightButton.setTag(clinicDetails.get(position));
        totalCount.setTag(clinicDetails.get(position));
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorClinicDetails model = (DoctorClinicDetails)v.getTag();
                ManageDoctorAppointment parentactivity = (ManageDoctorAppointment)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.CLINIC_ID,model.clinic.idClinic);
                parentactivity.getIntent().putExtras(bundle);
                ClinicDetailedView fragment = new ClinicDetailedView();
//                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicDetailedView.class.getName()).addToBackStack(ClinicDetailedView.class.getName()).commit();

            }
        });

        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorClinicDetails model = (DoctorClinicDetails)v.getTag();
                ManageDoctorAppointment parentactivity = (ManageDoctorAppointment)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.CLINIC_ID,model.clinic.idClinic);
                parentactivity.getIntent().putExtras(bundle);
                ClinicDetailedView fragment = new ClinicDetailedView();
//                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicDetailedView.class.getName()).addToBackStack(ClinicDetailedView.class.getName()).commit();

            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorClinicDetails model = (DoctorClinicDetails)v.getTag();
                ManageDoctorAppointment parentactivity = (ManageDoctorAppointment)activity;
                Bundle bundle = activity.getIntent().getExtras();
                if(model.slots.size() > 0)
                {
                    bundle.putInt(PARAM.CLINIC_ID, model.clinic.idClinic);
                    bundle.putInt(PARAM.DOCTOR_CLINIC_ID, model.slots.get(0).doctorClinicId);
                    activity.getIntent().putExtras(bundle);
                    ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
//                    ((ParentActivity) activity).attachFragment(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicAppointmentScheduleView.class.getName()).addToBackStack(ClinicAppointmentScheduleView.class.getName()).commit();
                }
            }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorClinicDetails model = (DoctorClinicDetails)v.getTag();
                ManageDoctorAppointment parentactivity = (ManageDoctorAppointment)activity;
                Bundle bundle = activity.getIntent().getExtras();
                if(model.slots.size() > 0)
                {
                    bundle.putInt(PARAM.CLINIC_ID, model.clinic.idClinic);
                    bundle.putInt(PARAM.DOCTOR_CLINIC_ID, model.slots.get(0).doctorClinicId);
                    activity.getIntent().putExtras(bundle);
                    ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
//                    ((ParentActivity) activity).attachFragment(fragment);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicAppointmentScheduleView.class.getName()).addToBackStack(ClinicAppointmentScheduleView.class.getName()).commit();
                }
            }
        });
        return convertView;

    }

    private void setAppointmentDates(View convertView, DoctorClinicDetails details)
    {
        TableLayout tableLayout = (TableLayout)convertView.findViewById(R.id.upcoming_appointment);
        tableLayout.setStretchAllColumns(true);
        TableRow dateRow = (TableRow)convertView.findViewById(R.id.date_row);
        TableRow appointRow = (TableRow)convertView.findViewById(R.id.nr_row);
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
