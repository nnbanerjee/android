package com.medico.view;

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

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentDoctorNote;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentDocument;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentInvoices;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentTreatmentPlan;
import com.mindnerves.meidcaldiary.Fragments.FeedbackFragment;
import com.mindnerves.meidcaldiary.R;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends ParentFragment {

    RelativeLayout replacementFragment;
    Button summaryBtn, documentationBtn, doctorNoteBtn, treatmentBtn, invoicesBtn, feedbackBtn;
    ParentFragment selectedFragment, summary, document, notes, treatment, invoice, feedback;


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

        selectedFragment = summary = new DoctorAppointmentSummary();
        document = new DoctorAppointmentDocument();
        notes = new DoctorAppointmentDoctorNote();
        treatment = new DoctorAppointmentTreatmentPlan();
        invoice = new DoctorAppointmentInvoices();
        feedback = new FeedbackFragment();

        Bundle bundle = getActivity().getIntent().getExtras();
        summary.setArguments(bundle);
        document.setArguments(bundle);
        notes.setArguments(bundle);
        treatment.setArguments(bundle);
        invoice.setArguments(bundle);
        feedback.setArguments(bundle);

        summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                summaryBtn.setSelected(true);
                summaryBtn.setBackgroundResource(R.drawable.tab_selected);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(summary);
                selectedFragment = summary;
                getSummaryInformation();
            }
        });

        documentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                documentationBtn.setSelected(true);
                documentationBtn.setBackgroundResource(R.drawable.tab_selected);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(document);
                selectedFragment = document;
                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                doctorNoteBtn.setSelected(true);
                doctorNoteBtn.setBackgroundResource(R.drawable.tab_selected);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(notes);
                selectedFragment = notes;
                getDoctorNoteInformation();
            }
        });

        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                treatmentBtn.setSelected(true);
                treatmentBtn.setBackgroundResource(R.drawable.tab_selected);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(treatment);
                selectedFragment = treatment;
                getTreatmentInformation();
            }
        });

        invoicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                invoicesBtn.setSelected(true);
                invoicesBtn.setBackgroundResource(R.drawable.tab_selected);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(invoice);
                selectedFragment = invoice;
                getInvoicesInformation();
            }
        });
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                feedbackBtn.setSelected(true);
                ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
                activity.fragmentList.remove(selectedFragment);
                activity.fragmentList.add(feedback);
                selectedFragment = feedback;
                feedbackBtn.setBackgroundResource(R.drawable.tab_selected);

            }
        });
        return view;
    }

    public void getSummaryInformation()
    {
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, summary, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDocumentationInformation() {
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, summary, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDoctorNoteInformation() {
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, summary, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getTreatmentInformation() {
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, summary, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getInvoicesInformation() {
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, summary, "Doctor Consultations").addToBackStack(null).commit();
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
        summaryBtn.setSelected(true);
        summaryBtn.setBackgroundResource(R.drawable.tab_selected);
        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
        activity.fragmentList.remove(selectedFragment);
        activity.fragmentList.add(summary);
        selectedFragment = summary;
        getSummaryInformation();
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
        MenuItem menuItem = menu.findItem(R.id.add);
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle.getInt(APPOINTMENT_ID) > 0) {
            menuItem.setChecked(false);
            selectedFragment.setEditable(false);
            menuItem.setIcon(R.drawable.edit);
            selectedFragment.setEditable(false);
        }
        else
        {
            selectedFragment.setEditable(true);
            menuItem.setChecked(true);
            menuItem.setIcon(R.drawable.save);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                if(item.isChecked() )
                {
                    selectedFragment.update();
                    if(selectedFragment.isChanged() )
                    {
                        if(selectedFragment.canBeSaved()) {
                            selectedFragment.save();
                            item.setChecked(false);
                            selectedFragment.setEditable(false);
                            item.setIcon(R.drawable.edit);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(selectedFragment.canBeSaved()) {
                        item.setChecked(false);
                        item.setIcon(R.drawable.edit);
                        selectedFragment.setEditable(false);
                        Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    item.setChecked(true);
                    item.setIcon(R.drawable.save);
                    selectedFragment.setEditable(true);
                }
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
