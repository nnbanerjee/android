package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 26-Mar-15.
 */
public class Histry {

    private String currentText;
    private String previoustext;
    private String date;
    private String user;

    public Histry(){

    }

    public Histry(String currentText, String previoustext, String date, String user){
        this.currentText = currentText;
        this.previoustext = previoustext;
        this.date = date;
        this.user = user;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public String getPrevioustext() {
        return previoustext;
    }

    public void setPrevioustext(String previoustext) {
        previoustext = previoustext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
