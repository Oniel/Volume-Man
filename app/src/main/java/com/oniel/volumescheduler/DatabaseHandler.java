/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: database modification handler (handler)
* ******************************
*/
package com.oniel.volumescheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    /* database information */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VolumeScheduler";

    /* database tables */
    private static final String TABLE_VOLUMESETTINGS = "VolumeSettings";

    /* database table column names*/
    //table: VolumeSettings
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";

    private static final String COL_FROM_HOUR = "from_hour";
    private static final String COL_FROM_MIN = "from_min";
    private static final String COL_TO_HOUR = "to_hour";
    private static final String COL_TO_MIN = "to_min";
    private static final String COL_TIMEFRAME = "time_frame";
    private static final String COL_DAYSOFWEEK = "days_of_week";
    private static final String COL_PHONE = "phone";
    private static final String COL_NOTIFICATION = "notification";
    private static final String COL_FEEDBACK = "feedback";
    private static final String COL_MEDIA = "media";
    private static final String COL_VIBRATION = "vibration";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VOLUMESETTINGS_TABLE = "CREATE TABLE "
                + TABLE_VOLUMESETTINGS + " ( "
                + COL_ID + " INTEGER PRIMARY KEY, "
                + COL_TITLE + " TEXT, "
                + COL_FROM_HOUR + " INTEGER, "
                + COL_FROM_MIN + " INTEGER, "
                + COL_TO_HOUR + " INTEGER,"
                + COL_TO_MIN + " INTEGER,"
                + COL_TIMEFRAME + " TEXT, "
                + COL_DAYSOFWEEK + " TEXT, "
                + COL_PHONE + " INTEGER, "
                + COL_NOTIFICATION + " INTEGER, "
                + COL_FEEDBACK + " INTEGER, "
                + COL_MEDIA + " INTEGER,"
                + COL_VIBRATION + " INTEGER, "
                + ")";
        db.execSQL(CREATE_VOLUMESETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLUMESETTINGS);
        // Create tables again
        onCreate(db);
    }

    public int getRowsCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_VOLUMESETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }


    public List<SettingObject> getAllRows(){
        List<SettingObject> settingsList = new ArrayList<SettingObject>();
        String sql = "SELECT * FROM " + TABLE_VOLUMESETTINGS + " ORDER BY " + COL_TITLE + " ASC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null); //cursor is basically resultset

        if(cursor.moveToFirst()) {
            do {
                SettingObject settingObject = new SettingObject();
                settingObject.setId(cursor.getInt(0));
                settingObject.setTitle(cursor.getString(1));
                settingObject.setFromHour(cursor.getInt(2));
                settingObject.setFromMin(cursor.getInt(3));
                settingObject.setToHour(cursor.getInt(4));
                settingObject.setToMin(cursor.getInt(5));
                settingObject.setTimeFrame(cursor.getString(6));
                settingObject.setDaysofweek(cursor.getString(7));
                settingObject.setPhone(cursor.getInt(8));
                settingObject.setNotification(cursor.getInt(9));
                settingObject.setFeedback(cursor.getInt(10));
                settingObject.setMedia(cursor.getInt(11));
                settingObject.setVibration(cursor.getInt(12)==1); //there is no boolean

                settingsList.add(settingObject);
            } while (cursor.moveToNext());
        }
        return settingsList;
    }

    public boolean rowExistence(String _title){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) AS FROM " + TABLE_VOLUMESETTINGS + " WHERE title='" + _title + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getInt(0) == 0) return false;
        return true;
    }

    public void addRow(SettingObject settingObject){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, settingObject.getTitle());
        values.put(COL_FROM_HOUR, settingObject.getFromHour());
        values.put(COL_FROM_MIN, settingObject.getFromMin());
        values.put(COL_TO_HOUR, settingObject.getToHour());
        values.put(COL_TO_MIN, settingObject.getToMin());
        values.put(COL_TIMEFRAME, settingObject.getTimeFrame());
        values.put(COL_DAYSOFWEEK, settingObject.getDaysofweek());
        values.put(COL_PHONE, settingObject.getPhone());
        values.put(COL_NOTIFICATION, settingObject.getNotification());
        values.put(COL_FEEDBACK, settingObject.getFeedback());
        values.put(COL_MEDIA, settingObject.getMedia());
        values.put(COL_VIBRATION, settingObject.getVibration());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_VOLUMESETTINGS, null, values);
        db.close();
    }

    public int updateRow(SettingObject settingObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, settingObject.getTitle());
        values.put(COL_FROM_HOUR, settingObject.getFromHour());
        values.put(COL_FROM_MIN, settingObject.getFromMin());
        values.put(COL_TO_HOUR, settingObject.getToHour());
        values.put(COL_TO_MIN, settingObject.getToMin());
        values.put(COL_TIMEFRAME, settingObject.getTimeFrame());
        values.put(COL_DAYSOFWEEK, settingObject.getDaysofweek());
        values.put(COL_PHONE, settingObject.getPhone());
        values.put(COL_NOTIFICATION, settingObject.getNotification());
        values.put(COL_FEEDBACK, settingObject.getFeedback());
        values.put(COL_MEDIA, settingObject.getMedia());
        values.put(COL_VIBRATION, settingObject.getVibration());

        // updating row
        return db.update(TABLE_VOLUMESETTINGS, values, COL_TITLE + " = ?",
                new String[] { String.valueOf(settingObject.getTitle()) });
    }

    public void deleteRow(SettingObject settingObject){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = new String[] { settingObject.getTitle() };
        db.delete( TABLE_VOLUMESETTINGS, COL_TITLE + " = ?", whereArgs);
        db.close();
    }

    /* */
    public void deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE * FROM " + TABLE_VOLUMESETTINGS);
        db.close();
    }

    /* Default Settings Table */

}
