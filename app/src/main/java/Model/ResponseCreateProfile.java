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
    private String profileId;

    public ResponseCreateProfile(String status, String profileId, String role) {
        this.status = status;
        this.profileId = profileId;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    private String role;

}
