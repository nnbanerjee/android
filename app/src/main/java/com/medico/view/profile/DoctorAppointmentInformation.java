package com.medico.view.profile;

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
import android.widget.Toast;

import com.medico.application.R;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends ParentFragment {

    RelativeLayout replacementFragment;
    Button summaryBtn, documentationBtn, doctorNoteBtn, treatmentBtn, invoicesBtn, feedbackBtn;
    ParentFragment selectedFragment ;
    Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_information, container, false);
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);
        setHasOptionsMenu(true);
        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        doctorNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
        feedbackBtn = (Button) view.findViewById(R.id.feedback_btn);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Visit Details");



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
                summaryBtn.setBackgroundResource(R.drawable.tab_selected);
                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                doctorNoteBtn.setSelected(true);
                summaryBtn.setBackgroundResource(R.drawable.tab_selected);
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
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                feedbackBtn.setSelected(true);
                feedbackBtn.setBackgroundResource(R.drawable.tab_selected);
            }
        });
        return view;
    }

    public void getSummaryInformation()
    {
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentSummary();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getDocumentationInformation()
    {
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentDocument();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();

//        MenuItem add = menu.findItem(R.id.add);
//        add.setIcon(R.drawable.add);
    }

    public void getDoctorNoteInformation()
    {
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentDoctorNote();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getTreatmentInformation()
    {
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentTreatmentPlan();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getInvoicesInformation()
    {
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentInvoices();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
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
    @Override
    public void onStart()
    {

        super.onStart();
//        summaryBtn.callOnClick();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult DoctorAppointmentInformation /////////////////////////////////");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.add);
//        Bundle bundle = getActivity().getIntent().getExtras();
//        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
//        ParentFragment fragment = activity.fragmentList.get(activity.fragmentList.size()-1);
//        if(bundle.getInt(APPOINTMENT_ID) > 0) {
//            menuItem.setChecked(false);
//            fragment.setEditable(false);
//            menuItem.setIcon(R.drawable.edit);
//        }
//        else
//        {
//            fragment.setEditable(true);
            menuItem.setChecked(true);
            menuItem.setTitle("SAVE");
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        ParentFragment fragment = (ParentFragment)activity.getParentFragment();
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
//               if(fragment instanceof DoctorAppointmentSummary || fragment instanceof PatientMedicinReminder || fragment instanceof PatientSummaryFileUpload) {
                   fragment.update();
                   if (fragment.isChanged()) {
                       if (fragment.canBeSaved()) {
                           fragment.save();
                       } else {
                           Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                       }
                   } else if (fragment.canBeSaved()) {
                       Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                   }
//               }
//               else if (fragment instanceof DoctorAppointmentDocument)
//               {
//                    fragment.save();
//               }

            }
            break;
            case R.id.home: {

            }
            break;

        }
        return true;
    }

    private void deselectButtons()
    {
        summaryBtn.setSelected(false);
        documentationBtn.setSelected(false);
        doctorNoteBtn.setSelected(false);
        treatmentBtn.setSelected(false);
        invoicesBtn.setSelected(false);
        feedbackBtn.setSelected(false);
        summaryBtn.setBackgroundResource(R.drawable.tab_default);
        documentationBtn.setBackgroundResource(R.drawable.tab_default);
        doctorNoteBtn.setBackgroundResource(R.drawable.tab_default);
        treatmentBtn.setBackgroundResource(R.drawable.tab_default);
        invoicesBtn.setBackgroundResource(R.drawable.tab_default);
        feedbackBtn.setBackgroundResource(R.drawable.tab_default);
    }

}
