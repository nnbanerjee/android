package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Model.Appointment;
import Model.AppointmentDB;
import Model.Reminder;

/**
 * Created by MNT on 18-Mar-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;

    public DatabaseHandler(Context context) {
        super(context,"MedicalDiary.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table reminder "+
                "(unique_id integer primary key autoincrement,user_id text,alarm_id text,alarm_date text,alarm_time text,alarm_title text)");
        db.execSQL("create table reminder_patient "+
                "(unique_id integer primary key autoincrement,user_id text,alarm_id text,alarm_date text,alarm_time text,alarm_title text)");
        db.execSQL("create table patient_appointment "+
                "(unique_id integer primary key autoincrement,patient_id text,alarm_id text,appointment_date text,appointment_time text,appointment_type text,appointment_status text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean savePatientAppointment(AppointmentDB appointment)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("patient_id",appointment.getPatientId());
        values.put("alarm_id",appointment.getAlarm_id());
        values.put("appointment_date",appointment.getAppointment_date());
        values.put("appointment_time",appointment.getAppointment_time());
        values.put("appointment_type",appointment.getAppointment_type());
        values.put("appointment_status",appointment.getAppointment_status());
        database.insert("patient_appointment", null, values);
        database.close();
        return true;
    }

    public boolean saveReminderPatient(Reminder reminder)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", reminder.getId());
        values.put("alarm_id",reminder.getAlarmId());
        values.put("alarm_date", reminder.getDate());
        values.put("alarm_time", reminder.getTime());
        values.put("alarm_title", reminder.getTitle());
        database.insert("reminder_patient", null, values);
        database.close();
        return true;
    }

    public boolean saveReminder(ArrayList<Reminder> reminderList)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        for(Reminder rem : reminderList) {

            ContentValues values = new ContentValues();
            values.put("user_id", rem.getId());
            values.put("alarm_id",rem.getAlarmId());
            values.put("alarm_date", rem.getDate());
            values.put("alarm_time", rem.getTime());
            values.put("alarm_title", rem.getTitle());
            database.insert("reminder", null, values);
        }


        database.close();
        return true;
    }

   public ArrayList<Reminder> getAllReminder(String id)
   {
       SQLiteDatabase database = this.getWritableDatabase();
       String query = "SELECT  * FROM reminder where "+"user_id"+" = '"+id+"'";
       Cursor cursor = database.rawQuery(query, null);
       String userId = id;

       ArrayList<Reminder> arrayReminder = new ArrayList<Reminder>();
       if(cursor.moveToFirst()) {
           do {
               System.out.println("in cursor = "+cursor.getString(cursor.getColumnIndex("alarm_id")));
               String alarm_id = cursor.getString(cursor.getColumnIndex("alarm_id"));
               String alarm_date = cursor.getString(cursor.getColumnIndex("alarm_date"));
               String alarm_time = cursor.getString(cursor.getColumnIndex("alarm_time"));
               String alarm_title = cursor.getString(cursor.getColumnIndex("alarm_title"));
               String uniqueId = cursor.getString(cursor.getColumnIndex("unique_id"));
               Reminder saveRem = new Reminder();
               saveRem.setId(userId);
               saveRem.setDate(alarm_date);
               saveRem.setTime(alarm_time);
               saveRem.setTitle(alarm_title);
               saveRem.setAlarmId(alarm_id);
               saveRem.setUniqueId(uniqueId);
               arrayReminder.add(saveRem);

           } while (cursor.moveToNext());
       }
       else{
           arrayReminder = null;

       }
       database.close();
       return arrayReminder;
   }

    public ArrayList<Reminder> getAllReminderPatient(String id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT  * FROM reminder_patient where "+"user_id"+" = '"+id+"'";
        Cursor cursor = database.rawQuery(query, null);
        String userId = id;

        ArrayList<Reminder> arrayReminder = new ArrayList<Reminder>();
        if(cursor.moveToFirst()) {
            do {
                System.out.println("in cursor = "+cursor.getString(cursor.getColumnIndex("alarm_id")));
                String alarm_id = cursor.getString(cursor.getColumnIndex("alarm_id"));
                String alarm_date = cursor.getString(cursor.getColumnIndex("alarm_date"));
                String alarm_time = cursor.getString(cursor.getColumnIndex("alarm_time"));
                String alarm_title = cursor.getString(cursor.getColumnIndex("alarm_title"));
                String uniqueId = cursor.getString(cursor.getColumnIndex("unique_id"));
                Reminder saveRem = new Reminder();
                saveRem.setId(userId);
                saveRem.setDate(alarm_date);
                saveRem.setTime(alarm_time);
                saveRem.setTitle(alarm_title);
                saveRem.setAlarmId(alarm_id);
                saveRem.setUniqueId(uniqueId);
                arrayReminder.add(saveRem);

            } while (cursor.moveToNext());
        }
        else{
            arrayReminder = null;

        }
        database.close();
        return arrayReminder;
    }
   public int setStatusAppointment(AppointmentDB db,String result)
   {
       String appointmentDate = "appointment_date";
       String appointmentTime = "appointment_time";
       SQLiteDatabase database = this.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put("appointment_status",result);
       int rows = database.update("patient_appointment",values,appointmentDate+" = ? AND "+appointmentTime+" =?",
               new String[]{db.getAppointment_date(),db.getAppointment_time()});
       return rows;
   }
   public ArrayList<AppointmentDB> getAllPatientAppointment(String id)
   {
       SQLiteDatabase database = this.getWritableDatabase();
       String query = "SELECT  * FROM patient_appointment where "+"patient_id"+" = '"+id+"'";
       Cursor cursor = database.rawQuery(query, null);
       ArrayList<AppointmentDB> arrayAppointments = new ArrayList<AppointmentDB>();
       if(cursor.moveToFirst()) {
           do {
               System.out.println("in cursor = "+cursor.getString(cursor.getColumnIndex("alarm_id")));
               String alarm = cursor.getString(cursor.getColumnIndex("alarm_id"));
               String appointmentDate = cursor.getString(cursor.getColumnIndex("appointment_date"));
               String appointmentTime = cursor.getString(cursor.getColumnIndex("appointment_time"));
               String appointmentType = cursor.getString(cursor.getColumnIndex("appointment_type"));
               String appointmentStatus = cursor.getString(cursor.getColumnIndex("appointment_status"));
               AppointmentDB db = new AppointmentDB();
               db.setPatientId(id);
               db.setAppointment_date(appointmentDate);
               db.setAlarm_id(alarm);
               db.setAppointment_time(appointmentTime);
               db.setAppointment_type(appointmentType);
               db.setAppointment_status(appointmentStatus);
               arrayAppointments.add(db);
           }while(cursor.moveToNext());
       }else{
            arrayAppointments = new ArrayList<AppointmentDB>();
       }
       database.close();
       return arrayAppointments;
   }
   public void deleteReminder(String uniqueId)
   {
       SQLiteDatabase database = this.getWritableDatabase();
       database.delete("reminder","unique_id "+"= '"+uniqueId+"'",null);
       database.close();
   }
    public void deleteReminderPatient(String uniqueId)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("reminder_patient","unique_id "+"= '"+uniqueId+"'",null);
        database.close();
    }

}
