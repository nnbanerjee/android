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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Application.MyApi;
import Model.AddConfirmDeny;
import Model.AddConfirmDenyDelegate;
import Model.AddDelegateElement;
import Model.AddDependent;
import Model.AddDependentElement;
import Model.Delegation;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Mar-15.
 */
public class RequestDelegationAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Delegation> delegates;
    private LayoutInflater inflater;
    private ImageView imageShow;
    public MyApi api;
    private TextView nameTv,locationTv,emailTv,accessLevelTv,statusTv,showStatusTv,mobileNumberTv;
    private RadioButton readOnly,fullAccess;
    private Button accept,reject;
    private View bar;

    public RequestDelegationAdapter(Activity activity, ArrayList<Delegation> delegates)
    {
            this.activity = activity;
            this.delegates = delegates;
    }
    @Override
    public int getCount() {
        return delegates.size();
    }

    @Override
    public Object getItem(int position) {
        return delegates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String patientId = session.getString("sessionID",null);
        final String typeId = session.getString("type",null);


        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        final View convertView = inflater.inflate(R.layout.patient_delegation_request, null);

        final int pos = position;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);


        nameTv = (TextView)convertView.findViewById(R.id.first_name);
        locationTv = (TextView)convertView.findViewById(R.id.location);
        emailTv = (TextView)convertView.findViewById(R.id.show_email);
        accessLevelTv = (TextView)convertView.findViewById(R.id.show_access_delegate);
        statusTv = (TextView)convertView.findViewById(R.id.status_id_delegate);
        showStatusTv = (TextView)convertView.findViewById(R.id.show_status_delegate);
        mobileNumberTv = (TextView)convertView.findViewById(R.id.mobile_show);
        readOnly = (RadioButton)convertView.findViewById(R.id.read_only_delegate);
        fullAccess = (RadioButton)convertView.findViewById(R.id.full_access_delegate);
        bar = convertView.findViewById(R.id.show_bar);
        accept = (Button)convertView.findViewById(R.id.request_button_delegate);
        reject = (Button)convertView.findViewById(R.id.close_request_delegate);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeId;
                String id = delegates.get(pos).getId();
                final Delegation removeObject = delegates.get(pos);
                String typeDelegate = delegates.get(pos).getType();
                String status = "C";
                String accessString = readOnly.getText().toString();
                AddDelegateElement element = new AddDelegateElement();
                element.setId(id);
                element.setStatus(status);
                element.setType(typeDelegate);

                if(type.equals("Doctor"))
                {
                    type = "D";
                }

                if(type.equals("Assistant"))
                {
                    type = "A";
                }
                if(type.equals("Patient"))
                {
                    type = "P";
                }
                if(accessString.equals("Read Only"))
                {
                    element.setAccessLevel("R");
                }

                if(accessString.equals("Full Access"))
                {
                    element.setAccessLevel("F");
                }

                System.out.println("Element::::"+element.toString());

                ArrayList<AddDelegateElement> depends = new ArrayList<AddDelegateElement>();
                depends.add(element);
                AddConfirmDenyDelegate add = new AddConfirmDenyDelegate();
                add.setId(patientId);
                add.setType(type);
                add.setParents(depends);

                System.out.println("ID::::::"+patientId);
                System.out.println("Type::::::"+type);

                for(int i=0;i<depends.size();i++)
                {
                    System.out.println("ArrayList id:::"+depends.get(i).getId());
                    System.out.println("ArrayList status:::"+depends.get(i).getStatus());
                    System.out.println("ArrayList Type:::"+depends.get(i).getType());
                    System.out.println("ArrayList Access:::"+depends.get(i).getAccessLevel());
                }




                System.out.println("Confirm::::"+add.toString());
                System.out.println("Confirmation:::::"+depends.toArray());

                api.confirmOrDenyParentForDelegates(add,new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        int status = response.getStatus();

                        if(status == 200)
                        {
                            System.out.println("Confirmed............");
                            delegates.remove(removeObject);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                           error.printStackTrace();
                    }
                });

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = delegates.get(pos).getId();
                String type = typeId;
                final Delegation removeObject = delegates.get(pos);
                String typeDelegate = delegates.get(pos).getType();
                String status = "D";

                String accessString = readOnly.getText().toString();

                AddDelegateElement element = new AddDelegateElement();
                element.setId(id);
                element.setStatus(status);
                element.setType(typeDelegate);
                if(type.equals("Doctor"))
                {
                    type = "D";
                }

                if(type.equals("Assistant"))
                {
                    type = "A";
                }

                if(type.equals("Patient"))
                {
                    type = "P";
                }
                if(accessString.equals("Read Only"))
                {
                    element.setAccessLevel("R");
                }

                if(accessString.equals("Full Access"))
                {
                    element.setAccessLevel("F");
                }

                ArrayList<AddDelegateElement> depends = new ArrayList<AddDelegateElement>();
                depends.add(element);
                AddConfirmDenyDelegate add = new AddConfirmDenyDelegate();
                add.setId(patientId);
                add.setType(typeDelegate);
                add.setParents(depends);

                System.out.println("OBject Structure::::::::");
                System.out.println("id"+add.getId());
                System.out.println("type"+add.getType());
                System.out.println("Delegate Id"+element.getId());
                System.out.println("Delegate type"+element.getType());
                System.out.println("Delegate access"+element.getAccessLevel());
                System.out.println("Delegate status"+element.getStatus());
                api.confirmOrDenyParentForDelegates(add,new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        int status = response.getStatus();

                        if(status == 200)
                        {
                            System.out.println("Denied............");
                            delegates.remove(removeObject);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        });

        final Delegation del = delegates.get(pos);

        if(del.getAccessLevel().equals("R"))
        {
            readOnly.setChecked(true);
        }
        if(del.getAccessLevel().equals("F"))
        {
            fullAccess.setChecked(true);
        }

        if((del.getName()).equals("No Delegation Found"))
        {
            nameTv.setText("No Delegation Found");
            locationTv.setVisibility(View.INVISIBLE);
            emailTv.setVisibility(View.INVISIBLE);
            statusTv.setVisibility(View.INVISIBLE);
            showStatusTv.setVisibility(View.INVISIBLE);
            accessLevelTv.setVisibility(View.INVISIBLE);
            mobileNumberTv.setVisibility(View.INVISIBLE);
            readOnly.setVisibility(View.INVISIBLE);
            fullAccess.setVisibility(View.INVISIBLE);
            bar.setVisibility(View.INVISIBLE);
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        }
        else
        {
            nameTv.setText(del.getName());
            locationTv.setText(del.getLocation());
            emailTv.setText(del.getEmailID());
            mobileNumberTv.setText(del.getMobileNumber());
        }



        return convertView;
    }
}
