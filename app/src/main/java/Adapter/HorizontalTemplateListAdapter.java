package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.ChangeFeildValueDialog;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Application.MyApi;

import com.medico.model.ResponseCodeVerfication;

import Model.TreatmentField;
import Model.TreatmentPlan;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 14-03-2015.
 */
public class HorizontalTemplateListAdapter extends BaseAdapter implements ChangeFeildValueDialog.Listener {
    private Activity activity;
    private List<TreatmentField> templates;
    SharedPreferences session;
    boolean firstLineColorFul;
    TextView templateHeader;
    HorizontalTemplateListAdapter horizontalTemplateListAdapter;
    int selectedPosition = 0;
    public MyApi api;
    int globalPosition;
    int listposition;
    String doctorId,patientId,appointmentId;

    public HorizontalTemplateListAdapter(Activity activity, List<TreatmentField> templates, boolean firstLineColorFul,int globalPosition,int listposition) {
        this.activity = activity;
        this.templates = templates;
        this.firstLineColorFul = firstLineColorFul;
        horizontalTemplateListAdapter = this;
        this.globalPosition=globalPosition;
                this.listposition=listposition;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
    }

    @Override
    public int getCount() {
        return templates.size();
    }

    @Override
    public TreatmentField getItem(int position) {
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
        doctorId = session.getString("sessionID", null);
        patientId = session.getString("patientId", null);
        appointmentId = session.getString("appointmentId", null);

        TextView templateName = (TextView) gridItem.findViewById(R.id.templateName);
        templateHeader = (TextView) gridItem.findViewById(R.id.templateHeader);
        RelativeLayout relative = (RelativeLayout) gridItem.findViewById(R.id.relative_main);


        if (firstLineColorFul) {
            relative.setBackgroundColor(activity.getResources().getColor(R.color.color_blue_default));
            templateHeader.setTextColor(activity.getResources().getColor(R.color.white));
            templateHeader.setText(templates.get(position).getFieldName());
        } else {
            relative.setBackgroundColor(activity.getResources().getColor(R.color.grey));
            templateHeader.setTextColor(activity.getResources().getColor(R.color.black));
            templateHeader.setText(templates.get(position).getValue());
        }
        templateHeader.setTag(position);
        // templateHeader.setText(templates.get(position).getFieldName());
        //templateName.setText(templates.get(position).getValue());

        templateHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPosition = (int) v.getTag();
                TreatmentField treat = templates.get(selectedPosition);


                SharedPreferences.Editor editor = session.edit();
                //editor.putString("editableValue",templates.get(position).getFieldDefaultValue());
                //  editor.putString("editableFieldId",templates.get(position).get);
                // editor.putString("editableTemplateId",templates.get(position).getTreatmentId());
                // editor.putString("editableFieldDisplayName",templates.get(position).getFieldName());
                // editor.putString("editableFieldName",templates.get(position).getFieldName());

                editor.putString("editableFieldType", "String");
                editor.putString("editableFieldValue", treat.getValue().toString());
                editor.putString("editableFieldDefaultValue", treat.getValue().toString());
                editor.commit();

                ChangeFeildValueDialog adf = new ChangeFeildValueDialog();
                // adf.setTargetFragment(activity,1);
                adf.setListener(horizontalTemplateListAdapter);
                adf.show(activity.getFragmentManager(), "Dialog");

            }
        });

        return gridItem;
    }

    @Override
    public void returnData(String result) {
        TreatmentField treat = templates.get(selectedPosition);

        treat.setValue(result);
        treat.setUpdated("Yes");
        this.notifyDataSetInvalidated();
        Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
      //  TreatmentPlan treatmentPlan = new TreatmentPlan();
      //  treatmentPlan= UtilSingleInstance.getTreatmentPlan().get(globalPosition);
      //  treatmentPlan.setTreatmentId(treat.getTreatmentId());
        List<TreatmentField> list=new ArrayList<TreatmentField>();
        list.add(treat);
     //   treatmentPlan.setTreatmentFields(list);
      //  treatmentPlan.setTreatmentValues(null);//we dont need to send these formatted values to server

        TreatmentPlan treatmentPlan= UtilSingleInstance.getTreatmentPlan().get(globalPosition);
        TreatmentField treats= treatmentPlan.getTreatmentValues().get(listposition).get(selectedPosition);
        treats.setValue(result);
        treats.setUpdated("Yes");

        horizontalTemplateListAdapter.notifyDataSetChanged();
        horizontalTemplateListAdapter.notifyDataSetInvalidated();





    }

    public void updateFieldTemplate(){

       TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan= UtilSingleInstance.getTreatmentPlan().get(globalPosition);
        TreatmentPlan newTreatmentPlan = new TreatmentPlan();
        newTreatmentPlan.setAppointmentId(appointmentId);
        newTreatmentPlan.setDoctorId(doctorId);
        newTreatmentPlan.setPatientId(patientId);
        newTreatmentPlan.setParentId("null");

        // treatmentPlan.setParentId();
        newTreatmentPlan.setCategoryId("1");//for procedure its 1
        newTreatmentPlan.setTemplateName(treatmentPlan.getTemplateName());
        newTreatmentPlan.setTemplateSubName(treatmentPlan.getTemplateSubName());

        List<TreatmentField> list=new ArrayList<TreatmentField>();
        for(int i=0; i< treatmentPlan.getTreatmentValues().size();i++){
           List<TreatmentField> listOfTreatments= treatmentPlan.getTreatmentValues().get(i);
            for(int j=0; j< listOfTreatments.size();j++) {
                TreatmentField treats = listOfTreatments.get(j);
                TreatmentField newTreatMent= new TreatmentField();
                if (treats.isUpdated()!=null && treats.isUpdated().equalsIgnoreCase("Yes")) {
                    newTreatMent.setFieldId(treats.getFieldId());
                    newTreatMent.setFieldName(treats.getFieldName());
                    newTreatMent.setValue(treats.getValue());
                    newTreatMent.setTreatmentId(treats.getTreatmentId());
                    newTreatMent.setTreatmentAttributeId(treats.getTreatmentAttributeId());
                    newTreatMent.setUpdated(null);
                    //set treatment ID
                    newTreatmentPlan.setTreatmentId(treats.getTreatmentId());

                list.add(newTreatMent);
            }
            }
        }


        newTreatmentPlan.setTreatmentFields(list);
        newTreatmentPlan.setTreatmentValues(null);


        api.updatePatientVisitTreatmentPlan(newTreatmentPlan, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication responseCodeVerfication, Response response) {
                Toast.makeText(activity, "Updated successfully !!!", Toast.LENGTH_LONG).show();
                //ChangeFeildValueDialog.this.getDialog().cancel();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}