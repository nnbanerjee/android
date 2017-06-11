package com.medicohealthcare.view.profile;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.medicohealthcare.adapter.PatientVisitDatesAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorIdPatientId;
import com.medicohealthcare.model.PatientVisits;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

//
//import Utils.UtilSingleInstance;

//import com.mindnerves.meidcaldiary.Global;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientVisitDatesView extends ParentFragment
{

    String doctor_email, patientId;
    public SharedPreferences session;
//    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
    PatientVisits appointments;
    String fragmentCall;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.sticky_header_list_view, container, false);
        final Bundle bun = getArguments();
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                PatientVisits visits = (PatientVisits)parent.getAdapter().getItem(position);
                bun.putInt(PARAM.APPOINTMENT_ID, visits.getAppointmentId());
                bun.putLong(PARAM.APPOINTMENT_DATETIME, visits.getDateTime());
                bun.putString(PARAM.REFERRED_BY, visits.getReferredBy());
                bun.putString(PARAM.CLINIC_NAME, visits.getClinicName());
                bun.putInt(PARAM.CLINIC_ID, visits.getClinicId());
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new DoctorAppointmentInformation();
//                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, DoctorAppointmentInformation.class.getName()).addToBackStack(DoctorAppointmentInformation.class.getName()).commit();
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.patient_visit_dates, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        setTitle("Visit Dates");
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStart()// getAllPatientAppointment()
     {
        super.onStart();
         showBusy();
         Bundle bundle = getActivity().getIntent().getExtras();
        DoctorIdPatientId doc = new DoctorIdPatientId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)), new Integer(bundle.getInt(PARAM.PATIENT_ID)));
        api.getPatientVisitDatesByDoctor1(doc, new Callback<List<PatientVisits>>()
        {
            @Override
            public void success(List<PatientVisits> visits, Response response) {
                    if (visits != null) {
                        PatientVisitDatesAdapter adapter = new PatientVisitDatesAdapter(getActivity().getApplicationContext(), visits);
                        allAppointments.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "No Visits found!", Toast.LENGTH_LONG).show();
                    }
                hideBusy();
            }
            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }

        });
         setTitle("Visit Dates");
         setHasOptionsMenu(true);
     }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_visit: {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                ParentFragment fragment = new DoctorAppointmentInformation();
//                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, DoctorAppointmentInformation.class.getName()).addToBackStack(DoctorAppointmentInformation.class.getName()).commit();
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return false;
            }

        }
        return false;
    }
}
