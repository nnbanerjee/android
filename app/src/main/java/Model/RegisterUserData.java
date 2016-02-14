package Model;

/**
 * Created by MNT on 17-Feb-15.
 */
public class RegisterUserData {


    public String name;
    public String emailID;
    public String password;
    public String gender;
    public String mobileNumber;
    public String dateOfBirth;
    public String location;
    public String bloodGroup;
    public String allergicTo;
    public String cloudType;
    public String cloudLoginId;
    public String cloudLoginPassword;
    public String path;
    public String country;
    public String region;
    public String practiceName;
    public String registrationNumber;
    public String latitude;
    public String longitude;
    public String city;
    public String address;


    public RegisterUserData(String name, String emailID, String password, String gender, String mobileNumber, String dateOfBirth, String location, String bloodGroup, String allergicTo, String cloudType, String cloudLoginId, String cloudLoginPassword) {
        this.name = name;
        this.emailID = emailID;
        this.password = password;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
        this.location = location;
        this.bloodGroup = bloodGroup;
        this.allergicTo = allergicTo;
        this.cloudType = cloudType;
        this.cloudLoginId = cloudLoginId;
        this.cloudLoginPassword = cloudLoginPassword;
    }
    public String getName() {
        return name;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLocation() {
        return location;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getAllergicTo() {
        return allergicTo;
    }

    public String getCloudType() {
        return cloudType;
    }

    public String getCloudLoginId() {
        return cloudLoginId;
    }

    public String getCloudLoginPassword() {
        return cloudLoginPassword;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setAllergicTo(String allergicTo) {
        this.allergicTo = allergicTo;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public void setCloudLoginId(String cloudLoginId) {
        this.cloudLoginId = cloudLoginId;
    }

    public void setCloudLoginPassword(String cloudLoginPassword) {
        this.cloudLoginPassword = cloudLoginPassword;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
