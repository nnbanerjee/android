package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import Adapter.AllInvoiceAdapter;
import Adapter.AllProcedureAdapter;
import Adapter.AllSingleProcedureAdapter;
import Adapter.HorizontalInvoiceListAdapter;
import Adapter.HorizontalTemplateListAdapter;
import Application.MyApi;
import Model.AllProcedureVm;
import Model.AllTemplateVm;
import Model.AllTreatmentPlanVm;
import Model.Field;
import Model.TotalInvoice;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentInvoices extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    MultiAutoCompleteTextView symptomsValue,diagnosisValue;
    EditText doctorNotes;
    String doctor_email,appointmentTime,currencyType;
    Button addNewInvoices,save;
    CheckBox shareWithPatient;
    ListView invoicesList;
    Double gTotalValue  = 0d;
    EditText discountValue,taxValue,advanceValue,grandTotal,
            discountPercent,taxPercent,totalDueValue;
    TextView noDataFound,invoiceTotal;
    String appointmentDate,patientEmail;
    String doctorId,patientId;
    Double discount =0.0,tax=0.0,advance=0.0,grandTotalFinal=0.0;
    int share = 0;
    HorizontalListView fieldList1,fieldList;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_invoices, container,false);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        patientId = session.getString("doctor_patientEmail", null);
        System.out.println("PatientId::::: "+patientId);
        patientEmail = session.getString("doctor_patientEmail", null);
        appointmentDate = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        shareWithPatient = (CheckBox)view.findViewById(R.id.share_with_patient);
        invoiceTotal = (TextView) view.findViewById(R.id.invoiceTotal);
        discountValue = (EditText) view.findViewById(R.id.discountValue);
        discountPercent = (EditText)view.findViewById(R.id.percentageDiscount);
        taxPercent = (EditText)view.findViewById(R.id.percentageTax);
        taxValue = (EditText) view.findViewById(R.id.taxValue);
        advanceValue = (EditText) view.findViewById(R.id.advanceValue);
        totalDueValue = (EditText) view.findViewById(R.id.totalDueValue);
        grandTotal = (EditText) view.findViewById(R.id.grandTotal);
        noDataFound = (TextView) view.findViewById(R.id.noDataFound);
        invoicesList = (ListView) view.findViewById(R.id.invoicesList);
        addNewInvoices = (Button) view.findViewById(R.id.addNewInvoices);
        save = (Button)view.findViewById(R.id.saveGrandTotal);
        fieldList1 = (HorizontalListView) view.findViewById(R.id.fieldList1);
        fieldList = (HorizontalListView) view.findViewById(R.id.fieldList1);

        shareWithPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean check = shareWithPatient.isChecked();
                System.out.println("Status:::::"+check);
                System.out.println("PatientId:::::"+patientId);
                TotalInvoice invoice = new TotalInvoice();
                invoice.setAppointmentDate(appointmentDate);
                invoice.setAppointmentTime(appointmentTime);
                invoice.setDoctorId(doctorId);
                invoice.setPatientId(patientId);
                invoice.setGrandTotal(invoiceTotal.getText().toString());
                invoice.setDiscount(discountValue.getText().toString());
                invoice.setTaxValue(taxValue.getText().toString());
                if(check)
                {
                    invoice.setShareWithPatient(1);
                    share = 1;
                }
                else
                {
                    invoice.setShareWithPatient(0);
                    share = 0;
                }
                api.saveShareWithPatientTotalInvoice(invoice,new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        int status = response.getStatus();
                        if(status == 200)
                        {
                            if(share == 1) {
                                Toast.makeText(getActivity(), "Data Shared With Patient", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Data Not Shared With Patient", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalInvoice invoice = new TotalInvoice();
                invoice.setDoctorId(doctorId);
                invoice.setPatientId(patientId);
                invoice.setGrandTotal(""+grandTotal.getText().toString());
                invoice.setShareWithPatient(share);
                invoice.setAppointmentDate(appointmentDate);
                invoice.setAppointmentTime(appointmentTime);
                invoice.setTaxValue(""+tax);
                invoice.setDiscount(""+discount);
                invoice.setTotal(""+invoiceTotal.getText().toString());
                invoice.setPercentageDiscount(discountPercent.getText().toString());
                invoice.setPercentageTax(taxPercent.getText().toString());
                invoice.setTotalDue(""+grandTotalFinal);
                invoice.setAdvance(""+advance);

                api.saveTotalInvoice(invoice,new Callback<TotalInvoice>() {
                    @Override
                    public void success(TotalInvoice totalInvoice, Response response) {
                        if(totalInvoice!=null)
                        {
                            Toast.makeText(getActivity(),"Invoice Data Saved",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });

            }
        });
        invoicesList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        taxPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String doubleString = taxPercent.getText().toString();
                if(doubleString.equals(""))
                {
                    taxValue.setText("0.00");
                    tax = 0.0;
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    grandTotal.setText(""+grandTotalFinal);


                }
                else
                {
                    tax = Double.parseDouble(doubleString);
                    double subTotal = ((gTotalValue*tax)/100);
                    tax = subTotal;
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    grandTotal.setText(""+grandTotalFinal);
                    taxValue.setText(""+subTotal);
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
                    discountValue.setText("0.00");
                    discount = 0.0;
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    grandTotal.setText(""+grandTotalFinal);
                }
                else
                {
                    discount = Double.parseDouble(doubleString);
                    double subTotal = ((gTotalValue*discount)/100);
                    discount = subTotal;
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    grandTotal.setText(""+grandTotalFinal);
                    discountValue.setText(""+subTotal);
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
                String doubleString = advanceValue.getText().toString();
                if(doubleString.equals("")){
                    advance = 0.0;
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    totalDueValue.setText(""+grandTotalFinal);
                }else{
                    advance = Double.parseDouble(doubleString);
                    grandTotalFinal = gTotalValue+tax-discount-advance;
                    totalDueValue.setText(""+grandTotalFinal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        addNewInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("in add button  ");
                Fragment fragment = new DoctorInvoiceManageProcedure();
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
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getAllInvoicesData(doctorId, patientEmail, appointmentDate, appointmentTime, new Callback<AllTreatmentPlanVm>() {
            @Override
            public void success(AllTreatmentPlanVm allTreatmentPlanVm, Response response) {
                System.out.println("allTreatmentPlanVm.procedure = "+allTreatmentPlanVm.procedure.size());
                //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();

                api.getInvoice(doctorId,patientId,appointmentDate,appointmentTime,new Callback<TotalInvoice>() {
                    @Override
                    public void success(TotalInvoice totalInvoice, Response response) {

                        if(totalInvoice.getId()!= null) {
                            discountPercent.setText("" + totalInvoice.getPercentageDiscount());
                            discountValue.setText("" + totalInvoice.getDiscount());
                            taxPercent.setText("" + totalInvoice.getPercentageTax());
                            taxValue.setText("" + totalInvoice.getTaxValue());
                            advanceValue.setText("" + totalInvoice.getAdvance());
                            grandTotal.setText("" + totalInvoice.getGrandTotal());
                            if (totalInvoice.getShareWithPatient() == 1) {
                                shareWithPatient.setChecked(true);
                            } else {
                                shareWithPatient.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });

                if (allTreatmentPlanVm == null) {
                    invoicesList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                } else {
                    invoicesList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                    gTotalValue = 0d;
                    String currency = "";
                    for(AllProcedureVm allProcedureVm : allTreatmentPlanVm.procedure){
                        for(AllTemplateVm allTemplateVm  : allProcedureVm.allTemplate){
                            for(Field field : allTemplateVm.templates){
                                if(field.getFieldName().equals("Cost")){
                                    System.out.println("field.getFieldDefaultValue() = "+field.getFieldDefaultValue());
                                    currencyType = field.getFieldType();
                                    if(currencyType==null) {
                                        currencyType = "";
                                    }

                                    Double grand = isNumeric(field.getFieldDefaultValue());
                                    if( grand != null){
                                        System.out.println(" grand = "+grand);
                                        gTotalValue = gTotalValue + grand;
                                    }
                                }

                                if(field.getFieldName().equals("Currency")){
                                    currency  = field.getFieldDefaultValue();
                                }
                            }
                        }
                    }

                    System.out.println("gTotalValue.toString() = "+gTotalValue.toString() +" currency ="+currency);
                    invoiceTotal.setText(gTotalValue.toString() + " "+currency);
                    AllInvoiceAdapter allProcedureAdapter = new AllInvoiceAdapter(getActivity(),allTreatmentPlanVm.procedure);
                    invoicesList.setAdapter(allProcedureAdapter);
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
                    HorizontalInvoiceListAdapter hrAdapter = new HorizontalInvoiceListAdapter(getActivity(),templates);
                    fieldList.setAdapter(hrAdapter);
                    fieldList1.setAdapter(hrAdapter);
                   /* AllSingleProcedureAdapter allProcedureAdapter = new AllSingleProcedureAdapter(getActivity(), allTreatmentPlanVm.procedure);
                    invoicesList.setAdapter(allProcedureAdapter);*/
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

    public void  goToBack(){
        Bundle bun = new Bundle();
        bun.putString("fragment","invoice");
        //Fragment fragment = new PatientAllAppointment();
        Fragment fragment = new AllDoctorPatientAppointment();
        fragment.setArguments(bun);
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
}
