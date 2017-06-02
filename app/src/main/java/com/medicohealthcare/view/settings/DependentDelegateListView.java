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

import com.medicohealthcare.adapter.DependentDelegationSettingListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DependentDelegatePerson;
import com.medicohealthcare.model.DependentDelegatePersonRequest;
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
public class DependentDelegateListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                DependentDelegatePerson profile = (DependentDelegatePerson)adapterView.getAdapter().getItem(i);
                ParentFragment fragment;
                if(bun.getInt(PROFILE_TYPE) == DEPENDENT)
                {
                    fragment = new DependentDelegateProfileView();
                    bun.putInt(PARAM.DEPENDENT_ID, profile.getId().intValue());
                    bun.putInt(PARAM.DEPENDENT_ROLE, profile.getRole().intValue());
                    bun.putString(PARAM.DEPENDENT_DELEGATE_RELATION, profile.relation);
                    getActivity().getIntent().putExtras(bun);
                    fragment.setArguments(bun);
                }
                else
                {
                    int role = profile.getRole().intValue();
                    switch(role)
                    {
                        case DOCTOR:
                        {
                            fragment = new DoctorProfileEditView();
                            break;
                        }
                        default:
                        {
                            fragment = new PersonProfileEditView();
                        }
                    }
                    bun.putInt(PARAM.PERSON_ID, profile.getId().intValue());
                    bun.putInt(PARAM.PERSON_ID, profile.getRole().intValue());
                    getActivity().getIntent().putExtras(bun);
                    fragment.setArguments(bun);
                }
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, DependentDelegateProfileView.class.getName()).addToBackStack(DependentDelegateProfileView.class.getName()).commit();
            }
        });

        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        Integer profileType = bundle.getInt(PROFILE_TYPE);
        if(profileType.intValue() == PARAM.DEPENDENT)
        {
            DependentDelegatePersonRequest request1 = new DependentDelegatePersonRequest(loggedinUserId, profileType);
            api.getAllDependentsDelegates(request1, new Callback<List<DependentDelegatePerson>>()
            {
                @Override
                public void success(final List<DependentDelegatePerson> allPatientsProfiles, Response response)
                {
                    DependentDelegationSettingListAdapter adapter = new DependentDelegationSettingListAdapter(getActivity(), allPatientsProfiles);
                    listView.setAdapter(adapter);
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });

            textviewTitle.setText("Dependent Profiles");
        }
        else
        {
            DependentDelegatePersonRequest request1 = new DependentDelegatePersonRequest(profileId, profileType);
            api.getAllDependentsDelegates(request1, new Callback<List<DependentDelegatePerson>>()
            {
                @Override
                public void success(final List<DependentDelegatePerson> allPatientsProfiles, Response response)
                {
                    DependentDelegationSettingListAdapter adapter = new DependentDelegationSettingListAdapter(getActivity(), allPatientsProfiles);
                    listView.setAdapter(adapter);
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
            textviewTitle.setText("Delagation Profiles");
        }
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
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                Integer profileId = bundle.getInt(PERSON_ID);
                Integer profileRole = bundle.getInt(PERSON_ROLE);
                Integer profileType = bundle.getInt(PROFILE_TYPE);
                Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
                if(profileType.intValue() == DEPENDENT)
                {
                    bundle.putInt(DEPENDENT_ID, 0);
                    bundle.putInt(PERSON_ROLE,PATIENT);
                    getActivity().getIntent().putExtras(bundle);
                    ParentFragment fragment = new DependentDelegateProfileView();
                    ((ManagePersonSettings) getActivity()).fragmentList.add(fragment);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment,DependentDelegateProfileView.class.getName()).addToBackStack(DependentDelegateProfileView.class.getName()).commit();
                }
                else
                {
                    //search person
                }

            }
            break;
        }
        return true;
    }
}
