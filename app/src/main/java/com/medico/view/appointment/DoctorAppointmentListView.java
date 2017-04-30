package com.medico.view.appointment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.PatientAppointmentListAdapter;
import com.medico.application.R;
import com.medico.model.PatientAppointmentsVM;
import com.medico.model.PatientId;
import com.medico.util.PARAM;
import com.medico.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorAppointmentListView extends ParentFragment {


    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
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
        api.getPatientAppointments(patient, new Callback<PatientAppointmentsVM>() {
            @Override
            public void success(PatientAppointmentsVM patientAppointments, Response response)
            {
                if(patientAppointments != null && patientAppointments.upcomingAppointments != null && patientAppointments.upcomingAppointments.size() > 0 )
                {
                    PatientAppointmentListAdapter adapter = new PatientAppointmentListAdapter(getActivity(), patientAppointments.upcomingAppointments);
                    doctorListView.setAdapter(adapter);
                }
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
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
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
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
        super.onCreateOptionsMenu(menu,inflater);
    }
//

}
