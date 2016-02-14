package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.ResetPassword;
import Model.ResponseCodeVerfication;
import Model.ResponseVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 16-02-2015.
 */
public class ForgetPasswordConfirm extends Fragment {

    MyApi api;
    public static final String MyPREFERENCES = "MyPrefs";
    public SharedPreferences session;
    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etRetypePass;
    ProgressDialog progress;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forget_password_reset,
                container, false);

        getActivity().getActionBar().hide();
        etOldPass = (EditText) view.findViewById(R.id.et_old_pass);
        etNewPass = (EditText) view.findViewById(R.id.et_new_pass);
        etRetypePass = (EditText) view.findViewById(R.id.et_retype_password);
        Bundle args = getArguments();
        email = args.getString("email", null);


        view.findViewById(R.id.btn_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {


                        final String oldPass = etOldPass.getText().toString();
                        final String newPass = etNewPass.getText().toString();
                        final String reTypePass = etRetypePass.getText().toString();
                        //Retrofit Initialization
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(getResources().getString(R.string.base_url))
                                .setClient(new OkClient())
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        api = restAdapter.create(MyApi.class);
                        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                        ResetPassword param = new ResetPassword(email, oldPass, newPass);
                        api.changePassword(param, new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication responseVm, Response response) {
                                System.out.println(response);
                                progress.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), "Successfully updated!!", Toast.LENGTH_LONG).show();
                               Login login = new Login();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.add(R.id.lower_content, new Login());

                                ft.commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });


                    }
                });

        return view;

    }



}
