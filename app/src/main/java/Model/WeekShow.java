package Model;

/**
 * Created by User on 7/15/15.
 */
public class WeekShow {

    private String appointmentDate;
    private String bookTime;
    private PersonVM person;
    private String slot;

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public PersonVM getPerson() {
        return person;
    }

    public void setPerson(PersonVM person) {
        this.person = person;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
