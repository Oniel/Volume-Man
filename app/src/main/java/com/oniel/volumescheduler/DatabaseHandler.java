package com.oniel.volumescheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: database modification handler (handler)
* ******************************
*/
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
    private static final String COL_PHONE_VIBRATE = "phone_vibrate";
    private static final String COL_NOTIFICATION_VIBRATE = "notification_vibrate";
    private static final String COL_FEEDBACK_VIBRATE = "feedback_vibrate";
    private static final String COL_MEDIA_VIBRATE = "media_vibrate";

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
                + COL_PHONE_VIBRATE + " INTEGER, "
                + COL_NOTIFICATION_VIBRATE + " INTEGER, "
                + COL_FEEDBACK_VIBRATE + " INTEGER, "
                + COL_MEDIA_VIBRATE + " INTEGER"
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

    /* */
    public void getAllRows(){

    }

    public void addRow(SettingObject settingObject){

    }

    public void
}
