package com.mindnerves.meidcaldiary.wizard;

/**
 * Created by User on 12-02-2015.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.MedicalRegDoctor;
import com.mindnerves.meidcaldiary.Fragments.RegistrationDoctor;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MainActivity;
import com.mindnerves.meidcaldiary.R;
import com.squareup.okhttp.Call;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.io.File;
import java.util.regex.Pattern;

import Application.MyApi;
import Model.CreateProfileData;
import Model.CreateProfileDataForDoctorUpdateDetails;
import Model.MobileEmail;
import Model.ResponseCheckMobileEmailAvailability;
import Model.ResponseCreateProfile;
import Model.ResponseCreateProfileForDoctorUpdateDetails;
import Model.ResponseVerifyRegistrationMobileEmailCode;
import Model.ResponsegetVerificationCodeForNewRegistration;
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

public class Registration_Formwizard_Doctor extends BasicWizardLayout {
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
    private TextView tvDob;
    @ContextVariable
    private String postalCode;
    @ContextVariable
    private Uri uri;
    @ContextVariable
    private Uri documentUri;
    @ContextVariable
    private String country;
    @ContextVariable
    private String region;
    @ContextVariable
    private double latitude;
    @ContextVariable
    private double longitude;
    @ContextVariable
    private String cityContext;
    MyApi api;
    public String bloodgroup, usrid = "", pasword1 = "";
    public String cloudtyp, cldtyp, speciality, practiceName, registrationNumber;
    String path;
    String verifyCode;
    Global go;
    ProgressDialog progress;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Tell WizarDroid that these are context variables and set default values.
     * These values will be automatically bound to any field annotated with {@link org.codepond.wizardroid.persistence.ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */

    public Registration_Formwizard_Doctor() {
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
                .addStep(RegistrationDoctor.class, true)
                /*
                Mark this step as 'required', preventing the user from advancing to the next step
                until a certain action is taken to mark this step as completed by calling WizardStep#notifyCompleted()
                from the step.
                 */
                .addStep(MedicalRegDoctor.class)
                .create();
    }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete() {
        System.out.println("in wizard...");
        super.onWizardComplete();
        int flagValidation = 0;
        String validation = "";
        go = (Global) getActivity().getApplicationContext();
        Spinner bloodgrp = (Spinner) getView().findViewById(R.id.bloodGroup);
        Spinner specialityspinner = (Spinner) getView().findViewById(R.id.speciality);
        Spinner practiceNameSpinner = (Spinner) getView().findViewById(R.id.practice_name);
        EditText registration = (EditText) getView().findViewById(R.id.registration_number);
        RadioGroup rg = (RadioGroup) getView().findViewById(R.id.group);
        EditText userid = (EditText) getView().findViewById(R.id.id);
        EditText paswordCloud = (EditText) getView().findViewById(R.id.password);
        bloodgroup = bloodgrp.getSelectedItem().toString();
        practiceName = practiceNameSpinner.getSelectedItem().toString();
        speciality = specialityspinner.getSelectedItem().toString();
        registrationNumber = registration.getText().toString();
        tvDob = (TextView) getView().findViewById(R.id.textView);
        dob = tvDob.getText().toString();
        RadioButton radioButton = (RadioButton) getView().findViewById(rg.getCheckedRadioButtonId());
        if (radioButton == null) {
            flagValidation = 1;
            validation = validation + "Please Select Cloud Type";
        } else {
            cldtyp = radioButton.getText().toString();

            if (cldtyp.equals("Save Locally (Mobile Memory)")) {
                cloudtyp = "1";
            } else if (cldtyp.equals("Personal Cloud")) {
                cloudtyp = "2";
                usrid = userid.getText().toString();
                pasword1 = paswordCloud.getText().toString();
            } else {
                cloudtyp = "3";
            }
        }

        if (name.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Name";

        }

        if (email.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Email";
        } else {
            Boolean value = isValid(email, EMAIL_REGEX);
            if (!value) {
                flagValidation = 1;
                validation = validation + "\nPlease Enter Email Address Proper";
            }
        }
        if (password.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Password";
        }

        if (gender.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Select Gender";
        }
        if (mobile.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Mobile Number";
        }
        if (dob.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Date of Birth";
        }
        if (location.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Location";
        }
        if (bloodgroup.equals("Select blood group")) {
            flagValidation = 1;
            validation = validation + "\nPlease Select Proper Blood Group";
        }
        if (registrationNumber.equals("")) {
            flagValidation = 1;
            validation = validation + "\nPlease Enter Registration Number";
        }
        if (uri != null) {
            path = getPath(uri);
        } else {
            path = "none";
        }

        if (flagValidation == 1) {
            Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
        } else {


            //Retrofit Initialization
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(getResources().getString(R.string.base_url))
                    .setClient(new OkClient())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = restAdapter.create(MyApi.class);

            int len = mobile.length();

            if (len == 10) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                mobile = postalCode + mobile;
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
*/
                                                        param.country = country;
                                                        param.region = region;
                                                        param.city = cityContext;
                                                        param.address = location;

                                               /* param.practiceName = practiceName;
                                                param.registrationNo = registrationNo;*/

                                                        api.createProfile(param, new Callback<ResponseCreateProfile>() {
                                                            @Override
                                                            public void success(ResponseCreateProfile s, Response response) {

                                                                //updateprofile Object
                                                                //{"personId":"1234","registrationNo":"234ewwf","qualification":"BA","specialization":"Dentist","experience":7,"awards":"National",
                                                                // "briefDescription":"wesfsdf","memberOf":"IIAM","searchNavigation":"asdad","services":"services","institution":"KNAA","uploadFileUrl":"ashish12",
                                                                // "practiceName":"vfdvfdvd"}

                                                                //String documentPath = getPath(documentUri);

                                                                CreateProfileDataForDoctorUpdateDetails newprofile = new CreateProfileDataForDoctorUpdateDetails("122", registrationNumber, "MBBS", "Dentist", "10Yrs", "National", "briefDescription", "IIM",
                                                                        "SearchNavigation", "Ortho_Neuro", "KNTRAA", "uploadPath", practiceName);

                                                                api.updateDetailedProfile(newprofile, new Callback<ResponseCreateProfileForDoctorUpdateDetails>() {
                                                                    @Override
                                                                    public void success(ResponseCreateProfileForDoctorUpdateDetails responseCreateProfile, Response response) {

                                                                        if (responseCreateProfile.getStatus().equalsIgnoreCase("0")) {

                                                                            Toast.makeText(getActivity(), R.string.account_verified_successfully, Toast.LENGTH_SHORT).show();
                                                                            Toast.makeText(getActivity().getApplicationContext(), R.string.doctor_registered_success, Toast.LENGTH_LONG).show();
                                                                            Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                            startActivity(intObj);
                                                                            getActivity().finish();

                                                                       /* File file = new File(path);
                                                                        // final String id = s;
                                                                        final String id = "1234";
                                                                        TypedFile picture = new TypedFile("application/octet-stream", file);
                                                                        api.setProfilePicDoctor(id, picture, new Callback<String>() {
                                                                            @Override
                                                                            public void success(String s, Response response) {
                                                                                if (documentUri != null) {
                                                                                    String documentPath = getPath(documentUri);
                                                                                    File documentFile = new File(documentPath);
                                                                                    TypedFile document = new TypedFile("application/octet-stream", documentFile);
                                                                                    api.doctorDocumentUpload(id, document, new Callback<String>() {
                                                                                        @Override
                                                                                        public void success(String s, Response response) {
                                                                                            Toast.makeText(getActivity(), "Your Account Verified Successfully", Toast.LENGTH_SHORT).show();
                                                                                            Toast.makeText(getActivity().getApplicationContext(), "Doctor Register Successfully", Toast.LENGTH_LONG).show();
                                                                                            Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                                            startActivity(intObj);
                                                                                            getActivity().finish();
                                                                                        }

                                                                                        @Override
                                                                                        public void failure(RetrofitError error) {
                                                                                            error.printStackTrace();
                                                                                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    Toast.makeText(getActivity(), R.string.account_verified_succesfully, Toast.LENGTH_SHORT).show();
                                                                                    Toast.makeText(getActivity().getApplicationContext(), R.string.doctor_registered_succesfully, Toast.LENGTH_LONG).show();
                                                                                    Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                                    startActivity(intObj);
                                                                                    getActivity().finish();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void failure(RetrofitError error) {
                                                                                error.printStackTrace();
                                                                                Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        });*/
                                                                        } else {
                                                                            Toast.makeText(getActivity().getApplicationContext(), "Successfully Registered !!", Toast.LENGTH_LONG).show();
                                                                        }

                                                                    }


                                                                    @Override
                                                                    public void failure(RetrofitError error) {
                                                                        Toast.makeText(getActivity().getApplicationContext(), "Failed in Update Details", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        }
        return true;
    }

}
