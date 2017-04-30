package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.Clinic1;
import com.medico.model.ClinicPersonRequest;
import com.medico.model.ResponseCodeVerfication;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.PatientDetailsView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicSearchListAdapter extends HomeAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    List<Clinic1> personList;
    private ProgressDialog progress;
    HomeAdapter callBack;
    Object callbackParameter;

    public ClinicSearchListAdapter(Activity activity, List<Clinic1> personList)
    {
        super(activity);
        this.activity = activity;
        this.personList = personList;
        this.callBack = callBack;
        this.callbackParameter = callbackParameter;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_patient_profile_list, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        Button add_profile = (Button)convertView.findViewById(R.id.add_profile);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView downImg = (ImageView) convertView.findViewById(R.id.downImg);
        TextView lastAppointmentText = (TextView)convertView.findViewById(R.id.lastAppointment);
        TextView lastAppointmentValue = (TextView)convertView.findViewById(R.id.lastAppointmentValue);
        TextView nextappointmentText = (TextView)convertView.findViewById(R.id.appointmentText);
        TextView nextAppointment = (TextView)convertView.findViewById(R.id.review_value);
        nextappointmentText.setVisibility(View.GONE);
        nextAppointment.setVisibility(View.GONE);

        final Bundle bundle = activity.getIntent().getExtras();
        int searchType = bundle.getInt(PARAM.SEARCH_TYPE);
        final int profileId = bundle.getInt(PARAM.PROFILE_ID);
        int loginrole = bundle.getInt(PARAM.PROFILE_ROLE);
        int searchRole = bundle.getInt(PARAM.SEARCH_ROLE);
        final Clinic1 person = personList.get(position);
        lastAppointmentText.setText("Clinic Id : ");
        lastAppointmentValue.setText(person.idClinic.toString());
        totalCount.setVisibility(View.GONE);
        downImg.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);

        if(loginrole == 1 && searchRole == PARAM.CLINIC && person.type == PARAM.CLINIC)
        {

            if(person.linkedWith.intValue() == 0)
            {
                add_profile.setVisibility(View.VISIBLE);
                add_profile.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        api.addClinic(new ClinicPersonRequest(person.idClinic, profileId), new Callback<ResponseCodeVerfication>()
                        {
                            @Override
                            public void success(ResponseCodeVerfication persons, Response response)
                            {
                                if(persons.getStatus() == 1)
                                {
                                    personList.get(position).linkedWith = 1;
                                    adapter.notifyDataSetInvalidated();
                                    Toast.makeText(activity, "Clinic is successfully added", Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(activity, "Clinic could not been added", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void failure(RetrofitError error)
                            {
                                Toast.makeText(activity, "Clinic could not been added", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
            else
                add_profile.setVisibility(View.GONE);
        }

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, person.idClinic);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsView();
                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientDetailsView.class.getName()).addToBackStack(PatientDetailsView.class.getName()).commit();
            }
        });
        viewImage.setBackground(null);
        viewImage.setImageResource(R.drawable.clinic_default);
        int role = person.type;
        switch (role)
        {
            case PARAM.CLINIC:
                viewImage.setImageResource(R.drawable.clinic_default);
                break;
            case PARAM.DIAGNOSTIC:
                viewImage.setImageResource(R.drawable.lab_default);
                break;
            case PARAM.ONLINE_CONSULATION:
                viewImage.setImageResource(R.drawable.clinic_online_default);
                break;
            case PARAM.HOME_VISIT:
                viewImage.setImageResource(R.drawable.lab_default);
                break;
        }

        if (personList.get(position).address != null) {
            if (personList.get(position).address.equals("")) {
                address.setText("None");

            } else {
                address.setText(personList.get(position).address);

            }
        }
        String imageUrl = personList.get(position).imageUrl;
        if (imageUrl != null && imageUrl.trim().length() > 0) {
                new ImageLoadTask(imageUrl, viewImage).execute();
        }

        doctorName.setText(personList.get(position).clinicName);
        doctorSpeciality.setText(personList.get(position).speciality);

        return convertView;

    }


}
