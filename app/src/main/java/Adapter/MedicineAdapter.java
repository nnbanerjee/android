package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.PatientMedicinReminder;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.MedicinePrescribed;
import Model.ReminderVM;
import Model.RemoveMedicineRequest;
import Model.ResponseCodeVerfication;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 04-11-2015.
 */
public class MedicineAdapter extends BaseAdapter {
    Activity activity;
    List<MedicinePrescribed> alarms;
    LayoutInflater inflater;
    ViewHolder mHolder = null;
    ReminderVM reminder;
    MyApi api;
    private String doctorId;
    SharedPreferences session;
    ProgressDialog progress;
    private String type;

    public MedicineAdapter(Activity activity, List<MedicinePrescribed> alarms, ReminderVM reminder) {
        this.activity = activity;
        this.alarms = alarms;
        this.reminder = reminder;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", "0");
        type = UtilSingleInstance.getUserType(session.getString("loginType", null));
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(activity.getString(R.string.base_url))
                    .setClient(new OkClient())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = restAdapter.create(MyApi.class);
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.medicine, null);
            mHolder = new ViewHolder();

//{"medicineId":12,"medicineName":"Azeethromycin","startDateTime":1457534000,"endDateTime":1889534000,"reminder":1}
            mHolder.medicineName = (TextView) convertView.findViewById(R.id.medicine_name);
            mHolder.alarm = (ImageView) convertView.findViewById(R.id.alarm_button);
            mHolder.remove = (ImageView) convertView.findViewById(R.id.close_button);
            if (alarms.get(position) != null && alarms.get(position).getMedicineName() != null && alarms.get(position).getMedicineName().equals("No Medicine")) {
                mHolder.medicineName.setText(alarms.get(position).getMedicineName());
                mHolder.alarm.setVisibility(View.GONE);
                mHolder.remove.setVisibility(View.GONE);
            } else
                mHolder.medicineName.setText(alarms.get(position).getMedicineName());

            if (alarms.get(position) != null && alarms.get(position).getReminder() != null && alarms.get(position).getReminder().equalsIgnoreCase("0")) {
                mHolder.alarm.setVisibility(View.INVISIBLE);
            }else{
                mHolder.alarm.setVisibility(View.VISIBLE);
            }

            mHolder.alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    MedicinePrescribed medicineVm = alarms.get(getPosition);
                    if (!medicineVm.getMedicineName().equals("No Medicine")) {
                        Bundle args = new Bundle();
                      /*  args.putString("visitedDate", medicineVm.g.getVisitDate());
                        args.putString("visit", medicineVm.visitType);
                        args.putString("referedBy", medicineVm.referredBy);
                        args.putString("symptomsValue", medicineVm.symptoms);
                        args.putString("diagnosisValue", medicineVm.diagnosis);
                        args.putString("testPrescribedValue", medicineVm.testsPrescribed);*/
                        args.putString("medicinName", medicineVm.getMedicineName());

                        Fragment fragment = new PatientMedicinReminder();
                        fragment.setArguments(args);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }
            });


          /*  convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    MedicinePrescribed medicineVm = alarms.get(getPosition);
                    if (!medicineVm.getMedicineName().equals("No Medicine")) {
                        Bundle args = new Bundle();
                        args.putString("state", "EDIT");
                      *//*  args.putString("visitedDate", medicineVm.g.getVisitDate());
                        args.putString("visit", medicineVm.visitType);
                        args.putString("referedBy", medicineVm.referredBy);
                        args.putString("symptomsValue", medicineVm.symptoms);
                        args.putString("diagnosisValue", medicineVm.diagnosis);
                        args.putString("testPrescribedValue", medicineVm.testsPrescribed);*//*
                        args.putString("medicinName", medicineVm.getMedicineName());
                        args.putString("medicineEndDate", medicineVm.getEndDateTime());
                        args.putString("medicineId", medicineVm.getMedicineId());
                        args.putString("medicineStartDate", medicineVm.getStartDateTime());
                        args.putString("medicineReminder", medicineVm.getReminder());


                        Fragment fragment = new PatientMedicinReminder();
                        fragment.setArguments(args);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }
            });*/

            mHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //{"medicineId":8,"loggedinUserId":104,"userType":1}
                    int getPosition = (Integer) v.getTag();
                    final MedicinePrescribed medicineVm = alarms.get(getPosition);
                    if (!medicineVm.getMedicineName().equals("No Medicine")) {
                        progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                        // System.out.println("alarm size= "+medicineVm.alarms.size());
                        // reminder.alarmReminderVMList = medicineVm.alarms;
                        RemoveMedicineRequest removeMedicineRequest = new RemoveMedicineRequest(alarms.get(getPosition).getMedicineId(), doctorId, type);


                        api.removePatientMedicine(removeMedicineRequest, new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication result, Response response) {
                                progress.dismiss();
                                if (result.getStatus().equalsIgnoreCase("1")) {
                                    Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
                                    alarms.remove(medicineVm);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progress.dismiss();
                                error.printStackTrace();
                                Toast.makeText(activity, "Failed to remove medicine", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            convertView.setTag(mHolder);
            convertView.setTag(R.id.medicine_name, mHolder.medicineName);
            convertView.setTag(R.id.alarm_button, mHolder.alarm);
            convertView.setTag(R.id.close_button, mHolder.remove);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.medicineName.setTag(position);
        mHolder.alarm.setTag(position);
        mHolder.remove.setTag(position);
       // convertView.setTag(position);
        return convertView;
    }





    static class ViewHolder {
        TextView medicineName;
        ImageView alarm, remove;
    }


}
