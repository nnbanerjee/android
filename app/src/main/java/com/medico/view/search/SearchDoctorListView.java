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
import android.widget.ListView;
import android.widget.TextView;

import com.medico.adapter.DoctorSearchListAdapter;
import com.medico.adapter.HomeAdapter;
import com.medico.application.R;
import com.medico.model.DoctorSearch;
import com.medico.model.DoctorSearchResult;
import com.medico.view.home.ParentFragment;
import com.medico.view.settings.PersonProfileEditView;

import java.util.List;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class SearchDoctorListView extends ParentFragment {


    SharedPreferences session;
    ListView listView;
    List<DoctorSearch> model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_view,container,false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getResources().getString(R.string.patient_search_result));
        listView = (ListView) view.findViewById(R.id.doctorListView);

        return view;
    }

    public void setModel(List<DoctorSearch>  model)
    {
        this.model = model;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        listView.setAdapter(new DoctorSearchListAdapter(getActivity(),model));
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
            case R.id.filter: {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                bun.putInt(PROFILE_ID,0);
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new PersonProfileEditView();
//                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
            break;
        }
        return true;
    }
}
