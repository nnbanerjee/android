package com.medicohealthcare.view.medicinealarm;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medicohealthcare.adapter.PatientMedicineListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.PatientAppointmentsVM;
import com.medicohealthcare.model.PatientId;
import com.medicohealthcare.model.PatientMedicine;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.PatientMedicinReminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientMedicineListView extends ParentFragment
{
    public static int NONE = 0;
    public static int UPCOMING = 1;
    public static int PAST = 2;
    public static int ALL = 3;
    Button upcomingAppointment, pastAppointment, allAppointment;
    StickyListHeadersListView appointmentList;
    List<PatientMedicine> patientMedicines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_appointment_list_view, container, false);
        final Bundle bun = getArguments();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Manage Finance");
        upcomingAppointment = (Button) view.findViewById(R.id.upcoming_appointment);
        pastAppointment = (Button) view.findViewById(R.id.past_appointment);
        allAppointment = (Button) view.findViewById(R.id.all_appointment);
        appointmentList = (StickyListHeadersListView) view.findViewById(R.id.appointment_list);
        upcomingAppointment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                upcomingAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                pastAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                allAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                List<PatientMedicine> upcomingPatientMedicines = new ArrayList<PatientMedicine>();
                if(patientMedicines != null)
                {
                    for (PatientMedicine medicine : patientMedicines)
                    {
                        if (medicine.getEndDate().longValue() > new Date().getTime())
                        {
                            upcomingPatientMedicines.add(medicine);
                        }
                    }
                    PatientMedicineListAdapter adapter = new PatientMedicineListAdapter(getActivity(), upcomingPatientMedicines);
                    appointmentList.setAdapter(adapter);
                }
            }
        });
        pastAppointment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                upcomingAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                pastAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                allAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                List<PatientMedicine> pastPatientMedicines = new ArrayList<PatientMedicine>();
                if(patientMedicines != null)
                {
                    for (PatientMedicine medicine : patientMedicines)
                    {
                        if (medicine.getEndDate().longValue() < new Date().getTime())
                        {
                            pastPatientMedicines.add(medicine);
                        }
                    }
                    PatientMedicineListAdapter adapter = new PatientMedicineListAdapter(getActivity(), pastPatientMedicines);
                    appointmentList.setAdapter(adapter);
                }
            }
        });
        allAppointment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                upcomingAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                pastAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                allAppointment.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                if(patientMedicines != null)
                {
                    List<PatientAppointmentsVM.Appointments> appointmentsList = new ArrayList<>();
                    PatientMedicineListAdapter adapter = new PatientMedicineListAdapter(getActivity(), patientMedicines);
                    appointmentList.setAdapter(adapter);
                }
            }
        });
        textviewTitle.setText("Manage Appointments");
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        PatientId patient = new PatientId(new Integer(bundle.getInt(PARAM.PROFILE_ID)));
        api.getAllMedicineDetails(patient, new Callback<List<PatientMedicine>>()
        {
            @Override
            public void success(List<PatientMedicine> medicines, Response response)
            {
//                if (patientAppointments != null && patientAppointments.upcomingAppointments != null && patientAppointments.upcomingAppointments.size() > 0)
                {
                    patientMedicines = medicines;
                    upcomingAppointment.callOnClick();
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        setHasOptionsMenu(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    // handle back button's click listener
//                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                Bundle args = getActivity().getIntent().getExtras();
                args.putInt(MEDICINE_ID,0);
                getActivity().getIntent().putExtras(args);
                ParentFragment fragment = new PatientMedicinReminder();
//                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientMedicinReminder.class.getName()).addToBackStack(PatientMedicinReminder.class.getName()).commit();

                return true;
            }
            default:
            {
                return false;
            }

        }
    }



}
