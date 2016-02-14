package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.DoctorClinicSchedule;

/**
 * Created by JAY GANESH on 12/9/2015.
 */
public class DoctorClinicAdapter extends BaseAdapter {
	Activity activity;
	List<DoctorClinicSchedule> doctorClinicSchedules;
	LayoutInflater inflater;
	public DoctorClinicAdapter(Activity activity,List<DoctorClinicSchedule> doctorClinicSchedules){
		this.activity = activity;
		this.doctorClinicSchedules = doctorClinicSchedules;
	}
	@Override
	public int getCount() {
		return doctorClinicSchedules.size();
	}

	@Override
	public Object getItem(int i) {
		return doctorClinicSchedules.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if(inflater == null){
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		View convertView = view;
		if (convertView == null)
			convertView = inflater.inflate(R.layout.doctor_clinic_shift_list_item, null);
		TextView slotName = (TextView)convertView.findViewById(R.id.slot_name);
		TextView dayOfWeek = (TextView)convertView.findViewById(R.id.days_of_week);
		TextView slotTime = (TextView)convertView.findViewById(R.id.slot_time);

		DoctorClinicSchedule schedule = doctorClinicSchedules.get(i);
		slotName.setText(schedule.slotName);
		if(schedule.daysOfWeek.equalsIgnoreCase("None")){
			dayOfWeek.setText(schedule.daysOfWeek);
		}else{
			String daysOfWeekString = "";
			daysOfWeekString = getDaysOfShifts(schedule.daysOfWeek);
			dayOfWeek.setText(daysOfWeekString);
		}
		if(schedule.startTime.equalsIgnoreCase("None")){
			slotTime.setText(schedule.daysOfWeek);
		}else{
			String slotString = schedule.startTime+" To "+schedule.endTime;
			slotTime.setText(slotString);
		}
		return convertView;
	}
	public String getDaysOfShifts(String rawString){
		String[] daysInStrings = rawString.split(",");
		String shiftDays = "";
		int endFlag = daysInStrings.length - 1;
		for(int i=0;i<daysInStrings.length;i++){
			String day = daysInStrings[i];
			switch (day){
				case "0":
						if(endFlag == i){
							shiftDays = shiftDays + "Mon";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Mon-";
						}
						break;
				case "1":
						if(endFlag == i){
							shiftDays = shiftDays + "Tue";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Tue-";
						}
						break;
				case "2":
						if(endFlag == i){
							shiftDays = shiftDays + "Wed";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Wed-";
						}
						break;
				case "3":
						if(endFlag == i){
							shiftDays = shiftDays + "Thu";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Thu-";
						}
						break;
				case "4":
						if(endFlag == i){
							shiftDays = shiftDays + "Fri";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Fri-";
						}
						break;
				case "5":
						if(endFlag == i){
							shiftDays = shiftDays + "Sat";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Sat-";
						}
						break;
				case "6":
						if(endFlag == i){
							shiftDays = shiftDays + "Sun";
							endFlag = endFlag +1;
						}else{
							shiftDays = shiftDays + "Sun-";
						}
						break;
			}
		}
		return shiftDays;
	}
}
