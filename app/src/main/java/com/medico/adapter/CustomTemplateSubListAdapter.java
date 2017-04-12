package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.CustomProcedureTemplate1;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.DoctorTreatmentPlanEditView;

import java.util.List;

/**
 * Created by User on 04-11-2015.
 */
public class CustomTemplateSubListAdapter extends HomeAdapter {
    Activity activity;
    List<CustomProcedureTemplate1> templates;
    LayoutInflater inflater;
//    MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    private Integer loggedInUserId;

    public CustomTemplateSubListAdapter(Activity activity, List<CustomProcedureTemplate1> templates, Integer userId)
    {
        super(activity);
        this.activity = activity;
        this.loggedInUserId = userId;
        this.templates = templates;
    }

    @Override
    public int getCount()
    {
        return templates.size();
    }

    @Override
    public Object getItem(int position)
    {
        return templates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(activity.getString(R.string.base_url))
//                        .setClient(new OkClient())
//                        .setLogLevel(RestAdapter.LogLevel.FULL)
//                        .build();
//                api = restAdapter.create(MyApi.class);
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.manage_settings, null);
                setView(convertView,position);
            }
            else
            {
                setView(convertView,position);
            }
            return convertView;
        }

    private void setView(final View convertView, int position)
    {
        CustomProcedureTemplate1 template1 = templates.get(position);
        convertView.setTag(template1);
        TextView name = (TextView) convertView.findViewById(R.id.setting_name);
        name.setText(template1.getTemplateSubName());
        ImageButton button = (ImageButton)convertView.findViewById(R.id.details_view);
        button.setTag(template1);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                CustomProcedureTemplate1 temp = (CustomProcedureTemplate1)v.getTag();
                Bundle args = activity.getIntent().getExtras();
                args.putInt(PARAM.CUSTOM_TEMPLATE_ID,temp.getTemplateId());
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new DoctorTreatmentPlanEditView();
                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Treatment Plan").addToBackStack(null).commit();


            }
        } );

    }


}
