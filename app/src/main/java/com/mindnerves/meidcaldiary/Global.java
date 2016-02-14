package com.mindnerves.meidcaldiary;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

import Model.AlarmReminderVM;
import Model.AllPatients;
import Model.AllTreatmentPlanVm;
import Model.Appointment;
import Model.Assistant;
import Model.Clinic;
import Model.ClinicAppointment;
import Model.ClinicDetailVm;
import Model.Delegation;
import Model.DoctorSearchResponse;
import Model.Field;
import Model.FileUpload;
import Model.Patient;
import Model.PatientClinic;
import Model.ReminderVM;
import Model.Schedule;
import Model.ShowTemplate;
import Model.Template;


/**
 * Created by MNT on 26-Mar-15.
 */
public class Global extends Application {

   private ArrayList<Patient> patientList;
   private ArrayList<DoctorSearchResponse> doctorList;
   private ArrayList<Assistant> assistantList;
   private ArrayList<Delegation> delegationList;
   private ArrayList<Patient> dependencyPatient;
   private ShowTemplate temp;
   private Field field;
   private List<AlarmReminderVM> alarmTime;
   private String timeChange;
   public  List<Field> removeFieldList = new ArrayList<Field>();
   private String location = "";
   private List<DoctorSearchResponse> doctorSearchResponses;
   private List<AllPatients> allPatients;

   private List<DoctorSearchResponse> allPatientOfDoctor;
   private String timeText;
   private AlarmReminderVM alaramObj;
   private ReminderVM reminderVM;
   private List<Clinic> allClinicsList;
   private List<ClinicDetailVm> allDoctorClinicList;
   private String appointmentDate;
   private String appointmentTime;
   private FileUpload upload;
   private String testPrescribed;
   private String dateString;
   private String endDateString;
   private Uri uri;
   private Patient patient;
   private String patientId;
   private Clinic clinic;
   private String doctorId;
   private ArrayList<Schedule> clinicSlots;
   private Appointment appointment;
   private Boolean summaryJump;
   private List<ClinicDetailVm> clinicDetailVm = new ArrayList<ClinicDetailVm>();
   public ClinicAppointment clinicDetailsData = new ClinicAppointment();
   public ArrayList<DoctorSearchResponse> doctorSpeciality;
   public String shift1AppointmentStatus;
   public String shift2AppointmentStatus;
   public String shift3AppointmentStatus;
   public String specialityString;
   public List<PatientClinic> patientClinics;
   public String diagnosisTestTime;
   public Double userLatitude = 0.0;
   public Double userLongitude = 0.0;
   public String countryName;
   public String regionName;
   public String city;

    public List<String> getAllCountriesList() {
        return allCountriesList;
    }

    public void setAllCountriesList(List<String> allCountriesList) {
        this.allCountriesList=new ArrayList<String>();
        this.allCountriesList = allCountriesList;
    }

    public List<String> allCountriesList=null;
    private List<AllTreatmentPlanVm> allFinance = new ArrayList<AllTreatmentPlanVm>();

    public List<AllTreatmentPlanVm> getAllFinance() {
        return allFinance;
    }

    public void setAllFinance(List<AllTreatmentPlanVm> allFinance) {
        this.allFinance = allFinance;
    }

    public List<ClinicDetailVm> getClinicDetailVm() {
        return clinicDetailVm;
    }

    public void setClinicDetailVm(List<ClinicDetailVm> clinicDetailVm) {
        this.clinicDetailVm = clinicDetailVm;
    }

    public List<Field> getRemoveFieldList() {
        return removeFieldList;
    }

    public void setRemoveFieldList(List<Field> removeFieldList) {
        this.removeFieldList = removeFieldList;
    }

    public List<DoctorSearchResponse> getDoctorSearchResponses() {
        return doctorSearchResponses;
    }

    public void setDoctorSearchResponses(List<DoctorSearchResponse> doctorSearchResponses) {
        this.doctorSearchResponses = doctorSearchResponses;
    }

    public ArrayList<Patient> getPatientList() {
        return patientList;
    }
    public void setPatientList(ArrayList<Patient> patientList) {
        this.patientList = patientList;
    }
    public ArrayList<DoctorSearchResponse> getDoctorList() {
        return doctorList;
    }
    public void setDoctorList(ArrayList<DoctorSearchResponse> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<Assistant> getAssistantList() {
        return assistantList;
    }

    public void setAssistantList(ArrayList<Assistant> assistantList) {
        this.assistantList = assistantList;
    }

    public ShowTemplate getTemp() {
        return temp;
    }

    public void setTemp(ShowTemplate temp) {
        this.temp = temp;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Delegation> getDelegationList() {
        return delegationList;
    }

    public void setDelegationList(ArrayList<Delegation> delegationList) {
        this.delegationList = delegationList;
    }

    public ArrayList<Patient> getDependencyPatient() {
        return dependencyPatient;
    }

    public void setDependencyPatient(ArrayList<Patient> dependencyPatient) {
        this.dependencyPatient = dependencyPatient;
    }

    public ReminderVM getReminderVM() {
        return reminderVM;
    }

    public void setReminderVM(ReminderVM reminderVM) {
        this.reminderVM = reminderVM;
    }

    public List<Clinic> getAllClinicsList() {
        return allClinicsList;
    }

    public void setAllClinicsList(List<Clinic> allClinicsList) {
        this.allClinicsList = allClinicsList;
    }

    public List<ClinicDetailVm> getAllDoctorClinicList() {
        return allDoctorClinicList;
    }

    public void setAllDoctorClinicList(List<ClinicDetailVm> allDoctorClinicList) {
        this.allDoctorClinicList = allDoctorClinicList;
    }

    public List<DoctorSearchResponse> getAllPatientOfDoctor() {
        return allPatientOfDoctor;
    }

    public void setAllPatientOfDoctor(List<DoctorSearchResponse> allPatientOfDoctor) {
        this.allPatientOfDoctor = allPatientOfDoctor;
    }

    public List<AllPatients> getAllPatients() {
        return allPatients;
    }

    public void setAllPatients(List<AllPatients> allPatients) {
        this.allPatients = allPatients;
    }



    public List<AlarmReminderVM> getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(List<AlarmReminderVM> alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getTimeChange() {
        return timeChange;
    }
    public void setTimeChange(String timeChange) {
        this.timeChange = timeChange;
    }
    public void setChangeFieldItems(Field changeFieldItems) {
        changeFieldItems = changeFieldItems;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public AlarmReminderVM getAlaramObj() {
        return alaramObj;
    }

    public void setAlaramObj(AlarmReminderVM alaramObj) {
        this.alaramObj = alaramObj;
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

    public FileUpload getUpload() {
        return upload;
    }

    public void setUpload(FileUpload upload) {
        this.upload = upload;
    }

    public String getTestPrescribed() {
        return testPrescribed;
    }

    public void setTestPrescribed(String testPrescribed) {
        this.testPrescribed = testPrescribed;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public ArrayList<Schedule> getClinicSlots() {
        return clinicSlots;
    }

    public void setClinicSlots(ArrayList<Schedule> clinicSlots) {
        this.clinicSlots = clinicSlots;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Boolean getSummaryJump() {
        return summaryJump;
    }

    public void setSummaryJump(Boolean summaryJump) {
        this.summaryJump = summaryJump;
    }

    public ArrayList<DoctorSearchResponse> getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(ArrayList<DoctorSearchResponse> doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getShift1AppointmentStatus() {
        return shift1AppointmentStatus;
    }

    public void setShift1AppointmentStatus(String shift1AppointmentStatus) {
        this.shift1AppointmentStatus = shift1AppointmentStatus;
    }

    public String getShift2AppointmentStatus() {
        return shift2AppointmentStatus;
    }

    public void setShift2AppointmentStatus(String shift2AppointmentStatus) {
        this.shift2AppointmentStatus = shift2AppointmentStatus;
    }

    public String getShift3AppointmentStatus() {
        return shift3AppointmentStatus;
    }

    public void setShift3AppointmentStatus(String shift3AppointmentStatus) {
        this.shift3AppointmentStatus = shift3AppointmentStatus;
    }

    public String getSpecialityString() {
        return specialityString;
    }

    public void setSpecialityString(String specialityString) {
        this.specialityString = specialityString;
    }

    public List<PatientClinic> getPatientClinics() {
        return patientClinics;
    }

    public void setPatientClinics(List<PatientClinic> patientClinics) {
        this.patientClinics = patientClinics;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        this.userLongitude = userLongitude;
    }
}
