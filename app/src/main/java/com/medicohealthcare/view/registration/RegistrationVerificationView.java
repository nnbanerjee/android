package com.medicohealthcare.view.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.RegistrationVerificationRequest;
import com.medicohealthcare.model.ServerResponseStatus;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Narendra on 11-03-2016.
 */
public class RegistrationVerificationView extends ParentFragment
{

    EditText emailverification, mobileverification;
    Button verify, emailresend, mobileresend;

    Boolean mobileVerificationRequired;
    String emailId;
    Long mobileNumber;

    boolean codeverified = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.registration_verification,
                container, false);
        emailverification = (EditText)view.findViewById(R.id.emailverification);
        emailresend = (Button)view.findViewById(R.id.emailresend);
        mobileverification = (EditText)view.findViewById(R.id.mobileverification);
        mobileresend = (Button)view.findViewById(R.id.mobileresend);
        verify = (Button)view.findViewById(R.id.verify);

        Bundle bundle = getActivity().getIntent().getExtras();
        emailId = bundle.getString(PERSON_EMAIL);
        mobileNumber = bundle.getLong(PERSON_MOBILE);
        mobileVerificationRequired = bundle.getBoolean("MOBILE_VERIFICATION_REQUIRED");

        if(!mobileVerificationRequired)
        {
            view.findViewById(R.id.emailverification_text).setVisibility(View.GONE);
            mobileverification.setVisibility(View.GONE);
            mobileresend.setVisibility(View.GONE);
        }


        emailresend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendVerificationCode(true, false);
            }
        });
        mobileresend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendVerificationCode(false,true);
            }
        });
        verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verifyCode(true,mobileVerificationRequired);
            }
        });


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        hideBusy();
    }

    private void sendVerificationCode(boolean email, boolean mobile)
    {
        showBusy();
        if(email && mobile)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(emailId, mobileNumber), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email and Mobile", Toast.LENGTH_LONG);

                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG);
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG);
                            break;
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
        else if(email)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(emailId), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG);
                            break;
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
        else if(mobile)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(mobileNumber), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG);
                            break;
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
    }
    private void verifyCode(boolean email, boolean mobile)
    {
        showBusy();
        if(email && mobile)
        {
            api.verifyCodeForNewRegistration(new RegistrationVerificationRequest(emailId,emailverification.getText().toString(),
                    mobileNumber, mobileverification.getText().toString()), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code did not match for Email and Mobile", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Congratulations! Your both the codes are successfully verified", Toast.LENGTH_LONG);
                            getActivity().setResult(1);
                            getActivity().finish();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Successfully verified for your Email but not for Mobile", Toast.LENGTH_LONG);
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Successfully verified for your Mobile but not for Email", Toast.LENGTH_LONG);
                            break;
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
        else if(email)
        {
            api.verifyCodeForNewRegistration(new RegistrationVerificationRequest(emailId,emailverification.getText().toString()), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code did not match for Email", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Congratulations! Your code is successfully verified", Toast.LENGTH_LONG);
                            getActivity().setResult(1);
                            getActivity().finish();
                            break;
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
    }
    public boolean isCodeverified()
    {
        return codeverified;
    }

}
