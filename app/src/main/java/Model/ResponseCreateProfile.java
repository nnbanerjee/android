package Model;

/**
 * Created by Narendra on 10-02-2016.
 */
public class ResponseCreateProfile {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponseCreateProfile(String status) {
        this.status = status;
    }

    //{"status":"confirmed"}
    private String status;

}
