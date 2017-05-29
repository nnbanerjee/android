package com.medicohealthcare.view.profile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.DoctorListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorProfileList;
import com.medicohealthcare.model.DoctorShortProfile;
import com.medicohealthcare.model.PatientId;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.search.PersonSearchView;
import com.medicohealthcare.view.settings.DoctorProfileEditView;
import com.medicohealthcare.view.settings.PersonProfileEditView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorProfileListView extends ParentFragment {


    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
//        doctorListView = (ListView)getActivity().findViewById(R.id.doctorListView);

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Doctor Profiles");
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        PatientId patient = new PatientId(new Integer(bundle.getInt(PARAM.PATIENT_ID)));
        api.getDoctorProfileList(patient, new Callback<List<DoctorShortProfile>>() {
            @Override
            public void success(final List<DoctorShortProfile> allDoctorProfiles, Response response) {
                DoctorListAdapter adapter = new DoctorListAdapter(getActivity(), new DoctorProfileList(allDoctorProfiles));
                doctorListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        setHasOptionsMenu(true);
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
                    // handle back button's click listener
//                    goToBack();
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
                bun.putInt(PARAM.PERSON_ROLE, DOCTOR);
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = null;
                fragment = new DoctorProfileEditView();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PersonProfileEditView.class.getName()).addToBackStack(PersonProfileEditView.class.getName()).commit();
                return true;
            }
            case R.id.search:
            {
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.DOCTOR_SETTING_VIEW);
                bundle.putInt(PARAM.SEARCH_ROLE, PARAM.DOCTOR);
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
