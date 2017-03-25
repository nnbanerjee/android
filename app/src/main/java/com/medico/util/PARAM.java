package com.medico.util;

/**
 * Created by Narendra on 17-01-2017.
 */

public interface PARAM
{
    public static final String MyPREFERENCES = "MyPrefs";

    public static String EXECUTION_STATUS = "status";
    public static int STATUS_WARNING = 2;
    public static int STATUS_SUCCESS = 1;
    public static int STATUS_FAILED = 0;

    public static final int DOCTOR = 1;
    public static final int PATIENT = 0;
    public static final int ASSISTANT = 2;
    public static final int DEPENDENT = 0;
    public static final int DELEGATE = 1;

    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    public static final int ALLOPATHIC = 1;
    public static final int HOMEOPATHIC = 2;
    public static final int AYURVEDIC = 3;


    public static final int REGISTED = 0;
    public static final int UNDER_VERIFICATION = 1;
    public static final int UNREGISTERED = 2;

    public static final String IS_PROFILE_OF_LOGGED_IN_USER = "profileOfLoggedIn";

    public static final String LOGGED_IN_ID = "Id";
    public static final String LOGGED_IN_USER_ROLE = "role";
    public static final String LOGGED_IN_USER_STATUS = "status";
    public static final String PROFILE_ROLE = "profile_role";
    public static final String PROFILE_STATUS = "profile_status";
 
    public static final String PROFILE_TYPE = "profile_type";

    public static final String PROFILE_ID = "profileId"; 
    public static final String DOCTOR_ID = "dictorId";
    public static final String PATIENT_ID = "patientId";
    public static final String ASSISTANT_ID = "assistantId";
    public static final String CLINIC_ID = "clinicId";
    public static final String APPOINTMENT_DATE = "appointment_date";
    public static final String APPOINTMENT_TIME = "appointment_time";
    public static final int APPOINTMENT_CONFIRMED = 0;
    public static final int APPOINTMENT_TENTATIVE = 1;

    public static final String DEPENDENT_ID = "dependentId";
    public static final String DEPENDENT_ROLE = "dependent_role";
    public static final String DEPENDENT_STATUS = "dependent_status";
    public static final String PARENT = "parent_activity";

    public static final int PROCEDURE_FIELD_NAME = 101;
    public static final int PROCEDURE_FIELD_DESCRIPTION = 102;
    public static final int PROCEDURE_FIELD_DATE = 103;
    public static final int PROCEDURE_FIELD_CURRENCY = 104;
    public static final int PROCEDURE_FIELD_COST = 105;
    public static final int PROCEDURE_FIELD_DISCOUNT = 106;
    public static final int PROCEDURE_FIELD_TAX = 107;
    public static final int PROCEDURE_FIELD_TOTAL = 108;
    public static final int PROCEDURE_FIELD_NOTES = 109;

    public static final int TEMPLATE_CATEGORY_PROCEDURE = 1;
    public static final int TEMPLATE_CATEGORY_INVOICE = 2;
    public static final int TEMPLATE_CATEGORY_EMAIL = 3;
    public static final int TEMPLATE_CATEGORY_SMS = 4;

    public static final String TREATMENT_ID = "treatmentId";



    public static final String CUSTOM_TEMPLATE_ID = "templateId";

    public static final String CUSTOM_TEMPLATE_NAME = "custom_template_name";


    public static final int INVOICE_FIELD_NAME = 201;
    public static final int INVOICE_FIELD_DESCRIPTION = 202;
    public static final int INVOICE_FIELD_DATE = 203;
    public static final int INVOICE_FIELD_CURRENCY = 204;
    public static final int INVOICE_FIELD_COST = 205;
    public static final int INVOICE_FIELD_DISCOUNT = 206;
    public static final int INVOICE_FIELD_TAX = 207;
    public static final int INVOICE_FIELD_TOTAL = 208;
    public static final int INVOICE_FIELD_NOTES = 209;

    public static final String INVOICE_ID = "invoiceId";
    public static final String APPOINTMENT_ID = "appointmentId";
    public static final String APPOINTMENT_DATETIME = "appointment_datetime";
    public static final String APPOINTMENT_SEQUENCE_NUMBER = "appointment_sequence_number";
    public static final String REFERRED_BY = "referred_by";
    public static final String CLINIC_NAME = "clinic_name";

    public static final String MEDICINE_ID = "medicineId";
    public static final String DIAGNOSTIC_TEST_ID = "diagnosticTestId";

    public static final String CUSTOM_TEMPLATE_CREATE_ACTIONS = "create_template";
    public static final int CREATE_TREATMENT = 1;
    public static final int CREATE_INVOICE = 2;
    public static final int COPY_TEMPLATE = 3;

    public static final String DOCTOR_CLINIC_ID = "doctorClinicId";
    public static final String SLOT_START_DATETIME = "slot_start_datetime";
    public static final String SLOT_END_DATETIME = "slot_end_datetime";
    public static final String SLOT_VISIT_DURATION = "slot_visit_duration";
    public static final String SLOT_TIME = "slot_time";


    public static final String SETTING_VIEW_ID = "setting_view_id";
    public static final int MANAGE_PROFILE_VIEW = 1;
    public static final int PATIENT_SETTING_VIEW = 2;
    public static final int ASSISTANT_SETTING_VIEW = 3;
    public static final int DEPENDENT_SETTING_VIEW = 4;
    public static final int DELEGATE_SETTING_VIEW = 5;
    public static final int CLINIC_SETTING_VIEW = 6;
    public static final int DOCTOR_SETTING_VIEW = 7;


    public static final String DEPENDENT_DELEGATE_RELATION = "dependent_delegate_relation";


    public static final String CLINIC_TYPE = "clinic_type";
    public static final int CLINIC = 0;
    public static final int PATHOLOGY = 1;
    public static final int DIAGNOSTIC = 3;
    public static final int ONLINE_CONSULATION = 4;
    public static final int HOME_VISIT = 5;

}
