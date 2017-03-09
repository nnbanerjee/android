package Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import Model.AllProcedureVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllInvoiceAdapter extends ArrayAdapter<AllProcedureVm> {

    public List<AllProcedureVm> procedure;
    private final Activity context;
    public SharedPreferences session;
    MyApi api;

   public AllInvoiceAdapter(Activity context, List<AllProcedureVm> procedure) {
        super(context, R.layout.all_procedure, procedure);
        this.context = context;
        this.procedure = procedure;
   }

    static class ViewHolder {
        private TextView procedureName;
        private ListView allTemplateList;
        private ImageView delteTemplate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.all_procedure, null);
            mHolder = new ViewHolder();
            mHolder.procedureName = (TextView) convertView.findViewById(R.id.procedureName);
            mHolder.allTemplateList = (ListView) convertView.findViewById(R.id.allTemplateList);
            mHolder.delteTemplate = (ImageView)convertView.findViewById(R.id.delete_template);
            convertView.setTag(mHolder);
            convertView.setTag(R.id.procedureName, mHolder.procedureName);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        mHolder.procedureName.setTag(position);
        mHolder.procedureName.setText(procedure.get(position).procedureName);

        System.out.println(" allTemplate size = "+procedure.get(position).allTemplate.size());
        Integer listSize = 90;

        listSize = procedure.get(position).allTemplate.size() * 90;

        AllTemplateInvoiceAdapter  allTemplateAdapter = new AllTemplateInvoiceAdapter(context, procedure.get(position).allTemplate);
        mHolder.allTemplateList.setAdapter(allTemplateAdapter);

        //mHolder.allTemplateList.setLayoutParams(new RelativeLayout.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, listSize));
        mHolder.allTemplateList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        mHolder.delteTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AllProcedureVm proc = procedure.get(position);
                System.out.println("Proc Name= "+proc.procedureName);
                api.deleteInvoiceTemplate(proc,new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        procedure.remove(proc);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //HorizontalTemplateListAdapter hrAdapter = new HorizontalTemplateListAdapter(context,procedure.get(position).allTemplate);
        //mHolder.horizontalTemplateList.setAdapter(hrAdapter);

        return convertView;
    }
}