package Model;

/**
 * Created by Narendra on 10-02-2016.
 */
public class VerifyRegistrationMobileEmailCode {
    private String email;
    private String emailCode;
    private String mobile;

    public VerifyRegistrationMobileEmailCode(String email, String emailCode, String mobile, String mobileCode) {
        this.email = email;
        this.emailCode = emailCode;
        this.mobile = mobile;
        this.mobileCode = mobileCode;
    }

    private String mobileCode;

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }



}
