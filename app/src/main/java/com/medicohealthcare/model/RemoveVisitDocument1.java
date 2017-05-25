package com.medicohealthcare.model;

/**
 * Created by Narendra on 01-04-2016.
 */
public class RemoveVisitDocument1
{
    public RemoveVisitDocument1(Integer fileId, Integer loggedinUserId) {
        this.fileId = fileId;
        this.loggedinUserId = loggedinUserId;
    }

    //{"fileId":4, "loggedinUserId":104}
    private Integer fileId;

    public Integer getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(Integer loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    private Integer loggedinUserId;
}
