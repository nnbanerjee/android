package com.medico.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medico.model.DiagnosticTest;
import com.medico.application.R;

import java.util.List;

/**
 * Created by Narendra on 28-02-2017.
 */

public class DiagnosticTestSpinnerAdapter extends ArrayAdapter
{
    List<DiagnosticTest> strClinic;
    Activity activity;
    public DiagnosticTestSpinnerAdapter(Activity ctx, int txtViewResourceId, List<DiagnosticTest> objects) {
        super(ctx, txtViewResourceId, objects);
        strClinic=objects;
        this.activity = ctx;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View mySpinner = inflater.inflate(R.layout.customize_spinner, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
        main_text.setText(strClinic.get(position).toString());
        return mySpinner;
    }

    public int getPositionId(Integer testId)
    {
        int i = 0;
        boolean found = false;
        for(DiagnosticTest test : strClinic)
        {
            if(test.testId.intValue() == testId.intValue())
            {
                found = true;
                return i;
            }

            i++;
        }
        if(found)
            return i;
        else
            return 0;
    }
    public DiagnosticTest getDiagnostic(Integer testId)
    {
        int i = 0;
        for(DiagnosticTest test : strClinic)
        {
            if(test.testId.intValue() == testId.intValue())
            {
                return test;
            }

            i++;
        }
        return null;
    }
}
