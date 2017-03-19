package com.medico.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.model.Person;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.ManagePatientProfile;
import com.medico.view.ParentFragment;
import com.medico.view.PatientDetailsFragment;
import com.medico.view.PatientVisitDatesView;
import com.medico.application.R;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientSettingListAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Person> personList;
    MyApi api;
    String doctorId;
    SharedPreferences session;
    private ProgressDialog progress;

    public PatientSettingListAdapter(Activity activity, List<Person> personList) {
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
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        new ImageLoadTask(activity.getString(R.string.image_base_url) + personList.get(position).getImageUrl(), viewImage).execute();

        viewImage.setBackgroundResource(R.drawable.patient);

        if (personList.get(position).getAddress() != null) {
            if (personList.get(position).getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(personList.get(position).getAddress());

            }
        }

        doctorName.setText(personList.get(position).getName());
        doctorSpeciality.setText(personList.get(position).getSpeciality());



        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ManagePatientProfile parentactivity = (ManagePatientProfile)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, personList.get(position).getId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsFragment();
                parentactivity.fragmentList.add(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // patientId = session.getString("patientId", null);
                SharedPreferences.Editor editor = session.edit();
//                editor.putString("doctorId", allPatients.get(position).getDoctorId());
                editor.putString("patientId", personList.get(position).getId().toString());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment", "doctorPatientListAdapter");
                Fragment fragment = new PatientVisitDatesView();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }


}
