package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.medico.model.TreatmentPlan1;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Application.MyApi;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by User on 04-11-2015.
 */
public class TreatmentPlanListAdapter extends BaseAdapter {
    Activity activity;
    List<List<TreatmentPlan1>> treatmentGroups;
    LayoutInflater inflater;
    MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    private Integer loggedInUserId;

    public TreatmentPlanListAdapter(Activity activity, List<TreatmentPlan1> treatments, Integer userId) {
        this.activity = activity;
        loggedInUserId = userId;
        if(treatments != null && treatments.size() > 0)
            this.treatmentGroups = defineGroups(treatments);
    }

    @Override
    public int getCount() {
        return treatmentGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return treatmentGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(activity.getString(R.string.base_url))
                        .setClient(new OkClient())
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                api = restAdapter.create(MyApi.class);
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.treatment_plan_list, null);
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
        List<TreatmentPlan1> treatments = treatmentGroups.get(position);
        convertView.setTag(treatments);
        TreatmentPlan1 treatmentPlan1 = treatments.get(0);
        final TextView name = (TextView)convertView.findViewById(R.id.title);
        name.setText(treatmentPlan1.getTemplateName() + " > " + treatmentPlan1.getTemplateSubName());
        TableLayout tableLayout = (TableLayout)convertView.findViewById(R.id.treatment_table);
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        //create dates
        boolean firstRow = true;
        for(TreatmentPlan1 plan:treatments)
        {

            if(firstRow)
            {
                TableRow row = new TableRow(activity);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                for(TreatmentPlan1.TreatmentField fields: plan.treatmentFields) {
                    TextView textView = new TextView(activity);
                    textView.setText(fields.fieldDisplayName);
                    row.addView(textView);
                }
                firstRow = false;
                tableLayout.addView(row);
            }
            TableRow row = new TableRow(activity);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            for(TreatmentPlan1.TreatmentField fields: plan.treatmentFields) {
                TextView textView = new TextView(activity);
                textView.setText(fields.value);
                row.addView(textView);

            }
            tableLayout.addView(row);
        }
        tableLayout.requestLayout();
    }

    private List<List<TreatmentPlan1>> defineGroups(List<TreatmentPlan1> treatments)
    {
        List<List<TreatmentPlan1>> treatmentGroups = new ArrayList<>();
        boolean found = false;
        List<List<TreatmentPlan1>> groups = new ArrayList<>();
        for(TreatmentPlan1 plan:treatments)
        {
            for(List<TreatmentPlan1> treatmentGroup:treatmentGroups)
            {
                if(treatmentGroup.size() > 0
                        && treatmentGroup.get(0).getTemplateName().equals(plan.getTemplateName())
                        && treatmentGroup.get(0).getTemplateSubName().equals(plan.templateSubName))
                {
                    treatmentGroup.add(plan);
                    found = true;
                    break;
                }

            }
            if(found == false)
            {
                ArrayList<TreatmentPlan1> treatmentPlan1s = new ArrayList<>();
                treatmentPlan1s.add(plan);
                treatmentGroups.add(treatmentPlan1s);
            }
        }
        return treatmentGroups;
    }

}
