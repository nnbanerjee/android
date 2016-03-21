package Application; /**
 * Created by MNT on 17-Feb-15.
 */

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Model.AddConfirmDeny;
import Model.AddConfirmDenyDelegate;
import Model.AddDelegate;
import Model.AddDependent;
import Model.AddDependentDoctor;
import Model.AddDiagnosisTestRequest;
import Model.AddPatientMedicineSummary;
import Model.AllClinicAppointment;
import Model.AllClinicsByDoctorPatientId;
import Model.AllClinicsForDoctorIdAndPatientId;
import Model.AllPatients;
import Model.AllProcedureVm;
import Model.AllTreatmentPlanVm;
import Model.AppointmentId;
import Model.AppointmentSlotsByDoctor;
import Model.AppointmentStatus;
import Model.Assistant;
import Model.BucketPatient;
import Model.Chat;
import Model.Clinic;
import Model.ClinicAppointment;
import Model.ClinicDetailVm;
import Model.ClinicPatientAppointments;
import Model.CreateProfileData;
import Model.CreateProfileDataForDoctorUpdateDetails;
import Model.CreateSummary;
import Model.Delegation;
import Model.Doctor;
import Model.DoctorAppointmentsResponse;
import Model.DoctorClinicAppointments;
import Model.DoctorClinicSchedule;
import Model.DoctorCreatesAppoinementResponse;
import Model.DoctorCreatesAppointment;
import Model.DoctorId;
import Model.DoctorIdPatientId;
import Model.DoctorNotesResponse;
import Model.DoctorNotesVM;
import Model.DoctorPersonalVM;
import Model.DoctorProfile;
import Model.DoctorSearchResponse;
import Model.FeedbackVM;
import Model.Field;
import Model.FileUpload;
import Model.GetDelegate;
import Model.HomeCountDoctor;
import Model.HomePatientCount;
import Model.MedicineId;
import Model.MobileEmail;
import Model.ModeVM;
import Model.NotificationVM;
import Model.Patient;
import Model.PatientTestId;
import Model.Person;
import Model.PersonID;
import Model.RegisterDoctorData;
import Model.Logindata;
import Model.RegisterAssistantData;
import Model.RegisterUserData;
import Model.ReminderVM;
import Model.RemoveDelegate;
import Model.RemoveDoctors;
import Model.RemoveMedicineRequest;
import Model.RemovePatientTestRequest;
import Model.RemovePatients;
import Model.ResetPassword;
import Model.ResponseCheckMobileEmailAvailability;
import Model.ResponseCodeVerfication;
import Model.ResponseCreateProfile;
import Model.ResponseCreateProfileForDoctorUpdateDetails;
import Model.ResponseVerifyRegistrationMobileEmailCode;
import Model.ResponseVm;
import Model.ResponsegetVerificationCodeForNewRegistration;
import Model.ShowProcedure;
import Model.ShowTemplate;
import Model.SummaryHistoryVM;
import Model.SummaryRequest;
import Model.SummaryResponse;
import Model.Template;
import Model.TestDetails;
import Model.TotalInvoice;
import Model.TreatmentPlan;
import Model.UpdateField;
import Model.VerificationCode;
import Model.VerifyRegistrationMobileEmailCode;
import Model.VisitEditLogRequest;
import Model.VisitEditLogResponse;
import Model.VisitHistory;
import Model.forgotPassword;
import Model.profileId;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface MyApi {



    @POST("/registerPatient")
    void registerPatient(@Body RegisterUserData param, Callback<String> cb);

    @POST("/registerDoctor")
    void registerDoctor(@Body RegisterDoctorData param, Callback<String> cb);

    @POST("/registerAssistent")
    void registerAssistent(@Body RegisterAssistantData param, Callback<String> cb);

    @GET("/searchDoctors")
    void searchDoctors(@Query("name") String name, Callback<ArrayList<DoctorSearchResponse>> callback);

    @POST("/addDoctor")
    void registerAddDoctor(@Query("id") String patientId, @Body DoctorSearchResponse res, Callback<Response> callback);

    @POST("/patientDoctor")
    void patientDoctor(@Query("id") String patientId, @Query("doctors") String doctorsIds, Callback<Response> callback);

    @GET("/searchPatients")
    void searchPatients(@Query("name") String name, Callback<ArrayList<Patient>> callback);

    @POST("/doctorsPatient")
    void addPatient(@Query("id") String patientId, @Query("patients") String patientIds, Callback<String> callback);

    @POST("/removeDoctorsPatient")
    void removeDoctorsPatient(@Body RemovePatients rem, Callback<String> callback);

    @POST("/removePatientsDoctor")
    void removePatientsDoctor(@Body RemoveDoctors remove, Callback<Response> callback);

    @GET("/getPatientsDoctors")
    void getPatientsDoctors(@Query("id") String name, Callback<List<DoctorSearchResponse>> callback);

    @GET("/getDoctorsPatients")
    void getDoctorsPatients(@Query("id") String name, Callback<List<Patient>> callback);

    @GET("/searchClinic")
    void searchClinic(@Query("clinicName") String clinicName, Callback<List<Clinic>> callback);

    @POST("/addClinic")
    void addClinic(@Body Clinic clinicDetails, Callback<String> callback);

    @POST("/saveDoctorClinicScheduleTime")
    void addTimeSchedule(@Body DoctorClinicSchedule toc, Callback<Response> callback);

    @GET("/getClinicScheduleshiftDetails")
    void getClinicDetails(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, Callback<ArrayList<DoctorClinicSchedule>> callback);

    @GET("/getAllDoctors")
    void getAllDoctors(Callback<List<Doctor>> callback);

    @GET("/getDoctorAssistant")
    void getDoctorAssistant(@Query("id") String name, Callback<ArrayList<Assistant>> callback);

    @POST("/removeDoctorsClinic")
    void removeDoctorClinic(@Query("id") String doctorId, @Query("clinics") String clinicIds, Callback<String> callback);

    @GET("/getDoctorsClinic")
    void doctorClinics(@Query("doctorId") String doctorId, Callback<List<Clinic>> callback);

    @GET("/searchAssistants")
    void searchAssistants(@Query("name") String name, Callback<ArrayList<Assistant>> callback);

    @POST("/addDoctorsAssistants")
    void addDoctorsAssistants(@Query("id") String id, @Query("assistants") String assistantIds, Callback<Response> cb);

    @POST("/addAssistant")
    void addAssistant(@Query("doctorId") String id, @Body Assistant ats, Callback<Response> cb);

    @POST("/removeDoctorAssistance")
    void removeDoctorAssistance(@Query("id") String doctorId, @Query("assistants") String assistantIds, Callback<Response> cb);

    @POST("/addPatientDependent")
    void addPatientDependent(@Body AddDependent a, Callback<Response> cb);

    @GET("/getAllPatientDependents")
    void getAllPatientDependents(@Query("id") String patientId, Callback<ArrayList<Patient>> callback);

    @POST("/removePatientDependent")
    void removePatientDependent(@Query("id") String patientId, @Query("dependents") String patientIds, Callback<Response> response);

    @POST("/confirmOrDenyParent")
    void confirmOrDenyParent(@Body AddConfirmDeny a, Callback<Response> cb);

    @GET("/getAllParents")
    void getAllParents(@Query("id") String patientId, Callback<ArrayList<Patient>> callback);

    @GET("/getAllDelegatesForParent")
    void getAllDelegatesForParent(@Query("id") String patientId, @Query("type") String type, Callback<ArrayList<Delegation>> response);

    @GET("/getAllParentsForDelegates")
    void getAllParentsForDelegates(@Query("id") String patientId, @Query("type") String type, Callback<ArrayList<Delegation>> response);

    @POST("/confirmOrDenyParentForDelegates")
    void confirmOrDenyParentForDelegates(@Body AddConfirmDenyDelegate a, Callback<Response> cb);

    @POST("/addDeleagatesForParent")
    void addDeleagatesForParent(@Body AddDelegate a, Callback<Response> cb);

    @GET("/getAllDoctorsAndAssistants")
    void getAllDoctorsAndAssistants(Callback<ArrayList<GetDelegate>> cb);

    @POST("/removeDelegatesForParent")
    void removeDelegatesForParent(@Body RemoveDelegate rem, Callback<Response> cb);

    @POST("/addPatient")
    void addPatient(@Query("id") String doctorId, @Body BucketPatient pat, Callback<String> callback);

    @POST("/addTemplate")
    void addTemplate(@Body Template tem, Callback<Response> cb);

    @POST("/addTemplateAllField")
    void addTemplateAllField(@Body ArrayList<Field> fieldArrayList, Callback<Response> cb);

    /*@GET("/getTemplateAllFields")
    public void getTemplateAllFields(@Query("id")String templateId,Callback<ArrayList<Field>> callback);*/

    @POST("/addField")
    void addField(@Body Field f, Callback<Response> cb);

    @POST("/updateField")
    void updateField(@Body UpdateField uf, Callback<Response> cb);

    @POST("/removeFields")
    void removeFields(@Query("id") String doctorId, @Query("fields") String fields, Callback<Response> cb);

    @POST("/removeSelectedFields")
    void removeSelectedFields(@Body List<Field> removeList, Callback<Response> cb);

    @GET("/getAllTemplates")
    void getAllTemplate(@Query("id") String doctorId, @Query("procedureName") String procedureName, Callback<ArrayList<ShowTemplate>> callback);

    @GET("/getAllFields")
    void getAllFields(@Query("id") String templateId, Callback<ArrayList<Field>> callback);

    @GET("/getChatMember")
    void getChatMember(@Query("id") String doctorId, @Query("type") String type, Callback<ArrayList<Chat>> callback);

    @GET("/getDoctorsClinicsDetails")
    void getAllClinicsDetailsList(@Query("doctorId") String doctorId, @Query("type") String type, Callback<List<ClinicDetailVm>> callback);

    @GET("/getClinicsAppointmentDetails")
    void getAllClinicsAppointment(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("shift") String shift, @Query("date") String date, Callback<List<ClinicAppointment>> callback);

    @POST("/saveClinicsAppointmentDetails")
    void saveClinicsAppointmentData(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);

    @GET("/cancelClinicsAppointment")
    void cancelClinicsAppointment(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, @Query("clinicId") Integer clinicId, @Query("shift") String shift, Callback<String> response);

    @GET("/getAllPatientAppointment")
    void getPatientAppointment(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, Callback<List<ClinicAppointment>> response);

    @GET("/saveVisitedPatientAppointment")
    void saveVisitedPatientAppointment(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("clinicId") String clinicId, @Query("shift") String shift, @Query("isVisited") Integer isVisited, Callback<String> response);

    @GET("/getPatientReminder")
    void getPatientReminderData(@Query("doctorId") Integer doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<ReminderVM> response);

    @POST("/savePatientReminder")
    void savePatientReminderDetails(@Body ReminderVM reminderVM, Callback<ReminderVM> response);

    @Multipart
    @POST("/uploadFiles")
    void uploadFile(@Part("picture") TypedFile file, @Part("type") String type, @Part("doctorId") String doctorId, @Part("patientId") String patientId, @Part("assistentId") String assistantId, @Part("documentType") String documentType, @Part("name") String name, @Part("category") String category, @Part("appointmentDate") String appointmentDate, @Part("appointmentTime") String appointmentTime, @Part("clinicId") String clinicId, @Part("clinicName") String clinicName, Callback<FileUpload> cb);

    @GET("/getUploadedFiles")
    void getFiles(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<FileUpload>> cb);

    @GET("/getAllPatientClinics")
    void getAllPatientClinics(@Query("patientId") String patientId, Callback<List<Clinic>> response);

    @GET("/getAllDoctorClinics")
    void getAllDoctorClinics(@Query("doctorId") String doctorId, @Query("date") Date currentDate, Callback<List<Clinic>> response);

    @GET("/getAlldoctorsOfClinic")
    void getAlldoctorsOfClinicDetail(@Query("clinicId") String clinicId, Callback<List<ClinicDetailVm>> callback);

    //All Doctors API ---------------------------------------------------------------------------------
    @GET("/getAllPatientInformation")
    void getAllPatientInformation(@Query("doctorId") String doctorId, Callback<List<AllPatients>> callback);

    @GET("/getAllDoctorPatientClinics")
    void getAllDoctorPatientClinics(@Query("doctorId") String doctorId, @Query("patientId") String patientId, Callback<List<ClinicDetailVm>> callback);

    @POST("/saveDoctorNotes")
    void saveDoctorNotes(@Body DoctorNotesVM doctorNotesVM, Callback<JsonObject> response);

    @GET("/getDoctorNotes")
    void getDoctorNotesData(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<DoctorNotesVM> callback);

    @GET("/getDoctorProcedures")
    void getAllProcedure(@Query("doctorId") String doctorId, Callback<ArrayList<ShowProcedure>> callback);

    @POST("/saveDoctorProcedures")
    void saveDoctorProcedures(@Body ShowProcedure tem, Callback<Response> cb);

   /* @POST("/saveTreatmentPlan")
    public void saveTreatmentPlanData(@Body TreatmentPlan tem,Callback<TreatmentPlan> cb);

    @GET("/getAllTreatmentPlan")
    public void getAllTreatmentPlan(@Query("doctorId")String doctorId,@Query("patientId")String patientId, @Query("appointmentDate")String appointmentDate,@Query("appointmentTime")String appointmentTime,Callback<AllTreatmentPlanVm> callback);
*/
    @POST("/updateTreatementTemplate")
    void updateFieldTemplateData(@Body Field field, Callback<Field> cb);

    @POST("/saveTreatementTemplate")
    void saveTreatmentPlanData(@Body TreatmentPlan treatmentPlan, Callback<TreatmentPlan> cb);


    @GET("/getAllTreatmentPlan")
    void getAllTreatmentPlan(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<AllTreatmentPlanVm> callback);

    @POST("/saveInvoices")
    void saveInvoicesData(@Body TreatmentPlan tem, Callback<TreatmentPlan> cb);

    @GET("/getAllInvoices")
    void getAllInvoicesData(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<AllTreatmentPlanVm> callback);

    @GET("/getAllClinicsAppointment")
    void getAllClinicsAppointmentData(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("appointmentDate") Date appointmentDate, Callback<List<AllClinicAppointment>> callback);

    @GET("/getAllClinicsWeekAppointment")
    void getAllClinicWeekAppointment(@Query("doctorId") String doctorId, @Query("clinicId") String clinicId, @Query("appointmentDate") String appointmentDate, Callback<List<AllClinicAppointment>> callback);

    @GET("/getAllClinics")
    void getAllClinics(Callback<List<Clinic>> cb);

    @POST("/saveShareWithPatientTotalInvoice")
    void saveShareWithPatientTotalInvoice(@Body TotalInvoice invoice, Callback<Response> cb);

    @POST("/saveTotalInvoice")
    void saveTotalInvoice(@Body TotalInvoice invoice, Callback<TotalInvoice> cb);

    @GET("/getTotalInvoice")
    void getInvoice(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<TotalInvoice> cb);

    @GET("/getUploadedFilesForPatient")
    void getFilesPatient(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<FileUpload>> cb);

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



    @GET("/getProfileDoctor")
    void getProfileDoctor(@Query("email") String email, Callback<Person> cb);

    @GET("/getPicture")
    void getPicture(@Query("path") String path, Callback<TypedFile> cb);

    @Multipart
    @POST("/profilePictureUpdateDoctor")
    void picutreUpdateDoctor(@Part("picture") TypedFile file, @Part("email") String email, Callback<String> cb);

    @POST("/updateDoctorProfile")
    void updateDoctorProfile(@Body Person person, Callback<String> cb);

    @GET("/getProfilePatient")
    void getProfilePatient(@Query("email") String email, Callback<Person> cb);

    @Multipart
    @POST("/udatePatientProfilePicture")
    void updatePatientProfilePicture(@Part("picture") TypedFile file, @Part("email") String email, Callback<String> cb);

    @POST("/updatePatientProfile")
    void updatePatientProfile(@Body Person person, Callback<String> cb);

    @POST("/addPatientDependentWithoutConfirmaton")
    void addPatientDependentWithoutConfirmat(@Body AddDependent a, Callback<Response> cb);

    @POST("/patientDoctorAdd")
    void patientDoctorAdd(@Query("id") String patientId, @Query("doctor") String doctorsId, Callback<Response> callback);

    @GET("/getAllSpeciality")
    void getAllSpeciality(Callback<List<String>> cb);

    @GET("/getPassword")
    void getPassword(@Query("id") String id, Callback<String> cb);

    @POST("/editClinic")
    void editClinic(@Body Clinic clinicDetails, Callback<String> callback);

    @GET("/getAllParentsDoctor")
    void getAllParentsDoctor(@Query("id") String doctorId, Callback<ArrayList<Patient>> callback);

    @GET("/getAllDoctorDependents")
    void getAllDoctorDependents(@Query("id") String doctorId, Callback<ArrayList<Patient>> callback);

    @POST("/addDoctorDependentWithoutConfirmaton")
    void addDoctorDependentWithoutConfirmat(@Body AddDependentDoctor a, Callback<Response> cb);

    @POST("/addDoctorDependent")
    void addDoctorDependent(@Body AddDependentDoctor a, Callback<Response> cb);

    @POST("/confirmOrDenyParentDoctor")
    void confirmOrDenyParentDoctor(@Body AddConfirmDeny a, Callback<Response> cb);

    @POST("/deletePatient")
    void deletePatient(@Query("id") String id, Callback<String> cb);

    @POST("/deleteDoctor")
    void deleteDoctor(@Query("id") String id, Callback<String> cb);

    @POST("/deleteAssistant")
    void deleteAssistant(@Query("id") String id, Callback<String> cb);

    @POST("/saveClinicAppointment")
    void saveClinicAppointment(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);

    @GET("/homeCountPatient")
    void homeCountPatient(@Query("id") String patientId, Callback<HomePatientCount> response);

    @POST("/setStatusSlot")
    void setStatusSlot(@Body ModeVM vm, Callback<String> cb);

    @POST("/saveFeedBackPatient")
    void saveFeedbackPatient(@Body FeedbackVM vm, Callback<ClinicAppointment> cb);

    @GET("/getAllDoctorFinance")
    void getAllDoctorFinance(@Query("doctorId") String doctorId, Callback<List<AllTreatmentPlanVm>> callback);

    @GET("/getAllHistory")
    void getHistry(@Query("doctorId") String doctorId, @Query("patientId") String patientId, @Query("appointmentDate") String appointmentDate, @Query("appointmentTime") String appointmentTime, Callback<List<SummaryHistoryVM>> response);

    @POST("/saveDoctorAppointment")
    void saveDoctorAppointment(@Body ClinicAppointment clinicAppointment, Callback<JsonObject> response);

    @GET("/getPatientAppointment")
    void getPatientAppointment(@Query("patientId") String patientId, Callback<List<Clinic>> callback);

    @POST("/getFeedbackPatient")
    void getFeedbackPatient(@Body FeedbackVM vm, Callback<FeedbackVM> callback);

    @GET("/getClinicSpeciality")
    void getClinicSpeciality(Callback<List<String>> cb);

    @POST("/saveNotification")
    void saveNotification(@Body NotificationVM vm, Callback<String> callback);

    @GET("/getNotification")
    void getNotification(@Query("email") String doctorId, Callback<NotificationVM> callback);

    @POST("/deleteNotification")
    void deleteNotification(@Body NotificationVM vm, Callback<String> callback);

    @POST("/addDoctorsClinic")
    void addDoctorClinic(@Query("id") String doctorId, @Query("clinics") String clinic, Callback<String> cb);

    @POST("/updatePatientReminder")
    void updatePatientReminder(@Body ReminderVM reminderVM, Callback<ReminderVM> response);

    @POST("/deletePatientReminder")
    void deletePatientReminder(@Body ReminderVM reminderVM, Callback<String> response);

    @POST("/deleteFile")
    void deleteFile(@Query("id") String id, Callback<String> cb);

    @GET("/homeCountDoctor")
    void homeCountDoctor(@Query("doctorId") String doctorId, Callback<HomeCountDoctor> cb);

    @POST("/deleteTreatmentTemplate")
    void deleteTreatmentTemplate(@Body AllProcedureVm vm, Callback<String> cb);

    @POST("/deleteInvoiceTemplate")
    void deleteInvoiceTemplate(@Body AllProcedureVm vm, Callback<String> cb);

    @GET("/doctorPatientsFeedback")
    void doctorPatientFeedback(@Query("email") String email, Callback<List<AllPatients>> cb);

    @GET("/doctorList")
    void doctorList(@Query("user_lat") String latitude, @Query("user_long") String longitude, @Query("user_distance") String distance, Callback<ArrayList<DoctorSearchResponse>> callback);

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

	@POST("/saveDoctorPersonDetail")
    void saveDoctorPersonDetail(@Body DoctorPersonalVM doctorPersonalVM, Callback<String> cb);

//New apis Integrated Raviraj

    @POST("/login")
    void login(@Body Logindata param, Callback<ResponseVm> cb);

    @POST("/getDoctorLandingPageDetails")
    void getDoctorLandingPageDetails(@Body DoctorId param, Callback<DoctorProfile> cb);

    @POST("/getPatientList")
    void getPatientList(@Body DoctorId param, Callback<List<AllPatients>> callback);


    @POST("/forgotPassword")
    void forgotPassword(@Body forgotPassword forgotObj, Callback<ResponseVm> cb);


    @POST("/verifyCode")
    void verifyCode(@Body VerificationCode verificationCode, Callback<ResponseCodeVerfication> cb);


    @POST("/checkMobileEmailAvailability")
    void checkMobileEmailAvailability(@Body MobileEmail mobilemail, Callback<ResponseCheckMobileEmailAvailability> cb);

    @POST("/changePassword")
    void changePassword(@Body ResetPassword resetPassword, Callback<ResponseCodeVerfication> cb);

    @POST("/createProfile")
    void createProfile(@Body CreateProfileData param, Callback<ResponseCreateProfile> cb);

    @POST("/createDetailedProfile")
    void createDetailedProfile(@Body CreateProfileDataForDoctorUpdateDetails param, Callback<ResponseCreateProfileForDoctorUpdateDetails> cb);

    @POST("/getVerificationCodeForNewRegistration")
    void getVerificationCodeForNewRegistration(@Body MobileEmail mobileEmail, Callback<ResponsegetVerificationCodeForNewRegistration> cb);

    @POST("/verifyCodeForNewRegistration")
    void verifyCodeForNewRegistration(@Body VerifyRegistrationMobileEmailCode verifyRegistrationMobileEmailCode, Callback<ResponseVerifyRegistrationMobileEmailCode> cb);

    @POST("/getPatientProfileList")
    void getPatientProfileList(@Body DoctorId param, Callback<List<AllPatients>> callback);



    @POST("/getProfile")
    void getProfile(@Body profileId param, Callback<AllPatients> callback);


    @POST("/getClinicsByDoctor")
    void getClinicsByDoctor(@Body DoctorId param, Callback<List<AppointmentSlotsByDoctor>> callback);

    @POST("/getPatientAppointmentsByDoctor")
    void getPatientAppointmentsByDoctor(@Body DoctorIdPatientId param, Callback<ClinicPatientAppointments> callback);



    @POST("/cancelAppointment")
    void cancelAppointment(@Body AppointmentId appointmentId, Callback<ResponseCodeVerfication> cb);

    @POST("/getPatientVisitDatesByDoctor")
    void getPatientVisitDatesByDoctor(@Body DoctorIdPatientId param,Callback<  VisitHistory> response);


    @POST("/getClinicSlotAvailabilityByDoctor")
    void getClinicSlotAvailabilityByDoctor(@Body DoctorClinicAppointments param,Callback< DoctorAppointmentsResponse> response);



    @POST("/createAppointment")
    void createAppointment(@Body DoctorCreatesAppointment param,Callback<DoctorCreatesAppoinementResponse> response);

    //
    @POST("/setAppointmentStatus")
    void setAppointmentStatus(@Body AppointmentStatus appointmentStatus, Callback<ResponseCodeVerfication> response);
    //updateAppointment

    //
    @POST("/getPatientVisitSummary")
    void getPatientVisitSummary(@Body SummaryRequest param,Callback<SummaryResponse> response);

    @POST("/createPatientVisitSummary")
    void createPatientVisitSummary(@Body CreateSummary reminderVM, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientVisitSummary")
    void updatePatientVisitSummary(@Body CreateSummary reminderVM, Callback<ResponseCodeVerfication> response);

    @POST("/addPatientMedicine")
    void addPatientMedicine(@Body AddPatientMedicineSummary addPatientMedicineSummary, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientMedicine")
    void updatePatientMedicine(@Body AddPatientMedicineSummary addPatientMedicineSummary, Callback<ResponseCodeVerfication> response);

    @POST("/removePatientMedicine")
    void removePatientMedicine(@Body RemoveMedicineRequest removeMedicineRequest, Callback<ResponseCodeVerfication> response);


    @POST("/getMedicineDetails")
    void getMedicineDetails(@Body MedicineId medicineId, Callback<AddPatientMedicineSummary> response);

    @POST("/getTestDetails")
    void getTestDetails(@Body PatientTestId patientTestId, Callback<TestDetails> response);

    @POST("/removePatientDiagnosticTest")
    void removePatientDiagnosticTest(@Body RemovePatientTestRequest removeMedicineRequest, Callback<ResponseCodeVerfication> response);


    @POST("/getPatientVisitDoctorNotes")
    void getPatientVisitDoctorNotes(@Body AppointmentId appointmentId, Callback<DoctorNotesResponse> response);

    @POST("/addPatientVisitDoctorNotes")
    void addPatientVisitDoctorNotes(@Body DoctorNotesResponse appointmentId, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientVisitDoctorNotes")
    void updatePatientVisitDoctorNotes(@Body DoctorNotesResponse appointmentId, Callback<ResponseCodeVerfication> response);

    @POST("/getPatientVisitEditLog")
    void getPatientVisitEditLog(@Body VisitEditLogRequest visitEditLogRequest, Callback<VisitEditLogResponse> response);

    @POST("/addPatientDiagnosticTest")
    void addPatientDiagnosticTest(@Body AddDiagnosisTestRequest addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);

    @POST("/updatePatientDiagnosticTest")
    void updatePatientDiagnosticTest(@Body AddDiagnosisTestRequest addDiagnosisTestRequest, Callback<ResponseCodeVerfication> response);

    @POST("/getAllClinics")
    void getAllClinics(@Body PersonID personID, Callback<Clinic> response);

    @POST("/getPatientVisitTreatmentPlan")
    void getPatientVisitTreatmentPlan(@Body AppointmentId appointmentId, Callback<List<TreatmentPlan>> response);






    @Multipart
    @POST("/addPatientVisitDocument")
    void addPatientVisitDocument(@Part("picture") TypedFile file,@Body PersonID personID, Callback<Clinic> response);

 /*   @Multipart
    @POST("/uploadFiles")
    void uploadFile(@Part("picture") TypedFile file, @Part("type") String type, @Part("doctorId") String doctorId, @Part("patientId") String patientId, @Part("assistentId") String assistantId, @Part("documentType") String documentType, @Part("name") String name, @Part("category") String category, @Part("appointmentDate") String appointmentDate, @Part("appointmentTime") String appointmentTime, @Part("clinicId") String clinicId, @Part("clinicName") String clinicName, Callback<FileUpload> cb);
*/

    //
//getPatientVisitTreatmentPlan

}
