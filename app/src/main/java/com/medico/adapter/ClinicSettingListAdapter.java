package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.Clinic1;
import com.medico.util.PARAM;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicSettingListAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Clinic1> personList;
    MyApi api;
    String doctorId;
    SharedPreferences session;
    private ProgressDialog progress;

    public ClinicSettingListAdapter(Activity activity, List<Clinic1> personList) {
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
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        doctorId = session.getString("id", null);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.person_list_item, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.clinic_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.clinicSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.clinic_image);
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
