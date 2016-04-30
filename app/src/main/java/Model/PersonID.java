package Model;

/**
 * Created by Narendra on 17-03-2016.
 */
public class PersonID {
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public PersonID(String personId) {
        this.personId = personId;
    }

    private String personId;
}
