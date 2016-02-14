package Model;

/**
 * Created by Narendra on 10-02-2016.
 */
public class ResponseVerifyRegistrationMobileEmailCode {

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    private String mobileStatus;

    public ResponseVerifyRegistrationMobileEmailCode(String mobileStatus, String emailStatus) {
        this.mobileStatus = mobileStatus;
        this.emailStatus = emailStatus;
    }

    private String emailStatus;


}
