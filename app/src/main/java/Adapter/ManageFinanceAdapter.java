package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ClinicAppointment;
import Model.ManageFinance;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class ManageFinanceAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    List<ManageFinance> appointment;
    public SharedPreferences session;
    String mode;
    Context context;

    public ManageFinanceAdapter(Context context, List<ManageFinance> appointment, String mode) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mode = mode;
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
            convertView = inflater.inflate(R.layout.finance_item, parent, false);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.date1 = (TextView) convertView.findViewById(R.id.date1);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.totalNumber = (TextView) convertView.findViewById(R.id.totalNumber);
            holder.visitType = (TextView) convertView.findViewById(R.id.visitType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final int pos = position;

        holder.date1.setText(appointment.get(position).getAppointmentDate());

        if(mode.equals("day")){
            holder.date.setText(appointment.get(position).getAppointmentDate());
        }else if(mode.equals("week")){
            holder.date.setText(appointment.get(position).getAppointmentDate());
        }else if(mode.equals("month")){
            holder.date.setText(appointment.get(position).getAppointmentDate().substring(3,appointment.get(position).getAppointmentDate().length()));
        }

        holder.time.setVisibility(View.GONE);
        System.out.println("appointment.get(position).getTotalCount()/////////// = "+appointment.get(position).getTotalCount());
        holder.totalNumber.setText(""+appointment.get(position).getTotalCount());
        //holder.time.setText(appointment.get(position).getBookTime());
        holder.visitType.setText(""+appointment.get(position).getTotalInvoice());

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
        String headerText = "" +appointment.get(position).getAppointmentDate().subSequence(7, 11);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return appointment.get(position).getAppointmentDate().subSequence(7, 11).charAt(3);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,date1,time,visitType,totalNumber;
        private RelativeLayout layout;
    }
}
