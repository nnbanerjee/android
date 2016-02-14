package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AllProcedureAdapter;
import Adapter.HorizontalTemplateListAdapter;
import Application.MyApi;
import Model.AllTreatmentPlanVm;
import Model.Field;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class PatientAppointmentAllTreatmentPlan extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    CheckBox shareWith;
    String doctor_email,appointmentTime;
    Button addNewTreatment;
    ListView treatmentPlanList;
    TextView noDataFound;
    HorizontalListView fieldList1,fieldList;
    String appointmentDate,doctorId,patientId;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_all_treatment_plan, container,false);

        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("patient_doctor_email", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        patientId = session.getString("sessionID", null);
        fieldList1 = (HorizontalListView) view.findViewById(R.id.fieldList1);
        fieldList = (HorizontalListView) view.findViewById(R.id.fieldList1);
        shareWith = (CheckBox)view.findViewById(R.id.share_with_patient);
        noDataFound = (TextView) view.findViewById(R.id.noDataFound);
        treatmentPlanList = (ListView) view.findViewById(R.id.treatmentPlanList);
        addNewTreatment = (Button) view.findViewById(R.id.addNewTreatment);
        shareWith.setVisibility(View.GONE);

        addNewTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = session.edit();
                editor.putString("doctor_email_from_patient", doctorId);
                editor.commit();
                Fragment fragment = new PatientAppointmentManageProcedure();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }
        });

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        getAllTreamentPlan();

        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void getAllTreamentPlan(){
        api.getAllTreatmentPlan(doctorId, patientId, appointmentDate,appointmentTime, new Callback<AllTreatmentPlanVm>() {
            @Override
            public void success(AllTreatmentPlanVm allTreatmentPlanVm, Response response) {
                //System.out.println("allTreatmentPlanVm.procedure = "+allTreatmentPlanVm.procedure.size());
                //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                if(allTreatmentPlanVm == null){
                    treatmentPlanList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }else{
                    treatmentPlanList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);

                    AllProcedureAdapter allProcedureAdapter = new AllProcedureAdapter(getActivity(),allTreatmentPlanVm.procedure);
                    treatmentPlanList.setAdapter(allProcedureAdapter);

                    List<Field> templates = new ArrayList<Field>();
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","3asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","3fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","4asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","4fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","5asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","5fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","6asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","6fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","7asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","7fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","8asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","8fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","9asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","9fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","0asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","0fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","1asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","1fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","2asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","2fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","31asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","31fgjmfhhfghnv"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","32asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","62gjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","33asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","63fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","34asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","64gjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","35asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","65fgjmfhhfghnvb"));
                    templates.add(new Field("1asddffhhfg","2asdasdsfdghfgh","36asddfhgsghd","4asdfghfgdcxcv","5ffdhgdfgghfg","66sfgjmfhhfghnvb"));

                    HorizontalTemplateListAdapter hrAdapter = new HorizontalTemplateListAdapter(getActivity(),templates);
                    fieldList.setAdapter(hrAdapter);
                    fieldList1.setAdapter(hrAdapter);
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });

    }

    public void  goToBack(){
        Fragment fragment = new PatientAllAppointment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
}
