package com.medico.view.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.CustomProcedureTemplate1;
import com.medico.model.CustomTemplateId;
import com.medico.model.ResponseAddTemplates1;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.TreatmentId1;
import com.medico.model.TreatmentPlan1;
import com.medico.application.R;
import com.medico.view.ParentActivity;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorTreatmentPlanEditView extends ParentFragment {

    ProgressDialog progress;
    TreatmentPlan1 doctorNotesModel = new TreatmentPlan1();
    EditText name,description,date,currency,cost,discount,tax,total,notes;
    ImageView calenderImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.treatment_plan_edit, container,false);
        setHasOptionsMenu(true);
        name = (EditText)view.findViewById(R.id.name_value);
        description = (EditText) view.findViewById(R.id.description_value);
        date = (EditText) view.findViewById(R.id.date_value);
        currency = (EditText)view.findViewById(R.id.currency_value);
        cost = (EditText)view.findViewById(R.id.cost_value);
        discount = (EditText) view.findViewById(R.id.discount_value);
        tax = (EditText) view.findViewById(R.id.tax_value);
        total = (EditText)view.findViewById(R.id.total_value);
        notes = (EditText)view.findViewById(R.id.notes_value);
        calenderImg = (ImageView)view.findViewById(R.id.calenderImg);
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(date);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer treatmentId = bundle.getInt(TREATMENT_ID);
        Integer templateId = bundle.getInt(CUSTOM_TEMPLATE_ID);

        if(treatmentId != null && treatmentId.intValue() > 0)
        {

            api.getTreatmentPlan(new TreatmentId1(treatmentId), new Callback<TreatmentPlan1>() {
                @Override
                public void success(TreatmentPlan1 plan, Response response) {
                    //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    doctorNotesModel = plan;
                    if (doctorNotesModel != null && doctorNotesModel.getTreatmentId() != null)
                    {
                        if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_PROCEDURE)
                        {
                            name.setText(doctorNotesModel.getField(PROCEDURE_FIELD_NAME).value);
                            description.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DESCRIPTION).value);
                            date.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DATE).value);
                            currency.setText(doctorNotesModel.getField(PROCEDURE_FIELD_CURRENCY).value);
                            cost.setText(doctorNotesModel.getField(PROCEDURE_FIELD_COST).value);
                            discount.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DISCOUNT).value);
                            tax.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TAX).value);
                            total.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TOTAL).value);
                            notes.setText(doctorNotesModel.getField(PROCEDURE_FIELD_NOTES).value);
                        }
                        else if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_INVOICE) {
                            name.setText(doctorNotesModel.getField(INVOICE_FIELD_COST).value);
                            description.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DESCRIPTION).value);
                            date.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DATE).value);
                            currency.setText(doctorNotesModel.getField(PROCEDURE_FIELD_CURRENCY).value);
                            cost.setText(doctorNotesModel.getField(PROCEDURE_FIELD_COST).value);
                            discount.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DISCOUNT).value);
                            tax.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TAX).value);
                            total.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TOTAL).value);
                            notes.setText(doctorNotesModel.getField(PROCEDURE_FIELD_NOTES).value);
                        }
                    }
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }
        else if(templateId != null && templateId.intValue() > 0) {

            api.getCustomTemplate(new CustomTemplateId(templateId), new Callback<CustomProcedureTemplate1>() {
                @Override
                public void success(CustomProcedureTemplate1 plan, Response response) {
                    if (plan != null && plan.getTemplateId() != null)
                    {
                        doctorNotesModel = new TreatmentPlan1(plan);
                        if(plan.getCategoryId() == TEMPLATE_CATEGORY_PROCEDURE)
                        {
                            name.setText(doctorNotesModel.getField(PROCEDURE_FIELD_NAME).value);
                            description.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DESCRIPTION).value);
                            date.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DATE).value);
                            currency.setText(doctorNotesModel.getField(PROCEDURE_FIELD_CURRENCY).value);
                            cost.setText(doctorNotesModel.getField(PROCEDURE_FIELD_COST).value);
                            discount.setText(doctorNotesModel.getField(PROCEDURE_FIELD_DISCOUNT).value);
                            tax.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TAX).value);
                            total.setText(doctorNotesModel.getField(PROCEDURE_FIELD_TOTAL).value);
                            notes.setText(doctorNotesModel.getField(PROCEDURE_FIELD_NOTES).value);
                        }
                        else if(doctorNotesModel.getCategoryId() == TEMPLATE_CATEGORY_INVOICE)
                        {
                            name.setText(doctorNotesModel.getField(INVOICE_FIELD_NAME).value);
                            description.setText(doctorNotesModel.getField(INVOICE_FIELD_DESCRIPTION).value);
                            date.setText(doctorNotesModel.getField(INVOICE_FIELD_DATE).value);
                            currency.setText(doctorNotesModel.getField(INVOICE_FIELD_CURRENCY).value);
                            cost.setText(doctorNotesModel.getField(INVOICE_FIELD_COST).value);
                            discount.setText(doctorNotesModel.getField(INVOICE_FIELD_DISCOUNT).value);
                            tax.setText(doctorNotesModel.getField(INVOICE_FIELD_TAX).value);
                            total.setText(doctorNotesModel.getField(INVOICE_FIELD_TOTAL).value);
                            notes.setText(doctorNotesModel.getField(INVOICE_FIELD_NOTES).value);
                        }
                    }
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }
        else
        {

        }
    }

    public void saveDoctorNotesData(TreatmentPlan1 doctorNotesModel){

        if(doctorNotesModel.getTreatmentId() == null || doctorNotesModel.getTreatmentId().intValue() == 0)
        {
            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
            api.addPatientVisitTreatmentPlan1(doctorNotesModel, new Callback<ResponseAddTemplates1>() {
                @Override
                public void success(ResponseAddTemplates1 jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    ((ParentActivity) getActivity()).onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }
        else {

            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
            api.updatePatientVisitTreatmentPlan1(doctorNotesModel, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    ((ParentActivity) getActivity()).onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }
    }
    @Override
    public boolean isChanged()
    {
        return doctorNotesModel.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer treatmentId = bundle.getInt(TREATMENT_ID);
        Integer templateId = bundle.getInt(CUSTOM_TEMPLATE_ID);
        if(treatmentId != null && treatmentId.intValue() > 0) {
            if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_PROCEDURE)
            {
                doctorNotesModel.setField(PROCEDURE_FIELD_NAME, name.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DESCRIPTION, description.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_CURRENCY, currency.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DATE, date.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_COST, cost.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DISCOUNT, discount.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_TAX, tax.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_NOTES, notes.getText().toString());
            }
            else if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_INVOICE)
            {
                doctorNotesModel.setField(INVOICE_FIELD_NAME, name.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DESCRIPTION, description.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_CURRENCY, currency.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DATE, date.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_COST, cost.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DISCOUNT, discount.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_TAX, tax.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_NOTES, notes.getText().toString());
            }
        }
        else if(templateId != null && templateId.intValue() > 0)
        {
            doctorNotesModel.setDoctorId(bundle.getInt(DOCTOR_ID));
            doctorNotesModel.setPatientId(bundle.getInt(PATIENT_ID));
            doctorNotesModel.setAppointmentId(bundle.getInt(APPOINTMENT_ID));
            doctorNotesModel.setInvoiceId(bundle.getInt(INVOICE_ID));
            if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_PROCEDURE)
            {
                doctorNotesModel.setField(PROCEDURE_FIELD_NAME, name.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DESCRIPTION, description.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_CURRENCY, currency.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DATE, date.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_COST, cost.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_DISCOUNT, discount.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_TAX, tax.getText().toString());
                doctorNotesModel.setField(PROCEDURE_FIELD_NOTES, notes.getText().toString());
            }
            else if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_INVOICE)
            {
                doctorNotesModel.setField(INVOICE_FIELD_NAME, name.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DESCRIPTION, description.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_CURRENCY, currency.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DATE, date.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_COST, cost.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_DISCOUNT, discount.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_TAX, tax.getText().toString());
                doctorNotesModel.setField(INVOICE_FIELD_NOTES, notes.getText().toString());
            }
        }

    }
    @Override
    public boolean save()
    {
        if(doctorNotesModel.canBeSaved())
        {
            saveDoctorNotesData(doctorNotesModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return doctorNotesModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {
    }

    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                dateField.setText(format.format(date));
                if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_PROCEDURE)
                    doctorNotesModel.setField(PROCEDURE_FIELD_DATE, dateField.getText().toString());
                else if(doctorNotesModel.getCategoryId().intValue() == TEMPLATE_CATEGORY_INVOICE)
                    doctorNotesModel.setField(INVOICE_FIELD_COST, dateField.getText().toString());
            }

        };
        Date date = null;
        if(dateField.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            try {
                date = format.parse(dateField.getText().toString());
            }
            catch(ParseException e)
            {
                date = new Date();
            }

        }

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .build();
        pickerDialog.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                update();
                if (isChanged())
                {
                    if (canBeSaved())
                    {
                        save();
                        activity.onBackPressed();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                }

            }
            break;
            case R.id.home: {

            }
            break;

        }
        return true;
    }


}
