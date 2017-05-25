package com.medicohealthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medicohealthcare.model.Delegation;
import com.medicohealthcare.application.R;

import java.util.List;

/**
 * Created by User on 8/19/15.
 */
public class ProfileDelegationAdapter extends BaseAdapter {
    Activity activity;
    List<Delegation> delegates;
    private LayoutInflater inflater;

    public ProfileDelegationAdapter(Activity activity,List<Delegation> delegates)
    {
        this.activity = activity;
        this.delegates = delegates;
    }
    @Override
    public int getCount() {
        return delegates.size();
    }

    @Override
    public Object getItem(int position) {
        return delegates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        final View convertView = inflater.inflate(R.layout.dependent_element_profile, null);
        TextView profileName = (TextView)convertView.findViewById(R.id.dependent_candidate);
        profileName.setText(delegates.get(position).getName());
        return convertView;
    }
}
