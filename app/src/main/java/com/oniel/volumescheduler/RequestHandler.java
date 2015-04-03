package com.oniel.volumescheduler;

import android.content.Intent;

/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Request Handler Class
*               holds constant and check functions, everything is static- no need for instantiation
* ******************************
*/
public class RequestHandler {

    public final static String REQUEST_CODE = "REQUESTCODE";

    /* request codes */
    public static final int REQ_NO_RESULT = 0;
    public static final int REQ_DEFAULT = 1;
    public static final int REQ_NEW_SETTING = 2;
    public static final int REQ_UPDATE_SETTING = 3;

    /* keys */
    public final static String TITLE = "TITLE";
//NOTE delete if not used
//    public final static String START_HOUR = "FROMHOUR";
//    public final static String START_MIN = "FROMMIN";
//    public final static String END_HOUR = "TOHOUR";
//    public final static String END_MIN = "TOMIN";
    public final static String STARTTIME = "STARTTIME";
    public final static String ENDTIME = "ENDTIME";
    public final static String TIMEFRAME = "TIMEFRAME";
    public final static String DAYSOFWEEK = "DAYSOFWEEK";

    public final static String PHONE = "PHONE";
    public final static String NOTIFICATION = "NOTIFICATION";
    public final static String FEEDBACK = "FEEDBACK";
    public final static String MEDIA = "MEDIA";
    public final static String VIBRATION = "VIBRATION";


    public final static String ALREADY_SET = "ALREADYSET";

    /* Determines if the start/stop times of 2 settings interfere */
    public static boolean doSettingsInterfere(){
        //TODO boolean return: check to see if a sound setting exists for this current time
        //may want to change to return the name of the setting that interferes
        return false;
    }


    //TODO boolean return: check to see if a default sound setting exists

    //creates a setting Object
    public static SettingObject createSettingObjectFromIntent(Intent intent){
        SettingObject settingObject = new SettingObject();
        settingObject.setTitle(intent.getStringExtra(RequestHandler.TITLE));
//NOTE delete if not used
//        settingObject.setFromHour(intent.getIntExtra(RequestHandler.START_HOUR, 0));
//        settingObject.setFromMin(intent.getIntExtra(RequestHandler.START_MIN, 0));
//        settingObject.setToHour(intent.getIntExtra(RequestHandler.END_HOUR, 0));
//        settingObject.setToMin(intent.getIntExtra(RequestHandler.END_MIN, 0));

        settingObject.setStartTime(intent.getStringExtra(RequestHandler.STARTTIME));
        settingObject.setEndTime(intent.getStringExtra(RequestHandler.ENDTIME));
        settingObject.setTimeFrame(intent.getStringExtra(RequestHandler.TIMEFRAME));//later let updateSetting handle this
        settingObject.setDaysofweek(intent.getStringExtra(RequestHandler.DAYSOFWEEK));
        settingObject.setPhone(intent.getIntExtra(RequestHandler.PHONE, 0));
        settingObject.setNotification(intent.getIntExtra(RequestHandler.NOTIFICATION, 0));
        settingObject.setFeedback(intent.getIntExtra(RequestHandler.FEEDBACK, 0));
        settingObject.setMedia(intent.getIntExtra(RequestHandler.MEDIA, 0));
        settingObject.setVibration(intent.getBooleanExtra(RequestHandler.VIBRATION, false));
        return settingObject;
    }

}
