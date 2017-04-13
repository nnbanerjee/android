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
import com.medico.model.DependentDelegatePerson;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;

import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DependentDelegationSettingListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<DependentDelegatePerson> personList;
//    MyApi api;
//    String doctorId;
//    SharedPreferences session;
    private ProgressDialog progress;

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
//        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(activity.getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
//        doctorId = session.getString("id", null);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.person_list_item, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        int role = personList.get(position).role;
        switch (role)
        {
            case PARAM.PATIENT:
                viewImage.setImageResource(R.drawable.patient);
                break;
            case PARAM.DOCTOR:
                viewImage.setImageResource(R.drawable.doctor);
                break;
            case PARAM.ASSISTANT:
                viewImage.setImageResource(R.drawable.assistant);
                break;
        }

        viewImage.setBackgroundResource(R.drawable.patient);

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
        doctorSpeciality.setText(personList.get(position).relation);

        return convertView;

    }


}
