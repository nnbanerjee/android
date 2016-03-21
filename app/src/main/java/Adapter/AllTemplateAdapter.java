package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentFragment;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Model.AllTemplateVm;
import Model.ClinicDetailVm;
import Model.Field;
import Model.TreatmentField;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllTemplateAdapter extends ArrayAdapter<TreatmentField> {

    public List<TreatmentField> allTemplate;
    public final Activity context;
    public SharedPreferences session;

   public AllTemplateAdapter(Activity context, List<TreatmentField> allTemplate) {
        super(context, R.layout.hr_field_item, allTemplate);
        this.context = context;
        this.allTemplate = allTemplate;
   }

    static class ViewHolder {
        private HorizontalListView fieldList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int firstVisible;
        ViewHolder mHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.hr_field_item, null);
            mHolder = new ViewHolder();
            mHolder.fieldList = (HorizontalListView) convertView.findViewById(R.id.fieldList);

            convertView.setTag(mHolder);
            convertView.setTag(R.id.doctorName, mHolder.fieldList);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.fieldList.setTag(position);

       // System.out.println("allTemplate size = "+allTemplate.get(position).templates.size());
        List<TreatmentField> templates = new ArrayList<TreatmentField>();
        templates=allTemplate;
       /* templates.add(new TreatmentField("raj1", "raj2", "raj3", "raj4"));
        templates.add(new TreatmentField("dongare1", "dongare2", "dongare3", "dongare4"));
        templates.add(new TreatmentField("pune1", "pune2", "pune3", "pune4"));*/

        HorizontalTemplateListAdapter hrAdapter = new HorizontalTemplateListAdapter(context,templates);
        mHolder.fieldList.setAdapter(hrAdapter);

        //mHolder.horizontalTemplateList.setAdapter(hrAdapter);
        //fieldList
        //mHolder.doctorName.setText(allTemplate.get(position).getDoctorName());

        return convertView;
    }
}