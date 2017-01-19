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

import Model.Delegation;
import Model.Patient;

/**
 * Created by MNT on 19-Mar-15.
 */
public class ShowDelegationAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Delegation> delegates;
    private LayoutInflater inflater;
    private TextView patientName,locationTv,emailTv,mobileTv,accessLevelTv,statusTv,showStatusTv,accessLevel,showAccessLevel;
    private ImageView imageShow;
    private View bar;
    private CheckBox checkBox;


    public ShowDelegationAdapter(Activity activity, ArrayList<Delegation> delegates) {

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
            convertView = inflater.inflate(R.layout.patient_delegation_confirmed, null);

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
        checkBox = (CheckBox)convertView.findViewById(R.id.check_delegate);
        final Delegation del = delegates.get(pos);

        /*checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                System.out.println("Check Status:::::"+cb.isChecked());
                Delegation del = (Delegation)cb.getTag();
                System.out.println("Delegation Object::::"+del.toString());

                if(cb.isChecked())
                {
                    del.setSelected(true);
                    System.out.println("Delelgation Object Value;:::"+del.getSelected());
                }
                else
                {
                    del.setSelected(false);
                }

            }
        });*/

        convertView.setTag(del);


        if((del.getName()).equals("No Delegation Found"))
        {
            patientName.setText("No Delegation Found");
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
            /*if(del.getStatus().equals("WC"))
            {
                showStatusTv.setText("Wait");
            }

            if(del.getStatus().equals("D"))
            {
                showStatusTv.setText("Denied");
            }

            if(del.getAccessLevel().equals("R"))
            {
                showAccessLevel.setText("Read Only");
            }
            if(del.getAccessLevel().equals("F"))
            {
                showAccessLevel.setText("Full Access");
            }

            patientName.setText(del.getName());
            //specialityTv.setText(doc.getSpecialization());
            locationTv.setText(del.getLocation());
            emailTv.setText(del.getEmailID());
            mobileTv.setText(del.getMobileNumber());

            if(del.getSelected())
            {
                checkBox.setChecked(true);
            }
            else
            {
                checkBox.setChecked(false);
            }
            checkBox.setTag(del);*/

        }
        return convertView;
    }
}
