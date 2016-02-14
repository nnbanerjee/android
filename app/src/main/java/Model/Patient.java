package Model;

import java.io.Serializable;

/**
 * Created by MNT on 21-Feb-15.
 */
public class Patient implements Serializable{

    public String patientId;
    public String name;
    public String mobileNumber;
    public String location;
    public String emailID;
    public String status;
    boolean selected = false;
    public String accessLevel;
    public Boolean readOnly = true;
    public Boolean fullAccess = false;
    public String type;
    public String doctorId;
    public String gender;
    public String blood_group;
    public String allergic_to;
    public String bookDate;
    public String bookTime;
    public String shift;
    public Integer clinicId;
    public String lastVisited;
    public String lastVisitedTime;
    public String appointmentDate;
    public String appointmentTime;
    public String appointmentType;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
