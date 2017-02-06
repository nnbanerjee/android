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
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.RegistrationAssistant;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MainActivity;
import com.mindnerves.meidcaldiary.R;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

import java.util.regex.Pattern;

import Application.MyApi;
import Model.CreateProfileData;
import Model.MobileEmail;
import Model.ResponseCheckMobileEmailAvailability;
import Model.ResponseCreateProfile;
import Model.ResponseVerifyRegistrationMobileEmailCode;
import Model.ResponsegetVerificationCodeForNewRegistration;
import Model.VerifyRegistrationMobileEmailCode;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * A sample to demonstrate a form in multiple steps.
 */

public class Registration_Formwizard_Assistant extends BasicWizardLayout {
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String gender;
    private String dob;
    private String location;
    private String bloodGroup;
    public String docid;
    private String postalCode;
    private Uri uri;
    private String country;
    private String latitude;
    private String longitude;
    private String cityContext;
    private String speciality;
    private String profession;
    MyApi api;
    private TextView tvDob;
    private String path;
    private ProgressDialog progress;
    private String verifyCode;
    Global go;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Tell WizarDroid that these are context variables and set default values.
     * These values will be automatically bound to any field annotated with {@link org.codepond.wizardroid.persistence.ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */

    public Registration_Formwizard_Assistant() {
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
                .addStep(RegistrationAssistant.class, true)
                /*
                Mark this step as 'required', preventing the user from advancing to the next step
                until a certain action is taken to mark this step as completed by calling WizardStep#notifyCompleted()
                from the step.
                 */
                        //.addStep(SelectDoctorAstnt.class)
                .create();
    }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete() {
        System.out.println("in wizard...");
        super.onWizardComplete();
        tvDob = (TextView) getView().findViewById(R.id.textView);
        dob = tvDob.getText().toString();
        System.out.println("Enterd Data:::" + name + email + password + gender + mobile + dob + location + bloodGroup + docid);
        int flagValidation = 0;
        String validation = "";
        go = (Global) getActivity().getApplicationContext();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

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
        if (bloodGroup.equals("Select blood group")) {
            flagValidation = 1;
            validation = validation + "\nPlease Select Proper Blood Group";
        }
        if (uri != null) {
            path = getPath(uri);
        } else {
            path = "none";
        }

        System.out.println("Path::::::::" + path);

        if (flagValidation == 1) {
            Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
        } else {

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

                                                        // if (verificationCode.equalsIgnoreCase(verifyCode)) {
                                                        CreateProfileData param = new CreateProfileData(name, email, password, gender, mobile, dob, go.getUserLongitude(), go.getUserLatitude(), location, cityContext, "", "1");
                                                        param.setSpecialization(speciality);
                                                        param.practiceName = profession;
                                                        param.country = country;
                                                        param.city = cityContext;
                                                        param.address = location;

                                                        api.createProfile(param, new Callback<ResponseCreateProfile>() {
                                                            @Override
                                                            public void success(ResponseCreateProfile s, Response response) {
                                                                Toast.makeText(getActivity(), R.string.account_verified_succesfully, Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getActivity().getApplicationContext(), R.string.assistant_registered_success, Toast.LENGTH_LONG).show();
                                                                Intent intObj = new Intent(getActivity(), MainActivity.class);
                                                                startActivity(intObj);
                                                                getActivity().finish();
                                                            }

                                                            @Override
                                                            public void failure(RetrofitError error) {
                                                                error.printStackTrace();
                                                                Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        Toast.makeText(getActivity(), R.string.verifyCode_invalid, Toast.LENGTH_SHORT).show();
                                                        progress.dismiss();
                                                    }
                                                });

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
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
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
