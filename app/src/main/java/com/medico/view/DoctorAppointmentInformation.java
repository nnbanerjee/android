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
import com.mindnerves.meidcaldiary.R;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInformation extends ParentFragment {

    RelativeLayout replacementFragment;
    Button summaryBtn, documentationBtn, doctorNoteBtn, treatmentBtn, invoicesBtn, feedbackBtn;
    ParentFragment selectedFragment ;

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
                getDocumentationInformation();
            }
        });

        doctorNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectButtons();
                doctorNoteBtn.setSelected(true);
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
            ((ManagePatientProfile)getActivity()).fragmentList.remove(selectedFragment);
        selectedFragment = new DoctorAppointmentSummary();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ManagePatientProfile)getActivity()).fragmentList.add(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDocumentationInformation()
    {
        if(selectedFragment != null)
            ((ManagePatientProfile)getActivity()).fragmentList.remove(selectedFragment);
        selectedFragment = new DoctorAppointmentDocument();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ManagePatientProfile)getActivity()).fragmentList.add(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getDoctorNoteInformation()
    {
        if(selectedFragment != null)
            ((ManagePatientProfile)getActivity()).fragmentList.remove(selectedFragment);
        selectedFragment = new DoctorAppointmentDoctorNote();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ManagePatientProfile)getActivity()).fragmentList.add(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getTreatmentInformation()
    {
        if(selectedFragment != null)
            ((ManagePatientProfile)getActivity()).fragmentList.remove(selectedFragment);
        selectedFragment = new DoctorAppointmentTreatmentPlan();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ManagePatientProfile)getActivity()).fragmentList.add(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getInvoicesInformation()
    {
        if(selectedFragment != null)
            ((ManagePatientProfile)getActivity()).fragmentList.remove(selectedFragment);
        selectedFragment = new DoctorAppointmentInvoices();
        Bundle bundle = getActivity().getIntent().getExtras();
        selectedFragment.setArguments(bundle);
        ((ManagePatientProfile)getActivity()).fragmentList.add(selectedFragment);
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementFragment, selectedFragment, "Doctor Consultations").addToBackStack(null).commit();
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
        MenuItem menuItem = menu.findItem(R.id.add);
        Bundle bundle = getActivity().getIntent().getExtras();
        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
        ParentFragment fragment = activity.fragmentList.get(activity.fragmentList.size()-1);
        if(bundle.getInt(APPOINTMENT_ID) > 0) {
            menuItem.setChecked(false);
            fragment.setEditable(false);
            menuItem.setIcon(R.drawable.edit);
        }
        else
        {
            fragment.setEditable(true);
            menuItem.setChecked(true);
            menuItem.setIcon(R.drawable.save);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
        ParentFragment fragment = activity.fragmentList.get(activity.fragmentList.size()-1);
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                if(item.isChecked() )
                {
                    fragment.update();
                    if(fragment.isChanged() )
                    {
                        if(fragment.canBeSaved()) {
                            fragment.save();
                            item.setChecked(false);
                            fragment.setEditable(false);
                            item.setIcon(R.drawable.edit);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(fragment.canBeSaved()) {
                        item.setChecked(false);
                        item.setIcon(R.drawable.edit);
                        fragment.setEditable(false);
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
                    fragment.setEditable(true);
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
