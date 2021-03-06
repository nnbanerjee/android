package com.medicohealthcare.view.settings;

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

import com.medicohealthcare.adapter.CustomTemplateSubListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.CustomProcedureTemplate1;
import com.medicohealthcare.model.PersonAndCategoryId1;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class CustomTemplateSubListView extends ParentFragment {


    SharedPreferences session;
    ListView customTemplateListView;
    List<CustomProcedureTemplate1> customTemplateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_view,container,false);
        setHasOptionsMenu(true);
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
        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorId = bundle.getInt(PARAM.LOGGED_IN_ID);
        final String name = bundle.getString(CUSTOM_TEMPLATE_NAME);
        final Integer type = bundle.getInt(CUSTOM_TEMPLATE_CREATE_ACTIONS);
        api.getAllCustomTemplate1(new PersonAndCategoryId1(doctorId, type == CREATE_TREATMENT?1:2), new Callback<List<CustomProcedureTemplate1>>() {
            @Override
            public void success(final List<CustomProcedureTemplate1> allTemplates, Response response) {
                customTemplateList = allTemplates;
                if(name != null)
                {
                    List<CustomProcedureTemplate1> sublist = new ArrayList<CustomProcedureTemplate1>();
                    for(CustomProcedureTemplate1 template1:allTemplates)
                    {
                        if(template1.getTemplateName().equals(name))
                            sublist.add(template1);
                    }
                    CustomTemplateSubListAdapter adapter = new CustomTemplateSubListAdapter(getActivity(), sublist, doctorId);
                    customTemplateListView.setAdapter(adapter);
                }
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
    }
}
