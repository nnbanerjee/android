package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.ClinicFragment;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 21-10-2015.
 */
public class ClinicSpecialityAdapter extends BaseAdapter {
    Activity activity;
    List<Clinic> clinics;
    private LayoutInflater inflater;
    public SharedPreferences session;
    MyApi api;
    String doctorId = "";
    public ClinicSpecialityAdapter(Activity activity,List<Clinic> clinics){
        this.activity = activity;
        this.clinics = clinics;
    }
    @Override
    public int getCount() {
        return clinics.size();
    }

    @Override
    public Object getItem(int position) {
        return clinics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.patient_clinic_list_item, null);
        TextView clinicName = (TextView) convertView.findViewById(R.id.doctorName);
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        doctorId = session.getString("sessionID",null);
        ImageView doctorImg = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView clinicsSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        TextView clinicLocation = (TextView)convertView.findViewById(R.id.clinic_location);
        ImageView downImage = (ImageView)convertView.findViewById(R.id.downImg);
        Button rightArrow = (Button)convertView.findViewById(R.id.nextBtn);
        doctorImg.setBackgroundResource(R.drawable.clinic);
        clinicName.setText(clinics.get(position).getClinicName());
        clinicsSpeciality.setText(clinics.get(position).speciality);
        clinicLocation.setText(clinics.get(position).getLocation());
        final int pos = position;
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(clinics.get(position).isSelected()){
            rightArrow.setBackgroundResource(R.drawable.right_arrow);
        }else{
            rightArrow.setBackgroundResource(R.drawable.add);
        }
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click Plus arrow:::::::::::::"+clinics.get(position).isSelected());
                if(clinics.get(position).isSelected()){

                }else{
                    api.addDoctorClinic(doctorId,""+clinics.get(position).getIdClinic(),new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            Toast.makeText(activity,"Clinic Added Successfully!!!!!!",Toast.LENGTH_SHORT).show();
                            clinics.get(position).setSelected(true);
                            notifyDataSetChanged();
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
