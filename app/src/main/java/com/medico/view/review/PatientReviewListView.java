package com.medico.view.review;

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

import com.medico.adapter.PatientReviewListAdapter;
import com.medico.application.R;
import com.medico.model.DoctorId1;
import com.medico.model.PatientReview;
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
public class PatientReviewListView extends ParentFragment {
    ListView patientListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Patient's Feedback");

        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        DoctorId1 doc= new DoctorId1(bundle.getInt(PARAM.DOCTOR_ID));
        api.getReviewsForDoctor(doc, new Callback<List<PatientReview>>() {
            @Override
            public void success(final List<PatientReview> patientReviews, Response response) {
                PatientReviewListAdapter adapter = new PatientReviewListAdapter(getActivity(), patientReviews);
                patientListView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Communication issue with the server", Toast.LENGTH_LONG).show();
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
