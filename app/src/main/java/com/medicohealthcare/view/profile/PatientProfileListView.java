package com.medicohealthcare.view.profile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ListView;

import com.medicohealthcare.adapter.PatientListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.model.PatientProfileList;
import com.medicohealthcare.model.PatientShortProfile;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.search.PersonSearchView;
import com.medicohealthcare.view.settings.PersonProfileEditView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientProfileListView extends ParentFragment
{


    SharedPreferences session;
    ListView patientListView;
//    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        showBusy();
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();

        Bundle bundle = getActivity().getIntent().getExtras();
        DoctorId doc= new DoctorId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)).toString());
        api.getPatientProfileList(doc, new Callback<List<PatientShortProfile>>() {
            @Override
            public void success(final List<PatientShortProfile> allPatientsProfiles, Response response)
            {
                PatientListAdapter adapter = new PatientListAdapter(getActivity(), new PatientProfileList(allPatientsProfiles));
                patientListView.setAdapter(adapter);
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        setHasOptionsMenu(true);
        setTitle("Patient List");
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
                    return true;
                }
                return false;
            }
        });
        setHasOptionsMenu(true);
        setTitle("Patient List");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.add_search_home, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
            {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(PERSON_ID,0);
                bun.putInt(PARAM.PERSON_ROLE, PATIENT);
                int profiletype = PATIENT;
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = null;
                fragment = new PersonProfileEditView();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PersonProfileEditView.class.getName()).addToBackStack(PersonProfileEditView.class.getName()).commit();
                return true;
            }
            case R.id.search:
            {
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.PATIENT_SETTING_VIEW);
                bundle.putInt(PARAM.SEARCH_ROLE, PARAM.PATIENT);
                bundle.putInt(PARAM.SEARCH_TYPE, PARAM.SEARCH_GLOBAL);
                getActivity().getIntent().putExtras(bundle);
                ParentFragment fragment = new PersonSearchView();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fragment,PersonSearchView.class.getName()).addToBackStack(PersonSearchView.class.getName()).commit();
                  return true;
            }
            case R.id.home:
            {
                ((ParentActivity)getActivity()).goHome();
            }
            default:
                return false;

        }
    }

}
