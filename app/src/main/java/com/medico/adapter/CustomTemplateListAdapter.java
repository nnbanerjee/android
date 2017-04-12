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
import com.medico.view.settings.CustomTemplateSubListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04-11-2015.
 */
public class CustomTemplateListAdapter extends HomeAdapter {
    Activity activity;
    List<List<CustomProcedureTemplate1>> templateGroup;
    LayoutInflater inflater;
//    MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    private Integer loggedInUserId;

    public CustomTemplateListAdapter(Activity activity, List<CustomProcedureTemplate1> templates, Integer userId)
    {
        super(activity);
        this.activity = activity;
        this.loggedInUserId = userId;
        this.templateGroup = defineGroups(templates);
    }

    @Override
    public int getCount()
    {
        return templateGroup.size();
    }

    @Override
    public Object getItem(int position)
    {
        return templateGroup.get(position);
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
            List<CustomProcedureTemplate1> template1s = templateGroup.get(position);
            convertView.setTag(template1s);
            CustomProcedureTemplate1 template1 = template1s.get(0);
            TextView name = (TextView) convertView.findViewById(R.id.setting_name);
            name.setText(template1.getTemplateName());
            ImageButton button = (ImageButton)convertView.findViewById(R.id.details_view);
            button.setTag(template1s);
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    List<CustomProcedureTemplate1> temp = (List<CustomProcedureTemplate1>)v.getTag();
                    Bundle args = activity.getIntent().getExtras();
                    args.putString(PARAM.CUSTOM_TEMPLATE_NAME,temp.get(0).getTemplateName());
                    activity.getIntent().putExtras(args);
                    ParentFragment fragment = new CustomTemplateSubListView();
                    ((ParentActivity)activity).attachFragment(fragment);
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().add(R.id.service, fragment, "Treatment Plan").addToBackStack(null).commit();


                }
            } );

    }

    private List<List<CustomProcedureTemplate1>> defineGroups(List<CustomProcedureTemplate1> treatments)
    {
        List<List<CustomProcedureTemplate1>> treatmentGroups = new ArrayList<>();
        boolean found = false;
        List<List<CustomProcedureTemplate1>> groups = new ArrayList<>();
        for(CustomProcedureTemplate1 plan:treatments)
        {
            for(List<CustomProcedureTemplate1> treatmentGroup:treatmentGroups)
            {
                if(treatmentGroup.size() > 0
                        && treatmentGroup.get(0).getTemplateName().equals(plan.getTemplateName()))
                {
                    treatmentGroup.add(plan);
                    found = true;
                    break;
                }

            }
            if(found == false)
            {
                ArrayList<CustomProcedureTemplate1> treatmentPlan1s = new ArrayList<>();
                treatmentPlan1s.add(plan);
                treatmentGroups.add(treatmentPlan1s);
            }
            found = false;
        }
        return treatmentGroups;
    }

}
