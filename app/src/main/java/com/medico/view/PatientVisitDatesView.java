package com.medico.view;

import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.PatientVisitDatesAdapter;
import com.medico.model.DoctorIdPatientId;
import com.medico.model.PatientVisits;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Utils.PARAM;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientVisitDatesView extends ParentFragment
{

    String doctor_email, patientId;
    public SharedPreferences session;
    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
    String doctorId;
    Global global;
    PatientVisits appointments;
//    ImageView addClinic;
    String fragmentCall;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.all_patient_appointment, container, false);
        final Bundle bun = getArguments();
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
//        addClinic = (ImageView) view.findViewById(R.id.add_clinic_appointment);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Visit Dates");

        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setHasOptionsMenu(false);
                //appointments
                //Fragment fragment = new DoctorAppointmentSummary();
                Bundle bun = getActivity().getIntent().getExtras();
                PatientVisits visits = (PatientVisits)parent.getAdapter().getItem(position);
                bun.putInt(PARAM.APPOINTMENT_ID, visits.getAppointmentId());
                bun.putLong(PARAM.APPOINTMENT_DATETIME, visits.getDateTime());
                bun.putString(PARAM.REFERRED_BY, visits.getReferredBy());
                bun.putString(PARAM.CLINIC_NAME, visits.getClinicName());
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new DoctorAppointmentInformation();
                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

//        //this button is hidden and moved to menu
//        addClinic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //appointments
//                //Fragment fragment = new DoctorAppointmentSummary();
//                ParentFragment fragment = new DoctorAppointmentInformation();
//                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
//                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//
//        }});

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        inflater.inflate(R.menu.patient_visit_dates, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.ic_note_add_black_24dp);
    }



    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    goToBack();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onStart()// getAllPatientAppointment()
     {
        super.onStart();
         Bundle bundle = getActivity().getIntent().getExtras();
        DoctorIdPatientId doc = new DoctorIdPatientId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)).toString(), new Integer(bundle.getInt(PARAM.PATIENT_ID)).toString());
        api.getPatientVisitDatesByDoctor1(doc, new Callback<List<PatientVisits>>()
        {
            @Override
            public void success(List<PatientVisits> visits, Response response) {
                String json = new String(((TypedByteArray) response.getBody()).getBytes());
                if (UtilSingleInstance.checkForServerErrorsInResponse(json).equalsIgnoreCase("")) {

                    if (visits != null) {
                        PatientVisitDatesAdapter adapter = new PatientVisitDatesAdapter(getActivity().getApplicationContext(), visits);
                        allAppointments.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "No Visits found!", Toast.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                } else {
//                    Toast.makeText(getActivity(), UtilSingleInstance.checkForServerErrorsInResponse(json), Toast.LENGTH_LONG).show();
                    progress.dismiss();

                }

            }
            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }

        });
     }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                ParentFragment fragment = new DoctorAppointmentInformation();
                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
            break;
            case R.id.home: {

            }
            break;

        }
        return true;
    }
}
