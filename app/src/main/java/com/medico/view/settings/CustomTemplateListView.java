package com.medico.view.settings;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.CustomTemplateListAdapter;
import com.medico.model.CustomProcedureTemplate1;
import com.medico.model.PersonAndCategoryId1;
import com.medico.application.R;

import java.util.ArrayList;
import java.util.List;

import com.medico.util.PARAM;
import com.medico.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class CustomTemplateListView extends ParentFragment {


    SharedPreferences session;
    ListView customTemplateListView;
    ProgressDialog progress;
    List<CustomProcedureTemplate1> customTemplateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        customTemplateListView = (ListView) view.findViewById(R.id.doctorListView);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Custom Template");


        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorId = bundle.getInt(PARAM.LOGGED_IN_ID);
        final Integer type = bundle.getInt(CUSTOM_TEMPLATE_CREATE_ACTIONS);
        bundle.remove(CUSTOM_TEMPLATE_NAME);
        api.getAllCustomTemplate1(new PersonAndCategoryId1(doctorId, type == CREATE_TREATMENT?1:2), new Callback<List<CustomProcedureTemplate1>>() {
            @Override
            public void success(final List<CustomProcedureTemplate1> allTemplates, Response response) {
                customTemplateList = allTemplates;
                CustomTemplateListAdapter adapter = new CustomTemplateListAdapter(getActivity(), customTemplateList, doctorId);
                customTemplateListView.setAdapter(adapter);
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
