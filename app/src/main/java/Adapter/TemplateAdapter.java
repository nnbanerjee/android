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
import java.util.List;

import Model.CustomProcedureTemplate;
import Model.ShowTemplate;
import Model.Template;

/**
 * Created by MNT on 28-Mar-15.
 */
public class TemplateAdapter extends BaseAdapter {

    private Activity activity;
    private  List<CustomProcedureTemplate> templateList;
    private LayoutInflater inflater;
    private TextView procedureTv,numberOfTemplate;
    private ImageView image;

    public TemplateAdapter(Activity activity, List<CustomProcedureTemplate> templateList)
    {
            this.activity = activity;
            this.templateList = templateList;

    }
    @Override
    public int getCount() {
        return templateList.size();
    }

    @Override
    public Object getItem(int position) {
        return templateList.get(position);
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
        System.out.println("Template Name:::"+templateList.get(position).getTemplateSubName());

        procedureTv = (TextView)convertView.findViewById(R.id.procedure);
        image = (ImageView)convertView.findViewById(R.id.show_image);
        numberOfTemplate = (TextView)convertView.findViewById(R.id.numberOfTemplate);

        numberOfTemplate.setVisibility(View.GONE);

        if(templateList.get(position).getTemplateName().equals("No Template Found")){
            procedureTv.setText("No Template Found");
            image.setVisibility(View.INVISIBLE);
        }
        else {
            procedureTv.setText(templateList.get(position).getTemplateSubName());
        }
        return convertView;
    }
}
