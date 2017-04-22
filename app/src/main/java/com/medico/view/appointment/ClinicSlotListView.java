package com.medico.view.appointment;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.medico.adapter.ClinicSlotListAdapter;
import com.medico.application.R;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.PersonID;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicSlotListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;
//    ProgressDialog progress;
    DoctorClinicDetails model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);

//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));

//        Bundle bundle = getActivity().getIntent().getExtras();


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                setHasOptionsMenu(false);
//                Bundle bun = getActivity().getIntent().getExtras();
//                Clinic1 profile = (Clinic1)adapterView.getAdapter().getItem(i);
//                        ParentFragment fragment = new ClinicProfileEditView();
//                        ((ManageDoctorAppointment)getActivity()).fragmentList.add(fragment);
//                        bun.putInt(PARAM.CLINIC_ID, profile.idClinic.intValue());
//                        bun.putInt(PARAM.CLINIC_TYPE, profile.type.intValue());
//                        getActivity().getIntent().putExtras(bun);
//                      fragment.setArguments(bun);
//                        FragmentManager fragmentManger = getActivity().getFragmentManager();
//                        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                ManageDoctorAppointment parentactivity = (ManageDoctorAppointment)activity;
                Bundle bundle = activity.getIntent().getExtras();
                DoctorClinicDetails.ClinicSlots slot = (DoctorClinicDetails.ClinicSlots)listView.getAdapter().getItem(position);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,slot.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
//                fragment.setModel(slot);
                ((ParentActivity)activity).attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicAppointmentScheduleView.class.getName()).addToBackStack(ClinicAppointmentScheduleView.class.getName()).commit();
            }
        });
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        Integer profileType = bundle.getInt(PROFILE_TYPE);
        PersonID profileId = new PersonID(loggedinUserId);
//        api.getAllClinics(profileId, new Callback<List<Clinic1>>() {
//            @Override
//            public void success(final List<Clinic1> allPatientsProfiles, Response response) {
                ClinicSlotListAdapter adapter = new ClinicSlotListAdapter(getActivity(), model);
                listView.setAdapter(adapter);
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

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Manage Appointments");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
//                setHasOptionsMenu(false);
//                Bundle bun = getActivity().getIntent().getExtras();
//                bun.putInt(CLINIC_ID,0);
//                bun.putInt(CLINIC_TYPE,0);
//                getActivity().getIntent().putExtras(bun);
//                ParentFragment fragment = new ClinicProfileEditView();
//                ((ManageDoctorAppointment)getActivity()).fragmentList.add(fragment);
//                fragment.setArguments(bun);
//                FragmentManager fragmentManger = getActivity().getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
            break;
        }
        return true;
    }
    public void setModel(DoctorClinicDetails model)
    {
        this.model = model;
    }

}
