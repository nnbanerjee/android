package Model;

/**
 * Created by Narendra on 22-02-2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorAppointmentsResponse {

    public DoctorAppointmentsResponse(List<String> bookedSlots, String date) {
        this.bookedSlots = bookedSlots;
        this.date = date;
    }

    public List<String> bookedSlots = new ArrayList<String>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(List<String> bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public String date;


}