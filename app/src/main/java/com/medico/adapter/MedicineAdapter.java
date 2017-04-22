package com.medico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.MedicineStatusRequest;
import com.medico.model.RemoveMedicineRequest;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SummaryResponse;
import com.medico.model.SummaryResponse.MedicinePrescribed;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.PatientMedicinReminder;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 04-11-2015.
 */
public class MedicineAdapter extends HomeAdapter {
    Activity activity;
    List<MedicinePrescribed> alarms;
    LayoutInflater inflater;
    ProgressDialog progress;
    private int loggedInUserId;

    public MedicineAdapter(Activity activity, List<MedicinePrescribed> alarms, int userId)
    {
        super(activity);
        this.activity = activity;
        this.alarms = alarms;
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

    private void setView(final View convertView, final int position)
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
                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = ((ParentActivity) activity).getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientMedicinReminder.class.getName()).addToBackStack( PatientMedicinReminder.class.getName()).commit();
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
                                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
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
        if(alarms.get(position).reminder.byteValue() == 1)
            alarm.setImageResource(R.drawable.ic_alarm_on_black_24dp);
        else
            alarm.setImageResource(R.drawable.ic_alarm_off_black_24dp);
        alarm.setTag(alarms.get(position));
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final MedicinePrescribed prescribed = (MedicinePrescribed)v.getTag();
                final int currentState = prescribed.reminder.intValue();
                final int postState = currentState==1?0:1;
                api.setMedicineAlarm(new MedicineStatusRequest(prescribed.medicineId,postState), new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication result, Response response) {
//                        progress.dismiss();
                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
                            Toast.makeText(activity, "Medicine reminder status updated", Toast.LENGTH_SHORT).show();
                            alarms.get(position).reminder = new Integer(postState).byteValue();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
//                        progress.dismiss();
                        error.printStackTrace();
                        Toast.makeText(activity, "Medicine reminder status updation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



}
