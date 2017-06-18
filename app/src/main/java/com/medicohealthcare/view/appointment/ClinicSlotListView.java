package com.medicohealthcare.view.appointment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.medicohealthcare.adapter.ClinicSlotListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.PersonID;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicSlotListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;
    DoctorClinicDetails model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                Bundle bundle = activity.getIntent().getExtras();
                DoctorClinicDetails.ClinicSlots slot = (DoctorClinicDetails.ClinicSlots)listView.getAdapter().getItem(position);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,slot.doctorClinicId);
                activity.getIntent().putExtras(bundle);
                ClinicAppointmentScheduleView fragment = new ClinicAppointmentScheduleView();
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
        if(model.slots != null && model.slots.size() > 0 )
        {
            ClinicSlotListAdapter adapter = new ClinicSlotListAdapter(getActivity(), model);
            listView.setAdapter(adapter);
        }
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

    public void setModel(DoctorClinicDetails model)
    {
        this.model = model;
    }

}
