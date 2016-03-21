package Model;


public class Slot {

    private String slotNumber;
    private String name;
    private String daysOfWeek;
    private String startTime;
    private String endTime;
    private String numberOfAppointmentsForToday;

 
    private String doctorClinicId;
    private String autoConfirm;
    private String availability;
    private String feeBooking;
    private String feesConsultation;
    private String percentageOverbook;
    private String slotType;

    public String getVisitDuration() {
        return visitDuration;
    }

    public void setVisitDuration(String visitDuration) {
        this.visitDuration = visitDuration;
    }

    public String getDoctorClinicId() {
        return doctorClinicId;
    }

    public void setDoctorClinicId(String doctorClinicId) {
        this.doctorClinicId = doctorClinicId;
    }

    public String getAutoConfirm() {
        return autoConfirm;
    }

    public void setAutoConfirm(String autoConfirm) {
        this.autoConfirm = autoConfirm;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getFeeBooking() {
        return feeBooking;
    }

    public void setFeeBooking(String feeBooking) {
        this.feeBooking = feeBooking;
    }

    public String getFeesConsultation() {
        return feesConsultation;
    }

    public void setFeesConsultation(String feesConsultation) {
        this.feesConsultation = feesConsultation;
    }

    public String getPercentageOverbook() {
        return percentageOverbook;
    }

    public void setPercentageOverbook(String percentageOverbook) {
        this.percentageOverbook = percentageOverbook;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    private String visitDuration;
    
    
    /**
     *
     * @return
     * The slotNumber
     */
    public String getSlotNumber() {
        return slotNumber;
    }

    /**
     *
     * @param slotNumber
     * The slotNumber
     */
    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The daysOfWeek
     */
    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     *
     * @param daysOfWeek
     * The daysOfWeek
     */
    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     *
     * @return
     * The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     * The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     * The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     * The endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return
     * The numberOfAppointmentsForToday
     */
    public String getNumberOfAppointmentsForToday() {
        return numberOfAppointmentsForToday;
    }

    /**
     *
     * @param numberOfAppointmentsForToday
     * The numberOfAppointmentsForToday
     */
    public void setNumberOfAppointmentsForToday(String numberOfAppointmentsForToday) {
        this.numberOfAppointmentsForToday = numberOfAppointmentsForToday;
    }

}