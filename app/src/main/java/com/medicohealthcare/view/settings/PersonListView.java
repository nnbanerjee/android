package com.medicohealthcare.view.settings;

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

import com.medicohealthcare.adapter.PatientSettingListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.LinkedPersonRequest;
import com.medicohealthcare.model.Person;
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
public class PersonListView extends ParentFragment {


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

//        Bundle bundle = getActivity().getIntent().getExtras();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                Person profile = (Person)adapterView.getAdapter().getItem(i);
                        ParentFragment fragment = new PersonProfileEditView();
                        ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                        bun.putInt(PARAM.PERSON_ID, profile.getId().intValue());
                        bun.putInt(PARAM.PERSON_ROLE, profile.getId().intValue());
                        getActivity().getIntent().putExtras(bun);
                      fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().add(R.id.service, fragment, PersonProfileEditView.class.getName()).addToBackStack(PersonProfileEditView.class.getName()).commit();
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
        Integer profileType = bundle.getInt(PROFILE_TYPE);
        LinkedPersonRequest  request = new LinkedPersonRequest(profileId,profileType);
        api.getPersonLinkage(request, new Callback<List<Person>>() {
            @Override
            public void success(final List<Person> allPatientsProfiles, Response response) {
                PatientSettingListAdapter adapter = new PatientSettingListAdapter(getActivity(), allPatientsProfiles);
                listView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) { 
                progress.dismiss();

            }
        });

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        switch (profileType)
        {
            case PATIENT:
                textviewTitle.setText("Patient Profiles");
                break;
            case DOCTOR:
                textviewTitle.setText("Doctor Profiles");
                break;
            case ASSISTANT:
                textviewTitle.setText("Assistant Profiles");
                break;
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
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(PERSON_ID,0);
                bun.putInt(PARAM.PERSON_ROLE, bun.getInt(PROFILE_TYPE));
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new PersonProfileEditView();
                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PersonProfileEditView.class.getName()).addToBackStack(PersonProfileEditView.class.getName()).commit();

            }
            break;
        }
        return true;
    }
}