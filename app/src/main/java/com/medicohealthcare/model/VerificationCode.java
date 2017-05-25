package com.medicohealthcare.model;

/**
 * Created by Narendra on 08-02-2016.
 */
public class VerificationCode {

    public VerificationCode(String code, String email) {
        this.email = email;
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String code;
    private String email;
}
