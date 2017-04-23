package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medico.application.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 04-11-2015.
 */
public class HistoryAdapter extends HomeAdapter {
    Activity activity;
    List<String> histories;
    LayoutInflater inflater;
    ProgressDialog progress;
    private int loggedInUserId;

    public HistoryAdapter(Activity activity, List<String> histories)
    {
        super(activity);
        this.activity = activity;
        this.histories = histories;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.history, null);
                setView(convertView,position);
            }
            else
            {
                setView(convertView,position);
            }
            return convertView;
        }

    private void setView(final View convertView, final int position)
    {
        TextView personName = (TextView)convertView.findViewById(R.id.doctor_name);
        TextView date = (TextView)convertView.findViewById(R.id.date_name);
        TextView changes = (TextView)convertView.findViewById(R.id.change_name);
        String history = histories.get(position);
        String[] historyFragments = history.split("<~~>");

        if(historyFragments.length > 2)
        {
            personName.setText(historyFragments[0]);
            try
            {
                long time = Long.parseLong(historyFragments[1]);
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
                date.setText(format.format(new Date(time)));
            }
            catch(NumberFormatException e)
            {
                date.setText("Time not mentioned");
            }
            changes.setText(historyFragments[2]);
        }
        else if(historyFragments.length > 1)
        {
            personName.setText(historyFragments[0]);
            changes.setText(historyFragments[1]);
        }
        else
        {
            changes.setText(historyFragments[0]);
        }


    }

}
