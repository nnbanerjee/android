package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.SpecialityAdapter;
import com.medico.application.MyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/24/15.
 */
public class ShowSpecialityDialog extends DialogFragment {
    private ListView listView;
    private MyApi api;
    private SpecialityAdapter specialityAdapter;
    Global go;
    public static ShowSpecialityDialog newInstance() {
        return new ShowSpecialityDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_speciality,container,false);
        listView = (ListView)view.findViewById(R.id.list_speciality);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        go = (Global)getActivity().getApplicationContext();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String speciality = (String)specialityAdapter.getItem(position);
                AddDialogSpecialityWiseDialog adf = AddDialogSpecialityWiseDialog.newInstance();
                adf.show(getFragmentManager(), "Dialog");
                go.setSpecialityString(speciality);

            }
        });
        return view;
    }
    public void showListView()
    {
        api.getAllSpeciality(new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                specialityAdapter = new SpecialityAdapter(getActivity(),strings);
                listView.setAdapter(specialityAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goBack(){

    }
}
