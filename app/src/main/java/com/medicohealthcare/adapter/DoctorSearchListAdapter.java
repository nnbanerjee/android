package com.medicohealthcare.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.DoctorClinic;
import com.medicohealthcare.model.DoctorSearch;
import com.medicohealthcare.model.LinkedPersonRequest;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.ClinicDoctorAppointmentView;
import com.medicohealthcare.view.profile.PatientDetailsView;

import java.text.DateFormat;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorSearchListAdapter extends HomeAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorSearch> personList;
    private ProgressDialog progress;

    public DoctorSearchListAdapter(Activity activity, List<DoctorSearch> personList)
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
            convertView = inflater.inflate(R.layout.doctor_search_result, null);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.speciality);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctor_image);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        Button add_profile = (Button)convertView.findViewById(R.id.add_profile);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView downImg = (ImageView) convertView.findViewById(R.id.downImg);

        TextView doctorId = (TextView)convertView.findViewById(R.id.profile_id);
        TextView clinicName = (TextView)convertView.findViewById(R.id.clinicName);
        TextView location = (TextView)convertView.findViewById(R.id.clinicLocation);
        TextView clinicContact = (TextView)convertView.findViewById(R.id.clinicContact);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.clinicSlots);
        layout.removeAllViews();
        final Bundle bundle = activity.getIntent().getExtras();
        int searchType = bundle.getInt(PARAM.SEARCH_TYPE);
        final int profileId = bundle.getInt(PARAM.PROFILE_ID);
        int loginrole = bundle.getInt(PARAM.PROFILE_ROLE);
        int searchRole = bundle.getInt(PARAM.SEARCH_ROLE);
        final DoctorSearch doctorSearch = personList.get(position);
        final Person person = doctorSearch.person;
        final Clinic1 clinic = doctorSearch.clinic;
        final List<DoctorClinic> slots = doctorSearch.slots;
        doctorName.setText(person.getName());
        doctorSpeciality.setText(person.getSpeciality());
        doctorId.setText(person.getId().toString());
        totalCount.setText(new Integer(slots.size()).toString());
        clinicName.setText(clinic.clinicName);
        location.setText(clinic.address);
        if(clinic.mobile != null && clinic.location != null)
            clinicContact.setText(" +" + clinic.location.toString() + " " + clinic.mobile.toString());
        if(slots != null && slots.size() > 0)
        {
            for(DoctorClinic slot: slots)
            {
                View child = activity.getLayoutInflater().inflate(R.layout.slot_list, null);
                addSlots(child,doctorSearch,slot);
                layout.addView(child);
            }
        }

        if(loginrole == PARAM.PATIENT && searchRole == PARAM.DOCTOR && person.role == PARAM.DOCTOR)
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
                        api.addPersonLinkage(new LinkedPersonRequest(profileId, person.id, PARAM.DOCTOR), new Callback<ResponseCodeVerfication>()
                        {
                            @Override
                            public void success(ResponseCodeVerfication persons, Response response)
                            {
                                if(persons.getStatus() == 1)
                                {
                                    personList.get(position).person.linkedWith = 1;
                                    adapter.notifyDataSetInvalidated();
                                    Toast.makeText(activity, "Profile is successfully added", Toast.LENGTH_LONG).show();
                                }
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
            else
                add_profile.setVisibility(View.GONE);
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

        String imageUrl = person.getImageUrl();
        if (imageUrl != null && imageUrl.trim().length() > 0) {
                new ImageLoadTask(imageUrl, viewImage).execute();
        }

        return convertView;

    }

    private void addSlots(View convertView, DoctorSearch doctorSearch, DoctorClinic slot )
    {
        TextView shiftName = (TextView)convertView.findViewById(R.id.shift_name);
        TextView shiftDays = (TextView)convertView.findViewById(R.id.shiftDays);
        TextView shiftTime = (TextView)convertView.findViewById(R.id.shiftTime);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        shiftName.setText("Slot " + slot.slotNumber + " : ");
        shiftDays.setText(daysOfWeek(slot.daysOfWeek));
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(slot.timeToStart) +" - " + formatTime.format(slot.TimeToStop);
        shiftTime.setText(shiftDateTime);
        bookOnline(bookOnline, doctorSearch, slot);
    }
    private void bookOnline(Button bookonline, final DoctorSearch doctorSearch, final DoctorClinic slot)
    {
        bookonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Bundle bundle = activity.getIntent().getExtras();
            DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
            String shiftDateTime = formatTime.format(slot.timeToStart) +" - " + formatTime.format(slot.TimeToStop);
            bundle.putInt(PARAM.APPOINTMENT_ID,0);
            bundle.putString(PARAM.DAYS_OF_WEEK,slot.daysOfWeek);
            bundle.putInt(PARAM.CLINIC_ID, doctorSearch.clinic.idClinic);
            bundle.putString(PARAM.CLINIC_NAME,doctorSearch.clinic.clinicName);
            bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
            bundle.putInt(PARAM.DOCTOR_CLINIC_ID,slot.doctorClinicId);
            bundle.putInt(PARAM.SLOT_VISIT_DURATION,slot.visitDuration);
            bundle.putLong(PARAM.SLOT_START_DATETIME,slot.timeToStart);
            bundle.putLong(PARAM.SLOT_END_DATETIME,slot.TimeToStop);
            activity.getIntent().putExtras(bundle);
            ParentFragment fragment = new ClinicDoctorAppointmentView();
//            ((ParentActivity)activity).attachFragment(fragment);
            fragment.setArguments(bundle);
            FragmentManager fragmentManger = activity.getFragmentManager();
            fragmentManger.beginTransaction().add(R.id.service,fragment,ClinicDoctorAppointmentView.class.getName()).addToBackStack(ClinicDoctorAppointmentView.class.getName()).commit();
            }
        });
    }
    private String daysOfWeek(String days)
    {
        String[] daysNumber = {"0,1,2,3,4,5,6","0,1,2,3,4,5","0,1,2,3,4","0,1,2,3","0,1,2","0,1","0",
                "1,2,3,4,5,6","1,2,3,4,5","1,2,3,4","1,2,3","1,2","1",
                "2,3,4,5,6","2,3,4,5","2,3,4","2,3","2",
                "3,4,5,6","3,4,5","3,4","3",
                "4,5,6","4,5","3",
                "5,6","5",
                "6"};
        String[] daysWord = {"MON-SUN","MON-SAT","MON-FRI","MON-THU","MON-WED","MON-TUE","MON",
                "TUE-SUN","TUE-SAT","TUE-FRI","TUE-THU","TUE-WED","TUE",
                "WED-SUN","WED-SAT","WED-FRI","WED-THU","WED",
                "THU-SUN","THU-SAT","THU-FRI","THU",
                "FRI-SUN","FRI-SAT","FRI",
                "SAT-SUN","SAT",
                "SUN"};

        for(int i = 0; i < daysNumber.length;i++)
        {
            days = days.replace(daysNumber[i],daysWord[i]);
        }

        return days;

    }

}
