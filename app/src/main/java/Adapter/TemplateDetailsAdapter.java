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

import java.util.ArrayList;
import java.util.List;

import Model.Field;
import Model.TemplateField;

/**
 * Created by MNT on 28-Mar-15.
 */
public class TemplateDetailsAdapter extends BaseAdapter {

    private Activity activity;
    private  List<TemplateField> fieldList;
    private LayoutInflater inflater;
    private TextView fieldNameTv,fieldTypeTv;

    public TemplateDetailsAdapter(Activity activity,  List<TemplateField> fieldList){
        this.activity = activity;
        this.fieldList = fieldList;
    }
    @Override
    public int getCount() {
        return fieldList.size();
    }

    @Override
    public Object getItem(int position) {
        return fieldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.details_procedure_element, null);

        fieldNameTv = (TextView)convertView.findViewById(R.id.name);
        fieldTypeTv = (TextView)convertView.findViewById(R.id.type);

        convertView.setTag(fieldList);
        if(fieldList.get(position).getFieldName().equals("No Field Found")){
            fieldNameTv.setText("No Field Found");
            fieldTypeTv.setVisibility(View.INVISIBLE);
        }

        fieldNameTv.setText(fieldList.get(position).getFieldName());
        fieldTypeTv.setText(fieldList.get(position).getValue());
        return convertView;
    }
}
