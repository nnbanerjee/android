package com.medico.view;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.medico.adapter.TreatmentPlanListAdapter;
import com.medico.model.TreatmentPlan1;
import com.medico.model.TreatmentPlanRequest;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentTreatmentPlan extends ParentFragment {

    private CheckBox shareWithPatient;
    private ListView treatmentPlanList;
    private ProgressDialog progress;
    private List<TreatmentPlan1> treatmentPlanModel = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_treatment_plan, container,false);
        shareWithPatient = (CheckBox) view.findViewById(R.id.share_with_patient);
        treatmentPlanList = (ListView) view.findViewById(R.id.treatmentPlanList);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getPatientVisitTreatmentPlan1(new TreatmentPlanRequest(appointMentId, 1), new Callback<List<TreatmentPlan1>>() {
            @Override
            public void success(List<TreatmentPlan1> treatments, Response response) {

                treatmentPlanModel = treatments;

                if (treatmentPlanModel != null && treatmentPlanModel.size() > 0)
                {
                    treatmentPlanList.setAdapter(new TreatmentPlanListAdapter(getActivity(), treatmentPlanModel,loggedInUserId));
                }

                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                progress.dismiss();
                error.printStackTrace();
            }
        });

    }

    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    protected void update()
    {

    }
    @Override
    protected boolean save()
    {
        ManagePatientProfile activity = (ManagePatientProfile)getActivity();
        Bundle args = activity.getIntent().getExtras();
        args.remove(TREATMENT_ID);
        args.putInt(CUSTOM_TEMPLATE_CREATE_ACTIONS, CREATE_TREATMENT);
        if(treatmentPlanModel != null && treatmentPlanModel.size() > 0)
            args.putInt(INVOICE_ID, ((TreatmentPlan1)treatmentPlanModel.get(0)).getInvoiceId());
        activity.getIntent().putExtras(args);
        ParentFragment fragment = new CustomTemplateListView();
        activity.fragmentList.add(fragment);
        fragment.setArguments(args);
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, "Treatment Plan").addToBackStack(null).commit();
        return true;
    }
    @Override
    protected boolean canBeSaved()
    {
        return true;
    }
    @Override
    protected void setEditable(boolean editable) {
    }

}
