package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.TimeAdapter;
import Application.MyApi;
import Model.Time;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MNT on 24-Feb-15.
 */
public class AddTime extends Fragment {

    private ListView listViewTime;
    public MyApi api;
    private TimeAdapter listAdapterManageTime;
    private ArrayList<Time> arrayNew;
    private Button addNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_timing,container,false);
        listViewTime = (ListView)view.findViewById(R.id.list_timing);

        Time toc = new Time();
        arrayNew = new ArrayList<Time>();

        arrayNew.add(toc);

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showListTime();

        addNew = (Button)view.findViewById(R.id.add_new);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time tocNew = new Time();
                arrayNew.add(tocNew);
                showListTime();
            }
        });

        return view;
    }

    public void showListTime()
    {
            listAdapterManageTime = new TimeAdapter(getActivity(),arrayNew);
            listViewTime.setAdapter(listAdapterManageTime);

            System.out.println("Adapter list Count "+listViewTime.getCount());
    }
}
