package com.medicohealthcare.model;


import java.util.List;

public class AlarmReminderVM {
	public Long id;
	public String alarmDate;
	public String time1;
	public String time2;
	public String time3;
	public String time4;
	public String time5;
	public String time6;
	public String medicineName;
    public int doses;
    public int duration;
    public String startDate;
    public String endDate;
    public String doctorInstruction;

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> times;
    public AlarmReminderVM(Long id, String alarmDate,List<String> timelist,String medicieName) {

        this.id = id;
        this.alarmDate = alarmDate;
        times=timelist;
        this.medicineName = medicieName;
        // TODO Auto-generated constructor stub
    }
	public AlarmReminderVM(Long id, String alarmDate, String time1,
			String time2, String time3, String time4, String time5,
			String time6,String medicieName) {
		
		this.id = id;
		this.alarmDate = alarmDate;
		this.time1 = time1;
		this.time2 = time2;
		this.time3 = time3;
		this.time4 = time4;
		this.time5 = time5;
		this.time6 = time6;
        this.medicineName = medicieName;
		// TODO Auto-generated constructor stub
	}
    public AlarmReminderVM(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getTime4() {
        return time4;
    }

    public void setTime4(String time4) {
        this.time4 = time4;
    }

    public String getTime5() {
        return time5;
    }

    public void setTime5(String time5) {
        this.time5 = time5;
    }

    public String getTime6() {
        return time6;
    }

    public void setTime6(String time6) {
        this.time6 = time6;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
