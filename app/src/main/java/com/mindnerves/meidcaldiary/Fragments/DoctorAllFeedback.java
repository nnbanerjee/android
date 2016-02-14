package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.FeedbackAdapter;
import Application.MyApi;
import Model.AllPatients;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 23-11-2015.
 */
public class DoctorAllFeedback extends Fragment {
    ProgressDialog progress;
    ListView feedbackList;
    SharedPreferences session;
    Button drawar,logout,back;
    ImageView profilePicture,medicoLogo,medicoText;
    TextView accountName;
    String type;
    RelativeLayout profileLayout;
    TextView globalTv;
    public MyApi api;
    FeedbackAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_all_feedback,container,false);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        feedbackList = (ListView)view.findViewById(R.id.feedback_list);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        back = (Button)getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        logout = (Button)getActivity().findViewById(R.id.logout);
        medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
        medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        type = session.getString("type",null);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showFeedbackList();
        manangeScreenIcons();
        return view;
    }
    public void showFeedbackList(){
        String  doctorId = session.getString("sessionID",null);
        api.doctorPatientFeedback(doctorId,new Callback<List<AllPatients>>() {
            @Override
            public void success(List<AllPatients> allPatientses, Response response) {
                if(allPatientses.size() == 0){
                    AllPatients patient = new AllPatients();
                    patient.setReviews("No Feedback Found");
                    allPatientses.add(patient);
                }
                adapter = new FeedbackAdapter(getActivity(),allPatientses);
                feedbackList.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }
    public void manangeScreenIcons(){
        globalTv.setText("Manage Feedback");
        drawar.setVisibility(View.GONE);
        medicoLogo.setVisibility(View.GONE);
        medicoText.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }
    public void goToBack(){
        globalTv.setText(type);
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        logout.setVisibility(View.GONE);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
        medicoLogo.setVisibility(View.VISIBLE);
        medicoText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }
}
