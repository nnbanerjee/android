package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorReview;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.DoctorAppointmentInformation;
import com.medicohealthcare.view.profile.DoctorDetailsView;

import java.text.DateFormat;
import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorReviewListAdapter extends ParentAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorReview> patientReviews;

    public DoctorReviewListAdapter(Activity activity, List<DoctorReview> patientReviews)
    {
        super();
        this.activity = activity;
        this.patientReviews = patientReviews;
    }

    @Override
    public int getCount() {
        return patientReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return patientReviews.get(position);
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
            convertView = inflater.inflate(R.layout.patient_review_list_item, null);
        TextView patient_name = (TextView) convertView.findViewById(R.id.patient_name);
        TextView speciality = (TextView) convertView.findViewById(R.id.speciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView patient_image = (ImageView) convertView.findViewById(R.id.patient_image);
        ImageButton recommdation_value = (ImageButton)convertView.findViewById(R.id.recommdation_value);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView rating_value = (TextView)convertView.findViewById(R.id.rating_value);
        TextView review_value = (TextView) convertView.findViewById(R.id.review_value);
        TextView visited_value = (TextView) convertView.findViewById(R.id.visited_value);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        patient_name.setText(patientReviews.get(position).doctorName);
        speciality.setText(patientReviews.get(position).profession);
        totalCount.setVisibility(View.GONE);
        if(patientReviews.get(position).recommendations != null && patientReviews.get(position).recommendations.byteValue() == 1)
            recommdation_value.setSelected(true);
        else
            recommdation_value.setSelected(false);
        if(patientReviews.get(position).review != null )
            review_value.setText((patientReviews.get(position).review));
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        visited_value.setText(format.format(patientReviews.get(position).appointmentDate));
        if(patientReviews.get(position).imageUrl != null )
            new ImageLoadTask(patientReviews.get(position).imageUrl, patient_image).execute();
//        patient_image.setBackgroundResource(R.drawable.patient);
        if(patientReviews.get(position).numberOfVisits != null )
            totalCount.setText(patientReviews.get(position).numberOfVisits.toString());
        if(patientReviews.get(position).rating != null )
            rating_value.setText(patientReviews.get(position).rating.toString());
        if (patientReviews.get(position).address != null) {
            if (patientReviews.get(position).address.equals("")) {
                address.setText("None");

            } else {
                address.setText(patientReviews.get(position).address);

            }
        }

        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, patientReviews.get(position).doctorId);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new DoctorDetailsView();
//                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, DoctorDetailsView.class.getName()).addToBackStack(DoctorDetailsView.class.getName()).commit();

            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = activity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, patientReviews.get(position).doctorId);
                bundle.putInt(PARAM.APPOINTMENT_ID, patientReviews.get(position).appointmentId);
                bundle.putInt(PARAM.CLINIC_ID, patientReviews.get(position).clinicId);
                bundle.putLong(PARAM.APPOINTMENT_DATETIME, patientReviews.get(position).appointmentDate);
                bundle.putInt(PARAM.VISIT_TYPE, patientReviews.get(position).visitType);
                bundle.putString(PARAM.CLINIC_NAME, patientReviews.get(position).clinicName);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new DoctorAppointmentInformation();
//                ((ParentActivity)activity).attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, DoctorAppointmentInformation.class.getName()).addToBackStack(DoctorAppointmentInformation.class.getName()).commit();
            }
        });
        return convertView;

    }


}
