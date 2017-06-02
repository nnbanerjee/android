package com.medicohealthcare.view.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.medicohealthcare.application.MainActivity;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.ResetPassword;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.mindnerves.meidcaldiary.MainActivity;
//import com.medico.application.R;
//import Model.ResetPassword;

/**
 * Created by User on 16-02-2015.
 */
public class ForgetPasswordConfirm extends ParentFragment
{

    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etRetypePass;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forget_password_reset,
                container, false);

        getActivity().getActionBar().hide();
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
                        showBusy();
                        ResetPassword param = new ResetPassword(email, "", newPass);
                        api.changePassword(param, new Callback<ResponseCodeVerfication>() {
                            @Override
                            public void success(ResponseCodeVerfication responseVm, Response response) {
                                System.out.println(response);
                                Toast.makeText(getActivity().getApplicationContext(), "Successfully updated!!", Toast.LENGTH_LONG).show();

                                Intent intent1 = new Intent(getActivity(),MainActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
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

        return view;

    }



}
