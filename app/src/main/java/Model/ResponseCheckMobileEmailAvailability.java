package Model;

/**
 * Created by Narendra on 09-02-2016.
 */
public class ResponseCheckMobileEmailAvailability {
    public ResponseCheckMobileEmailAvailability(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

}
