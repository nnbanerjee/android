package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Model.Patient;

/**
 * Created by MNT on 16-Mar-15.
 */
public class DependencyBasicAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Patient> patients;
    private LayoutInflater inflater;
    private Spinner accessLevelSpinner;
    private TextView nameTv,locationTv,emailTv,mobileTv,accessLevelTv,statusTv,showStatusTv;

    private ImageView imageShow;


    public DependencyBasicAdapter(Activity activity,ArrayList<Patient> patients)
    {
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

        nameTv = (TextView)convertView.findViewById(R.id.patient_first_name);
        locationTv = (TextView)convertView.findViewById(R.id.patient_location);
        emailTv = (TextView)convertView.findViewById(R.id.email_show);
        mobileTv = (TextView)convertView.findViewById(R.id.mobile_show);
        imageShow = (ImageView)convertView.findViewById(R.id.show_image);
        accessLevelTv = (TextView)convertView.findViewById(R.id.show_access);
        statusTv = (TextView)convertView.findViewById(R.id.show_status);
        showStatusTv = (TextView)convertView.findViewById(R.id.status_id);





        final Patient pat = patients.get(pos);

      /*  checkBox =  (CheckBox)convertView.findViewById(R.id.check_dependent);

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
        convertView.setTag(patients);*/

        final Patient doc = patients.get(position);

        if((doc.getName()).equals("No Patient Found"))
        {
         //   checkBox.setVisibility(View.INVISIBLE);

            locationTv.setVisibility(View.INVISIBLE);

        }
        else
        {
           /* if(doc.isSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(doc);*/
        }

        nameTv.setText(doc.getName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(doc.getLocation());


        return convertView;
    }
}
