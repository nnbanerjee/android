package Model;

import java.util.List;

/**
 * Created by User on 9/11/15.
 */
public class HomePatientCount {
    public int doctorsCount;
    public int clinicsCount;
    public int appointmentsCount;
    public PatientAppointmentVM appointmentVm;
    public List<DoctorSearchResponse> doctors;

    public int getDoctorsCount() {
        return doctorsCount;
    }

    public void setDoctorsCount(int doctorsCount) {
        this.doctorsCount = doctorsCount;
    }

    public int getClinicsCount() {
        return clinicsCount;
    }

    public void setClinicsCount(int clinicsCount) {
        this.clinicsCount = clinicsCount;
    }

    public int getAppointmentsCount() {
        return appointmentsCount;
    }

    public void setAppointmentsCount(int appointmentsCount) {
        this.appointmentsCount = appointmentsCount;
    }

    public PatientAppointmentVM getAppointmentVM() {
        return appointmentVm;
    }

    public void setAppointmentVM(PatientAppointmentVM appointmentVM) {
        this.appointmentVm = appointmentVM;
    }
}
