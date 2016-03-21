package Model;

/**
 * Created by Narendra on 26-02-2016.
 */
public class DoctorCreatesAppointment {
    //{"doctorId":1,"patientId":40,"clinicId":3,"slotNumber":1,
    // "appointmentDate":1456443900000 ,"type":0,"sequenceNumber":1, "userType":1}
//New api changed
    //{"doctorId":101,"patientId":106,"clinicId":101,"appointmentDate":1456936066768,"type":0,
    // "sequenceNumber":7,"appointmentStatus":0,"visitType":0,"rating":101,"doctorClinicId":101,"visitStatus":2,
    // "referred_by":104,"reviews":"Very good", "new_case_id":101}4
    private String sequenceNumber;
    private String doctorId;
    private String patientId;
    private String slotNumber;
    private String userType;
    private String clinicId;
    private String appointmentDate;
    private String status;

    //new data
    private String appointmentStatus;
    private String visitType;
    private String rating;
    private String visitStatus;
    private String referred_by;

    public DoctorCreatesAppointment(String sequenceNumber, String doctorId, String patientId, String slotNumber, String userType, String clinicId, String appointmentDate, String type, String doctorClinicId) {
        this.sequenceNumber = sequenceNumber;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.slotNumber = slotNumber;
        this.userType = userType;
        this.clinicId = clinicId;
        this.appointmentDate = appointmentDate;

        this.type = type;
        this.doctorClinicId = doctorClinicId;
    }

    public DoctorCreatesAppointment(String sequenceNumber, String doctorId, String patientId,
                                    String userType, String clinicId, String appointmentDate,  String doctorClinicId, String type,
                                    String appointmentStatus, String visitType, String rating, String visitStatus, String referred_by,
                                    String reviews, String new_case_id) {
        this.sequenceNumber = sequenceNumber;
        this.doctorId = doctorId;
        this.patientId = patientId;
       // this.slotNumber = slotNumber;
        this.userType = userType;
        this.clinicId = clinicId;
        this.appointmentDate = appointmentDate;
       // this.status = status;
        this.appointmentStatus = appointmentStatus;
        this.visitType = visitType;
        this.rating = rating;
        this.visitStatus = visitStatus;
        this.referred_by = referred_by;
        this.reviews = reviews;
        this.new_case_id = new_case_id;
        this.doctorClinicId = doctorClinicId;
        this.type = type;
    }

    private String reviews;

    public String getNew_case_id() {
        return new_case_id;
    }

    public void setNew_case_id(String new_case_id) {
        this.new_case_id = new_case_id;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getReferred_by() {
        return referred_by;
    }

    public void setReferred_by(String referred_by) {
        this.referred_by = referred_by;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    private String new_case_id;

    public String getDoctorClinicId() {
        return doctorClinicId;
    }

    public void setDoctorClinicId(String doctorClinicId) {
        this.doctorClinicId = doctorClinicId;
    }

    private String doctorClinicId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String type;
}
