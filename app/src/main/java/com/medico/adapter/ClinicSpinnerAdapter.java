package com.medico.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medico.model.Clinic1;
import com.medico.application.R;

import java.util.List;

/**
 * Created by Narendra on 28-02-2017.
 */

public class ClinicSpinnerAdapter extends ArrayAdapter
{
    List<Clinic1> strClinic;
    Activity activity;
    public ClinicSpinnerAdapter(Activity ctx, int txtViewResourceId, List<Clinic1> objects) {
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

    public int getPositionId(Integer clinicId)
    {
        int i = 0;
        boolean found = false;
        for(Clinic1 clinic : strClinic)
        {
            if(clinic.idClinic.intValue() == clinicId.intValue())
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
}
