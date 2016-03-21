package Model;

public class AllPatients {


    public String status;
    public String accessLevel;
    public Boolean readOnly = true;
    public Boolean fullAccess = false;
    public String appointmentType;
    public String appointmentClinicId;
    public String lastVisitedClinicId;
    public String patientName;
    boolean selected = false;
    private String patientId;
    private String doctorId;
    private String name;
    private String speciality;
    private String email;
    private String mobile;
    private String location;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String allergicTo;
    private Integer type;
    private String bookDate;
    private String bookTime;
    private String shift;
    private Integer clinicId;
    private String lastAppointmentDate;
    private String appointmentDate;
    private String appointmentTime;
    private String lastVisit;
    private String star;
    private String totalAppointment;
    private String reviews;
    private String imageUrl;




    public String getNumberOfVisits() {
        return numberOfVisits;
    }

    public void setNumberOfVisits(String numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }

    private String numberOfVisits;

    public String getUpcomingVisit() {
        return upcomingVisit;
    }

    public void setUpcomingVisit(String upcomingVisit) {
        this.upcomingVisit = upcomingVisit;
    }

    private String upcomingVisit;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    private String profession;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getValidaity() {
        return validaity;
    }

    public void setValidaity(String validaity) {
        this.validaity = validaity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLoyaltyCatId() {
        return loyaltyCatId;
    }

    public void setLoyaltyCatId(String loyaltyCatId) {
        this.loyaltyCatId = loyaltyCatId;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getPrime() {
        return prime;
    }

    public void setPrime(String prime) {
        this.prime = prime;
    }

    private String address;

    private String loyaltyPoints;
    private String password;
    private String role;
    private String validaity;
    private String region;
    private String city;
    private String registrationDate;
    private String addedBy;
    private String country;
    private String loyaltyCatId;
    private String referredBy;
    private String prime;

    public AllPatients(String patientId, String name, String speciality,
                       String email, String mobile, String location, String dateOfBirth,
                       String gender, String bloodGroup, String allergicTo, Integer type, String bookDate, String bookTime, String shift, Integer clinicId, String lastAppointmentDate) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.name = name;
        this.speciality = speciality;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.allergicTo = allergicTo;
        this.type = type;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.shift = shift;
        this.clinicId = clinicId;
        this.lastAppointmentDate = lastAppointmentDate;
    }

    public AllPatients() {

    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(Boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getpatientId() {
        return patientId;
    }

    public void setpatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergicTo() {
        return allergicTo;
    }

    public void setAllergicTo(String allergicTo) {
        this.allergicTo = allergicTo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getLastAppointmentDate() {
        return lastAppointmentDate;
    }

    public void setLastAppointmentDate(String lastAppointmentDate) {
        this.lastAppointmentDate = lastAppointmentDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getTotalAppointment() {
        return totalAppointment;
    }

    public void setTotalAppointment(String totalAppointment) {
        this.totalAppointment = totalAppointment;
    }

    public String getAppointmentClinicId() {
        return appointmentClinicId;
    }

    public void setAppointmentClinicId(String appointmentClinicId) {
        this.appointmentClinicId = appointmentClinicId;
    }

    public String getLastVisitedClinicId() {
        return lastVisitedClinicId;
    }

    public void setLastVisitedClinicId(String lastVisitedClinicId) {
        this.lastVisitedClinicId = lastVisitedClinicId;
    }
}
