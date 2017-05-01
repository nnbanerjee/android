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

import com.medico.adapter.DoctorReviewListAdapter;
import com.medico.application.R;
import com.medico.model.DoctorReview;
import com.medico.model.PatientId;
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
public class DoctorReviewListView extends ParentFragment {
    ListView patientListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        patientListView = (ListView) view.findViewById(R.id.doctorListView);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Doctor's Feedback");

        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        PatientId doc= new PatientId(bundle.getInt(PARAM.PATIENT_ID));
        api.getAllReviewsForPatient(doc, new Callback<List<DoctorReview>>() {
            @Override
            public void success(final List<DoctorReview> patientReviews, Response response) {
                DoctorReviewListAdapter adapter = new DoctorReviewListAdapter(getActivity(), patientReviews);
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
