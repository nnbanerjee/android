package com.medicohealthcare.view.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medicohealthcare.adapter.PatientAppointmentListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.PatientAppointmentsVM;
import com.medicohealthcare.model.PatientId;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorAppointmentListView extends ParentFragment
{
    public static int NONE = 0;
    public static int UPCOMING = 1;
    public static int PAST = 2;
    public static int ALL = 3;
    Button upcomingAppointment, pastAppointment, allAppointment;
    StickyListHeadersListView appointmentList;
    PatientAppointmentsVM patientAppointments;

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
                PatientAppointmentListAdapter adapter = new PatientAppointmentListAdapter(getActivity(), patientAppointments.upcomingAppointments);
                appointmentList.setAdapter(adapter);
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
                PatientAppointmentListAdapter adapter = new PatientAppointmentListAdapter(getActivity(), patientAppointments.historicalAppointments);
                appointmentList.setAdapter(adapter);
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
                List<PatientAppointmentsVM.Appointments> appointmentsList = new ArrayList<>();
                appointmentsList.addAll(patientAppointments.upcomingAppointments);
                appointmentsList.addAll(patientAppointments.historicalAppointments);
                PatientAppointmentListAdapter adapter = new PatientAppointmentListAdapter(getActivity(), appointmentsList);
                appointmentList.setAdapter(adapter);
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
        api.getPatientAppointments(patient, new Callback<PatientAppointmentsVM>()
        {
            @Override
            public void success(PatientAppointmentsVM appointments, Response response)
            {
                if (appointments != null && appointments.status.intValue() != 0)
                {
                    patientAppointments = appointments;

                    allAppointment.callOnClick();
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
        inflater.inflate(R.menu.doctor_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
