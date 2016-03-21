package Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentInformation;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Model.Appointment;
import Model.ClinicAppointment;
import Model.Visit;
import Utils.UtilSingleInstance;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class DoctorAllPatientAppointmentAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    List<Visit> appointment;
    public SharedPreferences session;
    Context context;

    public DoctorAllPatientAppointmentAdapter(Context context, List<Visit> appointment) {
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
           // holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
     
        holder.date.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(appointment.get(position).getDateTime()) );
        holder.visitType.setText(UtilSingleInstance.getUserVisitType(appointment.get(position).getVisitType()) );
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
        String headerText = "" +UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(appointment.get(position).getDateTime()).subSequence(7, 11);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(appointment.get(position).getDateTime()).subSequence(7, 11).charAt(3);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,time,visitType;
        private RelativeLayout layout;
    }
}
