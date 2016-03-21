package Model;

/**
 * Created by Narendra on 26-02-2016.
 */
public class ErrorResponse {
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorCode;
}
