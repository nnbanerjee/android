package Model;


public class MedicineSchedule {

    public MedicineSchedule(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    private String scheduleTime;

    /**
     *
     * @return
     * The scheduleTime
     */
    public String getScheduleTime() {
        return scheduleTime;
    }

    /**
     *
     * @param scheduleTime
     * The scheduleTime
     */
    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

}