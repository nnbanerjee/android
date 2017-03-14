package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.DoctorIdPatientId;
import com.medico.model.DoctorShortProfile;
import com.medico.util.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorDetailsFragment extends ParentFragment {

    ProgressDialog progress;
    SharedPreferences session;
    TextView patientName,doctorSpeciality, address, lastVisitedValue, nextAppointment, visitCounts;
    Button appointmentsBtn,profileBtn;
    ImageView viewImage, visitDates,closeMenu;
    Fragment childfragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_profile_details, container, false);
        patientName = (TextView) view.findViewById(R.id.patient_name);
        doctorSpeciality = (TextView) view.findViewById(R.id.doctorSpeciality);
        address = (TextView)view.findViewById(R.id.address);
        lastVisitedValue = (TextView) view.findViewById(R.id.last_visited);
        nextAppointment = (TextView) view.findViewById(R.id.next_appointment);

        visitCounts = (TextView) view.findViewById(R.id.visit_counts);

        //---------------------------------------------------------------
        appointmentsBtn = (Button) view.findViewById(R.id.appointment);
        profileBtn = (Button) view.findViewById(R.id.profile);

        viewImage = (ImageView) view.findViewById(R.id.doctorImg);
        viewImage.setBackgroundResource(R.drawable.patient);
        visitDates = (ImageView) view.findViewById(R.id.viewAll);
        closeMenu = (ImageView) view.findViewById(R.id.downImg);

        //------------------------------------------------------------------------


        visitDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        appointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentsBtn.setBackgroundResource(R.drawable.page_selected);
                profileBtn.setBackgroundResource(R.drawable.page_default);
                Fragment fragment = new ClinicAllPatientFragment();
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
                Fragment fragment = new PatientProfileDetails();
                childfragment = fragment;
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();


            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientProfileListView();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onStart()
    {
        super.onStart();

        progress = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer patientId = bundle.getInt(PATIENT_ID);
        DoctorIdPatientId doc = new DoctorIdPatientId(new Integer(bundle.getInt(DOCTOR_ID)), new Integer(bundle.getInt(PATIENT_ID)));
        api.getDoctorShortProfile(doc, new Callback<DoctorShortProfile>() {
                @Override
                public void success(DoctorShortProfile patient, Response response)
                {
                    if(patient != null) {
                        patientName.setText(patient.getName());

                        doctorSpeciality.setText(patient.getProfession());
                        address.setText(patient.getAddress());
                        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
                        if(patient.getLastVisit() != null)
                            lastVisitedValue.setText(dateFormat.format(new Date(patient.getLastVisit())));
                        if(patient.getUpcomingVisit() != null)
                            nextAppointment.setText(dateFormat.format(new Date(patient.getUpcomingVisit())));
                        visitCounts.setText(patient.getNumberOfVisits().toString());
                        viewImage.setBackgroundResource(R.drawable.patient);
                        if(patient.getImageUrl() != null && patient.getImageUrl().trim().length() > 0)
                        {
                            new ImageLoadTask(patient.getImageUrl(),viewImage);
                        }

                    }

                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        if(childfragment != null && childfragment.isDetached() == false)
        childfragment.onStart();


//        api.getProfile1(new ProfileId(allPatients.getPatientlist().get(position).getPatientId()), new Callback<Person>() {
//                    @Override
//                    public void success(Person patient, Response response)
//                    {
//
//                        Bundle args = new Bundle();
//                        //Store Selected Patient profile
//                        progress.dismiss();
//                        SharedPreferences.Editor editor = session.edit();
////                        global.setSelectedPatientsProfile(patient);
//                        Gson gson = new Gson();
//                        String json = gson.toJson(patient);
//                        editor.putString("SelectedPatient", json);
//                        editor.commit();
//                        editor.putString("patient_Last_Visited", allPatients.getPatientlist().get(position).getLastVisit().toString());
//                        editor.putString("patient_Upcoming_Appt", allPatients.getPatientlist().get(position).getUpcomingVisit().toString());
//                        editor.putString("patient_Total_visits", allPatients.getPatientlist().get(position).getNumberOfVisits().toString());
//                        editor.putString("patientId", allPatients.getPatientlist().get(position).getPatientId().toString());
//                        editor.putString("patient_Name", allPatients.getPatientlist().get(position).getName());
//                        editor.commit();
//
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        progress.dismiss();
//                        error.printStackTrace();
//                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

}
