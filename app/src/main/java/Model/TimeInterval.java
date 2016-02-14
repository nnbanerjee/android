package Model;

/**
 * Created by mindnerves on 5/7/2015.
 */
public class TimeInterval {

    private String time;
    private String isAvailable;
    private boolean selected;

    public TimeInterval(String time, String isAvailable,boolean selected){
        this.time = time;
        this.isAvailable = isAvailable;
        this.selected = selected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    //private boolean isSelected;

}
