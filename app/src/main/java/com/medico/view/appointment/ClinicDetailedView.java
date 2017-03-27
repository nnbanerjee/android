package com.medico.view.appointment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
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

import com.medico.application.R;
import com.medico.model.DoctorClinicDetails;
import com.medico.util.ImageLoadTask;
import com.medico.view.ParentFragment;
import com.medico.view.settings.ClinicProfileEditView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

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
    Fragment childfragment;

    DoctorClinicDetails model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appointment_clinic_slot_list, container, false);
        clinicName = (TextView) view.findViewById(R.id.clinic_name);
        speciality = (TextView) view.findViewById(R.id.clinicSpeciality);
        viewImage = (ImageView) view.findViewById(R.id.clinic_image);

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


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // patientId = session.getString("patientId", null);
            }
        });

        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentsBtn.setBackgroundResource(R.drawable.page_selected);
                profileBtn.setBackgroundResource(R.drawable.page_default);
                ClinicSlotListView fragment = new ClinicSlotListView();
                fragment.setModel(model);
                childfragment = fragment;
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();


            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  appointmentsBtn.setBackgroundResource(R.drawable.page_default);
                profileBtn.setBackgroundResource(R.drawable.page_selected);
                Fragment fragment = new ClinicProfileEditView();
                childfragment = fragment;
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();


            }
        });

//        closeMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new PatientProfileListView();
//                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            }
//        });
        return view;
    }

//    public void getClinicsProfile() {
//        Fragment fragment = new ClinicAllPatientFragment();
//        FragmentManager fragmentManger = getFragmentManager();
//        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
//    }
//
//    public void getPatientProfile() {
//        Fragment fragment = new PatientProfileDetails();
//        FragmentManager fragmentManger = getFragmentManager();
//        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
//    }

    public void setModel(DoctorClinicDetails model)
    {
        this.model = model;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onStart()
    {
        super.onStart();

//        progress = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        if(model != null)
            new ImageLoadTask(model.clinic.imageUrl, viewImage).execute();
//        totalCount.setText( clinicDetails.get(position).);
        address.setText(model.clinic.address);
        clinicName.setText(model.clinic.clinicName);
        speciality.setText(model.clinic.speciality);
        if(model.datecounts != null)
            totalCount.setText(new Integer(model.datecounts.size()).toString());
        else
            totalCount.setText(new Integer(0).toString());
        setAppointmentDates(model);

        if(childfragment != null && childfragment.isDetached() == false)
            childfragment.onStart();
//        progress.dismiss();

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
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        int i = 0;
        for(DoctorClinicDetails.AppointmentCounts count:counts)
        {
            TextView dateView = new TextView(activity);
            TextView countView = new TextView(activity);
            dateView.setText(format.format(new Date(count.date)));
            dateView.setBackgroundResource(R.drawable.medicine_schedule);
            dateView.setLeft(10);
            dateView.setTop(10);
            dateView.setRight(10);
            dateView.setBottom(10);
            dateRow.addView(dateView,i,lp);
            countView.setText(new Integer(count.counts).toString());
            countView.setBackgroundResource(R.drawable.medicine_schedule);
            countView.setLeft(10);
            countView.setTop(10);
            countView.setRight(10);
            countView.setBottom(10);
            appointRow.addView(countView,i,lp);

        }
        tableLayout.requestLayout();
    }

}
