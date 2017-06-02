package com.medicohealthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DependentDelegatePerson;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;

import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DependentDelegationSettingListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<DependentDelegatePerson> personList;

    public DependentDelegationSettingListAdapter(Activity activity, List<DependentDelegatePerson> personList)
    {
        super(activity);
        this.activity = activity;
        this.personList = personList;
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
        TextView lastAppointment = (TextView)convertView.findViewById(R.id.lastAppointment);
        lastAppointment.setVisibility(View.GONE);
        TextView lastAppointmentValue = (TextView)convertView.findViewById(R.id.lastAppointmentValue);
        lastAppointmentValue.setVisibility(View.GONE);
        TextView appointmentText = (TextView)convertView.findViewById(R.id.appointmentText);
        appointmentText.setVisibility(View.GONE);
        TextView review_value = (TextView)convertView.findViewById(R.id.review_value);
        review_value.setVisibility(View.GONE);
        TextView  totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        totalCount.setVisibility(View.GONE);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        downImage.setVisibility(View.GONE);
        int role = personList.get(position).role;
        viewImage.setBackground(null);
        switch (role)
        {
            case PARAM.PATIENT:
                viewImage.setImageResource(R.drawable.patient_default);
                break;
            case PARAM.DOCTOR:
                viewImage.setImageResource(R.drawable.doctor_default);
                break;
            case PARAM.ASSISTANT:
                viewImage.setImageResource(R.drawable.assistant_default);
                break;
        }
        if (personList.get(position).getAddress() != null) {
            if (personList.get(position).getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(personList.get(position).getAddress());

            }
        }
        String imageUrl = personList.get(position).getImageUrl();
        if (imageUrl != null && imageUrl.trim().length() > 0) {
                new ImageLoadTask(imageUrl, viewImage).execute();
        }

        doctorName.setText(personList.get(position).getName());
        doctorSpeciality.setText(personList.get(position).relation  + " | Id - " + personList.get(position).getId().toString());
        return convertView;

    }


}
