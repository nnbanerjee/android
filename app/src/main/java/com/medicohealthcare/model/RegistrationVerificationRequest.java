package com.medicohealthcare.model;

/**
 * Created by Narendra on 09-06-2017.
 */

public class RegistrationVerificationRequest
{
    public String email;
    public Long mobile;
    public String emailCode;
    public String mobileCode;


    public RegistrationVerificationRequest(String email)
    {
        this.email = email;
    }
    public RegistrationVerificationRequest(String email, Long mobile)
    {
        this.email = email;
        this.mobile = mobile;
    }
    public RegistrationVerificationRequest(Long mobile)
    {
        this.mobile = mobile;
    }
    public RegistrationVerificationRequest(String email, String emailCode)
    {
        this.email = email;
        this.emailCode = emailCode;
    }
    public RegistrationVerificationRequest(Long mobile, String mobileCode)
    {
        this.mobile = mobile;
        this.mobileCode = mobileCode;
    }
    public RegistrationVerificationRequest(String email, String emailCode, Long mobile, String mobileCode)
    {
        this.email = email;
        this.mobile = mobile;
        this.emailCode = emailCode;
        this.mobileCode = mobileCode;
    }
}
