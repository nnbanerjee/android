package com.mindnerves.meidcaldiary.Fragments;

import android.app.ProgressDialog;
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

import com.medico.model.InvoiceDetails1;
import com.medico.view.ParentFragment;
import com.mindnerves.meidcaldiary.R;

import Model.InvoiceId;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInvoices extends ParentFragment {


    CheckBox shareWithPatient;
    EditText discountValue,taxValue,advanceValue,grandTotal,
            discountPercent,taxPercent,totalDueValue;
    TextView noDataFound,invoiceTotal;
    ProgressDialog progress;

    InvoiceDetails1 invoiceDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_invoices, container,false);
        shareWithPatient = (CheckBox)view.findViewById(R.id.share_with_patient);
        invoiceTotal = (TextView) view.findViewById(R.id.invoiceTotal);
        discountValue = (EditText) view.findViewById(R.id.discountValue);
        discountPercent = (EditText)view.findViewById(R.id.percentageDiscount);
        taxPercent = (EditText)view.findViewById(R.id.percentageTax);
        taxValue = (EditText) view.findViewById(R.id.taxValue);
        advanceValue = (EditText) view.findViewById(R.id.advanceValue);
        totalDueValue = (EditText) view.findViewById(R.id.totalDueValue);
        grandTotal = (EditText)view.findViewById(R.id.grandTotal);

//        shareWithPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Boolean check = shareWithPatient.isChecked();
//                api.saveShareWithPatientTotalInvoice(invoice,new Callback<Response>() {
//                    @Override
//                    public void success(Response response, Response response2) {
//                        int status = response.getStatus();
//                        if(status == 200)
//                        {
//                            if(share == 1) {
//                                Toast.makeText(getActivity(), "Data Shared With Patient", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(getActivity(), "Data Not Shared With Patient", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                    @Override
//                    public void failure(RetrofitError error) {
//                        error.printStackTrace();
//                    }
//                });
//            }
//        });


        taxPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String doubleString = taxPercent.getText().toString();
                if(doubleString.equals(""))
                {
//                    taxValue.setText("0.00");
//                    grandTotal.setText(""+grandTotalFinal);


                }
                else
                {
//                    tax = Double.parseDouble(doubleString);
//                    double subTotal = ((gTotalValue*tax)/100);
//                    taxValue.setText(""+subTotal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        discountPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String doubleString = discountPercent.getText().toString();
                if(doubleString.equals(""))
                {
//                    discountValue.setText("0.00");
//                    grandTotal.setText(""+grandTotalFinal);
                }
                else
                {
//                    Double discount = Double.parseDouble(doubleString);
//                    double subTotal = ((gTotalValue*discount)/100);
//                    discount = subTotal;
//                    discountValue.setText(""+subTotal);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        advanceValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String doubleString = advanceValue.getText().toString();
//                if(doubleString.equals("")){
//                    totalDueValue.setText(""+grandTotalFinal);
//                }else{
//                    if(doubleString!=null&& ! doubleString.equalsIgnoreCase("") && !doubleString.equalsIgnoreCase("null")) {
//                        totalDueValue.setText("" + grandTotalFinal);
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }



    public static Double isNumeric(String str){
        Double d = null;
        try{
            d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return null;
        }
        return d;
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        final Integer invoiceId = bundle.getInt(INVOICE_ID);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getPatientVisitInvoice1(new InvoiceId(invoiceId), new Callback<InvoiceDetails1>() {
            @Override
            public void success(InvoiceDetails1 details, Response response) {

                invoiceDetails = details;

                if (invoiceDetails != null && invoiceDetails.getInvoiceId() != null &&
                        invoiceDetails.getInvoiceId().intValue() > 0)
                {
                    invoiceTotal.setText(invoiceDetails.getTotal().toString());
                    discountPercent.setText(invoiceDetails.getDiscount().toString());
                    taxPercent.setText(invoiceDetails.getTax().toString());
                    grandTotal.setText(invoiceDetails.getGrandTotal().toString());
                    advanceValue.setText(invoiceDetails.getAdvance().toString());
                    totalDueValue.setText(new Double(invoiceDetails.getGrandTotal()-invoiceDetails.advance).toString());
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
//        ManagePatientProfile activity = (ManagePatientProfile)getActivity();
//        Bundle args = activity.getIntent().getExtras();
//        args.remove(TREATMENT_ID);
//        args.putInt(CUSTOM_TEMPLATE_CREATE_ACTIONS, CREATE_TREATMENT);
//        if(treatmentPlanModel != null && treatmentPlanModel.size() > 0)
//            args.putInt(INVOICE_ID, ((TreatmentPlan1)treatmentPlanModel.get(0)).getInvoiceId());
//        activity.getIntent().putExtras(args);
//        ParentFragment fragment = new CustomTemplateListView();
//        activity.fragmentList.add(fragment);
//        fragment.setArguments(args);
//        FragmentManager fragmentManger = activity.getFragmentManager();
//        fragmentManger.beginTransaction().add(R.id.service, fragment, "Treatment Plan").addToBackStack(null).commit();
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
