package Model;

 
public class MedicinePrescribed {

    private String medicineId;
    private String medicineName;
    private String startDateTime;
    private String endDateTime;

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    private String reminder;

    /**
     *
     * @return
     * The medicineId
     */
    public String getMedicineId() {
        return medicineId;
    }

    /**
     *
     * @param medicineId
     * The medicineId
     */
    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    /**
     *
     * @return
     * The medicineName
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     *
     * @param medicineName
     * The medicineName
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     *
     * @return
     * The startDateTime
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     *
     * @param startDateTime
     * The startDateTime
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     *
     * @return
     * The endDateTime
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     *
     * @param endDateTime
     * The endDateTime
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

}