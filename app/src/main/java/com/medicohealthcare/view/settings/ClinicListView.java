package com.medicohealthcare.view.settings;

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

import com.medicohealthcare.adapter.ClinicSettingListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.PersonID;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.search.PersonSearchView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);

        showBusy();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                Clinic1 profile = (Clinic1)adapterView.getAdapter().getItem(i);
                        ParentFragment fragment = new ClinicProfileEditView();
                        ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                        bun.putInt(PARAM.CLINIC_ID, profile.idClinic.intValue());
                        bun.putInt(PARAM.CLINIC_TYPE, profile.type.intValue());
                        getActivity().getIntent().putExtras(bun);
                      fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicProfileEditView.class.getName()).addToBackStack(ClinicProfileEditView.class.getName()).commit();
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
        api.getAllClinics(profileId, new Callback<List<Clinic1>>() {
            @Override
            public void success(final List<Clinic1> allPatientsProfiles, Response response) {
                ClinicSettingListAdapter adapter = new ClinicSettingListAdapter(getActivity(), allPatientsProfiles);
                listView.setAdapter(adapter);
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Clinics");
        setHasOptionsMenu(true);
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.add_search_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
            {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(CLINIC_ID,0);
                bun.putInt(CLINIC_TYPE,0);
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new ClinicProfileEditView();
                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ClinicProfileEditView.class.getName()).addToBackStack(ClinicProfileEditView.class.getName()).commit();
                return true;
            }
            case R.id.search:
            {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(CLINIC_ID,0);
                bun.putInt(CLINIC_TYPE,0);
                bun.putInt(SETTING_VIEW_ID,CLINIC_SETTING_VIEW);
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new PersonSearchView();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ParentFragment.class.getName()).addToBackStack(ParentFragment.class.getName()).commit();
                return true;
            }
            case R.id.home:
            {
                getActivity().onBackPressed();
            }

        }
        return false;
    }


}
