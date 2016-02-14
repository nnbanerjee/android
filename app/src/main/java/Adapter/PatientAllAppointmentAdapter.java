package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ClinicAppointment;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 7/27/15.
 */
public class PatientAllAppointmentAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    List<ClinicAppointment> clinicAppointment;
    private LayoutInflater inflater;
    Context context;
    public SharedPreferences session;

    public PatientAllAppointmentAdapter(Context context, List<ClinicAppointment> clinicAppointment)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.clinicAppointment = clinicAppointment;
        session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
        String headerText = "" +clinicAppointment.get(position).getAppointmentDate().subSequence(7, 11);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return clinicAppointment.get(position).getAppointmentDate().subSequence(7, 11).charAt(3);
    }

    @Override
    public int getCount() {
        return clinicAppointment.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicAppointment.get(position);
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
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.visitType = (TextView)convertView.findViewById(R.id.visitType);
            holder.rightArrow = (ImageView)convertView.findViewById(R.id.rightArrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
        if(clinicAppointment.get(position).getBookTime().equals("")){
            holder.date.setText("No Appointment Scheduled");
            holder.time.setText("");
            holder.visitType.setVisibility(View.GONE);
            holder.rightArrow.setVisibility(View.GONE);
        }else{
            holder.visitType.setVisibility(View.VISIBLE);
            holder.rightArrow.setVisibility(View.VISIBLE);
            holder.date.setText(clinicAppointment.get(position).getAppointmentDate());
            holder.time.setText(clinicAppointment.get(position).getBookTime());
            holder.visitType.setText(clinicAppointment.get(position).getVisitType());
        }
        return convertView;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,time,visitType;
        private RelativeLayout layout;
        private ImageView rightArrow;
    }
}
