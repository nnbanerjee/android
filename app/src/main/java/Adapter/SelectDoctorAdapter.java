package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Doctor;
import Model.DoctorSearchResponse;


/**
 * Created by MNT on 16-Feb-15.
 */
public class SelectDoctorAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Doctor> doctorList;
    private int count = 0;
    private TextView firstNameTv;
    private TextView specialityTv;
    private TextView locationTv;
    private RadioButton select;

    public SelectDoctorAdapter(Activity activity, List<Doctor> doctorList) {
        this.activity = activity;
        this.doctorList = doctorList;
    }



    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
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
            convertView = inflater.inflate(R.layout.slect_doc_element, null);


        final int pos = position;

        firstNameTv = (TextView) convertView.findViewById(R.id.first_name);
        specialityTv = (TextView) convertView.findViewById(R.id.speciality);
        locationTv = (TextView) convertView.findViewById(R.id.doctor_location);


        convertView.setTag(doctorList);

        final Doctor doc = doctorList.get(position);

        if((doc.getName()).equals("No Doctor Found"))
        {

            specialityTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);

        }
        else
        {

        }

        firstNameTv.setText(doc.getName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(doc.getLocation());
        return convertView;

    }



}
