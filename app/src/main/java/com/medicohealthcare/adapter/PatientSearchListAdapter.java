package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.FragmentManager;
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

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.LinkedPersonRequest;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.appointment.ClinicAppointmentScheduleView;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.PatientDetailsView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;  


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientSearchListAdapter extends HomeAdapter  
{

    private Activity activity;
    private LayoutInflater inflater;
    List<Person> personList;
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
            lastAppointmentText.setText("Profile Id : ");
            lastAppointmentValue.setText(person.id.toString());
            if(loginrole == PARAM.DOCTOR && searchRole == PARAM.PATIENT && person.role == PARAM.PATIENT)
            {

                totalCount.setVisibility(View.GONE);
                downImg.setVisibility(View.GONE);
                rightButton.setVisibility(View.GONE);
                if(person.linkedWith.intValue() == 0)
                {

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
                                    {
                                        personList.get(position).linkedWith = 1;
                                        adapter.notifyDataSetInvalidated();
                                        Toast.makeText(activity, "Profile is successfully added", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                        Toast.makeText(activity, "Profile could not been added", Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void failure(RetrofitError error)
                                {
                                    hideBusy();
                                    new MedicoCustomErrorHandler(activity).handleError(error);
                                }
                            });
                        }
                    });
                }
                else
                    add_profile.setVisibility(View.GONE);
            }

        }
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, person.id);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsView();
//                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientDetailsView.class.getName()).addToBackStack(PatientDetailsView.class.getName()).commit();
            }
        });
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
