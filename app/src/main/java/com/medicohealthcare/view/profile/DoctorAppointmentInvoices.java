package com.medicohealthcare.view.profile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.InvoiceDetails1;
import com.medicohealthcare.model.Payment;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.settings.CustomTemplateListView;

import java.math.BigDecimal;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInvoices extends ParentFragment {


    CheckBox shareWithPatient;
    EditText discountValue,taxValue,advanceValue,grandTotal,other_charges,other_value,
            discountPercent,taxPercent,totalDueValue;
    TextView noDataFound,invoiceTotal;
    DoctorAppointmentTreatmentPlan treatmentPlan,invoice;
    InvoiceDetails1 invoiceDetails;

    private final int minDelta = 300;           // threshold in ms
    private long focusTime = 0;                 // time of last touch
    private View focusTarget = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_invoices, container,false);
        shareWithPatient = (CheckBox)view.findViewById(R.id.share_with_patient);
        invoiceTotal = (TextView) view.findViewById(R.id.invoiceTotal);
        discountValue = (EditText) view.findViewById(R.id.discountValue);
        discountPercent = (EditText)view.findViewById(R.id.percentageDiscount);
        other_charges = (EditText)view.findViewById(R.id.other_charges);
        other_value = (EditText)view.findViewById(R.id.other_value);
        taxPercent = (EditText)view.findViewById(R.id.percentageTax);
        taxValue = (EditText) view.findViewById(R.id.taxValue);
        advanceValue = (EditText) view.findViewById(R.id.advanceValue);
        totalDueValue = (EditText) view.findViewById(R.id.totalDueValue);
        invoice = (DoctorAppointmentTreatmentPlan) getActivity().getFragmentManager().findFragmentById(R.id.invoice_list);
        grandTotal = (EditText)view.findViewById(R.id.grandTotal);
        taxPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                Double tax;
                if(s.toString().trim().length() == 0)
                {
                    tax = 0.00;
                    taxPercent.setText(tax.toString());
                }
                else if((tax = Double.parseDouble(s.toString())) > 100.00)
                {
                   tax = 100.00;
                    taxPercent.setText(tax.toString());
                }
                invoiceDetails.setTax(tax);
                taxValue.setText(invoiceDetails.calculateTaxValue().toString());
                grandTotal.setText(invoiceDetails.calculateGrandTotal().toString());
                totalDueValue.setText(invoiceDetails.calculateDues().toString());
            }
        });
        discountPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                Double discount;
                if(s.toString().trim().length() == 0)
                {
                    discount = 0.00;
                    discountPercent.setText(discount.toString());
                }
                else if((discount = Double.parseDouble(s.toString())) > 100.00)
                {
                    discount = 100.00;
                    discountPercent.setText(discount.toString());
                }
                invoiceDetails.setDiscount(discount);
                discountValue.setText(invoiceDetails.calculateDiscountValue().toString());
                taxValue.setText(invoiceDetails.calculateTaxValue().toString());
                grandTotal.setText(invoiceDetails.calculateGrandTotal().toString());
                totalDueValue.setText(invoiceDetails.calculateDues().toString());

            }
        });
        other_charges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                Double other;
                if(s.toString().trim().length() == 0)
                {
                    other = 0.00;
                    other_charges.setText(other.toString());
                }
                else
                    other = Double.parseDouble(s.toString());
                invoiceDetails.setOtherCharges(other);
                other_value.setText(other.toString());
                discountValue.setText(invoiceDetails.calculateDiscountValue().toString());
                taxValue.setText(invoiceDetails.calculateTaxValue().toString());
                grandTotal.setText(invoiceDetails.calculateGrandTotal().toString());
                totalDueValue.setText(invoiceDetails.calculateDues().toString());
            }
        });
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                long t = System.currentTimeMillis();
                long delta = t - focusTime;
                if (hasFocus) {     // gained focus
                    if (delta > minDelta) {
                        focusTime = t;
                        focusTarget = view;
                    }
                }
                else {              // lost focus
                    if (delta <= minDelta  &&  view == focusTarget) {
                        focusTarget.post(new Runnable() {   // reset focus to target
                            public void run() {
                                focusTarget.requestFocus();
                            }
                        });
                    }
                }
            }
        };
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
        if(invoice != null)
            invoice.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        final Integer invoiceId = bundle.getInt(INVOICE_ID);
        api.getPatientVisitInvoice1(new AppointmentId1(appointMentId), new Callback<InvoiceDetails1>() {
            @Override
            public void success(InvoiceDetails1 details, Response response) {

                invoiceDetails = details;

                if (invoiceDetails != null && invoiceDetails.getInvoiceId() != null &&
                        invoiceDetails.getInvoiceId().intValue() > 0)
                {
                    invoiceTotal.setText(invoiceDetails.getTotal().toString());
                    other_charges.setText(invoiceDetails.getOtherCharges().toString());
                    other_value.setText(invoiceDetails.getOtherCharges().toString());
                    taxPercent.setText(invoiceDetails.getTax().toString());
                    taxValue.setText(invoiceDetails.calculateTaxValue().toString());
                    discountPercent.setText(invoiceDetails.getDiscount().toString());
                    discountValue.setText(invoiceDetails.calculateDiscountValue().toString());
                    grandTotal.setText(invoiceDetails.calculateGrandTotal().toString());
                    advanceValue.setText(invoiceDetails.getAdvance().toString());
                    totalDueValue.setText(invoiceDetails.calculateDues().toString());
                }

                hideBusy();
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });

    }

    public void saveInvoice(InvoiceDetails1 invoice){
        final ParentActivity activity = (ParentActivity)getActivity();
        showBusy();
        api.updatePatientVisitInvoiceDetails(invoice, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication jsonObject, Response response) {
                Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
    }

    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    public void update()
    {
        invoiceDetails.setDiscount(new Double(discountPercent.getText().toString()));
        invoiceDetails.setTax(new Double(taxPercent.getText().toString()));
        invoiceDetails.setOtherCharges(new Double(other_charges.getText().toString()));
    }
    @Override
    public boolean save()
    {
        saveInvoice(invoiceDetails);
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

    public void addInvoice()
    {
        ParentActivity activity = (ParentActivity)getActivity();
        Bundle args = activity.getIntent().getExtras();
        args.remove(TREATMENT_ID);
        args.putInt(TREATMENT_ID,0);
        args.putInt(CUSTOM_TEMPLATE_CREATE_ACTIONS, CREATE_INVOICE);
        if(invoiceDetails != null && invoiceDetails.getInvoiceId().intValue() > 0)
            args.putInt(INVOICE_ID, invoiceDetails.getInvoiceId());
        else
            args.putInt(INVOICE_ID, 0);
        activity.getIntent().putExtras(args);
        ParentFragment fragment = new CustomTemplateListView();
//        activity.attachFragment(fragment);
        fragment.setArguments(args);
        FragmentManager fragmentManger = activity.getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, CustomTemplateListView.class.getName()).addToBackStack(CustomTemplateListView.class.getName()).commit();
    }
    public void addPayment()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.payment_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        final String country = bundle.getString(COUNTRY_NAME);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                if(userInput.getText().length() > 0)
                                {
                                    showBusy();
                                    BigDecimal amount = new BigDecimal(Double.parseDouble(userInput.getText().toString()));
                                    Payment payment = new Payment(amount, new Date().getTime(), new Integer(1).byteValue(),
                                            new Integer(1).byteValue(), "Doctor's Visit",country,
                                            invoiceDetails.invoiceId,invoiceDetails.patientId , invoiceDetails.doctorId, invoiceDetails.patientId);

                                    api.addPayment(payment, new Callback<ResponseCodeVerfication>()
                                    {
                                        @Override
                                        public void success(ResponseCodeVerfication jsonObject, Response response)
                                        {
                                            fragment.onStart();
                                            Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                                            hideBusy();
                                        }

                                        @Override
                                        public void failure(RetrofitError error)
                                        {
                                            hideBusy();
                                            new MedicoCustomErrorHandler(getActivity()).handleError(error);
                                        }
                                    });
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

}
