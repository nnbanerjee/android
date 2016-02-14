package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 02-Apr-15.
 */
public class RemoveDelegate {

    private String id;
    private String type;
    private ArrayList<RemoveDelegateElement> delegates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<RemoveDelegateElement> getDelegates() {
        return delegates;
    }

    public void setDelegates(ArrayList<RemoveDelegateElement> delegates) {
        this.delegates = delegates;
    }
}
