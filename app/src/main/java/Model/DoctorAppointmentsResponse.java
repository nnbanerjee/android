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
        this.sequence_num = bookedSlots;
        this.date = date;
    }

    public List<String> sequence_num = new ArrayList<String>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getsequence_num() {
        return sequence_num;
    }

    public void setsequence_num(List<String> bookedSlots) {
        this.sequence_num = bookedSlots;
    }

    public String date;


}