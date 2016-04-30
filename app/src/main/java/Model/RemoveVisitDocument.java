package Model;

/**
 * Created by Narendra on 01-04-2016.
 */
public class RemoveVisitDocument {
    public RemoveVisitDocument(String fileId, String loggedinUserId) {
        this.fileId = fileId;
        this.loggedinUserId = loggedinUserId;
    }

    //{"fileId":4, "loggedinUserId":104}
    private String fileId;

    public String getLoggedinUserId() {
        return loggedinUserId;
    }

    public void setLoggedinUserId(String loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private String loggedinUserId;
}
