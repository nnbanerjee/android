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
      /*  arrayMenuList.add("Manage Dependency");
        arrayMenuList.add("Manage Delegation");*/
        arrayMenuList.add("Logout");
        return arrayMenuList;
    }

    public ArrayList<String> getDoctorMenus()
    {
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Profile");
        arrayMenuList.add("Manage Patient");
        arrayMenuList.add("Manage Clinic");
        arrayMenuList.add("Manage Assistant");
    /*    arrayMenuList.add("Manage Dependency");
        arrayMenuList.add("Manage Delegation");*/
        arrayMenuList.add("Manage Template");
        /*arrayMenuList.add("Patients Information");*/
        arrayMenuList.add("Logout");
        return arrayMenuList;
    }

    public ArrayList<String> getAssistantMenus(){
        arrayMenuList = new ArrayList<String>();
        arrayMenuList.add("Manage Delegation");
        return arrayMenuList;
    }

}
