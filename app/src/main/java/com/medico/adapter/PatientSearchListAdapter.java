package com.medico.adapter;

import android.app.Activity;
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
import com.medico.model.LinkedPersonRequest;
import com.medico.model.Person;
import com.medico.model.ResponseCodeVerfication;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.appointment.ClinicAppointmentScheduleView;
import com.medico.view.home.ParentActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientSearchListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Person> personList;
    private ProgressDialog progress;
    HomeAdapter callBack;
    Object callbackParameter;

    public PatientSearchListAdapter(Activity activity, List<Person> personList, HomeAdapter callBack,Object callbackParameter)
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
        final Bundle bundle = activity.getIntent().getExtras();
        int searchType = bundle.getInt(PARAM.SEARCH_TYPE);
        final int profileId = bundle.getInt(PARAM.PROFILE_ID);
        int loginrole = bundle.getInt(PARAM.PROFILE_ROLE);
        int searchRole = bundle.getInt(PARAM.SEARCH_ROLE);
        final Person person = personList.get(position);
        if(searchType == PARAM.APPOINTMENT_BOOKING)
        {
            rightButton.setVisibility(View.GONE);
            totalCount.setVisibility(View.GONE);
            downImg.setVisibility(View.GONE);
            bookOnline.setVisibility(View.VISIBLE);
            bookOnline.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    bundle.putInt("SELECTED_PATIENT_ID",personList.get(position).getId());
                    callBack.callBack(personList.get(position).getId(),personList.get(position), callbackParameter);
                    activity.getIntent().putExtras(bundle);
                    ((ParentActivity)activity).onBackPressed(ClinicAppointmentScheduleView.class.getName());
                }
            });
        }
        else if(searchType == PARAM.SEARCH_GLOBAL )
        {
            if(loginrole == PARAM.DOCTOR && searchRole == PARAM.PATIENT && person.role == PARAM.PATIENT)
            {
                if(person.linkedWith == 0)
                {
                    rightButton.setVisibility(View.GONE);
                    totalCount.setVisibility(View.GONE);
                    downImg.setVisibility(View.GONE);
                    add_profile.setVisibility(View.VISIBLE);
                    add_profile.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            api.addPersonLinkage(new LinkedPersonRequest(profileId, person.id, PARAM.PATIENT), new Callback<ResponseCodeVerfication>()
                            {
                                @Override
                                public void success(ResponseCodeVerfication persons, Response response)
                                {
                                    if(persons.getStatus() == 1)
                                        Toast.makeText(activity, "Profile is successfully added", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(activity, "Profile could not been added", Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void failure(RetrofitError error)
                                {
                                    Toast.makeText(activity, "Profile could not been added", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }

        }
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
