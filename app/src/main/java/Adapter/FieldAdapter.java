package Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import Model.Field;

/**
 * Created by MNT on 28-Mar-15.
 */
public class FieldAdapter extends ArrayAdapter<Field> {

    private Activity activity;
    private ArrayList<Field> fieldList;
    Calendar calendar = Calendar.getInstance();
    TextView displayCalender = null;
    Global global;

    public FieldAdapter(Activity activity,ArrayList<Field> fieldList) {
        super(activity, R.layout.procedure_element, fieldList);
        this.activity = activity;
        this.fieldList = fieldList;
        global = (Global) activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return fieldList.size();
    }

    @Override
    public Field getItem(int position) {
        return fieldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView displayName,systemName,type;
        EditText defaultValue;
        CheckBox selectField;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        //Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.procedure_element, null);

            holder = new ViewHolder();
            holder.defaultValue = (EditText) convertView.findViewById(R.id.defaultValue);
            holder.displayName = (TextView) convertView.findViewById(R.id.displayName);
            holder.systemName = (TextView) convertView.findViewById(R.id.systemName);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.selectField = (CheckBox) convertView.findViewById(R.id.selectField);

            holder.selectField.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        System.out.println(" selectField is selected !!!! ");
                        boolean checkfield = false;
                        for( Field field : global.removeFieldList){
                            if(field.getFieldId().equals(fieldList.get(position).getFieldId())){
                                checkfield = true;
                            }
                        }
                        if(!checkfield){
                            global.removeFieldList.add(fieldList.get(position));
                        }

                    }else{
                        for( Field field : global.removeFieldList){
                            if(field.getFieldId().equals(fieldList.get(position).getFieldId())){
                                global.removeFieldList.remove(field);
                            }
                        }

                        System.out.println(" selectField is not selected !!!! ");
                    }
                }
            });

            if(fieldList.get(position).getFieldType().equals("Date")){
                displayCalender = holder.defaultValue;
                holder.defaultValue.setText(new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
            }

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.defaultValue.setText(fieldList.get(position).getFieldDefaultValue());
        holder.displayName.setText(fieldList.get(position).getFieldDisplayName());
        holder.systemName.setText(fieldList.get(position).getFieldName());
        holder.type.setText(fieldList.get(position).getFieldType());

        TextWatcher displayNameWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldList.get(position).setFieldDisplayName(s.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        };

        TextWatcher defaultValueWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldList.get(position).setFieldDefaultValue(s.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        };

        holder.displayName.addTextChangedListener(displayNameWatcher);
        holder.defaultValue.addTextChangedListener(defaultValueWatcher);

        if(fieldList.get(position).getFieldType().equals("Numeric") || fieldList.get(position).getFieldType().equals("%")){
            holder.defaultValue.setInputType(InputType.TYPE_CLASS_PHONE);
        }

        return convertView;

    }

    public void setDate(){
        new DatePickerDialog(activity,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){




        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate();
        }

    };

    public void updatedate(){
        displayCalender.setText( calendar.get(Calendar.DAY_OF_MONTH) + "-" + showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.YEAR) );
    }

    public int showMonth(int month){
        int showMonth = month;
        switch(showMonth){
            case 0:
                showMonth = showMonth + 1;
                break;
            case 1:
                showMonth = showMonth + 1;
                break;
            case 2:
                showMonth = showMonth + 1;
                break;
            case 3:
                showMonth = showMonth + 1;
                break;
            case 4:
                showMonth = showMonth + 1;
                break;
            case 5:
                showMonth = showMonth + 1;
                break;
            case 6:
                showMonth = showMonth + 1;
                break;
            case 7:
                showMonth = showMonth + 1;
                break;
            case 8:
                showMonth = showMonth + 1;
                break;
            case 9:
                showMonth = showMonth + 1;
                break;
            case 10:
                showMonth = showMonth + 1;
                break;
            case 11:
                showMonth = showMonth + 1;
                break;

        }
        return showMonth;
    }

}
