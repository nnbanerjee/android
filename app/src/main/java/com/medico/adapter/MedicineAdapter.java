package com.medico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.model.RemoveMedicineRequest;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SummaryResponse;
import com.medico.model.SummaryResponse.MedicinePrescribed;
import com.medico.util.PARAM;
import com.medico.view.ManagePatientProfile;
import com.medico.view.ParentFragment;
import com.medico.view.PatientMedicinReminder;
import com.medico.application.R;

import java.util.List;

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
    MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    private int loggedInUserId;

    public MedicineAdapter(Activity activity, List<MedicinePrescribed> alarms, int userId) {
        this.activity = activity;
        this.alarms = alarms;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggedInUserId = userId;
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
                setView(convertView,position);
            }
            else
            {
                setView(convertView,position);
            }
            return convertView;
        }

    private void setView(final View convertView, int position)
    {
        final TextView name = (TextView)convertView.findViewById(R.id.medicine_name);
        name.setTag(alarms.get(position));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = activity.getIntent().getExtras();
                MedicinePrescribed medicinePrescribed = (MedicinePrescribed)name.getTag();
                args.putInt(PARAM.MEDICINE_ID, medicinePrescribed.medicineId);
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new PatientMedicinReminder();
                ((ManagePatientProfile)activity).fragmentList.add(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = ((ManagePatientProfile) activity).getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        name.setText(alarms.get(position).medicineName);
        convertView.setTag(alarms.get(position));
        ImageView close = (ImageView)convertView.findViewById(R.id.close_button);
        close.setTag(alarms.get(position));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Close buttom clicked");
                final SummaryResponse.MedicinePrescribed medicine = (SummaryResponse.MedicinePrescribed)v.getTag();
                new AlertDialog.Builder(convertView.getContext())
                        .setTitle("Delete Medicine")
                        .setMessage("Are you sure you want to delete this medicine?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                                RemoveMedicineRequest removeMedicineRequest = new RemoveMedicineRequest(medicine.medicineId, loggedInUserId);
                                api.removePatientMedicine(removeMedicineRequest, new Callback<ResponseCodeVerfication>() {
                                    @Override
                                    public void success(ResponseCodeVerfication result, Response response) {
                                        progress.dismiss();
                                        if (result.getStatus().equalsIgnoreCase("1")) {
                                            Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
                                            alarms.remove(medicine);
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
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        ImageView alarm = (ImageView)convertView.findViewById(R.id.alarm_button);
        alarm.setTag(alarms.get(position));
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("alarm buttom clicked");
            }
        });
        alarm.setVisibility(alarms.get(position).reminder==1?View.VISIBLE:View.INVISIBLE);
    }



}
