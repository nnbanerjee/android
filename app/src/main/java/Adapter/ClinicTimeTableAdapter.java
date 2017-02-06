package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ClinicDetailVm;
import Model.ClinicList;
import Model.Slot;
import Model.TimeInterval;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicTimeTableAdapter extends BaseAdapter {

    private Activity activity;
    private List<TimeInterval> timeList;
    private GridView timeTeableList;
    private View oldView = null;
    private int oldPosition = 0;
    Global global;
    private String appointmentTime;
    private List<ClinicDetailVm> clinicDetailVmList;
    public SharedPreferences session;
    private ClinicList selectedClinic;
    private Slot SelectedSlotDetailsWithClinic;
    private int selectedSlotNo;


     public static int selectedTimeSlotSequenceNo=0;
    public ClinicTimeTableAdapter(Activity activity, List<TimeInterval> timeList, GridView timeTeableList,String appointmentTime) {
        this.activity = activity;
        this.timeList = timeList;
        this.timeTeableList = timeTeableList;
        this.appointmentTime = appointmentTime;
        Gson gson = new Gson();
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String jsonSelectedClinic = session.getString("selectedClinic", "");
        selectedClinic=gson.fromJson(jsonSelectedClinic, ClinicList.class);

        String json = session.getString("SelectedSlotDetailsWithClinic", "");
        SelectedSlotDetailsWithClinic = gson.fromJson(json, Slot.class);
        selectedSlotNo = session.getInt("selectedSlotNo", 0);
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        protected TextView timeValue;
        protected TextView availableValue;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            convertView = inflator.inflate(R.layout.clinic_time_table_item, null);
            viewHolder = new ViewHolder();

            global = (Global) activity.getApplicationContext();

            //viewHolder.button = (Button) convertView.findViewById(R.id.country_name);
            viewHolder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
            viewHolder.availableValue = (TextView) convertView.findViewById(R.id.availableValue);

            viewHolder.timeValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    selectedTimeSlotSequenceNo=getPosition;
                    TextView availValue = null;
                    View parent = (View)v.getParent();
                    /*ClinicDetailVm clinicVm = new ClinicDetailVm();
                    clinicDetailVmList = global.getClinicDetailVm();
                    System.out.println("Size:::::::::"+clinicDetailVmList.size());

                    for(ClinicDetailVm vm : clinicDetailVmList)
                    {
                        if(vm.getClinicId().equals(""+global.clinicDetailsData.getClinicId())){
                            clinicVm = vm;
                        }
                    }*/


                    String onlineAppointment = "";//clinicVm.getOnlineAppointment();
                   //zSystem.out.println("Online Appointment:::::::"+clinicVm.getOnlineAppointment());
                    if (parent != null) {
                        availValue = (TextView) parent.findViewById(R.id.availableValue);
                    }
                    if(availValue.getText().toString().equals("Occupied"))
                    {
                        Toast.makeText(activity,"Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    else if(availValue.getText().toString().equals("OnRequest"))
                    {

                        if(oldView != null){
                            TextView oldTextView = (TextView) oldView.findViewById(R.id.timeValue);
                            TextView availableValue = (TextView) oldView.findViewById(R.id.availableValue);
                            if(availableValue.getText().toString().equals("OnRequest")){

                                oldTextView.setBackgroundResource(R.drawable.rounded_orange_color);
                            }else{
                                oldTextView.setBackgroundResource(R.drawable.rounded_green_color);
                            }
                            timeList.get(oldPosition).setSelected(false);
                        }
                        oldView = (View)v.getParent();
                        oldPosition = (Integer) v.getTag();

                        if(timeList.get(getPosition).isSelected()){
                            v.setBackgroundResource(R.drawable.rounded_orange_color);
                            timeList.get(getPosition).setSelected(false);
                        }else{
                            global.clinicDetailsData.setStatus("Occupied");
                            global.clinicDetailsData.setBookTime(timeList.get(getPosition).getTime());

                            v.setBackgroundResource(R.drawable.rounded_blue_orange_color);
                            timeList.get(getPosition).setSelected(true);
                        }
                    }
                    else if(availValue.getText().toString().equals("Not Available"))
                    {
                        Toast.makeText(activity,"Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(oldView != null){
                            TextView oldTextView = (TextView) oldView.findViewById(R.id.timeValue);
                            TextView availableValue = (TextView) oldView.findViewById(R.id.availableValue);
                            if(availableValue.getText().toString().equals("OnRequest")){
                                oldTextView.setBackgroundResource(R.drawable.rounded_orange_color);
                            }else{
                                oldTextView.setBackgroundResource(R.drawable.rounded_green_color);
                            }
                            timeList.get(oldPosition).setSelected(false);
                        }
                        oldView = (View)v.getParent();
                        oldPosition = (Integer) v.getTag();
                        if(timeList.get(getPosition).isSelected()){
                            v.setBackgroundResource(R.drawable.rounded_green_color);
                            timeList.get(getPosition).setSelected(false);
                        }else{
                            v.setBackgroundResource(R.drawable.rounded_dark_blue_color);
                            timeList.get(getPosition).setSelected(true);
                            if(onlineAppointment.equals("Always"))
                            {
                                global.clinicDetailsData.setStatus("Occupied");
                            }
                            else
                            {
                                global.clinicDetailsData.setStatus("OnRequest");
                            }
                            global.clinicDetailsData.setBookTime(timeList.get(getPosition).getTime());
                        }
                    }
                    timeTeableList.invalidateViews();
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.timeValue, viewHolder.timeValue);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.timeValue.setTag(position);
        viewHolder.timeValue.setText(timeList.get(position).getTime());
        viewHolder.availableValue.setText(timeList.get(position).getIsAvailable());

        boolean myState = timeList.get(position).isSelected();
        boolean occupiedState = timeList.get(position).getIsAvailable().equals("Occupied");
        boolean confirmedState = timeList.get(position).getIsAvailable().equals("confirmed");
        boolean availableState = timeList.get(position).getIsAvailable().equals("Available");
        boolean onRequestState = timeList.get(position).getIsAvailable().equals("tentative");
        boolean notAvailable = timeList.get(position).getIsAvailable().equals("Not Available");

        if(occupiedState){
            viewHolder.timeValue.setTextColor(activity.getResources().getColor(R.color.red));//.setBackgroundResource(R.drawable.rounded_red_color);
        } else if (availableState) {
            viewHolder.timeValue.setTextColor(activity.getResources().getColor(R.color.green));
        }else if(onRequestState){
            viewHolder.timeValue.setBackgroundResource(R.drawable.rounded_orange_color);
        }else if(confirmedState){
            viewHolder.timeValue.setBackgroundResource(R.drawable.rounded_green_color);
        }
        else if(notAvailable)
        {
            viewHolder.timeValue.setTextColor(activity.getResources().getColor(R.color.grey_text));
        }

        if(myState) {
            if(timeList.get(position).getIsAvailable().equals("OnRequest")){
                viewHolder.timeValue.setBackgroundResource(R.drawable.rounded_blue_orange_color);
            }else{
                viewHolder.timeValue.setBackgroundResource(R.drawable.rounded_dark_blue_color);
            }
        }

        if(appointmentTime != null) {
            if (appointmentTime.equals(timeList.get(position).getTime())) {
                viewHolder.timeValue.setBackgroundResource(R.drawable.rounded_blue_orange_color);
            }
        }
        return convertView;
    }

    public String getSelectedTime(){

        return timeList.get(selectedTimeSlotSequenceNo).getTime();

    }
}
