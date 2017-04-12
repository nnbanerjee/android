package com.medico.view.settings;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.DependentDelegationSettingListAdapter;
import com.medico.application.R;
import com.medico.model.DependentDelegatePerson;
import com.medico.model.DependentDelegatePersonRequest;
import com.medico.util.PARAM;
import com.medico.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ManageDependentDelegateListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                DependentDelegatePerson profile = (DependentDelegatePerson)adapterView.getAdapter().getItem(i);
                        ParentFragment fragment = new DependentDelegateProfileView();
                        ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                        bun.putInt(PARAM.PROFILE_ID, profile.getId().intValue());
                        bun.putInt(PARAM.PROFILE_ROLE, profile.getRole().intValue());
                        bun.putString(PARAM.DEPENDENT_DELEGATE_RELATION,profile.relation);
                        getActivity().getIntent().putExtras(bun);
                      fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
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
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        Integer profileType = bundle.getInt(PROFILE_TYPE);
        DependentDelegatePersonRequest request1 = new DependentDelegatePersonRequest(profileId, profileType);
        api.getAllDependentsDelegates(request1, new Callback<List<DependentDelegatePerson>>() {
            @Override
            public void success(final List<DependentDelegatePerson> allPatientsProfiles, Response response) {
                DependentDelegationSettingListAdapter adapter = new DependentDelegationSettingListAdapter(getActivity(), allPatientsProfiles);
                listView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) { 
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        switch (bundle.getInt(PROFILE_TYPE))
        {
            case DEPENDENT:
                textviewTitle.setText("Dependent Profiles");
                break;
            case DELEGATE:
                textviewTitle.setText("Delagation Profiles");
                break;
        }
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
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                Integer profileId = bundle.getInt(PROFILE_ID);
                Integer profileRole = bundle.getInt(PROFILE_ROLE);
                Integer profileType = bundle.getInt(PROFILE_TYPE);
                Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
                if(profileType.intValue() == DEPENDENT) {
                    bundle.putInt(PROFILE_ID, 0);
                    getActivity().getIntent().putExtras(bundle);
                    ParentFragment fragment = new DependentDelegateProfileView();
                    ((ManagePersonSettings) getActivity()).fragmentList.add(fragment);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
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
