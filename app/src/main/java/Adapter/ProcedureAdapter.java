package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.ShowProcedure;
import Model.ShowTemplate;

/**
 * Created by MNT on 28-Mar-15.
 */
public class ProcedureAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ShowProcedure> procedureList;
    private LayoutInflater inflater;
    private TextView procedureTv,numberOfTemplate,procedureId;
    private ImageView image;

    public ProcedureAdapter(Activity activity, ArrayList<ShowProcedure> procedureList)
    {
            this.activity = activity;
            this.procedureList = procedureList;

    }
    @Override
    public int getCount() {
        return procedureList.size();
    }

    @Override
    public Object getItem(int position) {
        return procedureList.get(position);
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
            convertView = inflater.inflate(R.layout.template_element, null);

        procedureTv = (TextView)convertView.findViewById(R.id.procedure);
        image = (ImageView)convertView.findViewById(R.id.show_image);
        numberOfTemplate = (TextView)convertView.findViewById(R.id.numberOfTemplate);
        procedureId = (TextView)convertView.findViewById(R.id.procedureId);

        procedureTv.setText(procedureList.get(position).getProcedureName());
        procedureId.setText(procedureList.get(position).getId());
        if(procedureList.get(position).getNumberOfTemplate().equals("0")){
            numberOfTemplate.setVisibility(View.GONE);
        }else{
            numberOfTemplate.setText(procedureList.get(position).getNumberOfTemplate());
        }

        return convertView;
    }
}
