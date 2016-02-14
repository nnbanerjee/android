package Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.AllProcedureVm;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllSingleProcedureAdapter extends ArrayAdapter<AllProcedureVm> {

    public List<AllProcedureVm> procedure;
    private final Activity context;
    public SharedPreferences session;


   public AllSingleProcedureAdapter(Activity context, List<AllProcedureVm> procedure) {
        super(context, R.layout.all_single_procedure, procedure);
        this.context = context;
        this.procedure = procedure;
   }

    static class ViewHolder {
        private TextView procedureName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.all_single_procedure, null);
            mHolder = new ViewHolder();
            mHolder.procedureName = (TextView) convertView.findViewById(R.id.procedureName);

            convertView.setTag(mHolder);
            convertView.setTag(R.id.procedureName, mHolder.procedureName);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.procedureName.setTag(position);
        mHolder.procedureName.setText(procedure.get(position).procedureName);

        return convertView;
    }
}