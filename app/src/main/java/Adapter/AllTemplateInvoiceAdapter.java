package Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.AllTemplateVm;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllTemplateInvoiceAdapter extends ArrayAdapter<AllTemplateVm> {

    public List<AllTemplateVm> allTemplate;
    public final Activity context;
    public SharedPreferences session;

   public AllTemplateInvoiceAdapter(Activity context, List<AllTemplateVm> allTemplate) {
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

        System.out.println("allTemplate size = "+allTemplate.get(position).templates.size());

        HorizontalInvoiceTemplateAdapter hrAdapter = new HorizontalInvoiceTemplateAdapter(context,allTemplate.get(position).templates);
        mHolder.fieldList.setAdapter(hrAdapter);

        //mHolder.horizontalTemplateList.setAdapter(hrAdapter);
        //fieldList
        //mHolder.doctorName.setText(allTemplate.get(position).getDoctorName());

        return convertView;
    }
}