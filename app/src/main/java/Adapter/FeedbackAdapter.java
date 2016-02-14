package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.AllPatients;

/**
 * Created by User on 23-11-2015.
 */
public class FeedbackAdapter extends BaseAdapter {
    Activity activity;
    List<AllPatients> patients;
    private LayoutInflater inflater;
    public FeedbackAdapter(Activity activity,List<AllPatients> patients){
        this.activity = activity;
        this.patients = patients;
    }
    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feedback_element, null);
        TextView feedbackDate = (TextView)convertView.findViewById(R.id.feedback_date);
        TextView feedbackTime = (TextView)convertView.findViewById(R.id.feedback_time);
        TextView patientName = (TextView)convertView.findViewById(R.id.patient_name);
        TextView rateShow = (TextView)convertView.findViewById(R.id.rate_text);
        TextView commentShow = (TextView)convertView.findViewById(R.id.comment_text);
        TextView review = (TextView)convertView.findViewById(R.id.review);
        ImageView star1 = (ImageView)convertView.findViewById(R.id.first_star);
        ImageView star2 = (ImageView)convertView.findViewById(R.id.second_star);
        ImageView star3 = (ImageView)convertView.findViewById(R.id.third_star);
        ImageView star4 = (ImageView)convertView.findViewById(R.id.fourth_star);
        ImageView star5 = (ImageView)convertView.findViewById(R.id.fifth_star);
        AllPatients patient = patients.get(position);
        if(patient.getReviews().equals("No Feedback Found")){
            feedbackDate.setVisibility(View.GONE);
            feedbackTime.setVisibility(View.GONE);
            patientName.setVisibility(View.GONE);
            rateShow.setVisibility(View.GONE);
            commentShow.setVisibility(View.GONE);
            review.setText(patient.getReviews());
            star1.setVisibility(View.GONE);
            star2.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
            star4.setVisibility(View.GONE);
            star5.setVisibility(View.GONE);
        }else{
            review.setText(patient.getReviews());
            feedbackDate.setText(patient.getAppointmentDate());
            feedbackTime.setText(patient.getAppointmentTime());
            patientName.setText(patient.patientName);
            int starCount = Integer.parseInt(patient.getStar());
            switch(starCount){
                case 1:
                      star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
                      break;
                case 2:
                      star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
                      star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
                      break;
                case 3:
                     star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     break;
                case 4:
                     star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star4.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     break;
                case 5:
                     star1.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star2.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star3.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star4.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     star5.setBackgroundResource(R.drawable.ic_star_black_24dp);
                     break;
            }
        }
        return convertView;
    }

}
