package com.medicohealthcare.model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class ResetPassword {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    private String email;
    private String password;

    public ResetPassword(String email, String password, String newpassword) {
        this.email = email;
        this.password = password;
        this.newpassword = newpassword;
    }

    private String newpassword;
}
