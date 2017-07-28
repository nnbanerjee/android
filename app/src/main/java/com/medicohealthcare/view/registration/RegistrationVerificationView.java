package com.medicohealthcare.view.registration;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.RegistrationVerificationRequest;
import com.medicohealthcare.model.ServerResponse;
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
                sendVerificationCode(true, true);
            }
        });
        mobileresend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendVerificationCode(true,true);
            }
        });
        verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showBusy();
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
                                Toast.makeText(activity, "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(activity, "Verification Code has been sent successfully to your Email and Mobile", Toast.LENGTH_LONG).show();

                                break;
                            case 2:
                                Toast.makeText(activity, "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(activity, "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(activity, "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(activity, "Verification Code has been resent successfully to your Email", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(activity, "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(activity, "Verification Code has been resent successfully to your Mobile", Toast.LENGTH_LONG).show();
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
        if(email && mobile)
        {
            if(emailverification.getText().toString().length() > 0 /*&& mobileverification.getText().toString().length() > 0*/)
            {
                showBusy();

                api.verifyCodeForNewRegistration(new RegistrationVerificationRequest(emailId,emailverification.getText().toString(),
                    mobileNumber, emailverification.getText().toString()), new Callback<ServerResponseStatus>()
                {
                    @Override
                    public void success(ServerResponseStatus s, Response response)
                    {
                        switch (s.status)
                        {
                            case 0:
                                Toast.makeText(activity, "Code did not be verified for Email and Mobile", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(activity, "Code verification successful!", Toast.LENGTH_LONG).show();
                                Bundle bundle = getActivity().getIntent().getExtras();
                                Gson gson = new Gson();
                                Person profile = (Person)gson.fromJson(bundle.getString("profile"),Person.class);
                                createProfile(profile);
                                break;
                            case 2:
                                Toast.makeText(activity, "Code could not be verified for Mobile", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(activity, "Code could not be verified for Email", Toast.LENGTH_LONG).show();
                                break;
                        }

                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        hideBusy();
                        new MedicoCustomErrorHandler(getActivity()).handleError(error);
                    }
                });
            }
            else
                Toast.makeText(activity, "Please enter Email and Mobile verification code", Toast.LENGTH_LONG).show();

        }
        else if(email)
        {
            if(emailverification.getText().toString().length() > 0 )
            {
                showBusy();
                api.verifyCodeForNewRegistration(new RegistrationVerificationRequest(emailId,emailverification.getText().toString()), new Callback<ServerResponseStatus>()
                {
                    @Override
                    public void success(ServerResponseStatus s, Response response)
                    {
                        switch (s.status)
                        {
                            case 0:
                                Toast.makeText(activity, "Verification Code did not match for Email", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(activity, "Code verification successful!", Toast.LENGTH_LONG).show();
                                Bundle bundle = getActivity().getIntent().getExtras();
                                Gson gson = new Gson();
                                Person profile = (Person)gson.fromJson(bundle.getString("profile"),Person.class);
                                createProfile(profile);
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
            else
                Toast.makeText(activity, "Please enter Email verification code", Toast.LENGTH_LONG).show();

        }
    }

    public void createProfile(final Person person)
    {

        if (person.getId() != null && person.getId().intValue() > 0)
        {
            api.updateProfile(person, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response)
                {
                    if (s.status == 1)
                    {
                        setHasOptionsMenu(false);
                        Bundle bundle = getActivity().getIntent().getExtras();
                        bundle.putString("person_name", person.getName());
                        bundle.putInt("gender", person.getGender().intValue());
                        bundle.putInt(PROFILE_ROLE, person.getRole().intValue());
                        bundle.putInt(PROFILE_ID, person.getId());
                        getActivity().getIntent().putExtras(bundle);
                        ParentFragment fragment = new RegistrationSuccessfulView();
                        FragmentManager manager = getActivity().getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.service, fragment, RegistrationSuccessfulView.class.getName()).commit();
                    }
                    else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Profile Could not be updated", Toast.LENGTH_LONG).show();
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
        else
        {
            api.createDoctorProfile(person, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response)
                {
                    if (s.status == 1)
                    {
                        setHasOptionsMenu(false);
                        Bundle bundle = getActivity().getIntent().getExtras();
                        bundle.putString("person_name", person.getName());
                        bundle.putInt("gender", person.getGender().intValue());
                        bundle.putInt(PROFILE_ROLE, person.getRole().intValue());
                        bundle.putInt(PROFILE_ID, s.profileId);
                        getActivity().getIntent().putExtras(bundle);
                        ParentFragment fragment = new RegistrationSuccessfulView();
                        FragmentManager manager = getActivity().getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.service, fragment, RegistrationSuccessfulView.class.getName()).commit();
                    } else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Profile Could not be created", Toast.LENGTH_LONG).show();
                    }
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
}
