package com.medicohealthcare.view.profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentFragment;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends ParentFragment {

    RelativeLayout replacementFragment;
    Button summaryBtn, documentationBtn, doctorNoteBtn, treatmentBtn, invoicesBtn;
    ParentFragment selectedFragment ;
    Menu menu;
    MenuInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_information, container, false);
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);
//        setHasOptionsMenu(true);
        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        doctorNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
        
        summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                summaryBtn.setSelected(true);
                summaryBtn.setBackgroundResource(R.drawable.tab_selected);
                getSummaryInformation();
            }
        });

        documentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                documentationBtn.setSelected(true);
                documentationBtn.setBackgroundResource(R.drawable.tab_selected);
                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                doctorNoteBtn.setSelected(true);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_selected);
                getDoctorNoteInformation();
            }
        });

        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                treatmentBtn.setSelected(true);
                treatmentBtn.setBackgroundResource(R.drawable.tab_selected);
                getTreatmentInformation();
            }
        });

        invoicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                invoicesBtn.setSelected(true);
                invoicesBtn.setBackgroundResource(R.drawable.tab_selected);
                getInvoicesInformation();
            }
        });
        return view;
    }

    public void getSummaryInformation()
    {
        selectedFragment = new DoctorAppointmentSummary();
        Bundle bundle = getActivity().getIntent().getExtras();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getDocumentationInformation()
    {
        selectedFragment = new DoctorAppointmentDocument();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getDoctorNoteInformation()
    {
        selectedFragment = new DoctorAppointmentDoctorNote();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getTreatmentInformation()
    {
        selectedFragment = new DoctorAppointmentTreatmentPlan();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment,null).commit();
    }

    public void getInvoicesInformation()
    {
        selectedFragment = new DoctorAppointmentInvoices();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(selectedFragment != null)
        {
            selectedFragment.onPause();
        }
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if(selectedFragment != null)
        {
            selectedFragment.onStop();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setTitle("Visit Details");
        if(selectedFragment != null)
        {
            selectedFragment.onResume();
        }
    }
    @Override
    public void onStart()
    {
        super.onStart();
        if(selectedFragment != null)
        {
            selectedFragment.onStart();
        }
        else
            summaryBtn.callOnClick();

        setTitle("Visit Details");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult DoctorAppointmentInformation /////////////////////////////////");
    }

    private void deselectButtons()
    {
        summaryBtn.setSelected(false);
        documentationBtn.setSelected(false);
        doctorNoteBtn.setSelected(false);
        treatmentBtn.setSelected(false);
        invoicesBtn.setSelected(false);
//        feedbackBtn.setSelected(false);
        summaryBtn.setBackgroundResource(R.drawable.tab_default);
        documentationBtn.setBackgroundResource(R.drawable.tab_default);
        doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
        treatmentBtn.setBackgroundResource(R.drawable.tab_default);
        invoicesBtn.setBackgroundResource(R.drawable.tab_default);
//        feedbackBtn.setBackgroundResource(R.drawable.tab_default);
    }

}
