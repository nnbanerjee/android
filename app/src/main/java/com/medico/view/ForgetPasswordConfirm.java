package com.medico.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.medico.application.MainActivity;
import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.ResetPassword;
import com.medico.model.ResponseCodeVerfication;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

//import com.mindnerves.meidcaldiary.MainActivity;
//import com.medico.application.R;
//import Model.ResetPassword;

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
       // etOldPass = (EditText) view.findViewById(R.id.et_old_pass);
        etNewPass = (EditText) view.findViewById(R.id.et_new_pass);
        etRetypePass = (EditText) view.findViewById(R.id.et_retype_password);
        Bundle args = getArguments();
        email = args.getString("email", null);


        view.findViewById(R.id.btn_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {


                      //  final String oldPass = etOldPass.getText().toString();
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
                        ResetPassword param = new ResetPassword(email, "", newPass);
                        api.changePassword(param, new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication responseVm, Response response) {
                                System.out.println(response);
                                progress.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), "Successfully updated!!", Toast.LENGTH_LONG).show();
                             /*  Login login = new Login();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.add(R.id.lower_content, new Login());

                                ft.commit();*/
                                Intent intent1 = new Intent(getActivity(),MainActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);

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



}
