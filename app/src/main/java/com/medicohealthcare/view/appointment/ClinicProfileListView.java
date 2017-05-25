package com.medicohealthcare.view.appointment;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.AppointmentClinicListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicProfileListView extends ParentFragment {


    SharedPreferences session;
    ListView patientListView;
    List<DoctorClinicDetails> clinicDetails;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);

//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Patient Profiles");

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                setHasOptionsMenu(false);
//                Bundle bun = getActivity().getIntent().getExtras();
//                PatientShortProfile profile = (PatientShortProfile)adapterView.getAdapter().getItem(i);
//                        ParentFragment fragment = new PatientVisitDatesView();
//                        ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
//                        bun.putInt(PARAM.PATIENT_ID, profile.getPatientId().intValue());
//                        getActivity().getIntent().putExtras(bun);
//                      fragment.setArguments(bun);
//                        FragmentManager fragmentManger = getActivity().getFragmentManager();
//                        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        return view;
    }

    //This will show all patients list for logged in doctor
//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        Bundle bundle = getActivity().getIntent().getExtras();
//        DoctorId doc= new DoctorId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)).toString());
//        api.getPatientProfileList(doc, new Callback<List<PatientShortProfile>>() {
//            @Override
//            public void success(final List<PatientShortProfile> allPatientsProfiles, Response response) {
//                AppointmentClinicListAdapter adapter = new AppointmentClinicListAdapter(getActivity(), new PatientProfileList(allPatientsProfiles));
//                patientListView.setAdapter(adapter);
//                progress.dismiss();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                progress.dismiss();
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                error.printStackTrace();
//            }
//        });
//    }
    @Override
    public void onStart()
    {
        super.onStart();
//        progress = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        api.getClinicsByDoctor1(new DoctorId(doctorId.toString()), new Callback<List<DoctorClinicDetails>>() {
            @Override
            public void success(List<DoctorClinicDetails> clinicDetailsreturn, Response response) {
                clinicDetails = clinicDetailsreturn;
                AppointmentClinicListAdapter adapter = new AppointmentClinicListAdapter(getActivity(), clinicDetailsreturn);
                patientListView.setAdapter(adapter);
//                 progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
//                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
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
        inflater.inflate(R.menu.patient_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:




            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
