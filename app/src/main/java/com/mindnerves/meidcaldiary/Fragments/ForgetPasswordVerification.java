package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.LoggingInterceptor;
import com.mindnerves.meidcaldiary.R;
import com.squareup.okhttp.OkHttpClient;

import com.medico.application.MyApi;
import com.medico.model.ResponseCodeVerfication;
import Model.ResponseVm;
import Model.VerificationCode;
import Model.forgotPassword;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 16-02-2015.
 */
public class ForgetPasswordVerification extends Fragment {

    MyApi api;
    public static final String MyPREFERENCES = "MyPrefs";
    public SharedPreferences session;
    private EditText verifyCode;

    ProgressDialog progress;
    private String emailTxt;
    private String mobileNoTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forget_password_verification,
                container, false);

     //   getActivity().getActionBar().hide();
        verifyCode = (EditText) view.findViewById(R.id.et_verify_code);

        Bundle args = getArguments();
//        emailTxt = args.getString("email", null);
//        mobileNoTxt = args.getString("mobile", null);


        view.findViewById(R.id.btn_confirm_code)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.networkInterceptors().add(new LoggingInterceptor());

                        //Retrofit Initialization
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(getResources().getString(R.string.base_url))
                                .setClient(new OkClient(okHttpClient))
                                .setLogLevel(RestAdapter.LogLevel.FULL)

                                .build();
                        api = restAdapter.create(MyApi.class);
                        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));


                        api.verifyCode(new VerificationCode(verifyCode.getText().toString(),emailTxt), new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication s,  Response response) {
                                System.out.println(response);
                                progress.dismiss();
                               /* if (responseVm.getId().equalsIgnoreCase("0")) {
                                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                                } else {*/
                                //saveToSession(responseVm);
                                //    mobileNo.setText("");


                                Fragment frag = new ForgetPasswordConfirm();
                                Bundle args = new Bundle();

                                args.putString("email", emailTxt);
                                frag.setArguments(args);
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.lower_content, frag, "Forget_Password_Confirm");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();


                                // }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });
                    }
                });

        view.findViewById(R.id.btn_resend_verification)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {



                       /* emailtxt = email.getText().toString();
                        passwordtxt = mobileNo.getText().toString();*/
                        //Retrofit Initialization
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(getResources().getString(R.string.base_url))
                                .setClient(new OkClient())
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        api = restAdapter.create(MyApi.class);
                        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));

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
                                progress.dismiss();
                               /* if (responseVm.getId().equalsIgnoreCase("0")) {
                                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                                } else {*/
                                //saveToSession(responseVm);
                               // emailTxt.setText("");
                                Fragment frag = new ForgetPasswordVerification();
                                /*Bundle args = new Bundle();
                                args.putString("mobile", passwordtxt);
                                args.putString("email", emailtxt);
                                frag.setArguments(args);*/
                           /*     FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.lower_content, frag, "Forget_Password_Verfication");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();*/
                                Toast.makeText(getActivity().getApplicationContext(), "Code resent successfully!!", Toast.LENGTH_LONG).show();

                                // }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
                                progress.dismiss();
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
