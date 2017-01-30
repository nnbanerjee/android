package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.Toast;

import com.medico.view.DoctorHome;
import com.mindnerves.meidcaldiary.ForgotPasswordActivity;
import com.medico.view.PatientHome;
import com.mindnerves.meidcaldiary.R;
import com.mindnerves.meidcaldiary.SigninActivity;
import com.mindnerves.meidcaldiary.SigninActivityAssistance;
import com.mindnerves.meidcaldiary.SigninActivityDoctor;

import java.util.List;

import Application.MyApi;
import Model.Logindata;
import Model.ResponseVm;
import Utils.PARAM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.client.Header ;

import static Utils.PARAM.LOGGED_IN_ID;
import static Utils.PARAM.LOGGED_IN_USER_ROLE;
import static Utils.PARAM.LOGGED_IN_USER_STATUS;

/**
 * Created by User on 16-02-2015.
 */
public class Login extends Fragment {

    MyApi api;
    public static final String MyPREFERENCES = "MyPrefs";
    public SharedPreferences session;
    private EditText email;
    private EditText password;
    ProgressDialog progress;
    Login login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login,
                container, false);

      //  getActivity().getActionBar().hide();
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);

        view.findViewById(R.id.signin)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intObj = new Intent(getActivity(), SigninActivity.class);
                        startActivity(intObj);
                    }
                });

        view.findViewById(R.id.registernow)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        RegistrationChooser login = new RegistrationChooser();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.lower_content, login);
                        ft.commit();


                    }
                });
        view.findViewById(R.id.signindoc)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intObj = new Intent(getActivity(), SigninActivityDoctor.class);
                        startActivity(intObj);
                    }
                });

        view.findViewById(R.id.signinassistance)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intObj = new Intent(getActivity(), SigninActivityAssistance.class);
                        startActivity(intObj);
                    }
                });
        view.findViewById(R.id.forgot_password)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {


                        Intent intObj = new Intent(getActivity(), ForgotPasswordActivity.class);
                        startActivity(intObj);

                       /* Fragment frag = new ForgetPassword();
                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        ft.replace(R.id.lower_content, frag, "Forget_Password");
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
*/

                    }
                });
        view.findViewById(R.id.login)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        final String emailtxt = email.getText().toString();
                        final String passwordtxt = password.getText().toString();

                        //Retrofit Initialization
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(getResources().getString(R.string.base_url))
                                .setClient(new OkClient())
                                .setLogLevel(RestAdapter.LogLevel.FULL)

                                .build();
                        if (passwordtxt != null && !passwordtxt.equalsIgnoreCase("")) {
                            api = restAdapter.create(MyApi.class);
                            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                            Logindata param = new Logindata(emailtxt, passwordtxt);
                            api.login(param, new Callback<ResponseVm>() {
                                @Override
                                public void success(ResponseVm responseVm, Response response) {
                                    System.out.println(response);
                                    List<Header> headers = response.getHeaders();
                                    for(Header header: headers)
                                    {
                                        if(header.getName().equals("Set-Cookie")) {
                                            CookieManager.getInstance().removeSessionCookie();
                                            CookieManager.getInstance().setCookie("PLAY_SESSION", header.getValue().substring(15));
                                            break;
                                        }
                                    }
                                    progress.dismiss();
                                    //0 is failure and  {1= Doctor,2=Patient,3-Assistant, 0 = Failure}
                                    if (responseVm == null || responseVm.getId() == 0)
                                    {
                                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        saveToSession(responseVm);
                                        password.setText("");
                                        if(responseVm.getType() == PARAM.DOCTOR) {
                                            Intent intObj = new Intent(getActivity(), DoctorHome.class);
                                            startActivity(intObj);
                                        }
                                        else
                                        {
                                            Intent intObj = new Intent(getActivity(), PatientHome.class);
                                            startActivity(intObj);
                                        }
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                }
                            });


                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Enter valid Password!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return view;

    }

    public void saveToSession(ResponseVm result) {

//        String userId = result.getId();
//        String type = result.getType();
//        String status = result.getStatus();
//        String id = result.getId();
        session = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        session.edit().putString("sessionID", userId).apply();

        //Need revisit
//        if (type.equalsIgnoreCase("1"))
//            session.edit().putString("loginType", "Doctor").apply();
//        if (type.equalsIgnoreCase("0"))
//            session.edit().putString("loginType", "Patient").apply();
//        if (type.equalsIgnoreCase("3"))
//            session.edit().putString("loginType", "Assistant").apply();
        //session.edit().putString("loginType", type).apply();
//        session.edit().putString("status", status).apply();
//        session.edit().putString("id", id).apply();

        //actual storage
        session.edit().putInt(LOGGED_IN_ID, result.getId()).apply();
        session.edit().putInt(LOGGED_IN_USER_ROLE, result.getType()).apply();
        session.edit().putInt(LOGGED_IN_USER_STATUS, result.getStatus()).apply();

        session.edit().putBoolean("profileOfLoggedIn", Boolean.TRUE).apply();

    }
}
