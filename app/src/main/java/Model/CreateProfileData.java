package Model;

/**
 * Created by Narendra on 09-02-2016.
 */
public class CreateProfileData {

    //Create Profile Object Data
    public String name;
    private String email;
    public String password;
    public String gender;
    private String mobile;
    public String dateOfBirth;
    public String location;
    private String accessLevel;
    private double locationLat;
    private double locationLong;
    public String city;
    public String address;
    private String role;
    public String region;

    //CreateProfile Object
    public CreateProfileData(String name, String email, String password, String gender, String mobile, String dateOfBirth, double locationLong, double locationLat,
                             String address, String city, String region, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mobile = mobile;
        this.dateOfBirth = dateOfBirth;
        this.locationLong = locationLong;
        this.locationLat = locationLat;


        this.address = address;
        this.city = city;
        this.region = region;
        this.role = role;
    }

    //Update profile object Data

    //{"personId":"1234","registrationNo":"234ewwf","qualification":"BA","specialization":"Dentist","experience":7,"awards":"National",
    // "briefDescription":"wesfsdf","memberOf":"IIAM","searchNavigation":"asdad","services":"services","institution":"KNAA","uploadFileUrl":"ashish12",
    // "practiceName":"vfdvfdvd"}

    private String personId;
    public String registrationNo;
    private String qualification;
    public String specialization;
    private String experience;
    private String awards;
    private String briefDescription;
    private String memberOf;
    private String searchNavigation;
    private String services;
    private String institution;
    private String uploadFileUrl;
    public String practiceName;
    //updateprofile Object
    //{"personId":"1234","registrationNo":"234ewwf","qualification":"BA","specialization":"Dentist","experience":7,"awards":"National",
    // "briefDescription":"wesfsdf","memberOf":"IIAM","searchNavigation":"asdad","services":"services","institution":"KNAA","uploadFileUrl":"ashish12",
    // "practiceName":"vfdvfdvd"}


    public CreateProfileData(String personId, String registrationNo, String qualification, String specialization, String experience, String awards,
                             String briefDescription, String memberOf, String searchNavigation, String services, String institution, String uploadFileUrl, String practiceName) {
        this.personId = personId;
        this.registrationNo = registrationNo;
        this.qualification = qualification;
        this.specialization = specialization;
        this.experience = experience;
        this.awards = awards;
        this.briefDescription = briefDescription;
        this.memberOf = memberOf;
        this.searchNavigation = searchNavigation;
        this.services = services;
        this.institution = institution;
        this.uploadFileUrl = uploadFileUrl;
        this.practiceName = practiceName;



    }

    //Patient Create profile Data
    private String allergicTo;
    private String urlImage;
    private String loyaltyCardId;
    private String loyaltyPoints;
    private String registrationDate;
    private String validity;
    private String addedBy;
    private String referredBy;
    private String lastAppointmentDate;
    private String isPrime;
    public String bloodGroup;
    public String cloudType;
    public String cloudLoginId;
    public String cloudLoginPassword;

    public String country;
    private String type;
    private String status;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
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

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public String getAllergicTo() {
        return allergicTo;
    }

    public void setAllergicTo(String allergicTo) {
        this.allergicTo = allergicTo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getLoyaltyCardId() {
        return loyaltyCardId;
    }

    public void setLoyaltyCardId(String loyaltyCardId) {
        this.loyaltyCardId = loyaltyCardId;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getLastAppointmentDate() {
        return lastAppointmentDate;
    }

    public void setLastAppointmentDate(String lastAppointmentDate) {
        this.lastAppointmentDate = lastAppointmentDate;
    }

    public String getIsPrime() {
        return isPrime;
    }

    public void setIsPrime(String isPrime) {
        this.isPrime = isPrime;
    }


    public String getName() {
        return name;
    }

    public String getemail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
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

    public String getCloudType() {
        return cloudType;
    }

    public String getCloudLoginId() {
        return cloudLoginId;
    }

    public String getCloudLoginPassword() {
        return cloudLoginPassword;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public void setCloudLoginId(String cloudLoginId) {
        this.cloudLoginId = cloudLoginId;
    }

    public void setCloudLoginPassword(String cloudLoginPassword) {
        this.cloudLoginPassword = cloudLoginPassword;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


}
