package Model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class MobileEmail {
    private String mobile;

    public MobileEmail(String mobile, String mobileNumber, String email) {
        this.mobile = mobile;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private String mobileNumber;

    public MobileEmail(String mobile, String email) {
        this.mobile = mobile;
        this.email = email;
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

    private String email;

}
