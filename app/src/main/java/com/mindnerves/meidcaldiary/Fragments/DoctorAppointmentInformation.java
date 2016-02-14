package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;


import Application.MyApi;
import Model.ReminderVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    RelativeLayout replacementFragment;
    Button summaryBtn,documentationBtn,doctorNoteBtn,treatmentBtn,invoicesBtn,feedbackBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_information, container,false);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);

        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        doctorNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
        feedbackBtn = (Button)view.findViewById(R.id.feedback_btn);
        getSummaryInformation();

        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_blue_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_grey_color);

                feedbackBtn.setTextColor(Color.parseColor("#000000"));
                summaryBtn.setTextColor(Color.parseColor("#ffffff"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                doctorNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));

                getSummaryInformation();
            }
        });

        documentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_blue_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_grey_color);

                feedbackBtn.setTextColor(Color.parseColor("#000000"));
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#ffffff"));
                doctorNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));

                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_blue_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_grey_color);

                feedbackBtn.setTextColor(Color.parseColor("#000000"));
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                doctorNoteBtn.setTextColor(Color.parseColor("#ffffff"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));

                getDoctorNoteInformation();
            }
        });

        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_blue_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_grey_color);

                feedbackBtn.setTextColor(Color.parseColor("#000000"));
                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                doctorNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#ffffff"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));

                getTreatmentInformation();
            }
        });

        invoicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_blue_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_grey_color);

                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                doctorNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#ffffff"));
                feedbackBtn.setTextColor(Color.parseColor("#000000"));

                getInvoicesInformation();
            }
        });
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.square_grey_color);
                documentationBtn.setBackgroundResource(R.drawable.square_grey_color);
                doctorNoteBtn.setBackgroundResource(R.drawable.square_grey_color);
                treatmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                invoicesBtn.setBackgroundResource(R.drawable.square_grey_color);
                feedbackBtn.setBackgroundResource(R.drawable.square_blue_color);

                summaryBtn.setTextColor(Color.parseColor("#000000"));
                documentationBtn.setTextColor(Color.parseColor("#000000"));
                doctorNoteBtn.setTextColor(Color.parseColor("#000000"));
                treatmentBtn.setTextColor(Color.parseColor("#000000"));
                invoicesBtn.setTextColor(Color.parseColor("#000000"));
                feedbackBtn.setTextColor(Color.parseColor("#ffffff"));

            }
        });
        return view;
    }

    public void getSummaryInformation(){
        Fragment fragment = new DoctorAppointmentSummary();
        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.getString("fragment") != null){
                String fragmentCall = bundle.getString("fragment");
                Bundle bun = new Bundle();
                bun.putString("fragment",fragmentCall);
                fragment.setArguments(bun);
            }
        }

        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    public void getDocumentationInformation(){
        Fragment fragment = new DoctorAppointmentDocument();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    public void getDoctorNoteInformation(){
        Fragment fragment = new DoctorAppointmentDoctorNote();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    public void getTreatmentInformation(){
        Fragment fragment = new DoctorAppointmentTreatmentPlan();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    public void getInvoicesInformation(){
        Fragment fragment = new DoctorAppointmentInvoices();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void  goToBack(){
        //Fragment fragment = new PatientAllAppointment();
        Fragment fragment = new AllDoctorPatientAppointment();
        FragmentManager fragmentManger = getFragmentManager();
        Bundle bun = new Bundle();
        bun.putString("fragment","from appintment information");
        fragment.setArguments(bun);
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult DoctorAppointmentInformation /////////////////////////////////");
    }

}
