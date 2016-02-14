package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.GetDelegate;
import Model.Patient;

/**
 * Created by MNT on 16-Mar-15.
 */
public class DelegateAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<GetDelegate> delegates;
    private LayoutInflater inflater;
    private TextView patientName,locationTv,emailTv,mobileTv;
    private CheckBox checkBox;
    private RadioButton readOnly;
    private RadioButton fullAccess;
    private RadioGroup access;




    public DelegateAdapter(Activity activity, ArrayList<GetDelegate> delegates)
    {
        this.activity = activity;
        this.delegates = delegates;
    }
    @Override
    public int getCount() {
        return delegates.size();
    }

    @Override
    public Object getItem(int position) {
        return delegates.get(position);
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
        convertView = inflater.inflate(R.layout.patient_delegation, null);

        final int pos = position;

        patientName = (TextView)convertView.findViewById(R.id.patient_first_name);
        locationTv = (TextView)convertView.findViewById(R.id.patient_location);
        emailTv = (TextView)convertView.findViewById(R.id.email_show);
        mobileTv = (TextView)convertView.findViewById(R.id.mobile_show);
        checkBox =  (CheckBox)convertView.findViewById(R.id.checkbox_patient);
        readOnly = (RadioButton)convertView.findViewById(R.id.read_only_id);
        access = (RadioGroup)convertView.findViewById(R.id.access_level1);
        fullAccess = (RadioButton)convertView.findViewById(R.id.full_access_id);

        final GetDelegate doc = delegates.get(position);



        readOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton cb = (RadioButton) v;

                System.out.println("I am here " + cb.isChecked());

                GetDelegate doc = (GetDelegate) cb.getTag();

                System.out.println("Patient ReadOnly::::: " + doc.getReadOnly());

                if(cb.isChecked())
                {
                    doc.setReadOnly(true);
                    doc.setAccessLevel("Read Only");
                    System.out.println("RadioButton Value:::::::"+cb.isChecked());
                }
                else
                {
                    doc.setAccessLevel("");
                    doc.setReadOnly(false);
                }



            }
        });

        fullAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton cb = (RadioButton) v;

                System.out.println("I am here " + cb.isChecked());

                GetDelegate doc = (GetDelegate) cb.getTag();

                System.out.println("Patient FullAccess::::: " + doc.getFullAccess());

                if(cb.isChecked())
                {
                    doc.setFullAccess(true);
                    doc.setAccessLevel("Full Access");
                    System.out.println("RadioButton Value:::::::"+cb.isChecked());
                }
                else
                {
                    doc.setAccessLevel("");
                    doc.setFullAccess(false);
                }




            }
        });



        checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
             CheckBox cb = (CheckBox) v;

             System.out.println("I am here " + cb.isChecked());

             GetDelegate doc = (GetDelegate) cb.getTag();

             System.out.println("Doc " + doc.toString());

             if (cb.isChecked()) {
                   doc.setSelected(true);
                    System.out.println("Doctor Object Value " + doc.getSelected());
             } else {
                 doc.setSelected(false);
             }

        }
         });

        convertView.setTag(delegates);

        System.out.println("Check Box:::::"+doc.getSelected());

        access.setTag(delegates);

        if((doc.getName()).equals("No Patient Found"))
        {
            checkBox.setVisibility(View.INVISIBLE);
            emailTv.setVisibility(View.INVISIBLE);
            mobileTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);
            readOnly.setVisibility(View.INVISIBLE);
            fullAccess.setVisibility(View.INVISIBLE);

        }
        else
        {
            if(doc.getSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(doc);

            if(doc.getReadOnly())
            {
                doc.setAccessLevel("Read Only");
            }
            else
            {
                doc.setAccessLevel("");
            }

            readOnly.setTag(doc);

            if(doc.getFullAccess())
            {
                System.out.println("Full Access :::::"+doc.getFullAccess());
               doc.setAccessLevel("Full Access");
            }
            else
            {
               doc.setAccessLevel("");
            }

            fullAccess.setTag(doc);

        }

        patientName.setText(doc.getName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(doc.getLocation());
        emailTv.setText(doc.getEmailID());
        mobileTv.setText(doc.getMobileNumber());


        return convertView;
    }


}
