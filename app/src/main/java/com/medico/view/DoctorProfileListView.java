package com.medico.view;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.DoctorListAdapter;
import com.medico.model.DoctorProfileList;
import com.medico.model.DoctorShortProfile;
import com.medico.model.PatientId;
import com.medico.util.PARAM;
import com.medico.application.R;

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
        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                DoctorShortProfile profile = (DoctorShortProfile)adapterView.getAdapter().getItem(i);
                        ParentFragment fragment = new PatientVisitDatesView();
                        ((ParentActivity)getActivity()).fragmentList.add(fragment);
                        bun.putInt(PARAM.DOCTOR_ID, profile.getDoctorId().intValue());
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
        inflater.inflate(R.menu.menu, menu);
        inflater.inflate(R.menu.doctor_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
//

}
