package Model;

import java.util.ArrayList;

/**
 * Created by MNT on 26-Mar-15.
 */
public class AddConfirmDenyDelegate {

    private String id;
    private String type;
    private ArrayList<AddDelegateElement> parents;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AddDelegateElement> getParents() {
        return parents;
    }

    public void setParents(ArrayList<AddDelegateElement> parents) {
        this.parents = parents;
    }





}
