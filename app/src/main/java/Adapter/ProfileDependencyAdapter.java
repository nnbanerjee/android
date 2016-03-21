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

import Model.Dependent;
import Model.Patient;

/**
 * Created by User on 8/19/15.
 */
public class ProfileDependencyAdapter extends BaseAdapter {
    Activity activity;
    List<Dependent> depdendents;
    private LayoutInflater inflater;

    public ProfileDependencyAdapter(Activity activity,List<Dependent> dependents)
    {
        this.activity = activity;
        this.depdendents = dependents;
    }
    @Override
    public int getCount() {
        return depdendents.size();
    }

    @Override
    public Object getItem(int position) {
        return depdendents.get(position);
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
        final View convertView = inflater.inflate(R.layout.dependent_element_profile, null);
        TextView profileName = (TextView)convertView.findViewById(R.id.dependent_candidate);
        profileName.setText(depdendents.get(position).getName()+" - "+depdendents.get(position).getRelation());
        return convertView;
    }
}
