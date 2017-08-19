package com.medicohealthcare.view.profile;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorIdPatientId;
import com.medicohealthcare.model.PatientShortProfile;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.DateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class  PatientDetailsView extends ParentFragment {

    SharedPreferences session;
    TextView patientName,doctorSpeciality, address, lastVisitedValue, nextAppointment, visitCounts;
    Button appointmentsBtn,profileBtn;
    ImageView viewImage, nextBtn, closeMenu;
    Fragment childfragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_patient_profile_list, container, false);
        RelativeLayout detailsLayout = (RelativeLayout)view.findViewById(R.id.layout11);
        detailsLayout.setVisibility(View.VISIBLE);
        patientName = (TextView) view.findViewById(R.id.doctor_name);
        nextBtn = (ImageView)view.findViewById(R.id.nextBtn);
        doctorSpeciality = (TextView) view.findViewById(R.id.speciality);
        address = (TextView)view.findViewById(R.id.address);
        lastVisitedValue = (TextView) view.findViewById(R.id.lastAppointmentValue);
        nextAppointment = (TextView) view.findViewById(R.id.review_value);

        visitCounts = (TextView) view.findViewById(R.id.totalCount);

        //---------------------------------------------------------------
        appointmentsBtn = (Button) view.findViewById(R.id.appointment);
        profileBtn = (Button) view.findViewById(R.id.profile);

        viewImage = (ImageView) view.findViewById(R.id.doctor_image);
        viewImage.setBackgroundResource(R.drawable.patient_default);
        closeMenu = (ImageView) view.findViewById(R.id.downImg);
        closeMenu.setBackgroundResource(R.drawable.arrow_up_red);

        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentsBtn.setBackgroundResource(R.drawable.page_selected);
                profileBtn.setBackgroundResource(R.drawable.page_default);
                Fragment fragment = new DoctorPatientClinicAppointmentListView();
                childfragment = fragment;
                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").commit();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  appointmentsBtn.setBackgroundResource(R.drawable.page_default);
                profileBtn.setBackgroundResource(R.drawable.page_selected);
                Fragment fragment = new PatientProfileDetails();
                childfragment = fragment;
                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").commit();

            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientProfileListView();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, PatientProfileListView.class.getName()).addToBackStack(PatientProfileListView.class.getName()).commit();
            }
        });
        visitCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActivity parentactivity = (ParentActivity)getActivity();
                Bundle bundle = parentactivity.getIntent().getExtras();
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActivity parentactivity = (ParentActivity)getActivity();
                Bundle bundle = getActivity().getIntent().getExtras();
                ParentFragment fragment = new PatientVisitDatesView();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientVisitDatesView.class.getName()).addToBackStack(PatientVisitDatesView.class.getName()).commit();
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(childfragment != null && childfragment.isDetached() == false)
            childfragment.onStart();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStart()
    {
        super.onStart();

        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer patientId = bundle.getInt(PATIENT_ID);
        DoctorIdPatientId doc = new DoctorIdPatientId(new Integer(bundle.getInt(DOCTOR_ID)), new Integer(bundle.getInt(PATIENT_ID)));
        api.getPatientShortProfile(doc, new Callback<PatientShortProfile>() {
                @Override
                public void success(PatientShortProfile patient, Response response)
                {
                    if(patient != null) {

                        patientName.setText(patient.getName());

                        doctorSpeciality.setText(patient.getProfession());
                        address.setText(patient.getAddress());
                        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                        if(patient.getLastVisit() != null)
                            lastVisitedValue.setText(dateFormat.format(new Date(patient.getLastVisit())));
                        else
                            lastVisitedValue.setText(getActivity().getResources().getString(R.string.no_visit));
                        if(patient.getUpcomingVisit() != null)
                            nextAppointment.setText(dateFormat.format(new Date(patient.getUpcomingVisit())));
                        else
                            nextAppointment.setText(getActivity().getResources().getString(R.string.no_visit));
                        if(patient.getNumberOfVisits() != null )
                            visitCounts.setText(patient.getNumberOfVisits().toString());
                        else
                            visitCounts.setText("0");
                        if(patient.getImageUrl() != null && patient.getImageUrl().trim().length() > 0)
                        {
                            new ImageLoadTask(patient.getImageUrl(),viewImage);
                        }

                    }

                    appointmentsBtn.callOnClick();

                   hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        if(childfragment != null && childfragment.isDetached() == false && childfragment.getActivity() != null)
            childfragment.onStart();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }

}
