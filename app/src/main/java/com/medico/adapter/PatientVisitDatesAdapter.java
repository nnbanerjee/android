package com.medico.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.model.PatientVisits;
import com.medico.application.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class PatientVisitDatesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    List<PatientVisits> appointment;
    public SharedPreferences session;
    Context context;

    public PatientVisitDatesAdapter(Context context, List<PatientVisits> appointment) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        countries = context.getResources().getStringArray(R.array.countries);
        this.appointment = appointment;
        session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return appointment.size();
    }

    @Override
    public Object getItem(int position) {
        return appointment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.all_patient_appo_item, parent, false);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.visitType = (TextView) convertView.findViewById(R.id.visitType);
           holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
        DateFormat format1 = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat format2 = DateFormat.getTimeInstance(DateFormat.SHORT);
        holder.date.setText(format1.format(new Date(appointment.get(position).dateTime)) );
        holder.time.setText(format2.format(new Date((appointment.get(position).dateTime)) ));
        holder.visitType.setText(context.getResources().getStringArray(R.array.visit_type_list)[appointment.get(position).visitType] );
      //  holder.time.setText(appointment.get(position).getBookTime());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_all_appointment, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.slot);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        String headerText = format.format(new Date(appointment.get(position).dateTime)).toString();
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
        return format.format(new Date(appointment.get(position).dateTime)).subSequence(7, 11).charAt(3);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,time,visitType;
        private RelativeLayout layout;
    }
}
