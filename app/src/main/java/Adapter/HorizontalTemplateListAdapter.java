package Adapter;

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

/**
 * Created by User on 14-03-2015.
 */
public class HorizontalTemplateListAdapter extends BaseAdapter {
    private Activity activity;
    private  List<Field> templates;
    SharedPreferences session;

    public HorizontalTemplateListAdapter(Activity activity, List<Field> templates) {
        this.activity = activity;
        this.templates = templates;
    }
    @Override
    public int getCount() {
        return templates.size();
    }
    @Override
    public Field getItem(int position) {
        return templates.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View gridItem = null;
        LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        gridItem = new View(activity);
        gridItem = inflater.inflate(R.layout.hr_template_item, null);

        TextView templateName = (TextView) gridItem.findViewById(R.id.templateName);
        TextView templateHeader = (TextView) gridItem.findViewById(R.id.templateHeader);

        templateHeader.setText(templates.get(position).getFieldDisplayName());
        templateName.setText(templates.get(position).getFieldDefaultValue());

        templateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println("templates = "+templates.get(position).getFieldDefaultValue());

                SharedPreferences.Editor editor = session.edit();
                //editor.putString("editableValue",templates.get(position).getFieldDefaultValue());
                editor.putString("editableFieldId",templates.get(position).getFieldId());
                editor.putString("editableTemplateId",templates.get(position).getTemplateId());
                editor.putString("editableFieldDisplayName",templates.get(position).getFieldDisplayName());
                editor.putString("editableFieldName",templates.get(position).getFieldName());
                editor.putString("editableFieldType",templates.get(position).getFieldType());
                editor.putString("editableFieldDefaultValue",templates.get(position).getFieldDefaultValue());

                editor.commit();

                ChangeFeildValueDialog adf = new ChangeFeildValueDialog();
                adf.show(activity.getFragmentManager(),"Dialog");


            }
        });

        return gridItem;
    }

}