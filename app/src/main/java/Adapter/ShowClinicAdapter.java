package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.mindnerves.meidcaldiary.R;

import java.util.List;


import com.medico.model.Clinic;

/**
 * Created by MNT on 25-Feb-15.
 */
public class ShowClinicAdapter extends BaseAdapter {

    private Activity activity;
    private List<Clinic> clinicList;
    private LayoutInflater inflater;
    private TextView firstNameTv,locationTv;
    private CheckBox checkBox;

    public ShowClinicAdapter(Activity activity, List<Clinic> clinicList) {
        this.activity = activity;
        this.clinicList = clinicList;
    }
    @Override
    public int getCount() {
        return clinicList.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        View convertView = cv;
        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
            convertView = inflater.inflate(R.layout.basic_clinic_element, null);

        final int pos = position;

        firstNameTv = (TextView) convertView.findViewById(R.id.clinic_name);
        locationTv = (TextView) convertView.findViewById(R.id.clinic_location);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_clinic);
        checkBox.setVisibility(View.INVISIBLE);
        convertView.setTag(clinicList);
        final Clinic cl = clinicList.get(position);

        System.out.println("Value of Doc "+cl.isSelected());

        if((cl.getClinicName()).equals("No Clinic Found"))
        {
            checkBox.setVisibility(View.INVISIBLE);

            locationTv.setVisibility(View.INVISIBLE);

        }

        firstNameTv.setText(cl.getClinicName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(cl.getLocation());

        return convertView;
    }
}
