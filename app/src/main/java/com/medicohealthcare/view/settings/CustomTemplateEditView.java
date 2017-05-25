package com.medicohealthcare.view.settings;

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

import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.CustomProcedureTemplate1;
import com.medicohealthcare.model.CustomTemplateId;
import com.medicohealthcare.model.ResponseVm;
import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

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
public class CustomTemplateEditView extends ParentFragment {

    ProgressDialog progress;
    CustomProcedureTemplate1 customTemplateModel = new CustomProcedureTemplate1();
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
        Integer templateId = bundle.getInt(CUSTOM_TEMPLATE_ID);
        if(templateId != null && templateId.intValue() > 0) {

            api.getCustomTemplate(new CustomTemplateId(templateId), new Callback<CustomProcedureTemplate1>() {
                @Override
                public void success(CustomProcedureTemplate1 plan, Response response) {
                    //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    customTemplateModel = plan;
                    if (customTemplateModel != null && customTemplateModel.getTemplateId() != null)
                    {
                        if(customTemplateModel.getCategoryId() == TEMPLATE_CATEGORY_PROCEDURE)
                        {
                            name.setText(customTemplateModel.getField(PROCEDURE_FIELD_NAME).value);
                            description.setText(customTemplateModel.getField(PROCEDURE_FIELD_DESCRIPTION).value);
                            date.setText(customTemplateModel.getField(PROCEDURE_FIELD_DATE).value);
                            currency.setText(customTemplateModel.getField(PROCEDURE_FIELD_CURRENCY).value);
                            cost.setText(customTemplateModel.getField(PROCEDURE_FIELD_COST).value);
                            discount.setText(customTemplateModel.getField(PROCEDURE_FIELD_DISCOUNT).value);
                            tax.setText(customTemplateModel.getField(PROCEDURE_FIELD_TAX).value);
                            total.setText(customTemplateModel.getField(PROCEDURE_FIELD_TOTAL).value);
                            notes.setText(customTemplateModel.getField(PROCEDURE_FIELD_NOTES).value);
                        }
                        else if(customTemplateModel.getCategoryId() == TEMPLATE_CATEGORY_INVOICE)
                        {
                            name.setText(customTemplateModel.getField(INVOICE_FIELD_NAME).value);
                            description.setText(customTemplateModel.getField(INVOICE_FIELD_DESCRIPTION).value);
                            date.setText(customTemplateModel.getField(INVOICE_FIELD_DATE).value);
                            currency.setText(customTemplateModel.getField(INVOICE_FIELD_CURRENCY).value);
                            cost.setText(customTemplateModel.getField(INVOICE_FIELD_COST).value);
                            discount.setText(customTemplateModel.getField(INVOICE_FIELD_DISCOUNT).value);
                            tax.setText(customTemplateModel.getField(INVOICE_FIELD_TAX).value);
                            total.setText(customTemplateModel.getField(INVOICE_FIELD_TOTAL).value);
                            notes.setText(customTemplateModel.getField(INVOICE_FIELD_NOTES).value);
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

    public void saveDoctorNotesData(CustomProcedureTemplate1 customTemplateModel){

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.updateCustomTemplate(customTemplateModel, new Callback<ResponseVm>() {
            @Override
            public void success(ResponseVm jsonObject, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                progress.dismiss();
                ((ParentActivity)getActivity()).onBackPressed();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });
    }
    @Override
    public boolean isChanged()
    {
        return customTemplateModel.isChanged();
    }
    @Override
    public void update()
    {
        if(customTemplateModel.getCategoryId() == TEMPLATE_CATEGORY_PROCEDURE)
        {
            customTemplateModel.setField(PROCEDURE_FIELD_NAME,name.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_DESCRIPTION,description.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_CURRENCY,currency.getText().toString());
//            customTemplateModel.setField(PROCEDURE_FIELD_DATE,date.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_COST,cost.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_DISCOUNT,discount.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_TAX,tax.getText().toString());
            customTemplateModel.setField(PROCEDURE_FIELD_NOTES,notes.getText().toString());
            Double total = new Double(cost.getText().toString()).doubleValue() *
                    (1 - new Double(discount.getText().toString())/100)*(1+new Double(tax.getText().toString())/100);
            customTemplateModel.setField(PROCEDURE_FIELD_TOTAL,total.toString());
        }
        else if(customTemplateModel.getCategoryId() == TEMPLATE_CATEGORY_INVOICE)
        {
            customTemplateModel.setField(INVOICE_FIELD_NAME,name.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_DESCRIPTION,description.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_CURRENCY,currency.getText().toString());
//            customTemplateModel.setField(INVOICE_FIELD_DATE,date.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_COST,cost.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_DISCOUNT,discount.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_TAX,tax.getText().toString());
            customTemplateModel.setField(INVOICE_FIELD_NOTES,notes.getText().toString());
            Double total = new Double(cost.getText().toString()).doubleValue() *
                    (1 - new Double(discount.getText().toString())/100)*(1+new Double(tax.getText().toString())/100);
            customTemplateModel.setField(INVOICE_FIELD_TOTAL,total.toString());
        }
        Bundle bundle1 = getActivity().getIntent().getExtras();


    }
    @Override
    public boolean save()
    {
        if(customTemplateModel.canBeSaved())
        {
            saveDoctorNotesData(customTemplateModel);
            customTemplateModel.isChanged = false;
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return customTemplateModel.canBeSaved();
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
                customTemplateModel.setField(PROCEDURE_FIELD_DATE, new Long(date.getTime()).toString());
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
        inflater.inflate(R.menu.custom_template_edit, menu);
        MenuItem menuItem = menu.findItem(R.id.save_custom_template);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.save_custom_template: {
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
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return true;
            }

        }
        return false;
    }

}
