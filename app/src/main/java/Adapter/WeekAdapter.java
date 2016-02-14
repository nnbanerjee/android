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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Week;

/**
 * Created by User on 7/15/15.
 */
public class WeekAdapter extends BaseAdapter {

    Activity activity;
    List<Week> weeks;
    private LayoutInflater inflater;

    public WeekAdapter(Activity activity,List<Week> weeks)
    {
        this.activity = activity;
        this.weeks = weeks;

    }
    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int position) {
        return weeks.get(position);
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
            convertView = inflater.inflate(R.layout.appointment_week_element, null);

        final ImageView patientImage = (ImageView)convertView.findViewById(R.id.image_patient);
        TextView timeText = (TextView)convertView.findViewById(R.id.time_text);
        timeText.setText(weeks.get(position).getAppointmentTime());
        Picasso.with(activity)
                .load(weeks.get(position).getUrl())
                .into(patientImage, new Callback() {
                    @Override
                    public void onSuccess() {

                        patientImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.out.println("Picasso Error:::::::::::::");
                    }
                });
        return convertView;
    }
}
