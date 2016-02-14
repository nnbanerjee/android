package Model;

/**
 * Created by mindnerves on 5/6/2015.
 */
public class ClinicDetailVm {

    private String doctorId;
    private String doctorName;
    private String clinicId;
    private String clinicName;
    private String clinicLocation;
    private String contactNumber;
    private TimeTableVm slot1;
    private TimeTableVm slot2;
    private TimeTableVm slot3;
    private boolean hasNextAppointment = false;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentShift;
    private String onlineAppointment;
    private String doctorEmail;
    public String clinicMobile;
    public String clinicEmail;
    public String clinicSpeciality;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicLocation() {
        return clinicLocation;
    }

    public void setClinicLocation(String clinicLocation) {
        this.clinicLocation = clinicLocation;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public TimeTableVm getSlot1() {
        return slot1;
    }

    public void setSlot1(TimeTableVm slot1) {
        this.slot1 = slot1;
    }

    public TimeTableVm getSlot2() {
        return slot2;
    }

    public void setSlot2(TimeTableVm slot2) {
        this.slot2 = slot2;
    }

    public TimeTableVm getSlot3() {
        return slot3;
    }

    public void setSlot3(TimeTableVm slot3) {
        this.slot3 = slot3;
    }

    public boolean isHasNextAppointment() {
        return hasNextAppointment;
    }

    public void setHasNextAppointment(boolean hasNextAppointment) {
        this.hasNextAppointment = hasNextAppointment;
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

    public String getAppointmentShift() {
        return appointmentShift;
    }

    public void setAppointmentShift(String appointmentShift) {
        this.appointmentShift = appointmentShift;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getOnlineAppointment() {
        return onlineAppointment;
    }

    public void setOnlineAppointment(String onlineAppointment) {
        this.onlineAppointment = onlineAppointment;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }
}
