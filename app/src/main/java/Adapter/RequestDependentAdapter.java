package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import com.medico.application.MyApi;
import Model.AddConfirmDeny;
import Model.AddDependentElement;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Mar-15.
 */
public class RequestDependentAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Patient> patients;
    private LayoutInflater inflater;
    private View bar;
    private TextView patientName,locationTv,emailTv,mobileTv,statusTv,showStatusTv,showAccessLevel;
    private Button request,reject;
    private ImageView imageShow;
    private RadioGroup radioGroup;
    private RadioButton readOnly,fullAccess;
    public MyApi api;

    public RequestDependentAdapter(Activity activity,ArrayList<Patient> patients)
    {
            this.activity = activity;
            this.patients = patients;
    }
    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String patientId = session.getString("sessionID",null);

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        final View convertView = inflater.inflate(R.layout.patient_dependent_request, null);

        final int pos = position;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);


        patientName = (TextView)convertView.findViewById(R.id.patient_first_name);
        locationTv = (TextView)convertView.findViewById(R.id.patient_location);
        emailTv = (TextView)convertView.findViewById(R.id.email_show);
        mobileTv = (TextView)convertView.findViewById(R.id.mobile_show);
        imageShow = (ImageView)convertView.findViewById(R.id.show_image);

        showAccessLevel = (TextView)convertView.findViewById(R.id.show_access);
        statusTv = (TextView)convertView.findViewById(R.id.show_status);
        showStatusTv = (TextView)convertView.findViewById(R.id.status_id);
        request = (Button)convertView.findViewById(R.id.request_button);
        reject =  (Button)convertView.findViewById(R.id.close_request);
        bar = convertView.findViewById(R.id.show_bar);
        radioGroup = (RadioGroup)convertView.findViewById(R.id.radio_access);
        readOnly = (RadioButton)convertView.findViewById(R.id.read_only);
        fullAccess = (RadioButton)convertView.findViewById(R.id.full_access);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int radioId = group.getCheckedRadioButtonId();
                readOnly = (RadioButton) convertView.findViewById(radioId);

            }
        });


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   final Patient removeObject = patients.get(pos);
                   String id = patients.get(pos).getPatientId();
                   System.out.println("Patient Id::::"+id);
                   String status = "C";
                   String accessString = readOnly.getText().toString();
                   AddDependentElement element = new AddDependentElement();
                   element.setId(id);
                   element.setStatus(status);

                   if(accessString.equals("Read Only"))
                   {
                       element.setAccessLevel("R");
                   }

                   if(accessString.equals("Full Access"))
                   {
                       element.setAccessLevel("F");
                   }

                   ArrayList<AddDependentElement> depends = new ArrayList<AddDependentElement>();
                   depends.add(element);
                   AddConfirmDeny add = new AddConfirmDeny();
                   add.setPatientId(patientId);
                   add.setParents(depends);

                   System.out.println("Add Object:::::::"+add);





                   api.confirmOrDenyParent(add,new Callback<Response>() {
                       @Override
                       public void success(Response response, Response response2) {

                           int status = response.getStatus();

                           if(status == 200)
                           {
                               System.out.println("Confirmed............");
                               patients.remove(removeObject);
                               notifyDataSetChanged();


                           }
                       }

                       @Override
                       public void failure(RetrofitError error) {

                           error.printStackTrace();
                           Toast.makeText(activity,error.toString(),Toast.LENGTH_SHORT).show();

                       }
                   });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Patient removeObject = patients.get(pos);
                String id = patients.get(pos).getPatientId();
                String status = "D";
                String accessString = readOnly.getText().toString();
                AddDependentElement element = new AddDependentElement();
                element.setId(id);
                element.setStatus(status);
                if(accessString.equals("Read Only"))
                {
                    element.setAccessLevel("R");
                }

                if(accessString.equals("Full Access"))
                {
                    element.setAccessLevel("F");
                }

                ArrayList<AddDependentElement> depends = new ArrayList<AddDependentElement>();
                depends.add(element);
                AddConfirmDeny add = new AddConfirmDeny();
                add.setPatientId(patientId);
                add.setParents(depends);

                api.confirmOrDenyParent(add,new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                        int status = response.getStatus();

                        if(status == 200)
                        {
                            System.out.println("Confirmed............");
                            patients.remove(removeObject);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });

        final Patient pat = patients.get(pos);

        if(pat.getAccessLevel().equals("R"));
        {
            readOnly.setChecked(true);
        }
        if(pat.getAccessLevel().equals("F"))
        {
            fullAccess.setChecked(true);
        }

        if((pat.getName()).equals("No Patient Found"))
        {
            patientName.setText("No Patient Found");
            emailTv.setVisibility(View.INVISIBLE);
            mobileTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);
            showAccessLevel.setVisibility(View.INVISIBLE);
            request.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
            readOnly.setVisibility(View.INVISIBLE);
            fullAccess.setVisibility(View.INVISIBLE);
            statusTv.setVisibility(View.INVISIBLE);
            showStatusTv.setVisibility(View.INVISIBLE);
            bar.setVisibility(View.INVISIBLE);

        }
        else
        {
            patientName.setText(pat.getName());
            //specialityTv.setText(doc.getSpecialization());
            locationTv.setText(pat.getLocation());
            emailTv.setText(pat.getEmailID());
            mobileTv.setText(pat.getMobileNumber());





        }

        return convertView;
    }
}
