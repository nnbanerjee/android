package Model;

/**
 * Created by Narendra on 08-04-2016.
 */
public class PersonAndCategoryId {
    public PersonAndCategoryId(String personId, String categoryId) {
        this.personId = personId;
        this.categoryId = categoryId;
    }

    private String personId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonI(String personId) {
        this.personId = personId;
    }

    private String categoryId;
}
