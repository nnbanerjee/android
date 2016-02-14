package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.ChangeFeildValueDialog;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Field;

/**
 * Created by User on 14-03-2015.
 */
public class HorizontalInvoiceListAdapter extends BaseAdapter {
    private Activity activity;
    private  List<Field> templates;
    SharedPreferences session;

    public HorizontalInvoiceListAdapter(Activity activity, List<Field> templates) {
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



        return gridItem;
    }

}