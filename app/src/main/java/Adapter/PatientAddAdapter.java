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
import android.widget.TextView;
import android.widget.Toast;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import Application.MyApi;
import Model.DoctorSearchResponse;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 22-10-2015.
 */
public class PatientAddAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Patient> patients;
    private TextView firstNameTv;
    private TextView specialityTv;
    private TextView locationTv;
    private Button addPatient;
    private String doctorId;
    public MyApi api;
    private TextView bookOnline;
    private ImageView downImage;
    private LayoutInflater inflater;

    public PatientAddAdapter(Activity activity,ArrayList<Patient> patients){
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
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_element_add, null);
        final int pos = position;
        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        firstNameTv = (TextView) convertView.findViewById(R.id.first_name);
        specialityTv = (TextView) convertView.findViewById(R.id.speciality);
        locationTv = (TextView) convertView.findViewById(R.id.doctor_location);
        addPatient = (Button) convertView.findViewById(R.id.add_doctor_button);
        downImage = (ImageView)convertView.findViewById(R.id.doctor_down_image);
        bookOnline = (TextView) convertView.findViewById(R.id.book_online_appointment);
        final Patient patient = patients.get(pos);
        addPatient.setVisibility(View.GONE);
        bookOnline.setVisibility(View.GONE);
        specialityTv.setVisibility(View.GONE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        firstNameTv.setText(patient.getName());
        if ((patient.getName()).equals("No Patient Found")) {
            specialityTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);
            addPatient.setVisibility(View.INVISIBLE);
        }
        if (patient.isSelected()) {
            addPatient.setVisibility(View.VISIBLE);
            addPatient.setBackgroundResource(R.drawable.right_arrow);
        } else {
            addPatient.setVisibility(View.VISIBLE);
            addPatient.setBackgroundResource(R.drawable.add);
        }
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patient.isSelected()){

                }else{
                  api.addPatient(doctorId,patient.getPatientId(),new Callback<String>() {
                      @Override
                      public void success(String response, Response response2) {
                            notifyDataSetChanged();
                            patient.setSelected(true);
                            Toast.makeText(activity,"Patient Added Successfully!!!!!!",Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void failure(RetrofitError error) {
                          error.printStackTrace();
                          Toast.makeText(activity,"Fail",Toast.LENGTH_SHORT).show();
                      }
                  });
                }
            }
        });
        return convertView;
    }
}
