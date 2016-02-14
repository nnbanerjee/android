package Model;

import java.util.List;

/**
 * Created by MNT on 24-Feb-15.
 */
public class Time {

    private String clinicId;
    private String doctorId;
    private List<Schedule> scheduleList;


    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }



}
