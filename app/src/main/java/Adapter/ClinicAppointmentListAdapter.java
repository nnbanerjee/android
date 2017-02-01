package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Model.Clinic;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicAppointmentListAdapter extends BaseAdapter {

    private Activity activity;
    List<Clinic> clinicList;
    ViewHolder mHolder = null;
    SharedPreferences session;
    String patientId;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    public ClinicAppointmentListAdapter(Activity activity, List<Clinic> clinicList) {
        this.activity = activity;
        this.clinicList = clinicList;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
    }
    static class ViewHolder {
        private TextView clinicName,clinicsSpeciality,appointmentDate,appointmentType,feedback,appointmentTypeShow,appointmentShow;
        private ImageView doctorImg,downImage,alarmSwitch,clockShow;
        private Button rightArrow;
    }

    @Override
    public int getCount() {
        return clinicList.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            convertView = inflator.inflate(R.layout.appointment_list_item, null);
            mHolder = new ViewHolder();
            mHolder.clinicName = (TextView) convertView.findViewById(R.id.doctorName);
            mHolder.doctorImg = (ImageView) convertView.findViewById(R.id.doctorImg);
            mHolder.clinicsSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
            mHolder.appointmentDate = (TextView) convertView.findViewById(R.id.appointmentValue);
            mHolder.appointmentType = (TextView) convertView.findViewById(R.id.appointment_type_value);
            mHolder.alarmSwitch = (ImageView) convertView.findViewById(R.id.alarm_switch);
            mHolder.feedback = (TextView)convertView.findViewById(R.id.summary_text);
            mHolder.downImage = (ImageView)convertView.findViewById(R.id.downImg);
            mHolder.clockShow = (ImageView)convertView.findViewById(R.id.clock_show);
            mHolder.rightArrow = (Button)convertView.findViewById(R.id.nextBtn);
            mHolder.appointmentTypeShow = (TextView)convertView.findViewById(R.id.appointment_type);
            mHolder.appointmentShow = (TextView)convertView.findViewById(R.id.appointment);
//            mHolder.feedback.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int getPosition = (Integer) v.getTag();
//                    Clinic c = clinicList.get(getPosition);
//                    System.out.println("OnClick Position= "+getPosition);
//                    if(!c.getClinicName().equals("No Appointment Found")) {
//                        if (c.star == null) {
//                            saveDataForFeedback(c);
//                            Fragment fragment = new FeedbackFragmentClinicAppointment();
//                            FragmentManager fragmentManger = activity.getFragmentManager();
//                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                        } else {
//                            saveDataForSummary(c);
//                            Bundle bun = new Bundle();
//                            bun.putString("fragment", "clinicAppointmentListAdapter");
//                            Fragment fragment = new PatientAppointmentInformation();
//                            fragment.setArguments(bun);
//                            FragmentManager fragmentManger = activity.getFragmentManager();
//                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patient Consultations").addToBackStack(null).commit();
//                        }
//                    }
//                }
//            });

            mHolder.alarmSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    Clinic c = clinicList.get(getPosition);
                    System.out.println("OnClick Position= "+getPosition);
                    if(!c.getClinicName().equals("No Appointment Found")) {

//                        if (c.upcomingFlag == true) {
//                            if (c.getAlarmFlag() == false) {
//                                v.setBackgroundResource(R.drawable.toggle_on);
//                                c.setAlarmFlag(true);
//                                setAlarm(c, "true");
//                                Toast.makeText(activity, "Alarm Set", Toast.LENGTH_SHORT).show();
//                            } else {
//                                v.setBackgroundResource(R.drawable.toggle_off);
//                                c.setAlarmFlag(false);
//                                setAlarm(c, "false");
//                                Toast.makeText(activity, "Alarm Cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(activity, "Alarm Cannot Set", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            });
            mHolder.clinicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    Clinic c = clinicList.get(getPosition);
                    if(!c.getClinicName().equals("No Appointment Found")) {
//                        if (c.appointmentType.equalsIgnoreCase("Physical Exam")) {
//                            Global global = (Global) activity.getApplicationContext();
//                            global.setAppointmentDate(c.appointmentDate);
//                            global.setAppointmentTime(c.appointmentTime);
//                            SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = session.edit();
//                            editor.putString("clinicDoctorId", clinicList.get(position).getDoctorId());
//                            editor.putString("doctor_patientEmail", clinicList.get(position).doctorEmail);
//                            editor.commit();
//                            Bundle bundle = new Bundle();
//                            Fragment fragment = new PatientAppointmentDocument();
//                            bundle.putString("fragment", "clinicAppointmentListAdapter");
//                            fragment.setArguments(bundle);
//                            FragmentManager fragmentManger = activity.getFragmentManager();
//                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                        }
                    }
                }
            });
//            mHolder.appointmentDate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int getPosition = (Integer) v.getTag();
//                    final Clinic c = clinicList.get(getPosition);
//                    if(!c.getClinicName().equals("No Appointment Found")) {
//                        MyApi api;
//                        RestAdapter restAdapter = new RestAdapter.Builder()
//                                .setEndpoint(activity.getString(R.string.base_url))
//                                .setClient(new OkClient())
//                                .setLogLevel(RestAdapter.LogLevel.FULL)
//                                .build();
//                        api = restAdapter.create(MyApi.class);
//                        api.getAlldoctorsOfClinicDetail(c.getIdClinic(), new Callback<List<ClinicDetailVm>>() {
//                            @Override
//                            public void success(List<ClinicDetailVm> clinicDetailVms, Response response) {
//                                Global global = (Global) activity.getApplicationContext();
//                                global.setAllDoctorClinicList(clinicDetailVms);
//                                Bundle bun = new Bundle();
////                                bun.putString("clinicId", c.getIdClinic());
////                                bun.putString("clinicDoctorId", c.getDoctorId());
////                                bun.putString("appointmentTime", c.appointmentTime);
////                                bun.putString("appointmentDate", c.appointmentDate);
////                                bun.putString("clinicShift", c.getShift());
//                                bun.putString("fragment", "clinicAppointmentListAdapter");
//                                Fragment fragment = new ClinicAppointmentBook();
//                                fragment.setArguments(bun);
//                                FragmentManager fragmentManger = activity.getFragmentManager();
//                                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//                                error.printStackTrace();
//                                Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//            });
            convertView.setTag(mHolder);
            convertView.setTag(R.id.appointmentValue,mHolder.appointmentDate);
            convertView.setTag(R.id.appointment_type_value,mHolder.appointmentType);
            convertView.setTag(R.id.alarm_switch,mHolder.alarmSwitch);
            convertView.setTag(R.id.doctorImg,mHolder.doctorImg);
            convertView.setTag(R.id.doctorName,mHolder.clinicName);
            convertView.setTag(R.id.summary_text,mHolder.feedback);
            convertView.setTag(R.id.doctorSpeciality,mHolder.clinicsSpeciality);
            convertView.setTag(R.id.clock_show,mHolder.clockShow);
            convertView.setTag(R.id.nextBtn,mHolder.rightArrow);
            convertView.setTag(R.id.downImg,mHolder.downImage);
            convertView.setTag(R.id.appointment_type,mHolder.appointmentTypeShow);
            convertView.setTag(R.id.appointment,mHolder.appointmentShow);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.appointmentDate.setTag(position);
        mHolder.appointmentType.setTag(position);
        mHolder.alarmSwitch.setTag(position);
        mHolder.doctorImg.setTag(position);
        mHolder.clinicName.setTag(position);
        mHolder.clinicsSpeciality.setTag(position);
        mHolder.feedback.setTag(position);
        mHolder.downImage.setTag(position);
        mHolder.rightArrow.setTag(position);
        mHolder.clockShow.setTag(position);
        mHolder.appointmentTypeShow.setTag(position);
        mHolder.appointmentShow.setTag(position);
        System.out.println("Normal Position= "+position);
        Clinic clinic = clinicList.get(position);
        if(clinic.getClinicName().equals("No Appointment Found")){
            mHolder.clinicName.setText(clinic.getClinicName());
            mHolder.doctorImg.setVisibility(View.GONE);
            mHolder.clinicsSpeciality.setVisibility(View.GONE);
            mHolder.rightArrow.setVisibility(View.GONE);
            mHolder.downImage.setVisibility(View.GONE);
            mHolder.clockShow.setVisibility(View.GONE);
            mHolder.appointmentShow.setVisibility(View.GONE);
            mHolder.appointmentTypeShow.setVisibility(View.GONE);
            mHolder.appointmentDate.setVisibility(View.GONE);
            mHolder.appointmentType.setVisibility(View.GONE);
            mHolder.feedback.setVisibility(View.GONE);
            mHolder.alarmSwitch.setVisibility(View.GONE);
        }else{
//            mHolder.doctorImg.setBackgroundResource(R.drawable.clinic);
//            mHolder.clinicName.setText(clinic.getClinicName());
//            mHolder.clinicsSpeciality.setText(clinic.getClinicName());
//            mHolder.appointmentDate.setText(clinic.appointmentDate + "   " +clinic.appointmentTime);
//            mHolder.appointmentType.setText(clinic.appointmentType);
//            if(clinic.star != null){
//                mHolder.feedback.setText("Summary");
//            }else{
//                mHolder.feedback.setText("Feedback");
//            }
//            if(clinic.upcomingFlag == true){
//                if(clinic.alarmFlag == false){
//                    mHolder.alarmSwitch.setBackgroundResource(R.drawable.toggle_off);
//                }else{
//                    mHolder.alarmSwitch.setBackgroundResource(R.drawable.toggle_on);
//                }
//            }
        }
        return convertView;
    }
    public void setAlarm(Clinic patientClinic,String result){
        Date date = null;
//        try {
//            date = formatter.parse(patientClinic.appointmentDate);
//            String[] timeValue;
//            Calendar calOne = Calendar.getInstance();
//            calOne.setTime(date);
//            timeValue = patientClinic.appointmentTime.split(":");
//            int hour1 = Integer.parseInt(timeValue[0].trim());
//            int min1 = Integer.parseInt(timeValue[1].trim().split(
//                    "[a-zA-Z ]+")[0]);
//            calOne.set(Calendar.HOUR_OF_DAY, hour1);
//            calOne.set(Calendar.MINUTE, min1);
//            calOne.set(Calendar.MINUTE,-15);
//            String strAM_PM = timeValue[1].replaceAll("[0-9]", "");
//            if (strAM_PM.equals("AM")) {
//                calOne.set(Calendar.AM_PM, 0);
//            } else {
//                calOne.set(Calendar.AM_PM, 1);
//            }
//            AlarmManager am = (AlarmManager) activity.getSystemService(activity.getApplicationContext().ALARM_SERVICE);
//            Intent intent = new Intent(activity, AlarmService.class);
//            long trigerTime = calOne.getTimeInMillis();
//            System.out.println("trigerTime = "+trigerTime);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, (int) trigerTime,
//                    intent, PendingIntent.FLAG_ONE_SHOT);
//            if(result.equalsIgnoreCase("true")){
//                am.set(AlarmManager.RTC_WAKEUP, trigerTime, pendingIntent);
//            }else{
//                am.cancel(pendingIntent);
//            }
//            DatabaseHandler databaseHandler = new DatabaseHandler(activity.getApplicationContext());
//            ArrayList<AppointmentDB> appointments = databaseHandler.getAllPatientAppointment(patientId);
//            System.out.println("Size of Appointment Table= "+appointments.size());
//            if(appointments.size() == 0){
//                AppointmentDB appointment = new AppointmentDB();
//                appointment.setPatientId(patientId);
//                appointment.setAppointment_status("true");
//                appointment.setAppointment_date(patientClinic.appointmentDate);
//                appointment.setAppointment_time(patientClinic.appointmentTime);
//                appointment.setAlarm_id(""+trigerTime);
//                appointment.setAppointment_type(patientClinic.appointmentType);
//                Boolean value = databaseHandler.savePatientAppointment(appointment);
//                System.out.println("Table Save= "+value);
//
//            }else{
//                int flag = 0;
//                System.out.println("Size of Appointment Table= "+appointments.size());
//                for(AppointmentDB appointmentDB : appointments){
//                    if((appointmentDB.getAppointment_date().equals(patientClinic.appointmentDate))&&
//                            (appointmentDB.getAppointment_time().equals(patientClinic.appointmentTime))){
//                            int rowsUpdated = databaseHandler.setStatusAppointment(appointmentDB,result);
//                            System.out.println("Rows Updated= "+rowsUpdated);
//                            flag = 1;
//                            break;
//                    }
//                }
//                if(flag == 0){
//                    AppointmentDB appointment = new AppointmentDB();
//                    appointment.setPatientId(patientId);
//                    appointment.setAppointment_status("true");
//                    appointment.setAppointment_date(patientClinic.appointmentDate);
//                    appointment.setAppointment_time(patientClinic.appointmentTime);
//                    appointment.setAlarm_id(""+trigerTime);
//                    appointment.setAppointment_type(patientClinic.appointmentType);
//                    Boolean value = databaseHandler.savePatientAppointment(appointment);
//                    System.out.println("Table Save= "+value);
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }
    public void saveDataForFeedback(Clinic cl){
//        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = session.edit();
//        editor.putString("patient_doctorId", cl.getDoctorId());
//        editor.putString("patient_clinicId", ""+cl.getIdClinic());
//        editor.putString("patient_appointment_date", cl.appointmentDate);
//        editor.putString("patient_appointment_time", cl.appointmentTime);
//        editor.commit();
    }
    public void saveDataForSummary(Clinic cl){
//        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = session.edit();
//        Global global = (Global) activity.getApplicationContext();
//        global.setAppointmentDate(cl.appointmentDate);
//        global.setAppointmentTime(cl.appointmentTime);
//        editor.putString("doctor_patient_appointmentDate",cl.appointmentDate);
//        editor.putString("doctor_patient_appointmentTime",cl.appointmentTime);
//        editor.putString("patient_DoctorEmail",cl.doctorEmail);
//        editor.putString("clinicDoctorId", cl.getDoctorId());
//        editor.commit();
    }


}
