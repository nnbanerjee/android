package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.Person;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;

import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientSearchListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Person> personList;
    private ProgressDialog progress;

    public PatientSearchListAdapter(Activity activity, List<Person> personList)
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
        rightButton.setVisibility(View.GONE);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        totalCount.setVisibility(View.GONE);
        ImageView downImg = (ImageView) convertView.findViewById(R.id.downImg);
        downImg.setVisibility(View.GONE);
        viewImage.setBackground(null);
        int role = personList.get(position).role;
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
        doctorSpeciality.setText(personList.get(position).getSpeciality());

        return convertView;

    }


}
