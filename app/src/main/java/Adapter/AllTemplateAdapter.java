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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentFragment;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.AllTemplateVm;
import Model.ClinicDetailVm;
import Model.Field;
import Model.TreatmenTvalueForHorizontalView;
import Model.TreatmentField;
import Model.TreatmentPlan;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.AddNewProcedureDialog;
import com.mindnerves.meidcaldiary.Fragments.ChangeFeildValueDialog;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Field;
import Model.TreatmentField;

/**
 * Created by User on 14-03-2015.
 */
public class AllTemplateAdapter extends BaseAdapter {
    private Activity activity;
    private List<List<TreatmentField>> templates;
    SharedPreferences session;
    HorizontalTemplateListAdapter hrAdapter;
    HorizontalListView fieldList;
    ViewHolder mHolder = null;
    boolean enableEditing=false;
    int procedureOrInvoiceGlobalPosition,horizontListPosition;
    public AllTemplateAdapter(Activity activity, List<List<TreatmentField>> templates,int position) {
        this.activity = activity;
        this.templates = templates;
        this.procedureOrInvoiceGlobalPosition=position;


    }

    @Override
    public int getCount() {
        if(this.templates!=null)
        return this.templates.size();
        else return 0;
    }

    public boolean enableEditing(boolean value){
        enableEditing=value;
        return enableEditing;
    }
    @Override
    public List<TreatmentField> getItem(int position) {
        return templates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {

        private HorizontalListView fieldList;
    }
    public void updateFieldTemplate(){
        if(hrAdapter!=null)
        hrAdapter.updateFieldTemplate();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int firstVisible;

        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            convertView = inflator.inflate(R.layout.hr_field_item, null);
            mHolder = new ViewHolder();
            mHolder.fieldList = (HorizontalListView) convertView.findViewById(R.id.fieldList);

             convertView.setTag(mHolder);
           // convertView.setTag(R.id.doctorName, mHolder.fieldList);

        } else {
             mHolder = (ViewHolder) convertView.getTag();
        }

      //  ArrayList<String> wordList = new ArrayList<String>(Arrays.asList(templates.get(position).getValues()));
        List<TreatmentField> list=(List<TreatmentField>)templates.get(position);
        if(position==0 )
        hrAdapter = new HorizontalTemplateListAdapter(activity,list,true,procedureOrInvoiceGlobalPosition,position);
        else
        hrAdapter = new HorizontalTemplateListAdapter(activity,list,false,procedureOrInvoiceGlobalPosition,position);
       // mHolder.fieldList.setTag(position);
        mHolder.fieldList.setAdapter(hrAdapter);
        mHolder.fieldList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHolder.fieldList.setSelection(position);
            }
        });
       /* fieldList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
*/

        return convertView;
    }

}
