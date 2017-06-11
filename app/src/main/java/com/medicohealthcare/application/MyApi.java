package com.medicohealthcare.application; /**
 * Created by MNT on 17-Feb-15.
 */

import com.medicohealthcare.model.AppointmentFeedback;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.AppointmentPatientIds;
import com.medicohealthcare.model.AppointmentResponse;
import com.medicohealthcare.model.AppointmentStatusRequest;
import com.medicohealthcare.model.ChatMessageCounts;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.ClinicByDoctorRequest;
import com.medicohealthcare.model.ClinicId;
import com.medicohealthcare.model.ClinicPersonRequest;
import com.medicohealthcare.model.ClinicSlotDetails;
import com.medicohealthcare.model.Country;
import com.medicohealthcare.model.CustomProcedureTemplate1;
import com.medicohealthcare.model.CustomTemplateId;
import com.medicohealthcare.model.Delegation;
import com.medicohealthcare.model.DependentDelegatePerson;
import com.medicohealthcare.model.DependentDelegatePersonRequest;
import com.medicohealthcare.model.Diagnosis;
import com.medicohealthcare.model.DiagnosticStatusRequest;
import com.medicohealthcare.model.DiagnosticTest;
import com.medicohealthcare.model.DoctorAppointment;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.DoctorClinicId;
import com.medicohealthcare.model.DoctorClinicRequest;
import com.medicohealthcare.model.DoctorHoliday;
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.model.DoctorId1;
import com.medicohealthcare.model.DoctorIdPatientId;
import com.medicohealthcare.model.DoctorNotes;
import com.medicohealthcare.model.DoctorProfile;
import com.medicohealthcare.model.DoctorReview;
import com.medicohealthcare.model.DoctorSearchResult;
import com.medicohealthcare.model.DoctorShortProfile;
import com.medicohealthcare.model.DoctorSlotBookings;
import com.medicohealthcare.model.FileUpload;
import com.medicohealthcare.model.FileUpload1;
import com.medicohealthcare.model.FinanceDetails;
import com.medicohealthcare.model.FinanceReportRequest;
import com.medicohealthcare.model.FinanceSummary;
import com.medicohealthcare.model.InvoiceDetails1;
import com.medicohealthcare.model.InvoiceId;
import com.medicohealthcare.model.LinkedPersonRequest;
import com.medicohealthcare.model.Logindata;
import com.medicohealthcare.model.Medicine;
import com.medicohealthcare.model.MedicineId;
import com.medicohealthcare.model.MedicineStatusRequest;
import com.medicohealthcare.model.Message;
import com.medicohealthcare.model.MessageRequest;
import com.medicohealthcare.model.PatientAppointmentByDoctor;
import com.medicohealthcare.model.PatientAppointmentsVM;
import com.medicohealthcare.model.PatientDiagnostic;
import com.medicohealthcare.model.PatientId;
import com.medicohealthcare.model.PatientMedicine;
import com.medicohealthcare.model.PatientProfile;
import com.medicohealthcare.model.PatientReview;
import com.medicohealthcare.model.PatientShortProfile;
import com.medicohealthcare.model.PatientTestId;
import com.medicohealthcare.model.PatientVisits;
import com.medicohealthcare.model.Payment;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.PersonAndCategoryId1;
import com.medicohealthcare.model.PersonDetailProfile;
import com.medicohealthcare.model.PersonID;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.model.RegistrationVerificationRequest;
import com.medicohealthcare.model.RemoveMedicineRequest;
import com.medicohealthcare.model.RemovePatientTestRequest;
import com.medicohealthcare.model.RemoveVisitDocument1;
import com.medicohealthcare.model.ResetPassword;
import com.medicohealthcare.model.ResponseAddDocuments;
import com.medicohealthcare.model.ResponseAddTemplates1;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.model.ResponseVm;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.SearchParameterRequest;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.model.ServerResponseStatus;
import com.medicohealthcare.model.Specialization;
import com.medicohealthcare.model.SummaryResponse;
import com.medicohealthcare.model.Symptom;
import com.medicohealthcare.model.TreatmentId;
import com.medicohealthcare.model.TreatmentId1;
import com.medicohealthcare.model.TreatmentPlan1;
import com.medicohealthcare.model.TreatmentPlanRequest;
import com.medicohealthcare.model.VerificationCode;
import com.medicohealthcare.model.VisitEditLogRequest;
import com.medicohealthcare.model.VisitEditLogResponse;
import com.medicohealthcare.model.forgotPassword;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface MyApi {

    @POST("/verifyCodeForNewRegistration1")
    void verifyCodeForNewRegistration(@Body RegistrationVerificationRequest param, Callback<ServerResponseStatus> cb);
    @POST("/getVerificationCodeForNewRegistration1")
    void getVerificationCodeForNewRegistration(@Body RegistrationVerificationRequest param, Callback<ServerResponseStatus> cb);

    @POST("/getChatPersonList")
    void getChatPersonList(@Body ProfileId param, Callback<List<Person>> cb);
    @POST("/getMessages")
    void getMessages(@Body MessageRequest request, Callback<List<Message>> cb);

    @POST("/getMessagesAfter")
    void getMessagesAfter(@Body MessageRequest request, Callback<List<Message>> cb);

    @POST("/getNewMessageCountsByRecipient")
    void getNewMessageCountsByRecipient(@Body MessageRequest request, Callback<List<ChatMessageCounts>> cb);

    @POST("/sendMessage")
    void sendMessages(@Body Message request, Callback<ServerResponse> cb);

    @POST("/checkMobileEmailAvailability")
    void checkMobileEmailAvailability(@Body Person param, Callback<ServerResponse> cb);

    @POST("/searchPerson")
    void searchPerson(@Body SearchParameterRequest param, Callback<List<Person>> cb);

    @POST("/searchPerson")
    void searchPersonById(@Body SearchParameterRequest param, Callback<Person> cb);

    @POST("/searchPerson")
    void searchDoctor(@Body SearchParameterRequest param, Callback<List<DoctorSearchResult>> cb);

    @POST("/searchClinic")
    void searchClinic(@Body SearchParameterRequest param, Callback<List<Clinic1>> cb);

    @POST("/searchClinic")
    void searchClinicById(@Body SearchParameterRequest param, Callback<Clinic1> cb);

    @POST("/getSupportedCountryList")
    void getSupportedCountryList(@Body ProfileId param, Callback<List<Country>> cb);

    @POST("/getClinicSlotBookingByDoctor")
    void getClinicSlotBookingByDoctor(@Body DoctorClinicId param, Callback<List<DoctorSlotBookings>> cb);

    @POST("/getDoctorHolidayList")
    void getDoctorHolidayList(@Body DoctorHoliday param, Callback<List<DoctorHoliday>> cb);

    @POST("/getPatientAppointment")
    void getPatientAppointment(@Body AppointmentId1 param, Callback<DoctorAppointment> cb);

    @POST("/createAppointment")
    void createAppointment(@Body DoctorAppointment param, Callback<AppointmentResponse> cb);

    @POST("/updateAppointment")
    void updateAppointment(@Body DoctorAppointment param, Callback<AppointmentResponse> cb);

    @POST("/cancelAppointment")
    void cancelAppointment(@Body AppointmentId1 param, Callback<AppointmentResponse> cb);

    @POST("/setAppointmentStatus")
    void setAppointmentStatus(@Body AppointmentStatusRequest param, Callback<ServerResponse> cb);
    @POST("/setAppointmentVisitType")
    void setAppointmentVisitType(@Body AppointmentStatusRequest param, Callback<ServerResponse> cb);
    @POST("/setAppointmentVisitStatus")
    void setAppointmentVisitStatus(@Body AppointmentStatusRequest param, Callback<ServerResponse> cb);
    @POST("/addDoctorHoliday")
    void addDoctorHoliday(@Body DoctorHoliday param, Callback<ServerResponse> cb);
    @POST("/removeDoctorHoliday")
    void removeDoctorHoliday(@Body DoctorHoliday param, Callback<ServerResponse> cb);
    @POST("/getFinanceSummary1")
    void getFinanceSummary(@Body FinanceReportRequest param, Callback<List<FinanceSummary>> cb);

    @POST("/getFinanceSummaryDetails")
    void getFinanceSummaryDetails(@Body FinanceReportRequest param, Callback<List<FinanceDetails>> cb);
    @POST("/getAllReviews1")
    void getReviewsForDoctor(@Body DoctorId1 param, Callback<List<PatientReview>> cb);

    @POST("/getAllReviewsForPatient")
    void getAllReviewsForPatient(@Body PatientId param, Callback<List<DoctorReview>> cb);

    @POST("/setMedicineAlarm")
    void setMedicineAlarm(@Body MedicineStatusRequest param, Callback<ResponseCodeVerfication> cb);

    @POST("/setDiagnosticTestAlarm")
    void setDiagnosticTestAlarm(@Body DiagnosticStatusRequest param, Callback<ResponseCodeVerfication> cb);

    @POST("/updatePatientVisitInvoiceDetails")
    void updatePatientVisitInvoiceDetails(@Body InvoiceDetails1 param, Callback<ResponseCodeVerfication> cb);

    @POST("/addPayment")
    void addPayment(@Body Payment param, Callback<ResponseCodeVerfication> cb);
//    @POST("/registerPatient")
//    void registerPatient(@Body RegisterUserData param, Callback<String> cb);
//
//    @POST("/registerDoctor")
//    void registerDoctor(@Body RegisterDoctorData param, Callback<String> cb);
//
//    @POST("/registerAssistent")
//    void registerAssistent(@Body RegisterAssistantData param, Callback<String> cb);

//    @GET("/searchDoctors")
//    void searchDoctors(@Query("name") String name, Callback<ArrayList<DoctorSearchResponse>> callback);
//
//    @POST("/addDoctor")
//    void registerAddDoctor(@Query("id") String patientId, @Body DoctorSearchResponse res, Callback<Response> callback);

    @POST("/patientDoctor")
    void patientDoctor(@Query("id") String patientId, @Query("doctors") String doctorsIds, Callback<Response> callback);

    @GET("/searchPatients")
//    void searchPatients(@Query("name") String name, Callback<ArrayList<Patient>> callback);

    @POST("/doctorsPatient")
    void addPatient(@Query("id") String patientId, @Query("patients") String patientIds, Callback<String> callback);

//    @POST("/removeDoctorsPatient")
//    void removeDoctorsPatient(@Body RemovePatients rem, Callback<String> callback);
//
//    @POST("/removePatientsDoctor")
//    void removePatientsDoctor(@Body RemoveDoctors remove, Callback<Response> callback);

//    @GET("/getPatientsDoctors")
//    void getPatientsDoctors(@Query("id") String name, Callback<List<DoctorSearchResponse>> callback);
//
//    @GET("/getDoctorsPatients")
//    void getDoctorsPatients(@Query("id") String name, Callback<List<Patient>> callback);

//    @GET("/searchClinic")
//    void searchClinic(@Query("clinicName") String clinicName, Callback<List<Clinic>> callback);
//
    @POST("/addClinic")
    void addClinic(@Body Clinic1 clinicDetails, Callback<ServerResponse> callback);

    @POST("/createClinic")
    void createClinic(@Body Clinic1 clinicDetails, Callback<ServerResponse> callback);

    @POST("/createSlot")
    void createSlot(@Body ClinicSlotDetails request, Callback<ResponseCodeVerfication> callback);

    @POST("/updateSlot")
    void updateSlot(@Body ClinicSlotDetails request, Callback<ResponseCodeVerfication> callback);

    @POST("/removeSlot")
    void removeSlot(@Body DoctorClinicId request, Callback<ResponseCodeVerfication> callback);

    @POST("/setSlotAvailability")
    void setSlotAvailability(@Body DoctorClinicId request, Callback<ResponseCodeVerfication> callback);

    @POST("/getSlotDetail")
    void getSlotDetail(@Body DoctorClinicId request, Callback<ClinicSlotDetails> callback);

    @POST("/getAllSlotDetails")
    void getAllSlotDetails(@Body DoctorClinicRequest request, Callback<List<ClinicSlotDetails>> callback);

    @POST("/getClinicDetails")
    void getClinicDetails(@Body ClinicId clinicId, Callback<Clinic1> callback);

    @POST("/updateClinic")
    void updateClinic(@Body Clinic1 clinic, Callback<ServerResponse> response);

//    @POST("/saveDoctorClinicScheduleTime")
//    void addTimeSchedule(@Body DoctorClinicSchedule toc, Callback<Response> callback);

//    @GET("/getClinicScheduleshiftDetails")
//    void getClinicDetails(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, Callback<ArrayList<DoctorClinicSchedule>> callback);

//    @GET("/getAllDoctors")
//    void getAllDoctors(Callback<List<Doctor>> callback);

//    @GET("/getDoctorAssistant")
//    void getDoctorAssistant(@Query("id") String name, Callback<ArrayList<Assistant>> callback);

    @POST("/removeDoctorsClinic")
    void removeDoctorClinic(@Query("id") String doctorId, @Query("clinics") String clinicIds, Callback<String> callback);

//    @GET("/getDoctorsClinic")
//    void doctorClinics(@Query("doctorId") String doctorId, Callback<List<Clinic>> callback);

//    @GET("/searchAssistants")
//    void searchAssistants(@Query("name") String name, Callback<ArrayList<Assistant>> callback);

    @POST("/addDoctorsAssistants")
    void addDoctorsAssistants(@Query("id") String id, @Query("assistants") String assistantIds, Callback<Response> cb);

//    @POST("/addAssistant")
//    void addAssistant(@Query("doctorId") String id, @Body Assistant ats, Callback<Response> cb);

    @POST("/removeDoctorAssistance")
    void removeDoctorAssistance(@Query("id") String doctorId, @Query("assistants") String assistantIds, Callback<Response> cb);

//    @POST("/addPatientDependent")
//    void addPatientDependent(@Body AddDependent a, Callback<Response> cb);

//    @GET("/getAllPatientDependents")
//    void getAllPatientDependents(@Query("id") String patientId, Callback<ArrayList<Patient>> callback);

    @POST("/removePatientDependent")
    void removePatientDependent(@Query("id") String patientId, @Query("dependents") String patientIds, Callback<Response> response);

//    @POST("/confirmOrDenyParent")
//    void confirmOrDenyParent(@Body AddConfirmDeny a, Callback<Response> cb);

//    @GET("/getAllParents")
//    void getAllParents(@Query("id") String patientId, Callback<ArrayList<Patient>> callback);

    @GET("/getAllDelegatesForParent")
    void getAllDelegatesForParent(@Query("id") String patientId, @Query("type") String type, Callback<ArrayList<Delegation>> response);

    @GET("/getAllParentsForDelegates")
    void getAllParentsForDelegates(@Query("id") String patientId, @Query("type") String type, Callback<ArrayList<Delegation>> response);

//    @POST("/confirmOrDenyParentForDelegates")
//    void confirmOrDenyParentForDelegates(@Body AddConfirmDenyDelegate a, Callback<Response> cb);

//    @POST("/addDeleagatesForParent")
//    void addDeleagatesForParent(@Body AddDelegate a, Callback<Response> cb);
//
//    @GET("/getAllDoctorsAndAssistants")
//    void getAllDoctorsAndAssistants(Callback<ArrayList<GetDelegate>> cb);
//
//    @POST("/removeDelegatesForParent")
//    void removeDelegatesForParent(@Body RemoveDelegate rem, Callback<Response> cb);
//
//    @POST("/addPatient")
//    void addPatient(@Query("id") String doctorId, @Body BucketPatient pat, Callback<String> callback);
//
//    @POST("/addTemplate")
//    void addTemplate(@Body Template tem, Callback<Response> cb);
//
//    @POST("/addTemplateAllField")
//    void addTemplateAllField(@Body ArrayList<Field> fieldArrayList, Callback<Response> cb);

    /*@GET("/getTemplateAllFields")
    public void getTemplateAllFields(@Query("id")String templateId,Callback<ArrayList<Field>> callback);*/

//    @POST("/addField")
//    void addField(@Body Field f, Callback<Response> cb);

//    @POST("/updateField")
//    void updateField(@Body UpdateField uf, Callback<Response> cb);

    @POST("/removeFields")
    void removeFields(@Query("id") String doctorId, @Query("fields") String fields, Callback<Response> cb);

//    @POST("/removeSelectedFields")
//    void removeSelectedFields(@Body List<Field> removeList, Callback<Response> cb);

//    @GET("/getAllTemplates")
//    void getAllTemplate(@Query("id") String doctorId, @Query("procedureName") String procedureName, Callback<ArrayList<ShowTemplate>> callback);
//
//    @GET("/getAllFields")
//    void getAllFields(@Query("id") String templateId, Callback<ArrayList<Field>> callback);

//    @GET("/getChatMember")
//    void getChatMember(@Query("id") String doctorId, @Query("type") String type, Callback<ArrayList<Chat>> callback);

//    @GET("/getDoctorsClinicsDetails")
//    void getAllClinicsDetailsList(@Query("doctorId") String doctorId, @Query("type") String type, Callback<List<ClinicDetailVm>> callback);
//
//    @GET("/getClinicsAppointmentDetails")
//    void getAllClinicsAppointment(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("shift") String shift, @Query("date") String date, Callback<List<ClinicAppointment>> callback);
//
//    @POST("/saveClinicsAppointmentDetails")
//    void saveClinicsAppointmentData(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);

    @GET("/cancelClinicsAppointment")
    void cancelClinicsAppointment(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, @Query("clinicId") Integer clinicId, @Query("shift") String shift, Callback<String> response);

//    @GET("/getAllPatientAppointment")
//    void getPatientAppointment(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, Callback<List<ClinicAppointment>> response);

    @GET("/saveVisitedPatientAppointment")
    void saveVisitedPatientAppointment(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("clinicId") String clinicId, @Query("shift") String shift, @Query("isVisited") Integer isVisited, Callback<String> response);

//    @GET("/getPatientReminder")
//    void getPatientReminderData(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<ReminderVM> response);

//    @POST("/savePatientReminder")
//    void savePatientReminderDetails(@Body ReminderVM reminderVM, Callback<ReminderVM> response);

    @Multipart
    @POST("/uploadFiles")
    void uploadFile(@Part("picture") TypedFile file, @Part("type") String type, @Part("doctorId") String doctorId, @Part("patientId") String patientId, @Part("assistentId") String assistantId, @Part("documentType") String documentType, @Part("name") String name, @Part("category") String category, @Part("appointmentDate") String appointmentDate, @Part("appointmentTime") String appointmentTime, @Part("clinicId") String clinicId, @Part("clinicName") String clinicName, Callback<FileUpload> cb);
//
//    @GET("/getUploadedFiles")
//    void getFiles(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<FileUpload>> cb);

//    @GET("/getAllPatientClinics")
//    void getAllPatientClinics(@Query("patientId") String patientId, Callback<List<Clinic>> response);
//
//    @GET("/getAllDoctorClinics")
//    void getAllDoctorClinics(@Query("doctorId") String doctorId, @Query("date") Date currentDate, Callback<List<Clinic>> response);

//    @GET("/getAlldoctorsOfClinic")
//    void getAlldoctorsOfClinicDetail(@Query("clinicId") String clinicId, Callback<List<ClinicDetailVm>> callback);
//
//    //All Doctors API ---------------------------------------------------------------------------------
//    @GET("/getAllPatientInformation")
//    void getAllPatientInformation(@Query("doctorId") String doctorId, Callback<List<AllPatients>> callback);

//    @GET("/getAllDoctorPatientClinics")
//    void getAllDoctorPatientClinics(@Query("doctorId") String doctorId, @Query("patientId") String patientId, Callback<List<ClinicDetailVm>> callback);

//    @POST("/saveDoctorNotes")
//    void saveDoctorNotes(@Body DoctorNotesVM doctorNotesVM, Callback<JsonObject> response);

//    @GET("/getDoctorNotes")
//    void getDoctorNotesData(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<DoctorNotesVM> callback);
//
//    @GET("/getDoctorProcedures")
//    void getAllProcedure(@Query("doctorId") String doctorId, Callback<ArrayList<ShowProcedure>> callback);
//
//    @POST("/saveDoctorProcedures")
//    void saveDoctorProcedures(@Body ShowProcedure tem, Callback<Response> cb);

   /* @POST("/saveTreatmentPlan")
    public void saveTreatmentPlanData(@Body TreatmentPlan tem,Callback<TreatmentPlan> cb);

    @GET("/getAllTreatmentPlan")
    public void getAllTreatmentPlan(@Query("doctorId")String doctorId,@Query("patientId")String patientId, @Query("appointmentDate")String appointmentDate,@Query("appointmentTime")String appointmentTime,Callback<AllTreatmentPlanVm> callback);
*/
//    @POST("/updateTreatementTemplate")
//    void updateFieldTemplateData(@Body Field field, Callback<Field> cb);
//
//    @POST("/saveTreatementTemplate")
//    void saveTreatmentPlanData(@Body TreatmentPlan treatmentPlan, Callback<TreatmentPlan> cb);
//
//
//    @GET("/getAllTreatmentPlan")
//    void getAllTreatmentPlan(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<AllTreatmentPlanVm> callback);
//
//    @POST("/saveInvoices")
//    void saveInvoicesData(@Body TreatmentPlan tem, Callback<TreatmentPlan> cb);

//    @GET("/getAllInvoices")
//    void getAllInvoicesData(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<AllTreatmentPlanVm> callback);
//
//    @GET("/getAllClinicsAppointment")
//    void getAllClinicsAppointmentData(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("appointmentDate") Date appointmentDate, Callback<List<AllClinicAppointment>> callback);
//
//    @GET("/getAllClinicsWeekAppointment")
//    void getAllClinicWeekAppointment(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("appointmentDate") String appointmentDate, Callback<List<AllClinicAppointment>> callback);
//


//    @POST("/saveShareWithPatientTotalInvoice")
//    void saveShareWithPatientTotalInvoice(@Body TotalInvoice invoice, Callback<Response> cb);
//
//    @POST("/saveTotalInvoice")
//    void saveTotalInvoice(@Body TotalInvoice invoice, Callback<TotalInvoice> cb);
//
//    @GET("/getTotalInvoice")
//    void getInvoice(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<TotalInvoice> cb);
//
//    @GET("/getUploadedFilesForPatient")
//    void getFilesPatient(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<FileUpload>> cb);

    @GET("/getFile")
    void getFile(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, @Query("fileName") String fileName, Callback<TypedFile> cb);

    @Multipart
    @POST("/profilePicturePatient")
    void setProfilePicPatient(@Part("patientId") String patientId, @Part("picture") TypedFile file, Callback<String> cb);

    @Multipart
    @POST("/profilePictureDoctor")
    void setProfilePicDoctor(@Part("doctorId") String doctorId, @Part("picture") TypedFile file, Callback<String> cb);

    @Multipart
    @POST("/profilePictureAssistant")
    void setProfilePicAssistant(@Part("assistantId") String assistantId, @Part("picture") TypedFile file, Callback<String> cb);

    @GET("/availableEmail")
    void isEmailIdAvailable(@Query("email") String email, Callback<String> cb);


//
//    @GET("/getProfileDoctor")
//    void getProfileDoctor(@Query("email") String email, Callback<PersonTemp> cb);

    @GET("/getPicture")
    void getPicture(@Query("path") String path, Callback<TypedFile> cb);

    @Multipart
    @POST("/profilePictureUpdateDoctor")
    void picutreUpdateDoctor(@Part("picture") TypedFile file, @Part("email") String email, Callback<String> cb);

    @POST("/updateProfile")
    void updateProfile(@Body Person person, Callback<ServerResponse> cb);

    @POST("/createProfile1")
    void createDoctorProfile(@Body Person person, Callback<ServerResponse> cb);

    @POST("/createProfile1")
    void createProfile(@Body Person person, Callback<ServerResponse> cb);

    @POST("/registerProfile")
    void createProfileWithVerification(@Body Person person, Callback<ServerResponse> cb);

    @POST("/createProfile1")
    void createDependentProfile(@Body DependentDelegatePerson person, Callback<ServerResponse> cb);

    @POST("/createDetailedProfile")
    void createDetailedProfile(@Body PersonDetailProfile person, Callback<ServerResponse> cb);

    @POST("/updateDetailedProfile")
    void updateDetailedProfile(@Body PersonDetailProfile person, Callback<ServerResponse> cb);

//    @POST("/updateDoctorProfile")
//    void updateDoctorProfile(@Body PersonTemp person, Callback<String> cb);
//
//    @GET("/getProfilePatient")
//    void getProfilePatient(@Query("email") String email, Callback<PersonTemp> cb);

    @Multipart
    @POST("/udatePatientProfilePicture")
    void updatePatientProfilePicture(@Part("picture") TypedFile file, @Part("email") String email, Callback<String> cb);

//    @POST("/updatePatientProfile")
//    void updatePatientProfile(@Body PersonTemp person, Callback<String> cb);
//
//    @POST("/addPatientDependentWithoutConfirmaton")
//    void addPatientDependentWithoutConfirmat(@Body AddDependent a, Callback<Response> cb);

    @POST("/patientDoctorAdd")
    void patientDoctorAdd(@Query("id") String patientId, @Query("doctor") String doctorsId, Callback<Response> callback);

    @GET("/getAllSpeciality")
    void getAllSpeciality(Callback<List<String>> cb);

    @GET("/getPassword")
    void getPassword(@Query("id") String id, Callback<String> cb);

//    @POST("/editClinic")
//    void editClinic(@Body Clinic clinicDetails, Callback<String> callback);

//    @GET("/getAllParentsDoctor")
//    void getAllParentsDoctor(@Query("id") String doctorId, Callback<ArrayList<Patient>> callback);
//
//    @GET("/getAllDoctorDependents")
//    void getAllDoctorDependents(@Query("id") String doctorId, Callback<ArrayList<Patient>> callback);
//
//    @POST("/addDoctorDependentWithoutConfirmaton")
//    void addDoctorDependentWithoutConfirmat(@Body AddDependentDoctor a, Callback<Response> cb);
//
//    @POST("/addDoctorDependent")
//    void addDoctorDependent(@Body AddDependentDoctor a, Callback<Response> cb);
//
//    @POST("/confirmOrDenyParentDoctor")
//    void confirmOrDenyParentDoctor(@Body AddConfirmDeny a, Callback<Response> cb);

    @POST("/deletePatient")
    void deletePatient(@Query("id") String id, Callback<String> cb);

    @POST("/deleteDoctor")
    void deleteDoctor(@Query("id") String id, Callback<String> cb);

    @POST("/deleteAssistant")
    void deleteAssistant(@Query("id") String id, Callback<String> cb);

//    @POST("/saveClinicAppointment")
//    void saveClinicAppointment(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);
//
//    @GET("/homeCountPatient")
//    void homeCountPatient(@Query("id") String patientId, Callback<HomePatientCount> response);
//
//    @POST("/setStatusSlot")
//    void setStatusSlot(@Body ModeVM vm, Callback<String> cb);
//
//    @POST("/saveFeedBackPatient")
//    void saveFeedbackPatient(@Body FeedbackVM vm, Callback<ClinicAppointment> cb);
//
//    @GET("/getAllDoctorFinance")
//    void getAllDoctorFinance(@Query("doctorId") String doctorId, Callback<List<AllTreatmentPlanVm>> callback);
//
//    @GET("/getAllHistory")
//    void getHistry(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<SummaryHistoryVM>> response);
//
//    @POST("/saveDoctorAppointment")
//    void saveDoctorAppointment(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);
//
//    @GET("/getPatientAppointment")
//    void getPatientAppointment(@Query("patientId") String patientId, Callback<List<Clinic>> callback);
//
//    @POST("/getFeedbackPatient")
//    void getFeedbackPatient(@Body FeedbackVM vm, Callback<FeedbackVM> callback);
//
//    @GET("/getClinicSpeciality")
//    void getClinicSpeciality(Callback<List<String>> cb);
//
//    @POST("/saveNotification")
//    void saveNotification(@Body NotificationVM vm, Callback<String> callback);
//
//    @GET("/getNotification")
//    void getNotification(@Query("email") String doctorId, Callback<NotificationVM> callback);
//
//    @POST("/deleteNotification")
//    void deleteNotification(@Body NotificationVM vm, Callback<String> callback);
//
//    @POST("/addDoctorsClinic")
//    void addDoctorClinic(@Query("id") String doctorId, @Query("clinics") String clinic, Callback<String> cb);
//
//    @POST("/updatePatientReminder")
//    void updatePatientReminder(@Body ReminderVM reminderVM, Callback<ReminderVM> response);
//
//    @POST("/deletePatientReminder")
//    void deletePatientReminder(@Body ReminderVM reminderVM, Callback<String> response);
//
//    @POST("/deleteFile")
//    void deleteFile(@Query("id") String id, Callback<String> cb);
//
//    @GET("/homeCountDoctor")
//    void homeCountDoctor(@Query("doctorId") String doctorId, Callback<HomeCountDoctor> cb);
//
//    @POST("/deleteTreatmentTemplate")
//    void deleteTreatmentTemplate(@Body AllProcedureVm vm, Callback<String> cb);
//
//    @POST("/deleteInvoiceTemplate")
//    void deleteInvoiceTemplate(@Body AllProcedureVm vm, Callback<String> cb);
//
//    @GET("/doctorPatientsFeedback")
//    void doctorPatientFeedback(@Query("email") String email, Callback<List<AllPatients>> cb);
//
//    @GET("/doctorList")
//    void doctorList(@Query("user_lat") String latitude, @Query("user_long") String longitude, @Query("user_distance") String distance, Callback<ArrayList<DoctorSearchResponse>> callback);

    @FormUrlEncoded
    @POST("/getAllCountry")
    void getAllCountry(@retrofit.http.Field("junk") String junk, Callback<List<String>> cb);

    @GET("/getRegions")
    void getRegions(@Query("country") String country, Callback<List<String>> cb);

    @Multipart
    @POST("/doctorDocumentUpload")
    void doctorDocumentUpload(@Part("doctorId") String doctorId, @Part("document") TypedFile file, Callback<String> cb);

    @Multipart
    @POST("/updateDocumentDoctor")
    void updateDocumentDoctor(@Part("id") String id, @Part("document") TypedFile file, Callback<String> cb);

    @GET("/getAllCities")
    void getAllCity(@Query("country") String country, Callback<List<String>> cb);

    @Multipart
    @POST("/saveClinicPicture")
    void savePictureClinic(@Part("clinicId") String id, @Part("picture") TypedFile file, Callback<String> cb);

//	@POST("/saveDoctorPersonDetail")
//    void saveDoctorPersonDetail(@Body DoctorPersonalVM doctorPersonalVM, Callback<String> cb);









//New apis Integrated Raviraj

    @POST("/login")
    void login(@Body Logindata param, Callback<ResponseVm> cb);
//
    @POST("/getDoctorLandingPageDetails")
    void getDoctorLandingPageDetails(@Body DoctorId param, Callback<DoctorProfile> cb);
//
//    @POST("/getPatientList")
//    void getPatientList(@Body DoctorId param, Callback<List<AllPatients>> callback);
//
//
    @POST("/forgotPassword")
    void forgotPassword(@Body forgotPassword forgotObj, Callback<ResponseVm> cb);
//
//
    @POST("/verifyCode")
    void verifyCode(@Body VerificationCode verificationCode, Callback<ResponseCodeVerfication> cb);
//
//
//    @POST("/checkMobileEmailAvailability")
//    void checkMobileEmailAvailability(@Body MobileEmail mobilemail, Callback<ResponseCheckMobileEmailAvailability> cb);
//
    @POST("/changePassword")
    void changePassword(@Body ResetPassword resetPassword, Callback<ResponseCodeVerfication> cb);
//
//    @POST("/createProfile")
//    void createProfile(@Body CreateProfileData param, Callback<ResponseCreateProfile> cb);
//
//    @POST("/createDetailedProfile")
//    void createDetailedProfile(@Body CreateProfileDataForDoctorUpdateDetails param, Callback<ResponseCreateProfileForDoctorUpdateDetails> cb);
//
//    @POST("/getVerificationCodeForNewRegistration")
//    void getVerificationCodeForNewRegistration(@Body MobileEmail mobileEmail, Callback<ResponsegetVerificationCodeForNewRegistration> cb);
//
//    @POST("/verifyCodeForNewRegistration")
//    void verifyCodeForNewRegistration(@Body VerifyRegistrationMobileEmailCode verifyRegistrationMobileEmailCode, Callback<ResponseVerifyRegistrationMobileEmailCode> cb);

    @POST("/getPatientProfileList")
    void getPatientProfileList(@Body DoctorId param, Callback<List<PatientShortProfile>> callback);

    @POST("/getPersonLinkage")
    void getPersonLinkage(@Body LinkedPersonRequest param, Callback<List<Person>> callback);

    @POST("/addPersonLinkage")  
    void addPersonLinkage(@Body LinkedPersonRequest param, Callback<ResponseCodeVerfication> callback);

    @POST("/addClinic")
    void addClinic(@Body ClinicPersonRequest param, Callback<ResponseCodeVerfication> callback);
    @POST("/removeClinic")
    void removeClinic(@Body ClinicPersonRequest param, Callback<ResponseCodeVerfication> callback);

    @POST("/getAllDependentsDelegates")
    void getAllDependentsDelegates(@Body DependentDelegatePersonRequest param, Callback<List<DependentDelegatePerson>> callback);

    @POST("/getProfile")
    void getProfile(@Body ProfileId param, Callback<Person> callback);
    @POST("/getProfile")
    void getDependentProfile(@Body ProfileId param, Callback<DependentDelegatePerson> callback);

    @POST("/getDetailedProfile")
    void getDetailedProfile(@Body ProfileId param, Callback<PersonDetailProfile> callback);

    @POST("/getProfile")
    void getProfile1(@Body ProfileId param, Callback<Person> callback);

    @POST("/getPatientShortProfile")
    void getPatientShortProfile(@Body DoctorIdPatientId param, Callback<PatientShortProfile> callback);

    @POST("/getDoctorShortProfile")
    void getDoctorShortProfile(@Body DoctorIdPatientId param, Callback<DoctorShortProfile> callback);

//    @POST("/getClinicsByDoctor")
//    void getClinicsByDoctor(@Body DoctorId param, Callback<List<AppointmentSlotsByDoctor>> callback);

    @POST("/getClinicsByDoctor")
    void getClinicsByDoctor1(@Body DoctorId param, Callback<List<DoctorClinicDetails>> callback);

    @POST("/getClinicByDoctor")
    void getClinicByDoctor(@Body ClinicByDoctorRequest param, Callback<DoctorClinicDetails> callback);

//    @POST("/getPatientAppointmentsByDoctor")
//    void getPatientAppointmentsByDoctor(@Body DoctorIdPatientId param, Callback<ClinicPatientAppointments> callback);

    @POST("/getPatientAppointmentsByDoctor")
    void getPatientAppointmentsByDoctor1(@Body DoctorIdPatientId param, Callback<PatientAppointmentByDoctor> callback);

//    @POST("/cancelAppointment")
//    void cancelAppointment(@Body AppointmentId appointmentId, Callback<ResponseCodeVerfication> cb);

    @POST("/getPatientVisitDatesByDoctor1")
    void getPatientVisitDatesByDoctor1(@Body DoctorIdPatientId param,Callback<List<PatientVisits>> response);


//    @POST("/getClinicSlotAvailabilityByDoctor")
//    void getClinicSlotAvailabilityByDoctor(@Body DoctorClinicAppointments param,Callback< DoctorAppointmentsResponse> response);



//    @POST("/createAppointment")
//    void createAppointment(@Body DoctorCreatesAppointment param,Callback<DoctorCreatesAppoinementResponse> response);

    @POST("/setAppointmentVisitStatus")
    void setAppointmentVisitStatus(@Body AppointmentFeedback appointmentStatus, Callback<ResponseCodeVerfication> response);

    @POST("/setAppointmentStatus")
    void setAppointmentStatus(@Body AppointmentFeedback appointmentStatus, Callback<ResponseCodeVerfication> response);
    //

//    @POST("/updateAppointment")
//    void updateAppointment(@Body DoctorCreatesAppointment param,Callback<DoctorCreatesAppoinementResponse> response);


    //
    @POST("/getPatientVisitSummary")
    void getPatientVisitSummary(@Body AppointmentId1 param, Callback<SummaryResponse> response);

    @POST("/createPatientVisitSummary")
    void createPatientVisitSummary(@Body SummaryResponse reminderVM, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientVisitSummary")
    void updatePatientVisitSummary(@Body SummaryResponse reminderVM, Callback<ResponseCodeVerfication> response);

    @POST("/getAllMedicineDetails")
    void getAllMedicineDetails(@Body PatientId patientId, Callback<List<PatientMedicine>> response);

    @POST("/addPatientMedicine")
    void addPatientMedicine(@Body PatientMedicine addPatientMedicineSummary, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientMedicine")
    void updatePatientMedicine(@Body PatientMedicine addPatientMedicineSummary, Callback<ResponseCodeVerfication> response);

    @POST("/removePatientMedicine")
    void removePatientMedicine(@Body RemoveMedicineRequest removeMedicineRequest, Callback<ResponseCodeVerfication> response);


    @POST("/getPatientMedicine")
    void getPatientMedicine(@Body MedicineId medicineId, Callback<PatientMedicine> response);

    @POST("/getTestDetails")
    void getTestDetails1(@Body PatientTestId patientTestId, Callback<PatientDiagnostic> response);
//    @POST("/getTestDetails")
//    void getTestDetails(@Body PatientTestId patientTestId, Callback<TestDetails> response);
//
//
    @POST("/removePatientDiagnosticTest")
    void removePatientDiagnosticTest(@Body RemovePatientTestRequest removeMedicineRequest, Callback<ResponseCodeVerfication> response);


    @POST("/getPatientVisitDoctorNotes")
    void getPatientVisitDoctorNotes(@Body AppointmentId1 appointmentId, Callback<DoctorNotes> response);

    @POST("/addPatientVisitDoctorNotes")
    void addPatientVisitDoctorNotes(@Body DoctorNotes appointmentId, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientVisitDoctorNotes")
    void updatePatientVisitDoctorNotes(@Body DoctorNotes doctorNotes, Callback<ResponseCodeVerfication> response);

    @POST("/getPatientVisitEditLog")
    void getPatientVisitEditLog(@Body VisitEditLogRequest visitEditLogRequest, Callback<VisitEditLogResponse> response);

    @POST("/addPatientDiagnosticTest")
    void addPatientDiagnosticTest1(@Body PatientDiagnostic addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientDiagnosticTest")
    void updatePatientDiagnosticTest1(@Body PatientDiagnostic addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);

//    @POST("/addPatientDiagnosticTest")
//    void addPatientDiagnosticTest(@Body AddDiagnosisTestRequest addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);
//
//    @POST("/updatePatientDiagnosticTest")
//    void updatePatientDiagnosticTest(@Body AddDiagnosisTestRequest addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);
//
//    @POST("/getAllClinics")
//    void getAllClinics1(@Body ProfileId personID, Callback<List<Clinic1>> response);
//
//    @POST("/getAllClinics")
//    void getAllClinics1(@Body PersonID personID, Callback<List<Clinic1>> response);
//
    @POST("/getAllClinics")
    void getAllClinics(@Body PersonID personID, Callback<List<Clinic1>> response);

//    @POST("/getPatientVisitTreatmentPlan")
//    void getPatientVisitTreatmentPlan(@Query("appointmentId") String appointmentId, @Query("categoryId") String categoryId, Callback<List<TreatmentPlan>> response);
    @POST("/getPatientVisitTreatmentPlan")
    void getPatientVisitTreatmentPlan1(@Body TreatmentPlanRequest treatmentPlanRequest, Callback<List<TreatmentPlan1>> response);
    @POST("/getTreatmentPlan")
    void getTreatmentPlan(@Body TreatmentId1 treatmentId, Callback<TreatmentPlan1> response);
    @POST("/getTreatmentPlan")
    void getCustomTemplate(@Body TreatmentId1 treatmentId, Callback<TreatmentPlan1> response);



   /* @GET("/getAllClinics")
    void getAllClinics(Callback<List<Clinic>> cb);*/

    //Extra headers needs to be added
   /* httpPost.addHeader("x-patientId", "102");
    httpPost.addHeader("x-appointmentId", "584");
    httpPost.addHeader("x-clinicId", "101");
    httpPost.addHeader("x-type", "1");
    httpPost.addHeader("x-loggedinUserId", "102");
    httpPost.addHeader("x-fileName", "ANkLE BoNe X-ray.xlsx");*/

    @Multipart
    @POST("/addPatientVisitDocument")
    void addPatientVisitDocument(@Header("x-patientId") String patientId,
                                 @Header("x-appointmentId") String appointmentId,
                                 @Header("x-clinicId") String clinicId,
                                 @Header("x-type") String type,
                                 @Header("x-loggedinUserId") String loggedInUserId,
                                 @Header("x-fileName") String fileName,
                                 @Header("x-category") Byte category,
                                 @Header("x-subcategory") Integer subcategory,
                                 @Part("upload") TypedFile file, Callback<ResponseAddDocuments> response);


    @Multipart
    @POST("/updateProfilePicture")
    void updateProfilePicture(@Header("x-personId") String personId,
                                 @Header("x-filename") String filename,
                                 @Part("profilePicture") TypedFile file, Callback<ResponseAddDocuments> response);

    @POST("/getPatientVisitDocuments")
    void getPatientVisitDocuments(@Body AppointmentPatientIds appointmentPatientIds , Callback<List<FileUpload>> cb);

    @POST("/getPatientVisitDocuments")
    void getPatientVisitDocuments1(@Body AppointmentId1 appointmentPatientIds ,   Callback<List<FileUpload1>> cb);

//    @POST("/removePatientVisitDocuments") //{"fileId":4, "loggedinUserId":104}
//    void removePatientVisitDocuments(@Body RemoveVisitDocument removeVisitDocument , Callback<ResponseCodeVerfication> cb);

    @POST("/removePatientVisitDocuments") //{"fileId":4, "loggedinUserId":104}
    void removePatientVisitDocuments1(@Body RemoveVisitDocument1 removeVisitDocument , Callback<ResponseCodeVerfication> cb);

//    @POST("/getAllCustomTemplate")
//    void getAllCustomTemplate(@Body PersonAndCategoryId doctorId , Callback<List<CustomProcedureTemplate>> cb);

    @POST("/getAllCustomTemplate")
    void getAllCustomTemplate1(@Body PersonAndCategoryId1 doctorId , Callback<List<CustomProcedureTemplate1>> cb);
    // void getAllProcedure(@Query("doctorId") String doctorId, Callback<ArrayList<ShowProcedure>> callback);

    @POST("/getCustomTemplate")
    void getCustomTemplate(@Body CustomTemplateId templateId , Callback<CustomProcedureTemplate1> cb);

    @POST("/updateTemplate")
    void updateCustomTemplate(@Body CustomProcedureTemplate1 template , Callback<ResponseVm> cb);
//
    @POST("/getPatientLandingPageDetails")
    void getPatientLandingPageDetails(@Body PatientId patientId, Callback<PatientProfile> cb);

    @POST("/getDoctorProfileList")
    void getDoctorProfileList(@Body PatientId patientId, Callback<List<DoctorShortProfile>> callback);


    @POST("/getPatientAppointments")
    void getPatientAppointments(@Body PatientId patientId, Callback<PatientAppointmentsVM> callback);

//    @POST("/getDoctorProfileList")
//    void getDoctorProfileList1(@Body PatientId patientId, Callback<List<AllPatients>> callback);
//
//    @POST("/getPatientVisitInvoice")
//    void getPatientVisitInvoice(@Body InvoiceId invoiceId, Callback<InvoiceDetails> response);
//
    @POST("/getPatientVisitInvoice")
    void getPatientVisitInvoice1(@Body InvoiceId invoiceId, Callback<InvoiceDetails1> response);

    @POST("/getPatientVisitInvoice")
    void getPatientVisitInvoice1(@Body AppointmentId1 appointmentId, Callback<InvoiceDetails1> response);
//
//    @POST("/addPatientVisitInvoice")
//    void addPatientVisitInvoice(@Body TreatmentPlan treatmentPlan, Callback<ResponseAddTemplates> response);
//
//    @POST("/addPatientVisitTreatmentPlan")
//    void addPatientVisitTreatmentPlan(@Body TreatmentPlan treatmentPlan, Callback<ResponseAddTemplates> response);

    @POST("/addPatientVisitTreatmentPlan")
    void addPatientVisitTreatmentPlan1(@Body TreatmentPlan1 treatmentPlan, Callback<ResponseAddTemplates1> response);

    @POST("/removePatientVisitTreatmentPlan")
    void removePatientVisitTreatmentPlan(@Body TreatmentId invoiceId, Callback< ResponseCodeVerfication > response);


//    @POST("/removePatientVisitInvoiceDetails")

//    void removePatientVisitInvoiceDetails(@Body TreatmentId invoiceId, Callback< ResponseCodeVerfication  > response);
//
//
//    @POST("/updatePatientVisitTreatmentPlan")
//    void updatePatientVisitTreatmentPlan(@Body TreatmentPlan treatmentPlan, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientVisitTreatmentPlan")
    void updatePatientVisitTreatmentPlan1(@Body TreatmentPlan1 treatmentPlan, Callback<ResponseCodeVerfication> response);


//    @POST("/updatePatientVisitInvoiceDetails")
//    void updatePatientVisitInvoiceDetails(@Body InvoiceDetails invoiceId, Callback<ResponseCodeVerfication> response);

    @POST("/searchAutoFill1")
    void searchAutoFill(@Body SearchParameter parameter, Callback<List<Medicine>> response);
    @POST("/searchAutoFill1")
    void searchAutoFillSymptom(@Body SearchParameter parameter, Callback<List<Symptom>> response);
    @POST("/searchAutoFill1")
    void searchAutoFillDiagnosis(@Body SearchParameter parameter, Callback<List<Diagnosis>> response);
    @POST("/searchAutoFill1")
    void searchAutoFillDiagnostic(@Body SearchParameter parameter, Callback<List<DiagnosticTest>> response);
    @POST("/searchAutoFill1")
    void searchAutoFillSpecialization(@Body SearchParameter parameter, Callback<List<Specialization>> response);

}
