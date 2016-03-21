package Model;

import java.util.ArrayList;
import java.util.List;


public class AppointmentSlotsByDoctor {


    private List<Slot> slots = new ArrayList<Slot>();
    private String lastAppointmentl;
    private Clinic clinic;

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }




    /**
     *
     * @return
     * The slots
     */
    public List<Slot> getSlots() {
        return slots;
    }

    /**
     *
     * @param slots
     * The slots
     */
    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }


    /**
     *
     * @return
     * The lastAppointmentl
     */
    public String getLastAppointmentl() {
        return lastAppointmentl;
    }

    /**
     *
     * @param lastAppointmentl
     * The lastAppointmentl
     */
    public void setLastAppointmentl(String lastAppointmentl) {
        this.lastAppointmentl = lastAppointmentl;
    }

}