package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medico.view.AddClinic;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import Adapter.TimeAdapter;
import com.medico.application.MyApi;

import Model.Time;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MNT on 24-Feb-15.
 */
public class ShowClinic extends Fragment {

    private String name,location,clinicId;
    private TextView clinicNameTv,locationTv;
    private TimeAdapter listAdapterManageTime;
    private ArrayList<Time> arrayNew;
    private MyApi api;
    private Button buttonOk;
    private String doctorId = "";

    @Override
    public void onResume()
    {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new AddClinic();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2, null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_selected,container,false);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Show Clinic");
        doctorId = session.getString("sessionID", null);
        getActivity().getActionBar().hide();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new AddClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        Bundle bun = getArguments();
        name = bun.getString("name");
        location = bun.getString("location");
        clinicId = bun.getString("id");
        System.out.println("Location "+location);
        clinicNameTv = (TextView)view.findViewById(R.id.name_clinic);
        locationTv = (TextView)view.findViewById(R.id.city);
        clinicNameTv.setText(name);

        locationTv.setText(location);



      /*  api.getClinicDetails(doctorId,clinicId, new Callback<ArrayList<Schedule>>() {
            @Override
            public void success(ArrayList<Schedule> schedules, Response response) {

                System.out.println("Array Elements:"+schedules.toArray());

                Fragment frag2 = new EditTimeScheduleShowClinic();
                Bundle bun1 = new Bundle();
                bun1.putSerializable("totalShift",schedules);
                bun1.putString("doctorId",doctorId);
                bun1.putString("clinicId",clinicId);
                frag2.setArguments(bun1);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.add(R.id.layout_edit_time,frag2,null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();

            }
        });*/
        return view;
    }

}
