package Utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Model.CustomProcedureTemplate;
import Model.TreatmentPlan;

/**
 * Created by Narendra on 14-02-2016.
 */
public class UtilSingleInstance {

    public static UtilSingleInstance utilSingleInstance = null;
    public static List<CustomProcedureTemplate> listOfProcedureTemplates;
    public static List<TreatmentPlan> treatmentPlan;

    public static CustomProcedureTemplate getCustomProcedureTemplate() {
        return customProcedureTemplate;
    }

    public static void setCustomProcedureTemplate(CustomProcedureTemplate customProcedureTemplate) {
        UtilSingleInstance.customProcedureTemplate = customProcedureTemplate;
    }

    public static CustomProcedureTemplate customProcedureTemplate;


    public static List<TreatmentPlan> getTreatmentPlan() {
        return treatmentPlan;
    }



    public static void setTreatmentPlan(List<TreatmentPlan> treatment) {
        treatmentPlan = treatment;
    }


    public static List<CustomProcedureTemplate> getListOfProcedureTemplates() {
        return listOfProcedureTemplates;
    }

    public static void setListOfProcedureTemplates(List<CustomProcedureTemplate> listOfProcedureTemplates) {
        UtilSingleInstance.listOfProcedureTemplates = listOfProcedureTemplates;
    }


    private UtilSingleInstance() {

    }

    public static UtilSingleInstance getInstance() {

        if (utilSingleInstance == null) {
            utilSingleInstance = new UtilSingleInstance();
        }
        return utilSingleInstance;
    }

    public String getLoggedInType() {
        return LoggedInType;
    }

    public void setLoggedInType(String loggedInType) {
        LoggedInType = loggedInType;
    }

    String LoggedInType;

    public static String getDateFormattedInStringFormatUsingLong(String longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = Long.parseLong(longDate);
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }
    public static String getDateFormattedInStringFormatUsingLong(Long longDate) {
        String dateText = "";
        if (longDate != null && longDate.longValue() > 0) {
            long val = longDate.longValue();
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }

    public static String getOnlyDateFormattedInStringFormatUsingLong(String longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = Long.parseLong(longDate);
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }
    public static String getOnlyDateFormattedInStringFormatUsingLong(Long longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = longDate.longValue();
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }

    public static String getOnlyTimeFormattedInStringFormatUsingLong(String longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = Long.parseLong(longDate);
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }
    public static String getOnlyTimeFormattedInStringFormatUsingLong(Long longDate) {
        String dateText = "";
        if (longDate != null && longDate.intValue() > 0) {
            long val = longDate.longValue();
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }

    public static String getDateFormattedInStringFormatUsingLongForMedicinDetails(String longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = Long.parseLong(longDate);
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm aa  yyyy-MM-dd", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }

    public static String getTimeFromLongDate(String longDate) {
        String dateText = "";
        if (longDate != null && longDate.toString().length() > 0) {
            long val = Long.parseLong(longDate);
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            dateText = df2.format(date);
            System.out.println(dateText);
        }

        return dateText;
    }

    public static String checkForServerErrorsInResponse(String strResponse) {
        // {"errorCode":"233"}
        JSONObject obj = null;
        String responseCode = "";
        String errorReturn = "";
        try {
            obj = new JSONObject(strResponse);
            responseCode = obj.getString("errorCode");

        } catch (Exception e) {

        }

        if (!responseCode.equalsIgnoreCase("") && responseCode.startsWith("2")) {
            errorReturn = "No Records Found";
        }
        return errorReturn;
    }

    public static String getUserType(String type) {
        String retString = "";
        if (type.equalsIgnoreCase("patient")) {
            retString = "0";
        } else if (type.equalsIgnoreCase("doctor")) {
            retString = "1";
        } else if (type.equalsIgnoreCase("assistant")) {
            retString = "2";
        } else if (type.equalsIgnoreCase("0")) {
            retString = "Patient";
        } else if (type.equalsIgnoreCase("1")) {
            retString = "Doctor";
        } else if (type.equalsIgnoreCase("2")) {
            retString = "Assistant";
        }
        return retString;
    }

    public static String getUserVisitType(byte type) {
        //  `visit_status` TINYINT(5) NULL DEFAULT NULL COMMENT '0=not visited\n1=visited\n2=unknown\n\n',

        String retString = "";
        switch( type)
        {
            case 0:
                retString = "New Case";
                break;
            case 1:
                retString = "Follow Up";
                break;
            case 2:
                retString = "Reports";
                break;
            case 3:
                retString = "Immunisation";
                break;
            default:
                retString = "unknown";

        }
        return retString;
    }

    public static int showMonth(int month) {
        int showMonth = month;
        switch (showMonth) {
            case 0:
                showMonth = showMonth + 1;
                break;
            case 1:
                showMonth = showMonth + 1;
                break;
            case 2:
                showMonth = showMonth + 1;
                break;
            case 3:
                showMonth = showMonth + 1;
                break;
            case 4:
                showMonth = showMonth + 1;
                break;
            case 5:
                showMonth = showMonth + 1;
                break;
            case 6:
                showMonth = showMonth + 1;
                break;
            case 7:
                showMonth = showMonth + 1;
                break;
            case 8:
                showMonth = showMonth + 1;
                break;
            case 9:
                showMonth = showMonth + 1;
                break;
            case 10:
                showMonth = showMonth + 1;
                break;
            case 11:
                showMonth = showMonth + 1;
                break;

        }
        return showMonth;
    }

    public static Calendar getStringToCal(String str) {

        String[] time = str.split(":");
        String[] timeMin = time[1].split(" ");
        int hr = Integer.parseInt(time[0]);
        int min = Integer.parseInt(timeMin[0]);
        String am = timeMin[1];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR, hr);
        if (am.equals("AM")) {
            cal.set(Calendar.AM_PM, Calendar.AM);
        } else {
            cal.set(Calendar.AM_PM, Calendar.PM);
        }
        //  cal.setTime(date);
        return cal;
    }

    public static String getStringToCalWithTodaysDate(String str) {
        //System.out.println("str --->"+UtilSingleInstance.getDateFormattedInStringFormatUsingLong("" + str));
        String[] time = str.split(":");
        String[] timeMin = time[1].split(" ");
        int hr = Integer.parseInt(time[0]);
        int min = Integer.parseInt(timeMin[0]);
        String am = timeMin[1];

        Calendar cal = Calendar.getInstance();


        cal.set(Calendar.HOUR, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (am.equals("AM")) {
            cal.set(Calendar.AM_PM, Calendar.AM);
        } else {
            cal.set(Calendar.AM_PM, Calendar.PM);
        }

        System.out.println("Appointment Date time--->" + UtilSingleInstance.getDateFormattedInStringFormatUsingLong("" + cal.getTimeInMillis()));


        // cal.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
        //  cal.setTime(date);
        return "" + cal.getTimeInMillis();
    }

    public Calendar getStringToTime(String time, Calendar calendar) {
        String[] timeValue;
        timeValue = time.split(":");
        int hour1 = Integer.parseInt(timeValue[0].trim());
        int min1 = Integer.parseInt(timeValue[1].trim().split("[a-zA-Z ]+")[0]);
        calendar.set(Calendar.HOUR, hour1);
        calendar.set(Calendar.MINUTE, min1);

        String strAM_PM = timeValue[1].replaceAll("[0-9]", "");
        if (strAM_PM.equals("AM")) {
            calendar.set(Calendar.AM_PM, 0);
        } else {
            calendar.set(Calendar.AM_PM, 1);
        }
        return calendar;
    }

    public Date getStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = null;
        try {
            dateValue = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateValue;
    }

}
