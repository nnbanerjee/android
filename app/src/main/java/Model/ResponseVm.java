package Model;

/**
 * Created by MNT on 12-Mar-15.
 */
public class ResponseVm {

    private String email;
    private int loginType;
    private int status;
    private int id;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private String sessionId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return loginType;
    }

    public void setType(int type) {
        this.loginType = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
