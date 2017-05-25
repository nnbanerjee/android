package com.medicohealthcare.view.profile;

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

import com.medicohealthcare.adapter.PatientListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.model.PatientProfileList;
import com.medicohealthcare.model.PatientShortProfile;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

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
        showBusy();
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);
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
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                hideBusy();
            }
        });
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
        inflater.inflate(R.menu.patient_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add_patient: {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }

            }
            return true;
            case R.id.sort_patient:
            {
                return true;
            }
            case R.id.filter_patient:
            {
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
            }
            default:
                return false;

        }
    }

}
