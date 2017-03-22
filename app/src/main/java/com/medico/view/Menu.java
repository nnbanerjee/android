package com.medico.view;

import java.util.ArrayList;

/**
 * Created by MNT on 2/17/15.
 */
public class Menu {

    private String menuName;
    private ArrayList<String> arrayMenuList;



    public Menu(String menuName) {
        this.menuName = menuName;

    }

    public Menu() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public ArrayList<String> getMenus()
    {
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Doctor");
        arrayMenuList.add("Manage Patient");
        arrayMenuList.add("Manage Clinic");
        arrayMenuList.add("Manage Assistant");
       /* arrayMenuList.add("Manage Dependency");*/
        arrayMenuList.add("Manage Reminder");
        return arrayMenuList;
    }

    public ArrayList<String> getPatientMenus()
    {
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Profile");
        arrayMenuList.add("Manage Doctors");
        arrayMenuList.add("Manage Dependents");
        arrayMenuList.add("Manage Delegations");
//        arrayMenuList.add("Terms & Conditions");
        arrayMenuList.add("Logout");
        return arrayMenuList;
    }

    public ArrayList<String> getDoctorMenus()
    {
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Profile");
        arrayMenuList.add("Manage Patients");
        arrayMenuList.add("Manage Clinics");
        arrayMenuList.add("Manage Assistants");
        arrayMenuList.add("Manage Dependents");
//        arrayMenuList.add("Manage Delegations");
//        arrayMenuList.add("Manage Templates");
//        arrayMenuList.add("Terms & Conditions");
        arrayMenuList.add("Logout");
        return arrayMenuList;
    }

    public ArrayList<String> getAssistantMenus(){
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Delegation");
        return arrayMenuList;
    }

}
