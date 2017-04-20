package com.medico.view.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.PatientListAdapter;
import com.medico.application.R;
import com.medico.model.DoctorId;
import com.medico.model.PatientProfileList;
import com.medico.model.PatientShortProfile;
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
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);

//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getResources().getString(R.string.patients_profiles));
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
            public void success(final List<PatientShortProfile> allPatientsProfiles, Response response) {
                PatientListAdapter adapter = new PatientListAdapter(getActivity(), new PatientProfileList(allPatientsProfiles));
                patientListView.setAdapter(adapter);
//                ApplicationProgressManager.getInstance(getActivity()).getAnimation().reset();
//                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
//                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
//                ApplicationProgressManager.getInstance(getActivity()).getAnimation().reset();
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


}
