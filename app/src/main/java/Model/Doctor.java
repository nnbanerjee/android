package Model;

/**
 * Created by MNT on 16-Feb-15.
 */
public class Doctor {

    private String doctorId;
    private String name;
    private String specility;
    private String emailID;
    private String mobileNumber;
    private String location;



    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecility() {
        return specility;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecility(String specility) {
        this.specility = specility;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
