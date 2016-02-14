package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.Appointment;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    ArrayList<Appointment> appointment;
    Global global;
    Context context;

    public MyAdapter(Context context,ArrayList<Appointment> appointment) {
        inflater = LayoutInflater.from(context);
        countries = context.getResources().getStringArray(R.array.countries);
        this.appointment = appointment;
        this.context = context;
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
        global = (Global) context;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.test_list_item_layout, parent, false);
            holder.countTv = (TextView) convertView.findViewById(R.id.count);
            holder.timeTv = (TextView)convertView.findViewById(R.id.time);
            holder.patientName = (TextView)convertView.findViewById(R.id.patientName);
            holder.patientInfo = (RelativeLayout)convertView.findViewById(R.id.patientInfo);
            holder.appointmentTypeValue = (TextView)convertView.findViewById(R.id.appointmentTypeValue);
            holder.patientImage = (ImageView)convertView.findViewById(R.id.patientImg);
            holder.downImage = (ImageView)convertView.findViewById(R.id.down_image);
            holder.appointmentText = (TextView)convertView.findViewById(R.id.appointmentTypeText);
            holder.appointmentValue = (TextView)convertView.findViewById(R.id.appointmentTypeValue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.countTv.setText(""+appointment.get(position).getAppointmentCount());
        holder.timeTv.setText(appointment.get(position).getAppointmentTime());


        if(appointment.get(position).getPersonInfo() != null){
            holder.patientInfo.setVisibility(View.VISIBLE);
            holder.patientImage.setVisibility(View.VISIBLE);
            holder.downImage.setVisibility(View.VISIBLE);
            holder.appointmentText.setVisibility(View.VISIBLE);
            holder.appointmentValue.setVisibility(View.VISIBLE);
            holder.appointmentTypeValue.setText(appointment.get(position).getAppointmentType());
            holder.patientName.setText(appointment.get(position).getPersonInfo().name);
        }else{
             holder.patientName.setText(appointment.get(position).getAppointmentStatus());
             holder.patientImage.setVisibility(View.INVISIBLE);
             holder.downImage.setVisibility(View.INVISIBLE);
             holder.appointmentText.setVisibility(View.INVISIBLE);
             holder.appointmentValue.setVisibility(View.INVISIBLE);
             /* holder.patientInfo.setVisibility(View.INVISIBLE);*/
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.slot);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText = "Slot 1 : ";
        //set header text as first char in name
        if(appointment.get(position).getSlot().equals("Slot 1")){
            headerText = "Slot 1 : " +appointment.get(position).getStartTime()+"-"+appointment.get(position).getEndTime();
        }else if(appointment.get(position).getSlot().equals("Slot 2")){
            headerText = "Slot 2 : " +appointment.get(position).getStartTime()+"-"+appointment.get(position).getEndTime();
        }else if(appointment.get(position).getSlot().equals("Slot 3")){
            headerText = "Slot 3 : " +appointment.get(position).getStartTime()+"-"+appointment.get(position).getEndTime();
        }

        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return appointment.get(position).getStartTime().subSequence(0, 4).charAt(1);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView countTv,timeTv,patientName,appointmentTypeValue,appointmentText,appointmentValue;
        RelativeLayout patientInfo;
        ImageView patientImage,downImage;
    }
}
