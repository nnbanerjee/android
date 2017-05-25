package com.medicohealthcare.model;

/**
 * Created by Narendra on 31-03-2016.
 */
public class ResponseAddDocuments {
    public String status;
    public String errorCode;
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String  field;
}
