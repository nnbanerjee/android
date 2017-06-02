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
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.util.PARAM;

import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicSettingListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Clinic1> personList;
    public ClinicSettingListAdapter(Activity activity, List<Clinic1> personList)
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
            convertView = inflater.inflate(R.layout.person_list_item, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        int role = personList.get(position).type;
        viewImage.setImageResource(R.drawable.clinic_default);
        switch (role)
        {
            case PARAM.CLINIC:
                viewImage.setImageResource(R.drawable.clinic_default);
                break;
            case PARAM.PATHOLOGY:
                viewImage.setImageResource(R.drawable.clinic_default);
                break;
            case PARAM.DIAGNOSTIC:
                viewImage.setImageResource(R.drawable.clinic_default);
            case PARAM.ONLINE_CONSULATION:
                viewImage.setImageResource(R.drawable.clinic_default);
            case PARAM.HOME_VISIT:
                viewImage.setImageResource(R.drawable.clinic_default);
                break;
        }

        if (personList.get(position).address != null && personList.get(position).address.trim().length() > 0)
        {
            address.setText(personList.get(position).address);
        }
        else
            address.setText("None");

//        String imageUrl = personList.get(position).imageUrl;
//        if (imageUrl != null && imageUrl.trim().length() > 0) {
//                new ImageLoadTask(imageUrl, viewImage).execute();
//        }

        doctorName.setText(personList.get(position).clinicName);
        doctorSpeciality.setText(personList.get(position).speciality);

        return convertView;

    }


}
