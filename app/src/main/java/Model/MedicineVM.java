package Model;

import java.util.List;

/**
 * Created by User on 04-11-2015.
 */
public class MedicineVM {
    public String medicineName;
    public List<AlarmReminderVM> alarms;

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<AlarmReminderVM> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<AlarmReminderVM> alarms) {
        this.alarms = alarms;
    }
}
