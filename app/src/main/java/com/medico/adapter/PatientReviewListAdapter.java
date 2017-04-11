package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.PatientReview;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.ParentActivity;
import com.medico.view.ParentFragment;
import com.medico.view.profile.PatientDetailsFragment;
import com.medico.view.profile.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.List;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientReviewListAdapter extends ParentAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<PatientReview> patientReviews;
    MyApi api;
    private ProgressDialog progress;

    public PatientReviewListAdapter(Activity activity, List<PatientReview> patientReviews)
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
        ImageView recommdation_value = (ImageView)convertView.findViewById(R.id.recommdation_value);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView rating_value = (TextView)convertView.findViewById(R.id.rating_value);
        TextView review_value = (TextView) convertView.findViewById(R.id.review_value);
        TextView visited_value = (TextView) convertView.findViewById(R.id.visited_value);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        patient_name.setText(patientReviews.get(position).patientName);
        speciality.setText(patientReviews.get(position).profession);
        if(patientReviews.get(position).recommendations != null && patientReviews.get(position).recommendations.byteValue() == 1)
            recommdation_value.setImageResource(R.drawable.recommendation_checkbox);
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
                bundle.putInt(PARAM.PATIENT_ID, patientReviews.get(position).patientId);
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsFragment();
                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.PATIENT_ID, patientReviews.get(position).patientId);
                ParentFragment fragment = new PatientVisitDatesView();
                ((ParentActivity)activity).attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }


}