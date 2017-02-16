package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentDoctorNote;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentDocument;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentInvoices;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentTreatmentPlan;
import com.mindnerves.meidcaldiary.R;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends ParentFragment {

    RelativeLayout replacementFragment;
    Button summaryBtn,documentationBtn,doctorNoteBtn,treatmentBtn,invoicesBtn,feedbackBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_information, container,false);
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);
        setHasOptionsMenu(true);
        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        doctorNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
        feedbackBtn = (Button)view.findViewById(R.id.feedback_btn);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Visit Details");

        getSummaryInformation();

//        final Button back = (Button)getActivity().findViewById(R.id.back_button);
//        back.setVisibility(View.VISIBLE);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBack();
//            }
//        });

        summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_selected);
                documentationBtn.setBackgroundResource(R.drawable.tab_default);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
                treatmentBtn.setBackgroundResource(R.drawable.tab_default);
                invoicesBtn.setBackgroundResource(R.drawable.tab_default);
                feedbackBtn.setBackgroundResource(R.drawable.tab_default);
                getSummaryInformation();
            }
        });

        documentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_default);
                documentationBtn.setBackgroundResource(R.drawable.tab_selected);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
                treatmentBtn.setBackgroundResource(R.drawable.tab_default);
                invoicesBtn.setBackgroundResource(R.drawable.tab_default);
                feedbackBtn.setBackgroundResource(R.drawable.tab_default);
                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_default);
                documentationBtn.setBackgroundResource(R.drawable.tab_default);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_selected);
                treatmentBtn.setBackgroundResource(R.drawable.tab_default);
                invoicesBtn.setBackgroundResource(R.drawable.tab_default);
                feedbackBtn.setBackgroundResource(R.drawable.tab_default);
                getDoctorNoteInformation();
            }
        });

        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_default);
                documentationBtn.setBackgroundResource(R.drawable.tab_default);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
                treatmentBtn.setBackgroundResource(R.drawable.tab_selected);
                invoicesBtn.setBackgroundResource(R.drawable.tab_default);
                feedbackBtn.setBackgroundResource(R.drawable.tab_default);
                getTreatmentInformation();
            }
        });

        invoicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_default);
                documentationBtn.setBackgroundResource(R.drawable.tab_default);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
                treatmentBtn.setBackgroundResource(R.drawable.tab_default);
                invoicesBtn.setBackgroundResource(R.drawable.tab_selected);
                feedbackBtn.setBackgroundResource(R.drawable.tab_default);
                getInvoicesInformation();
            }
        });
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryBtn.setBackgroundResource(R.drawable.tab_default);
                documentationBtn.setBackgroundResource(R.drawable.tab_default);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
                treatmentBtn.setBackgroundResource(R.drawable.tab_default);
                invoicesBtn.setBackgroundResource(R.drawable.tab_default);
                feedbackBtn.setBackgroundResource(R.drawable.tab_selected);

            }
        });
        return view;
    }

    public void getSummaryInformation(){
        ParentFragment fragment = new DoctorAppointmentSummary();
        ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
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
        fragmentManger.beginTransaction().add(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
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

//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//                    goToBack();
//                    return true;
//                }
//                return false;
//            }
//        });
    }


//    public void  goToBack(){
//        //Fragment fragment = new PatientAllAppointment();
//        Fragment fragment = new PatientVisitDatesView();
//        FragmentManager fragmentManger = getFragmentManager();
//        Bundle bun = new Bundle();
//        bun.putString("fragment","from appintment information");
//        fragment.setArguments(bun);
//        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
//        final Button back = (Button)getActivity().findViewById(R.id.back_button);
//        back.setVisibility(View.INVISIBLE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult DoctorAppointmentInformation /////////////////////////////////");
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.edit);
    }

}
