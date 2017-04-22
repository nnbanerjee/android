package com.medico.model;

import java.util.List;

/**
 * Created by Narendra on 15-03-2016.
 */
public class VisitEditLogResponse {
    /*
    ############## symptom log ############
{"symptomLogs":"Doctor Vidya Pandit\\rTemp"}

############## diagnosis log ############
{"diagnosisLogs":"Doctor Vidya Pandit\\rRunning Nose"}

############## medicine log ############
{"medicineLogs":["Doctor Vidya Pandit\\rcrocine- schedule 0- No. of doses 2- duration 4\\r Milind Darda\\r medicine removed\\r Milind Darda\\r medicine removed\\r Doctor Milind Darda\\r medicine removed\\r Doctor Milind Darda\\r medicine removed"]}

############## diagnostic test log ############
{"testLogs":["Doctor Vidya Pandit\\rRight Hand xray\\r Doctor Vidya Pandit\\rRight Hand xray","Doctor Vidya Pandit\\rRight Hand xray"]}
     */

    private String symptomLogs;
    private String diagnosisLogs;
    private List<String> medicineLogs;
    private List<String> testLogs;

    public List<String> getTestLogs() {
        return testLogs;
    }

    public void setTestLogs(List<String> testLogs) {
        this.testLogs = testLogs;
    }

    public String getSymptomLogs() {
        return symptomLogs;
    }

    public void setSymptomLogs(String symptomLogs) {
        this.symptomLogs = symptomLogs;
    }

    public String getDiagnosisLogs() {
        return diagnosisLogs;
    }

    public void setDiagnosisLogs(String diagnosisLogs) {
        this.diagnosisLogs = diagnosisLogs;
    }

    public List<String> getMedicineLogs() {
        return medicineLogs;
    }

    public void setMedicineLogs(List<String> medicineLogs) {
        this.medicineLogs = medicineLogs;
    }




}
