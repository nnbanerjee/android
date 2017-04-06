package com.medico.view.search;

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

import com.medico.adapter.HomeAdapter;
import com.medico.adapter.PatientSearchListAdapter;
import com.medico.application.R;
import com.medico.model.Person;
import com.medico.view.ParentFragment;
import com.medico.view.appointment.ManageDoctorAppointment;
import com.medico.view.settings.ManagePersonSettings;
import com.medico.view.settings.PersonProfileEditView;

import java.util.List;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class SearchPersonListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;
    List<Person> model;
//    ProgressDialog progress;
    HomeAdapter adapter;
    Object adapterParameter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);

        listView = (ListView) view.findViewById(R.id.doctorListView);

//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));

//        Bundle bundle = getActivity().getIntent().getExtras();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                Person profile = (Person)adapterView.getAdapter().getItem(i);
                bun.putInt("SELECTED_PATIENT_ID",profile.getId());
                adapter.callBack(profile.getId(),profile, adapterParameter);
                getActivity().getIntent().putExtras(bun);
                ((ManageDoctorAppointment)getActivity()).onBackPressed();
//                        ParentFragment fragment = new PersonProfileEditView();
//                        ((Doc)getActivity()).fragmentList.add(fragment);
//                        bun.putInt(PARAM.PROFILE_ID, profile.getId().intValue());
//                        bun.putInt(PARAM.PROFILE_ROLE, profile.getId().intValue());
//                        getActivity().getIntent().putExtras(bun);
//                      fragment.setArguments(bun);
//                        FragmentManager fragmentManger = getActivity().getFragmentManager();
//                        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void setModel(List<Person> model)
    {
        this.model = model;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        listView.setAdapter(new PatientSearchListAdapter(getActivity(),model));
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
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(PROFILE_ID,0);
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new PersonProfileEditView();
                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
            break;
        }
        return true;
    }
    public void setAdapter(HomeAdapter adapter, Object parameter)
    {
        this.adapter = adapter;
        this.adapterParameter = parameter;
    }
}
