package com.medicohealthcare.view.appointment;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.medicohealthcare.adapter.PersonSettingListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.LinkedPersonRequest;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorListView extends ParentFragment
{


    SharedPreferences session;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                Person profile = (Person)adapterView.getAdapter().getItem(i);
                ParentFragment fragment = new ClinicProfileListView();
                bun.putInt(PARAM.DOCTOR_ID, profile.getId().intValue());
                getActivity().getIntent().putExtras(bun);
                fragment.setArguments(bun);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fragment,ClinicProfileListView.class.getName()).addToBackStack(ClinicProfileListView.class.getName()).commit();
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
        Integer profileId = bundle.getInt(PROFILE_ID);
        LinkedPersonRequest  request = new LinkedPersonRequest(profileId,DOCTOR);
        api.getPersonLinkage(request, new Callback<List<Person>>() {
            @Override
            public void success(final List<Person> allPatientsProfiles, Response response) {
                PersonSettingListAdapter adapter = new PersonSettingListAdapter(getActivity(), allPatientsProfiles);
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });

        setTitle("Doctor Appointments");
        setHasOptionsMenu(false);
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
        setHasOptionsMenu(false);
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

}
