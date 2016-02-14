package Model;

import android.content.Intent;

/**
 * Created by mindnerves on 5/11/15.
 */
public class ClinicAppointment {

    private Integer id;
    private Integer doctorId;
    private String patientId;
    private String timeSlot;
    private String shift;
    private Integer clinicId;
    private String bookTime;
    private String appointmentDate;
    private String status;
    private String visitType;

    public ClinicAppointment(){

    }

    public ClinicAppointment(Integer doctorId,String patientId,String timeSlot,String shift,Integer clinicId,String bookTime,String appointmentDate,String status, String visitType){
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.timeSlot = timeSlot;
        this.shift = shift;
        this.clinicId = clinicId;
        this.bookTime = bookTime;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.visitType = visitType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

}
