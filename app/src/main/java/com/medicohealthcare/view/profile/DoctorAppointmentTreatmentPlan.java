package com.medicohealthcare.view.profile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.medicohealthcare.adapter.TreatmentPlanListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.TreatmentPlan1;
import com.medicohealthcare.model.TreatmentPlanRequest;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.settings.CustomTemplateListView;

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
    public void onStart()
    {
        super.onStart();
        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        String tag = getTag();
        int type = 1;
        if(tag != null && tag.equals("invoice"))
            type = 2;
        api.getPatientVisitTreatmentPlan1(new TreatmentPlanRequest(appointMentId, type), new Callback<List<TreatmentPlan1>>() {
            @Override
            public void success(List<TreatmentPlan1> treatments, Response response) {

                treatmentPlanModel = treatments;

                if (treatmentPlanModel != null && treatmentPlanModel.size() > 0)
                {
                    Bundle bundle = getActivity().getIntent().getExtras();
                    bundle.putInt(INVOICE_ID,treatmentPlanModel.get(0).getInvoiceId());
                    getActivity().getIntent().putExtras(bundle);
                    treatmentPlanList.setAdapter(new TreatmentPlanListAdapter(getActivity(), treatmentPlanModel,loggedInUserId));
                }

                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        setTitle("Visit Details");
        if(getTag() == null )
            setHasOptionsMenu(true);
        else
            setHasOptionsMenu(false);
    }

    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    public void update()
    {

    }
    @Override
    public boolean save()
    {
        ParentActivity activity = (ParentActivity)getActivity();
        Bundle args = activity.getIntent().getExtras();
        args.remove(TREATMENT_ID);
        args.putInt(TREATMENT_ID,0);
        args.putInt(CUSTOM_TEMPLATE_CREATE_ACTIONS, CREATE_TREATMENT);
        if(treatmentPlanModel != null && treatmentPlanModel.size() > 0)
            args.putInt(INVOICE_ID, ((TreatmentPlan1)treatmentPlanModel.get(0)).getInvoiceId());
        activity.getIntent().putExtras(args);
        ParentFragment fragment = new CustomTemplateListView();
        setHasOptionsMenu(false);
        fragment.setArguments(args);
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, CustomTemplateListView.class.getName()).addToBackStack(CustomTemplateListView.class.getName()).commit();
        return true;
    }
    @Override
    public boolean canBeSaved()
    {
        return true;
    }
    @Override
    public void setEditable(boolean editable) {
    }
    @Override
    public void onResume() {
        super.onResume();
        setTitle("Visit Details");
        if(getTag() == null)
            setHasOptionsMenu(true);
        else
            setHasOptionsMenu(false);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.patient_visist_summary, menu);
        MenuItem menuItem = menu.findItem(R.id.save_summary);
        menuItem.setIcon(R.drawable.ic_add_white_24dp);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.save_summary:
            {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            case R.id.add_invoice:
            {
                ((DoctorAppointmentInvoices)fragment).addInvoice();
                return true;
            }
            case R.id.add_payment:
            {
                ((DoctorAppointmentInvoices)fragment).addPayment();
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return false;
            }
            default:
            {
                return false;
            }

        }
    }
}
