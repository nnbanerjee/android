package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentFragment;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Application.MyApi;
import Model.AllProcedureVm;
import Model.Clinic;
import Model.ClinicDetailVm;
import Model.InvoiceId;
import Model.ResponseCodeVerfication;
import Model.TreatmentField;
import Model.TreatmentId;
import Model.TreatmentPlan;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllProcedureAdapter extends ArrayAdapter<TreatmentPlan> {

    public List<TreatmentPlan> procedure;
    private final Activity context;
    public SharedPreferences session;
    MyApi api;
   /* View fieldView;
    HorizontalListView fieldList;*/
    List<TreatmentField> treatmentFieldList;
    HorizontalTemplateListAdapter hrAdapter;
    AllTemplateAdapter  allTemplateAdapter;
    // View horizontalView;
  //  HorizontalListView  horizontalListView;
    ViewHolder mHolder = null;

   public AllProcedureAdapter(Activity context, List<TreatmentPlan> procedure) {
        super(context, R.layout.all_procedure, procedure);
        this.context = context;
        this.procedure = procedure;
       RestAdapter restAdapter = new RestAdapter.Builder()
               .setEndpoint(context.getString(R.string.base_url))
               .setClient(new OkClient())
               .setLogLevel(RestAdapter.LogLevel.FULL)
               .build();
       api = restAdapter.create(MyApi.class);
   }

    static class ViewHolder {
        private TextView procedureName;
        private ListView allTemplateList;
        private ImageView delteTemplate,editTemplate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        treatmentFieldList= new ArrayList<TreatmentField>();

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.all_procedure, null);
          //  horizontalView = inflator.inflate(R.layout.hr_field_item, null);
            //  mHolder = new ViewHolder();
          //  horizontalListView = (HorizontalListView) convertView.findViewById(R.id.fieldList);

           /*fieldView= inflator.inflate(R.layout.hr_field_item, null);
            fieldList = (HorizontalListView) fieldView.findViewById(R.id.fieldList);*/
            mHolder = new ViewHolder();
            mHolder.procedureName = (TextView) convertView.findViewById(R.id.procedureName);
            mHolder.allTemplateList = (ListView) convertView.findViewById(R.id.allTemplateList);
            mHolder.delteTemplate = (ImageView)convertView.findViewById(R.id.delete_template);
            mHolder.editTemplate = (ImageView)convertView.findViewById(R.id.edit_template);

            convertView.setTag(mHolder);
            convertView.setTag(R.id.procedureName, mHolder.procedureName);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.procedureName.setTag(position);
        mHolder.procedureName.setText(procedure.get(position).getTemplateSubName());

       // System.out.println(" allTemplate size = " + procedure.get(position).getFieldArrayList().size());//.allTemplate.size());
        Integer listSize = 90;

         listSize = procedure.get(position).getTreatmentFields().size() * 90;




      /*  for(int i=0; i < procedure.get(position).getTreatmentValues().size();i++ ){

            List<TreatmentField> list=(List<TreatmentField>) procedure.get(position).getTreatmentValues().get(i);
            //System.out.println("Value = " + list.get());
            if(i==0 )
                hrAdapter = new HorizontalTemplateListAdapter(context,list,true);
            else
                hrAdapter = new HorizontalTemplateListAdapter(context,list,false);
          //  horizontalListView.setTag(position);
            horizontalListView.setAdapter(hrAdapter);
            horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    horizontalListView.setSelection(position);
                }
            });
        }*/


        /*for(int i=0; i<procedure.get(position).getTreatmentValues().size();i++)
            Log.i("","Procedure data-->getTreatmentValues().size() ->"+procedure.get(position).getTreatmentValues().size()) ;*/


        allTemplateAdapter = new AllTemplateAdapter(context, procedure.get(position).getTreatmentValues(),position);
        mHolder.allTemplateList.setAdapter(allTemplateAdapter);

        mHolder.editTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TreatmentPlan proc = procedure.get(position);
                System.out.println("Proc Name= " + proc.getTemplateSubName());//.procedureName);
                if(allTemplateAdapter.enableEditing)
                {
                    allTemplateAdapter.enableEditing(!allTemplateAdapter.enableEditing);
                    v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edit));
                    allTemplateAdapter.updateFieldTemplate();

                }else {
                    allTemplateAdapter.enableEditing(!allTemplateAdapter.enableEditing);
                    v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.upload));
                }
            }
        });


          // mHolder.allTemplateList.setLayoutParams(new RelativeLayout.LayoutParams(GridLayout.LayoutParams.FILL_PARENT, listSize));
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
                final TreatmentPlan proc = procedure.get(position);
                System.out.println("Proc Name= " + proc.getTemplateSubName());//.procedureName);
                api.removePatientVisitTreatmentPlan(new TreatmentId(proc.getTreatmentId()), new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication s, Response response) {
                        procedure.remove(proc);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
       /* HorizontalListView newList= new HorizontalListView(context,null);
          HorizontalTemplateListAdapter hrAdapter = new HorizontalTemplateListAdapter(context,procedure.get(position).getTreatmentFields());
         // mHolder.horizontalTemplateList.setAdapter(hrAdapter);
        newList.setAdapter(hrAdapter);*/

        return convertView;
    }

   /* public void updateFieldTemplate(){

        TreatmentPlan proc = procedure.get(position);
        treatmentPlan= UtilSingleInstance.getTreatmentPlan().get(globalPosition);
        treatmentPlan.getTreatmentValues().get(listposition).set(selectedPosition,treat);
        api.updatePatientVisitTreatmentPlan(treatmentPlan, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication responseCodeVerfication, Response response) {
                Toast.makeText(context, "Updated successfully !!!", Toast.LENGTH_LONG).show();
                //ChangeFeildValueDialog.this.getDialog().cancel();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }*/
}