package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.ClinicAllDoctorFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorDetailsFragment;
import com.mindnerves.meidcaldiary.Fragments.PatientAllAppointment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.Clinic;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 8/13/15.
 */
public class DoctorAddAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DoctorSearchResponse> doctorList;
    private TextView firstNameTv;
    private TextView specialityTv;
    private TextView locationTv;
    private Button addDoctor;
    private String patientId;
    public MyApi api;
    private TextView bookOnline;
    private ImageView downImage;

    public DoctorAddAdapter(Activity activity, List<DoctorSearchResponse> doctorList) {
        this.activity = activity;
        this.doctorList = doctorList;
    }

    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_element_add, null);
        final int pos = position;
        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        firstNameTv = (TextView) convertView.findViewById(R.id.first_name);
        specialityTv = (TextView) convertView.findViewById(R.id.speciality);
        locationTv = (TextView) convertView.findViewById(R.id.doctor_location);
        addDoctor = (Button) convertView.findViewById(R.id.add_doctor_button);
        downImage = (ImageView)convertView.findViewById(R.id.doctor_down_image);
        bookOnline = (TextView) convertView.findViewById(R.id.book_online_appointment);
        final DoctorSearchResponse doc = doctorList.get(position);
        addDoctor.setVisibility(View.GONE);
        bookOnline.setVisibility(View.GONE);
        if ((doc.getName()).equals("No Doctor Found")) {
            specialityTv.setVisibility(View.INVISIBLE);
            locationTv.setVisibility(View.INVISIBLE);
            addDoctor.setVisibility(View.INVISIBLE);

        }
        if (doc.isSelected()) {
            bookOnline.setVisibility(View.VISIBLE);
            addDoctor.setVisibility(View.VISIBLE);
            addDoctor.setBackgroundResource(R.drawable.right_arrow);
        } else {
            addDoctor.setVisibility(View.VISIBLE);
        }
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doc.isSelected()) {
                    saveDataToSharedPreference(doc);
                    Bundle bun = new Bundle();
                    bun.putString("fragment","doctorAdapter");
                    Fragment fragment = new PatientAllAppointment();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                } else {
                    String docId = doc.getDoctorId();
                    api.patientDoctorAdd(patientId, docId, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Toast.makeText(activity, "Doctor Added", Toast.LENGTH_SHORT).show();
                            doctorList.remove(doc);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doc.isSelected()){
                    saveDataToSharedPreference(doc);
                    Bundle args = new Bundle();
                    args.putString("doctor_email", doc.getEmailID());
                    args.putString("fragment","doctorAdapter");
                    Fragment fragment = new DoctorDetailsFragment();
                    fragment.setArguments(args);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });
        bookOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToSharedPreference(doctorList.get(position));
                Bundle args = new Bundle();
                args.putString("doctorId", doctorList.get(position).getDoctorId());
                args.putString("doctor_email", doctorList.get(position).getEmailID());
                args.putString("fragment", "doctorAdapter");
                Fragment fragment = new ClinicAllDoctorFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        firstNameTv.setText(doc.getName());
        specialityTv.setText(doc.getSpeciality());
        locationTv.setText(doc.getLocation());
        return convertView;
    }

    public void saveDataToSharedPreference(DoctorSearchResponse doctor) {
        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString("patient_DoctorName", doctor.getName());
        editor.putString("patient_DoctorId", doctor.getDoctorId());
        editor.putString("patient_doctor_email", doctor.getEmailID());
        editor.putString("patient_DoctorEmail", doctor.getEmailID());
        editor.commit();
        final Global global = (Global) activity.getApplicationContext();
        global.setDoctorSearchResponses(doctorList);
    }
}
