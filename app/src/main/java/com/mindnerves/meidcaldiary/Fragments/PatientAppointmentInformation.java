package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.PersonTemp;
import Model.TotalInvoice;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 7/27/15.
 */
public class PatientAppointmentInformation extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    RelativeLayout replacementFragment;
    String doctor_email, appointmentDate, appointmentTime, patientId;
    Button summaryBtn, documentationBtn, patientNoteBtn, treatmentBtn, invoicesBtn, drawar, logout,feedbackBtn;

    String doctorId = "";
    String type = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_appointment_information, container, false);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);
        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        patientNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
        feedbackBtn = (Button)view.findViewById(R.id.feedback_btn);
        feedbackBtn.setVisibility(View.GONE);
        doctorId = session.getString("patient_doctor_email", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        patientId = session.getString("sessionID", null);
        type = session.getString("loginType", null);
        System.out.println("Appointment Time:::::::" + appointmentTime);
        System.out.println("Appointment Date:::::::" + appointmentDate);
        System.out.println("doctorId:::::::" + doctorId);
        System.out.println("patientId:::::::" + patientId);
        getSummaryButton();
        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToBack();
            }
        });
        patientNoteBtn.setVisibility(View.GONE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getInvoice("" + doctorId, patientId, appointmentDate, appointmentTime, new Callback<TotalInvoice>() {
            @Override
            public void success(TotalInvoice totalInvoice, Response response) {

                if (totalInvoice.getShareWithPatient() != null) {
                    if (totalInvoice.getShareWithPatient() == 1) {
                        treatmentBtn.setVisibility(View.VISIBLE);
                        invoicesBtn.setVisibility(View.VISIBLE);
                    } else {
                        treatmentBtn.setVisibility(View.GONE);
                        invoicesBtn.setVisibility(View.GONE);
                    }

                } else {
                    treatmentBtn.setVisibility(View.GONE);
                    invoicesBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                treatmentBtn.setVisibility(View.GONE);
                invoicesBtn.setVisibility(View.GONE);
            }
        });
        summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_blue_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                patientNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                summaryBtn.setTextColor(Color.parseColor("#ffffff"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                patientNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));
                getSummaryButton();
            }
        });
        documentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_blue_color);
                patientNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#ffffff"));
                patientNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));
                getDocumentationButton();

            }
        });
        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                patientNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_blue_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                patientNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#ffffff"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));
                getPatientTreatmentPlan();
            }
        });
        invoicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                patientNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_blue_color);
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                patientNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#ffffff"));
                getPatientInvoice();
            }
        });
        return view;
    }

    public void getSummaryButton() {
        Bundle bun = getArguments();
        Fragment fragment = new PatientAppointmentSummary();
        if(bun != null) {
            if (bun.getString("fragment") != null) {
                fragment.setArguments(bun);
            }else if(bun.getString("patientAllAppointment") != null){
                fragment.setArguments(bun);
            }
        }
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDocumentationButton() {
        Fragment fragment = new PatientAppointmentDocument();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getPatientTreatmentPlan() {
        Fragment fragment = new PatientAppointmentAllTreatmentPlan();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getPatientInvoice() {
        Fragment fragment = new PatientAppointmentInvoices();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goToBack() {
        Bundle bunDoctor = getArguments();
        if (global.getSummaryJump() != null) {
            if (global.getSummaryJump()) {
                global.setSummaryJump(false);
                TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment1 = new PatientMenusManage();
                FragmentManager fragmentManger1 = getFragmentManager();
                fragmentManger1.beginTransaction().replace(R.id.content_frame, fragment1, "Patients Information").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
                final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
                final RelativeLayout profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
                LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
                drawar = (Button) getActivity().findViewById(R.id.drawar_button);
                logout = (Button) getActivity().findViewById(R.id.logout);
                logout.setBackgroundResource(R.drawable.logout);
                drawar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                profileLayout.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
                api.getProfilePatient(patientId, new Callback<PersonTemp>() {
                    @Override
                    public void success(PersonTemp person, Response response) {
                        new ImageLoadTask(getResources().getString(R.string.image_base_url) + person.getId(), profilePicture).execute();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }else if (bunDoctor.getString("fragment") != null) {
                Fragment fragment = new PatientAllDoctors();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }

        }else if(bunDoctor.getString("fragment") != null){
            Fragment fragment = new PatientAllDoctors();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        }else if (type.equalsIgnoreCase("Patient")) {
            Fragment fragment = new AppointmentsPatient();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
        } else if (bunDoctor.getString("fragment") != null) {
            Fragment fragment = new PatientAllDoctors();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
        } else {
            Fragment fragment = new PatientAllAppointment();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
            System.out.println("condition:::::::" + global.getSummaryJump());
        }
    }


}
