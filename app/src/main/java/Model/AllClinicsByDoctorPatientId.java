package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 12-02-2016.
 */
public class AllClinicsByDoctorPatientId {

    private int doctorId;
    private int patientId;
    private String name;
    private List<Clinics> clinicList = new ArrayList<Clinics>();

    /**
     *
     * @return
     * The doctorId
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     *
     * @param doctorId
     * The doctorId
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /**
     *
     * @return
     * The patientId
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     * The patientId
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The clinicList
     */
    public List<Clinics> getClinics() {
        return clinicList;
    }

    /**
     *
     * @param clinicList
     * The clinicList
     */
    public void setClinics(List<Clinics> clinicList) {
        this.clinicList = clinicList;
    }



    private class Appointment {

        private int appointmentId;
        private int dateTime;

        /**
         *
         * @return
         * The appointmentId
         */
        public int getAppointmentId() {
            return appointmentId;
        }

        /**
         *
         * @param appointmentId
         * The appointmentId
         */
        public void setAppointmentId(int appointmentId) {
            this.appointmentId = appointmentId;
        }

        /**
         *
         * @return
         * The dateTime
         */
        public int getDateTime() {
            return dateTime;
        }

        /**
         *
         * @param dateTime
         * The dateTime
         */
        public void setDateTime(int dateTime) {
            this.dateTime = dateTime;
        }

    }


    

   
    private class Clinic {

        private String clinicName;
        private int landLineNumber;
        private int mobileNumber;
        private String address;
        private String location;
        private Object doctorEmail;
        private String email;
        private String doctorId;
        private Object parameter;
        private boolean selected;
        private Object onlineAppointment;
        private String speciality;
        private int idClinic;
        private Object schedules;
        private int type;
        private String about;
        private String description;
        private String service;
        private String timing;
        private String imageURL;
        private Object searchNavigation;
        private Object country;

        /**
         *
         * @return
         * The clinicName
         */
        public String getClinicName() {
            return clinicName;
        }

        /**
         *
         * @param clinicName
         * The clinicName
         */
        public void setClinicName(String clinicName) {
            this.clinicName = clinicName;
        }

        /**
         *
         * @return
         * The landLineNumber
         */
        public int getLandLineNumber() {
            return landLineNumber;
        }

        /**
         *
         * @param landLineNumber
         * The landLineNumber
         */
        public void setLandLineNumber(int landLineNumber) {
            this.landLineNumber = landLineNumber;
        }

        /**
         *
         * @return
         * The mobileNumber
         */
        public int getMobileNumber() {
            return mobileNumber;
        }

        /**
         *
         * @param mobileNumber
         * The mobileNumber
         */
        public void setMobileNumber(int mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        /**
         *
         * @return
         * The address
         */
        public String getAddress() {
            return address;
        }

        /**
         *
         * @param address
         * The address
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         *
         * @return
         * The location
         */
        public String getLocation() {
            return location;
        }

        /**
         *
         * @param location
         * The location
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         *
         * @return
         * The doctorEmail
         */
        public Object getDoctorEmail() {
            return doctorEmail;
        }

        /**
         *
         * @param doctorEmail
         * The doctorEmail
         */
        public void setDoctorEmail(Object doctorEmail) {
            this.doctorEmail = doctorEmail;
        }

        /**
         *
         * @return
         * The email
         */
        public String getEmail() {
            return email;
        }

        /**
         *
         * @param email
         * The email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         *
         * @return
         * The doctorId
         */
        public String getDoctorId() {
            return doctorId;
        }

        /**
         *
         * @param doctorId
         * The doctorId
         */
        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        /**
         *
         * @return
         * The parameter
         */
        public Object getParameter() {
            return parameter;
        }

        /**
         *
         * @param parameter
         * The parameter
         */
        public void setParameter(Object parameter) {
            this.parameter = parameter;
        }

        /**
         *
         * @return
         * The selected
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         *
         * @param selected
         * The selected
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         *
         * @return
         * The onlineAppointment
         */
        public Object getOnlineAppointment() {
            return onlineAppointment;
        }

        /**
         *
         * @param onlineAppointment
         * The onlineAppointment
         */
        public void setOnlineAppointment(Object onlineAppointment) {
            this.onlineAppointment = onlineAppointment;
        }

        /**
         *
         * @return
         * The speciality
         */
        public String getSpeciality() {
            return speciality;
        }

        /**
         *
         * @param speciality
         * The speciality
         */
        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        /**
         *
         * @return
         * The idClinic
         */
        public int getIdClinic() {
            return idClinic;
        }

        /**
         *
         * @param idClinic
         * The idClinic
         */
        public void setIdClinic(int idClinic) {
            this.idClinic = idClinic;
        }

        /**
         *
         * @return
         * The schedules
         */
        public Object getSchedules() {
            return schedules;
        }

        /**
         *
         * @param schedules
         * The schedules
         */
        public void setSchedules(Object schedules) {
            this.schedules = schedules;
        }

        /**
         *
         * @return
         * The type
         */
        public int getType() {
            return type;
        }

        /**
         *
         * @param type
         * The type
         */
        public void setType(int type) {
            this.type = type;
        }

        /**
         *
         * @return
         * The about
         */
        public String getAbout() {
            return about;
        }

        /**
         *
         * @param about
         * The about
         */
        public void setAbout(String about) {
            this.about = about;
        }

        /**
         *
         * @return
         * The description
         */
        public String getDescription() {
            return description;
        }

        /**
         *
         * @param description
         * The description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         *
         * @return
         * The service
         */
        public String getService() {
            return service;
        }

        /**
         *
         * @param service
         * The service
         */
        public void setService(String service) {
            this.service = service;
        }

        /**
         *
         * @return
         * The timing
         */
        public String getTiming() {
            return timing;
        }

        /**
         *
         * @param timing
         * The timing
         */
        public void setTiming(String timing) {
            this.timing = timing;
        }

        /**
         *
         * @return
         * The imageURL
         */
        public String getImageURL() {
            return imageURL;
        }

        /**
         *
         * @param imageURL
         * The imageURL
         */
        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        /**
         *
         * @return
         * The searchNavigation
         */
        public Object getSearchNavigation() {
            return searchNavigation;
        }

        /**
         *
         * @param searchNavigation
         * The searchNavigation
         */
        public void setSearchNavigation(Object searchNavigation) {
            this.searchNavigation = searchNavigation;
        }

        /**
         *
         * @return
         * The country
         */
        public Object getCountry() {
            return country;
        }

        /**
         *
         * @param country
         * The country
         */
        public void setCountry(Object country) {
            this.country = country;
        }

    }


   
    private class Clinics {

        private Clinic clinic;
        private List<Appointment> appointments = new ArrayList<Appointment>();

        /**
         *
         * @return
         * The clinic
         */
        public Clinic getClinic() {
            return clinic;
        }

        /**
         *
         * @param clinic
         * The clinic
         */
        public void setClinic(Clinic clinic) {
            this.clinic = clinic;
        }

        /**
         *
         * @return
         * The appointments
         */
        public List<Appointment> getAppointments() {
            return appointments;
        }

        /**
         *
         * @param appointments
         * The appointments
         */
        public void setAppointments(List<Appointment> appointments) {
            this.appointments = appointments;
        }

    }


   

}
