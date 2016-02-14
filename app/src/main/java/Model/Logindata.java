package Model;

/**
 * Created by MNT on 17-Feb-15.
 */
public class Logindata {


    public String email;
    public String password;

    public Logindata(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmailID() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmailID(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
