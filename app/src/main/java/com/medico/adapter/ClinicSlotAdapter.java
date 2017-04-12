package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.ClinicSlotDetails;
import com.medico.model.DoctorClinicId;
import com.medico.model.ResponseCodeVerfication;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.settings.ClinicSlotEditView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 04-11-2015.
 */
public class ClinicSlotAdapter extends HomeAdapter {
    Activity activity;
    List<ClinicSlotDetails> slots;
    LayoutInflater inflater;
    MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    private int loggedInUserId;

    public ClinicSlotAdapter(Activity activity, List<ClinicSlotDetails> slotDetailses, int userId)
    {
        super(activity);
        this.activity = activity;
        this.slots = slotDetailses;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggedInUserId = userId;
    }

    @Override
    public int getCount() {
        return slots.size();
    }

    @Override
    public Object getItem(int position) {
        return slots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(activity.getString(R.string.base_url))
//                        .setClient(new OkClient())
//                        .setLogLevel(RestAdapter.LogLevel.FULL)
//                        .build();
//                api = restAdapter.create(MyApi.class);
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
        name.setTag(slots.get(position));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = activity.getIntent().getExtras();
                ClinicSlotDetails medicinePrescribed = (ClinicSlotDetails)name.getTag();
                args.putInt(PARAM.DOCTOR_CLINIC_ID, medicinePrescribed.doctorClinicId);
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new ClinicSlotEditView();
                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = ((ParentActivity) activity).getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        name.setText(slots.get(position).slotName + " ( " + slots.get(position).slotNumber + " )");
        convertView.setTag(slots.get(position));
        ImageView close = (ImageView)convertView.findViewById(R.id.close_button);
        close.setTag(slots.get(position));
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Close buttom clicked");
//                final ClinicSlotDetails medicine = (ClinicSlotDetails)v.getTag();
//                new AlertDialog.Builder(convertView.getContext())
//                        .setTitle("Delete Medicine")
//                        .setMessage("Are you sure you want to disable the slot?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
//                                DoctorClinicId removeSlotRequest = new DoctorClinicId(medicine.doctorClinicId);
//                                api.removeSlot(removeSlotRequest, new Callback<ResponseCodeVerfication>() {
//                                    @Override
//                                    public void success(ResponseCodeVerfication result, Response response) {
//                                        progress.dismiss();
//                                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
//                                            Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
//                                            slots.remove(medicine);
//                                            notifyDataSetChanged();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void failure(RetrofitError error) {
//                                        progress.dismiss();
//                                        error.printStackTrace();
//                                        Toast.makeText(activity, "Failed to remove medicine", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//            }
//        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Close buttom clicked");
                final ClinicSlotDetails medicine = (ClinicSlotDetails)v.getTag();
                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                DoctorClinicId removeSlotRequest = new DoctorClinicId(medicine.doctorClinicId,0);
                api.setSlotAvailability(removeSlotRequest, new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication result, Response response) {
                        progress.dismiss();
                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
                            Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        else if (result.getStatus().intValue() == PARAM.STATUS_WARNING) {
                            Toast.makeText(activity, "Medicine Removed however there are future appointments", Toast.LENGTH_SHORT).show();
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
        });
        ImageView alarm = (ImageView)convertView.findViewById(R.id.alarm_button);
        alarm.setTag(slots.get(position));
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("alarm buttom clicked");
            }
        });
    }



}
