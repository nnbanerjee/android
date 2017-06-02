package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.CustomProcedureTemplate1;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.DoctorTreatmentPlanEditView;

import java.util.List;

/**
 * Created by User on 04-11-2015.
 */
public class CustomTemplateSubListAdapter extends HomeAdapter {
    Activity activity;
    List<CustomProcedureTemplate1> templates;
    LayoutInflater inflater;
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
        TextView count = (TextView) convertView.findViewById(R.id.count_name);
        count.setVisibility(View.GONE);
        name.setText(template1.getTemplateSubName());
        ImageButton button = (ImageButton)convertView.findViewById(R.id.nextBtn);
        button.setTag(template1);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                CustomProcedureTemplate1 temp = (CustomProcedureTemplate1)v.getTag();
                Bundle args = activity.getIntent().getExtras();
                args.putInt(PARAM.CUSTOM_TEMPLATE_ID,temp.getTemplateId());
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new DoctorTreatmentPlanEditView();
//                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, DoctorTreatmentPlanEditView.class.getName()).addToBackStack(DoctorTreatmentPlanEditView.class.getName()).commit();


            }
        } );

    }


}
