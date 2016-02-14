package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 26-Mar-15.
 */
public class AddDelegate {

    private String id;
    private String type;
    private ArrayList<AddDelegateElement> delegates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AddDelegateElement> getDelegates() {
        return delegates;
    }

    public void setDelegates(ArrayList<AddDelegateElement> delegates) {
        this.delegates = delegates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
