package com.medicohealthcare.util;

public class BackStress {

    private static boolean ManageDoctorFragmentShown=false ;
    private static boolean ManagePatientFragmentShown=false ;
    private static boolean ManageClinicFragmentShown=false;
    private static boolean AddDoctorFragmentShown=false ;
    private static boolean AddPatientFragmentShown = false;
    private static boolean AddClinicFragmentShown=false;
    private static boolean AddNewDoctorFragmentShown=false ;
    private static boolean AddNewClinicFragmentShown=false;
    private static boolean AddNewTimeFragmentShown=false;
    private static boolean showClinicFragmentShown=false;
    private static boolean mainActivity=false ;
    public static int staticflag = 0;



    public static void setAllFalse()
    {
        ManageDoctorFragmentShown=false ;
        ManagePatientFragmentShown=false ;
        ManageClinicFragmentShown=false;
        AddDoctorFragmentShown=false ;
        AddNewDoctorFragmentShown=false ;
        AddClinicFragmentShown=false;
        AddNewClinicFragmentShown=false;
        AddNewTimeFragmentShown=false;
        AddPatientFragmentShown=false ;
        showClinicFragmentShown=false;
        mainActivity=false ;

    }

    public static boolean isAddNewTimeFragmentShown() {
        return AddNewTimeFragmentShown;
    }

    public static void setAddNewTimeFragmentShown(boolean addTimeFragmentShown) {
        AddNewTimeFragmentShown = addTimeFragmentShown;
    }

    public static boolean isShowClinicFragmentShown() {
        return showClinicFragmentShown;
    }

    public static void setShowClinicFragmentShown(boolean showClinicFragmentShown) {
        BackStress.showClinicFragmentShown = showClinicFragmentShown;
    }

    public static boolean isAddNewClinicFragmentShown() {
        return AddNewClinicFragmentShown;
    }

    public static void setAddNewClinicFragmentShown(boolean addNewClinicFragmentShown) {
        AddNewClinicFragmentShown = addNewClinicFragmentShown;
    }

    public static boolean isAddClinicFragmentShown() {
        return AddClinicFragmentShown;
    }

    public static void setAddClinicFragmentShown(boolean addClinicFragmentShown) {
        AddClinicFragmentShown = addClinicFragmentShown;
    }
    public static boolean isManageClinicFragmentShown() {
        return ManageClinicFragmentShown;
    }


    public static void setManageClinicFragmentShown(boolean manageClinicFragmentShown) {
        ManageClinicFragmentShown = manageClinicFragmentShown;
    }

    public static boolean isMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(boolean mainActivity) {
        BackStress.mainActivity = mainActivity;
    }

    public static boolean isManageDoctorFragmentShown() {
        return ManageDoctorFragmentShown;
    }

    public static void setManageDoctorFragmentShown(boolean manageDoctorFragmentShown) {
        ManageDoctorFragmentShown = manageDoctorFragmentShown;
    }

    public static boolean isManagePatientFragmentShown() {
        return ManagePatientFragmentShown;
    }

    public static void setManagePatientFragmentShown(boolean managePatientFragmentShown) {
        ManagePatientFragmentShown = managePatientFragmentShown;
    }

    public static boolean isAddDoctorFragmentShown() {
        return AddDoctorFragmentShown;
    }

    public static void setAddDoctorFragmentShown(boolean addDoctorFragmentShown) {
        AddDoctorFragmentShown = addDoctorFragmentShown;
    }

    public static boolean isAddNewDoctorFragmentShown() {
        return AddNewDoctorFragmentShown;
    }

    public static void setAddNewDoctorFragmentShown(boolean addNewDoctorFragmentShown) {
        AddNewDoctorFragmentShown = addNewDoctorFragmentShown;
    }

    public static boolean isAddPatientFragmentShown() {
        return AddPatientFragmentShown;
    }

    public static void setAddPatientFragmentShown(boolean addPatientFragmentShown) {
        AddPatientFragmentShown = addPatientFragmentShown;
    }




}
