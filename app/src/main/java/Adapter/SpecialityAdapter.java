package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

/**
 * Created by User on 8/18/15.
 */
public class SpecialityAdapter extends BaseAdapter {
    List<String> specialities;
    Activity activity;
    LayoutInflater inflater;

    public SpecialityAdapter(Activity activity,List<String> specialities)
    {
        this.activity = activity;
        this.specialities = specialities;
    }
    @Override
    public int getCount() {
        return specialities.size();
    }

    @Override
    public Object getItem(int position) {
        return specialities.get(position);
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
            convertView = inflater.inflate(R.layout.speciality_element, null);

        TextView specialityName = (TextView)convertView.findViewById(R.id.speciality_name);
        specialityName.setText(specialities.get(position));


        return convertView;
    }
}
