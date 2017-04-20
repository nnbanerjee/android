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
    Button summaryBtn, documentationBtn, doctorNoteBtn, treatmentBtn, invoicesBtn;
    ParentFragment selectedFragment ;
    Menu menu;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_information, container, false);
        this.inflater = inflater;
        replacementFragment = (RelativeLayout) view.findViewById(R.id.replacementFragment);
        setHasOptionsMenu(true);
        summaryBtn = (Button) view.findViewById(R.id.summaryBtn);
        documentationBtn = (Button) view.findViewById(R.id.documentationBtn);
        doctorNoteBtn = (Button) view.findViewById(R.id.doctorNoteBtn);
        treatmentBtn = (Button) view.findViewById(R.id.treatmentBtn);
        invoicesBtn = (Button) view.findViewById(R.id.invoicesBtn);
//        feedbackBtn = (Button) view.findViewById(R.id.feedback_btn);

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
        if(menu != null)
        {
            MenuItem menuItem = menu.findItem(R.id.add);
            menuItem.setIcon(null);
            menuItem.setTitle("SAVE");
        }
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
        if (menu != null)
        {
            MenuItem menuItem = menu.findItem(R.id.add);
            menuItem.setIcon(R.drawable.ic_add_white_24dp);
            menuItem.setChecked(true);
        }
        if(selectedFragment != null)
            ((ParentActivity)getActivity()).detachFragment(selectedFragment);
        selectedFragment = new DoctorAppointmentDocument();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ParentActivity)getActivity()).attachFragment(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").commit();
    }

    public void getDoctorNoteInformation()
    {
        if(menu != null)
        {
            MenuItem menuItem = menu.findItem(R.id.add);
            menuItem.setIcon(null);
            menuItem.setTitle("SAVE");
        }
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
        if (menu != null)
        {
            MenuItem menuItem = menu.findItem(R.id.add);
            menuItem.setIcon(R.drawable.ic_add_white_24dp);
            menuItem.setChecked(true);
        }
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
        if (menu != null)
        {
            MenuItem menuItem = menu.findItem(R.id.add);
            menuItem.setIcon(R.drawable.ic_add_white_24dp);
            menuItem.setChecked(true);
        }
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
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart()
    {

        super.onStart();
        summaryBtn.callOnClick();
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
        menuItem.setIcon(null);
        menuItem.setChecked(true);
        menuItem.setTitle("SAVE");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        ParentFragment fragment = selectedFragment;
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
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

            }
            break;
            case R.id.home: {
                return false;
            }

        }
        return false;
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
