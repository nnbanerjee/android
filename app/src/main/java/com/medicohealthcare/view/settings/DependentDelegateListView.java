package com.medicohealthcare.view.settings;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.DependentDelegationSettingListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DependentDelegatePerson;
import com.medicohealthcare.model.DependentDelegatePersonRequest;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.PersonDelegation;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

import java.math.BigDecimal;
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
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment,DependentDelegateProfileView.class.getName()).addToBackStack(DependentDelegateProfileView.class.getName()).commit();
                }
                else
                {

                }

            }
            break;
        }
        return true;
    }
    public void addDelegate()
    {
        final Bundle bundle = getActivity().getIntent().getExtras();
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.person_autofill, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final AutoCompleteTextView userInput = (AutoCompleteTextView) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1).trim();
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillPerson(new SearchParameter(searchText,2,bundle.getInt(PROFILE_ID), 1, 100, 5), new Callback<List<Person>>() {
                        @Override
                        public void success(List<Person> specializationList, Response response)
                        {
                            Person[] options = new Person[specializationList.size()];
                            specializationList.toArray(options);
                            ArrayAdapter<Person> diagnosisAdapter = new ArrayAdapter<Person>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
                            userInput.setAdapter(diagnosisAdapter);
                            diagnosisAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
//                            error.printStackTrace();
                        }
                    });
                }

            }
        });
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                if(userInput.getText().length() > 0)
                                {
                                    showBusy();
                                    BigDecimal amount = new BigDecimal(Double.parseDouble(userInput.getText().toString()));
                                    PersonDelegation person = new PersonDelegation(101, bundle.getInt(PROFILE_ID), 1, "Assistant", 1);
                                    api.addDelegation(person, new Callback<ServerResponse>() {
                                        @Override
                                        public void success(ServerResponse s, Response response) {
                                            if (s.status == 1)
                                            {
                                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                                                getActivity().onBackPressed();
                                            }
                                            else
                                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            hideBusy();
                                            new MedicoCustomErrorHandler(getActivity()).handleError(error);
                                        }
                                    });
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
