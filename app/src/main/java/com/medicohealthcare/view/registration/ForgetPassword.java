package com.medicohealthcare.view.registration;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.ResponseVm;
import com.medicohealthcare.model.forgotPassword;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.medico.application.R;
//import Model.ResponseVm;
//import Model.forgotPassword;

/**
 * Created by User on 16-02-2015.
 */
public class ForgetPassword extends ParentFragment
{

//    MyApi api;
//    public SharedPreferences session;
    private EditText email;
    private EditText mobileNo;
    private String emailtxt;
    private String passwordtxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forget_password,
                container, false);

//        getActivity().getActionBar().hide();
        email = (EditText) view.findViewById(R.id.email);
        mobileNo = (EditText) view.findViewById(R.id.mobile_no);


        view.findViewById(R.id.btn_verification)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {


                        emailtxt = email.getText().toString();
                        passwordtxt = mobileNo.getText().toString();
                        //Retrofit Initialization
                        showBusy();
                        forgotPassword fObj = null;
                        if (emailtxt != null && emailtxt.toString().length() > 0) {
                            fObj = new forgotPassword("email", emailtxt);
                        } else if (passwordtxt != null && passwordtxt.toString().length() > 0) {
                            fObj = new forgotPassword("sms", passwordtxt);
                        }

                        api.forgotPassword(fObj, new Callback<ResponseVm>() {
                            @Override
                            public void success(ResponseVm responseVm, Response response) {
                                System.out.println(response);
                                hideBusy();
                                mobileNo.setText("");
                                Fragment frag = new ForgetPasswordVerification();
                                Bundle args = new Bundle();
                                args.putString("mobile", passwordtxt);
                                args.putString("email", emailtxt);
                                frag.setArguments(args);
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.lower_content, frag, "Forget_Password_Verfication");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(ForgetPasswordVerification.class.getName());
                                ft.commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });


                    }
                });

        return view;

    }


}
