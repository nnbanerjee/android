package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.Patient;

/**
 * Created by MNT on 19-Mar-15.
 */
public class ShowDependentAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Patient> patients;
    private LayoutInflater inflater;
    private TextView patientName,locationTv,emailTv,mobileTv,accessLevelTv,statusTv,showStatusTv,accessLevel,showAccessLevel;
    private ImageView imageShow;
    private View bar;
    private CheckBox checkBox;


    public ShowDependentAdapter(Activity activity, ArrayList<Patient> patients) {

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
            convertView = inflater.inflate(R.layout.patient_dependent_confirmed, null);

        final int pos = position;


        patientName = (TextView)convertView.findViewById(R.id.patient_first_name);
        locationTv = (TextView)convertView.findViewById(R.id.patient_location);
        emailTv = (TextView)convertView.findViewById(R.id.email_show);
        mobileTv = (TextView)convertView.findViewById(R.id.mobile_show);
        imageShow = (ImageView)convertView.findViewById(R.id.show_image);
        accessLevelTv = (TextView)convertView.findViewById(R.id.show_access);
        statusTv = (TextView)convertView.findViewById(R.id.show_status);
        showStatusTv = (TextView)convertView.findViewById(R.id.status_id);
        accessLevel = (TextView)convertView.findViewById(R.id.show_access);
        bar = convertView.findViewById(R.id.show_view_bar);
        showAccessLevel = (TextView)convertView.findViewById(R.id.access_level);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_id);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                System.out.println("I am here " + cb.isChecked());

                Patient doc = (Patient) cb.getTag();

                System.out.println("Doc " + doc.toString());

                if (cb.isChecked()) {
                    doc.setSelected(true);
                    System.out.println("Doctor Object Value " + doc.isSelected());
                } else {
                    doc.setSelected(false);
                }
            }
        });

        convertView.setTag(patients);

        final Patient pat = patients.get(pos);

        if((pat.getName()).equals("No Patient Found"))
        {
            patientName.setText("No Patient Found");
            emailTv.setVisibility(View.INVISIBLE);
            mobileTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);
            showAccessLevel.setVisibility(View.INVISIBLE);
            bar.setVisibility(View.INVISIBLE);
            showAccessLevel.setVisibility(View.INVISIBLE);
            accessLevel.setVisibility(View.INVISIBLE);
            statusTv.setVisibility(View.INVISIBLE);
            showStatusTv.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);

        }
        else
        {
            if(pat.getStatus().equals("WC"))
            {
                showStatusTv.setText("Wait");
            }

            if(pat.getStatus().equals("D"))
            {
                showStatusTv.setText("Denied");
            }

            if(pat.getAccessLevel().equals("R"))
            {
                showAccessLevel.setText("Read Only");
            }
            if(pat.getAccessLevel().equals("F"))
            {
                showAccessLevel.setText("Full Access");
            }

            patientName.setText(pat.getName());
            //specialityTv.setText(doc.getSpecialization());
            locationTv.setText(pat.getLocation());
            emailTv.setText(pat.getEmailID());
            mobileTv.setText(pat.getMobileNumber());

            if(pat.isSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(pat);

        }
        return convertView;
    }
}
