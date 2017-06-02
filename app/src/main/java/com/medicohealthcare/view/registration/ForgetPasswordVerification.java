package com.medicohealthcare.view.registration;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.model.ResponseVm;
import com.medicohealthcare.model.VerificationCode;
import com.medicohealthcare.model.forgotPassword;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by User on 16-02-2015.
 */
public class ForgetPasswordVerification extends ParentFragment
{

    private EditText verifyCode;
    private String emailTxt;
    private String mobileNoTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forget_password_verification,
                container, false);
        verifyCode = (EditText) view.findViewById(R.id.et_verify_code);

        Bundle args = getArguments();


        view.findViewById(R.id.btn_confirm_code)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        showBusy();
                        api.verifyCode(new VerificationCode(verifyCode.getText().toString(),emailTxt), new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication s,  Response response) {
                                System.out.println(response);

                                Fragment frag = new ForgetPasswordConfirm();
                                Bundle args = new Bundle();

                                args.putString("email", emailTxt);
                                frag.setArguments(args);
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.lower_content, frag, "Forget_Password_Confirm");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(ForgetPasswordConfirm.class.getName());
                                ft.commit();
                                hideBusy();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                });

        view.findViewById(R.id.btn_resend_verification)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        showBusy();
                        forgotPassword fObj=null;
                        if(emailTxt!=null && emailTxt.toString().length()>0)
                        {
                            fObj= new forgotPassword("email",emailTxt);
                        }else if(mobileNoTxt!=null && mobileNoTxt.toString().length()>0){
                            fObj= new forgotPassword("sms",mobileNoTxt);
                        }
                        api.forgotPassword(fObj, new Callback<ResponseVm>() {
                            @Override
                            public void success(ResponseVm responseVm, Response response) {
                                System.out.println(response);
                                hideBusy();
                                Fragment frag = new ForgetPasswordVerification();
                                Toast.makeText(getActivity().getApplicationContext(), "Code resent successfully!!", Toast.LENGTH_LONG).show();

                                // }
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




    public void saveToSession(ResponseVm result) {

//        String userId = result.getEmail();
//        String type = result.getType();
//        String status = result.getStatus();
//        String id = result.getId();
//        session = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        session.edit().putString("sessionID", userId).apply();
//
//        //Need revisit
//        if (type.equalsIgnoreCase("1"))
//            session.edit().putString("loginType", "Doctor").apply();
//        if (type.equalsIgnoreCase("2"))
//            session.edit().putString("loginType", "Patient").apply();
//        if (type.equalsIgnoreCase("3"))
//            session.edit().putString("loginType", "Assistant").apply();
//        //session.edit().putString("loginType", type).apply();
//        session.edit().putString("status", status).apply();
//        session.edit().putString("id", id).apply();
    }
}
