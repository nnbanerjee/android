package Model;

/**
 * Created by User on 10/1/15.
 */
public class PatientClinic {
    public String doctorId;
    public String doctorName;
    public String clinicId;
    public String clinicName;
    public String clinicLocation;
    public String contactNumber;
    public ShiftTime shift1;
    public ShiftTime shift2;
    public ShiftTime shift3;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getClinicLocation() {
        return clinicLocation;
    }

    public void setClinicLocation(String clinicLocation) {
        this.clinicLocation = clinicLocation;
    }

    public ShiftTime getShift1() {
        return shift1;
    }

    public void setShift1(ShiftTime shift1) {
        this.shift1 = shift1;
    }

    public ShiftTime getShift2() {
        return shift2;
    }

    public void setShift2(ShiftTime shift2) {
        this.shift2 = shift2;
    }

    public ShiftTime getShift3() {
        return shift3;
    }

    public void setShift3(ShiftTime shift3) {
        this.shift3 = shift3;
    }
}
