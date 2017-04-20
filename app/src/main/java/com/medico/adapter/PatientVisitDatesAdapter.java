package com.medico.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.PatientVisits;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            convertView = inflater.inflate(R.layout.sticky_header_visit_dates, parent, false);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            holder.date = (TextView) convertView.findViewById(R.id.visit_date1);
            holder.visitType = (TextView) convertView.findViewById(R.id.visit_type1);
           holder.time = (TextView) convertView.findViewById(R.id.visit_time1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
//        DateFormat format1 = DateFormat.getDateInstance(DateFormat.MEDIUM);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMMM ( EEEE )", Locale.getDefault());
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(appointment.get(position).dateTime));
        holder.text.setText(new Integer(calendar.get(Calendar.YEAR)).toString());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(appointment.get(position).dateTime));
        return calendar.get(Calendar.YEAR);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,time,visitType;
        private RelativeLayout layout;
    }
}
