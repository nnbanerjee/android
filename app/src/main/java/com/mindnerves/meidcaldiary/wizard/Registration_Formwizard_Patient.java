package com.mindnerves.meidcaldiary.wizard;

/**
 * Created by User on 12-02-2015.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.MedicalReg;
import com.mindnerves.meidcaldiary.Fragments.Registration;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MainActivity;
import com.mindnerves.meidcaldiary.R;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.io.File;
import java.util.regex.Pattern;

import Application.MyApi;
import Model.CreateProfileData;
import Model.CreateProfileDataForDoctorUpdateDetails;
import Model.MobileEmail;
import Model.RegisterUserData;
import Model.ResponseCheckMobileEmailAvailability;
import Model.ResponseCodeVerfication;
import Model.ResponseCreateProfile;
import Model.ResponseCreateProfileForDoctorUpdateDetails;
import Model.ResponseVerifyRegistrationMobileEmailCode;
import Model.ResponsegetVerificationCodeForNewRegistration;
import Model.VerificationCode;
import Model.VerifyRegistrationMobileEmailCode;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


/**
 * A sample to demonstrate a form in multiple steps.
 */
public class Registration_Formwizard_Patient extends BasicWizardLayout {
    @ContextVariable
    private String name;
    @ContextVariable
    private String email;
    @ContextVariable
    private String password;
    @ContextVariable
    private String mobile;
    @ContextVariable
    private String gender;
    @ContextVariable
    private String dob;
    @ContextVariable
    private String location;
    @ContextVariable
    private String postalCode;
    @ContextVariable
    private Uri uri;
    @ContextVariable
    private String country;
    @ContextVariable
    private String region;
    @ContextVariable
    private String latitude;
    @ContextVariable
    private String longitude;
    @ContextVariable
    private String cityContext;
    private TextView tvDob;
    MyApi api;
    String path;
    public String bloodgroup,usrid="",pasword1="",alergi;
    public String cloudtyp,url,respage,sms;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Global go;
    ProgressDialog progress;
    String verifyCode;

    private CheckBox agreeTc;
    /**
     * Tell WizarDroid that these are context variables and set default values.
     * These values will be automatically bound to any field annotated with {@link org.codepond.wizardroid.persistence.ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */


    public Registration_Formwizard_Patient() {
        super();
    }

    /*
        You must override this method and create a wizard flow by
        using WizardFlow.Builder as shown in this example
     */
    @Override
    public WizardFlow onSetup() {


        return new WizardFlow.Builder()
                /*
                Add your steps in the order you want them to appear and eventually call create()
                to create the wizard flow.
                 */
                .addStep(Registration.class,true)
                /*
                Mark this step as 'required', preventing the user from advancing to the next step
                until a certain action is taken to mark this step as completed by calling WizardStep#notifyCompleted()
                from the step.
                 */
/*                .addStep(MedicalReg.class)*/
                .create();



    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /*
                You'd normally override onWizardComplete to access the wizard context and/or close the wizard
             */
    @Override
    public void onWizardComplete() {
        System.out.println("in wizard...");
        super.onWizardComplete();
        go = (Global)getActivity().getApplicationContext();
        int flagValidation = 0;
        String validation = "";
        Spinner bloodgrp = (Spinner) getView().findViewById(R.id.bloodGroup);
        EditText etalergic = (EditText) getView().findViewById(R.id.allergic);
        RadioGroup rg = (RadioGroup) getView().findViewById(R.id.group);
        EditText userid = (EditText) getView().findViewById(R.id.id);
        EditText paswordCloud = (EditText) getView().findViewById(R.id.password);
        bloodgroup = bloodgrp.getSelectedItem().toString();
        tvDob = (TextView)getView().findViewById(R.id.textView);
        dob = tvDob.getText().toString();
        alergi = etalergic.getText().toString();
        Uri uri = go.getUri();

        System.out.println("Password :::::>"+password);
        System.out.println("name :::::>"+name);
        cloudtyp = "1";
       /* RadioButton radioButton = (RadioButton) getView().findViewById(rg.getCheckedRadioButtonId());

        if(radioButton == null)
        {
            flagValidation = 1;
            validation = validation +"Please Select Cloud Type";
        }
        else {
            String cldtyp = "";
            cldtyp = radioButton.getText().toString();

            if (cldtyp.equals("Save Locally (Mobile Memory)")) {
                cloudtyp = "1";
            } else if (cldtyp.equals("Personal Cloud")) {
                cloudtyp = "2";
                usrid = userid.getText().toString();
                password = paswordCloud.getText().toString();
            } else {
                cloudtyp = "3";
            }
        }*/

        int len = mobile.length();

        if(name.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Name";

        }

        if(email.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Email";
        }
        else
        {
            Boolean value = isValid(email,EMAIL_REGEX);
            if(!value)
            {
                flagValidation =1;
                validation = validation+"\nPlease Enter Email Address Proper";
            }
        }
        if(password.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Password";
        }

        if(gender.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Select Gender";
        }
        if(mobile.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Mobile Number";
        }
        if(dob.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Date of Birth";
        }
        if(location.equals(""))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Enter Location";
        }
        if(bloodgroup.equals("Select blood group"))
        {
            flagValidation = 1;
            validation = validation+"\nPlease Select Proper Blood Group";
        }

        if(uri!=null)
        {
            path = getPath(uri);
        }
        else
        {
            path = "none";
        }

        if(flagValidation == 1)
        {
            Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
        }

        else
        {
            progress = ProgressDialog.show(getActivity(), "", "Loading... Please wait...");
            System.out.println("Mobile Length:::::  " + len);
            if (len == 10)
            {
                mobile = postalCode + mobile;
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(getResources().getString(R.string.base_url))
                        .setClient(new OkClient())
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                api = restAdapter.create(MyApi.class);
                final String emailID = email;

                api.checkMobileEmailAvailability(new MobileEmail(mobile, email), new Callback<ResponseCheckMobileEmailAvailability>() {

                    @Override
                    public void success(ResponseCheckMobileEmailAvailability s, Response response) {
                        System.out.println("s is->" + s + "response is " + response);
                        if (s.getStatus().equalsIgnoreCase("0")) {
                            api.getVerificationCodeForNewRegistration(new MobileEmail("", mobile, email), new Callback<ResponsegetVerificationCodeForNewRegistration>() {
                                @Override
                                public void success(ResponsegetVerificationCodeForNewRegistration s, Response response) {
                                    //Positive scenario
                                    if (s.getEmailSent().equalsIgnoreCase("0") && s.getSmsSent().equalsIgnoreCase("0")) {

                                        //  verifyCode = s.getStatus();
                                        progress.dismiss();
                                        LinearLayout layout = new LinearLayout(getActivity());
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView mobileTv = new TextView(getActivity());
                                        final EditText mobileEB = new EditText(getActivity());

                                        mobileTv.setText(R.string.enter_mobile_code);
                                        layout.addView(mobileTv);
                                        layout.addView(mobileEB);

                                        TextView emailTv = new TextView(getActivity());
                                        final EditText emailEB = new EditText(getActivity());
                                        emailTv.setText(R.string.enter_email_code);
                                        layout.addView(emailTv);
                                        layout.addView(emailEB);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                        alert.setTitle(R.string.verify_account);
                                        alert.setView(layout);
                                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String mobileVerificationCode = mobileEB.getText().toString();
                                                String emailVerificationCode = emailEB.getText().toString();


                                                api.verifyCodeForNewRegistration(new VerifyRegistrationMobileEmailCode(email, emailVerificationCode, mobile, mobileVerificationCode), new Callback<ResponseVerifyRegistrationMobileEmailCode>() {

                                                    @Override
                                                    public void success(ResponseVerifyRegistrationMobileEmailCode responseVerifyRegistrationMobileEmailCode, Response response) {
                                                        //  responseVerifyRegistrationMobileEmailCode.getEmailStatus().equalsIgnoreCase("success")
                                                        if (gender.equalsIgnoreCase("Male"))
                                                            gender = "0";
                                                        else
                                                            gender = "1";
                                                        //setting pune default
                                                        go.setUserLatitude(18.52);
                                                        go.setUserLongitude(73.85);
                                                        // if (verificationCode.equalsIgnoreCase(verifyCode)) {
                                                        CreateProfileData param = new CreateProfileData(name, email, password, gender, mobile, dob, go.getUserLongitude(), go.getUserLatitude(), location, cityContext, region, "1");

                                             /*CreateProfileData(String name, String email, String password, String gender, String mobile, String dateOfBirth,
                                                    String locationLong,String locationLat, String bloodGroup, String cloudType, String cloudLoginId, String cloudLoginPassword,
                                                    String specialization, String address,String city,String region,String role)
*/                                                      param.setBloodGroup(bloodgroup);
                                                        param.setAllergicTo(alergi);
                                                        param.country = country;
                                                        param.region = region;
                                                        param.city = cityContext;
                                                        param.address = location;

                                               /* param.practiceName = practiceName;
                                                param.registrationNo = registrationNo;*/

                                                        api.createProfile(param, new Callback<ResponseCreateProfile>() {
                                                            @Override
                                                            public void success(ResponseCreateProfile s, Response response) {


                                                                Toast.makeText(getActivity(), R.string.account_verified_succesfully, Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getActivity().getApplicationContext(), R.string.patient_registered_success, Toast.LENGTH_LONG).show();
                                                                Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                startActivity(intObj);
                                                                getActivity().finish();

                                                            }

                                                            @Override
                                                            public void failure(RetrofitError error) {
                                                                error.printStackTrace();
                                                                Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {

                                                    }
                                                });


                                                // }

                                            }
                                        });
                                        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });
                                        alert.show();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.failed_to_send_code, Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    System.out.print("in error of retrofit................");
                                    error.printStackTrace();
                                    progress.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (s.getStatus().equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), R.string.email_already_exists, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        } else if (s.getStatus().equalsIgnoreCase("2")) {
                            Toast.makeText(getActivity(), R.string.mobile_already_exists, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        } else {
                            Toast.makeText(getActivity(), R.string.mobile_email_already_exists, Toast.LENGTH_SHORT).show();

                            progress.dismiss();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.print("in error of retrofit................");
                        error.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });

          /*      api.isEmailIdAvailable(emailID,new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                         System.out.println("Status:::::::::>"+s);
                        if(s.equalsIgnoreCase("Available")){
                            api.getVerificationCode(new VerificationCode("1234", emailID),new Callback<ResponseCodeVerfication>() {
                                @Override
                                public void success(ResponseCodeVerfication s, Response response) {
                                    System.out.println("res::::::"+s);
                                    verifyCode= s.getStatus();
                                    progress.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                    final EditText edittext= new EditText(getActivity());
                                    alert.setMessage("Enter Password");
                                    alert.setTitle("Verify Account");
                                    alert.setView(edittext);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String verificationCode = edittext.getText().toString();
                                            if(verificationCode.equalsIgnoreCase(verifyCode))
                                            {
                                                RegisterUserData param = new RegisterUserData(name, email, password, gender, mobile, dob, location, bloodgroup, alergi, cloudtyp, usrid, pasword1);
                                                param.country = country;
                                                param.region = region;
                                                param.city = cityContext;
                                                param.address = location;
                                                param.latitude = latitude;
                                                param.longitude = longitude;
                                                go.setUserLatitude(0.0);
                                                go.setUserLongitude(0.0);
                                                api.registerPatient(param,new Callback<String>() {
                                                    @Override
                                                    public void success(String s, Response response) {
                                                        File file = new File(path);
                                                        TypedFile picture = new TypedFile("application/octet-stream",file);
                                                        api.setProfilePicPatient(s,picture,new Callback<String>() {
                                                            @Override
                                                            public void success(String s, Response response) {
                                                                Toast.makeText(getActivity(),"Your Account Verified Successfully",Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getActivity().getApplicationContext(), "Patient Register Successfully", Toast.LENGTH_LONG).show();
                                                                Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                startActivity(intObj);
                                                                getActivity().finish();
                                                            }

                                                            @Override
                                                            public void failure(RetrofitError error) {
                                                                error.printStackTrace();
                                                                Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        error.printStackTrace();
                                                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(),"Please Enter Correct Verification Code",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    });
                                    alert.show();
                                }
                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                    progress.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Your Email Id Is Already Exists", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Please Enter Another Email Id", Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });*/
            } else {
                Toast.makeText(getActivity(), "Enter Mobile Number Properly", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }
    }

    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        };
        return true;
    }

}
