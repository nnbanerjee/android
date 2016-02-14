package Model;

/**
 * Created by Narendra on 10-02-2016.
 */
public class ResponseCreateProfileForDoctorUpdateDetails {

    //{"status":0,"errorCode":"208"}
    private String status;

    public ResponseCreateProfileForDoctorUpdateDetails(String status, String errorcode) {
        this.status = status;
        this.errorcode = errorcode;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String errorcode;


}
