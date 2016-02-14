package Model;

/**
 * Created by Narendra on 10-02-2016.
 */
public class CreateProfileDataForDoctorUpdateDetails {
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

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getSearchNavigation() {
        return searchNavigation;
    }

    public void setSearchNavigation(String searchNavigation) {
        this.searchNavigation = searchNavigation;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUploadFileUrl() {
        return uploadFileUrl;
    }

    public void setUploadFileUrl(String uploadFileUrl) {
        this.uploadFileUrl = uploadFileUrl;
    }

    public String practiceName;
    //updateprofile Object
    //{"personId":"1234","registrationNo":"234ewwf","qualification":"BA","specialization":"Dentist","experience":7,"awards":"National",
    // "briefDescription":"wesfsdf","memberOf":"IIAM","searchNavigation":"asdad","services":"services","institution":"KNAA","uploadFileUrl":"ashish12",
    // "practiceName":"vfdvfdvd"}


    public CreateProfileDataForDoctorUpdateDetails(String personId, String registrationNo, String qualification, String specialization, String experience, String awards,
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
}
