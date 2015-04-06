package com.oniel.volumescheduler;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

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
    public final static String TIMEFRAME = "TIMEFRAME"; //startHour:startMin:startDaysOfWeek:endHours:endMin:endDaysOfWeek
    public final static String TIME = "HUMANTIMEFRAME"; //12 hour format: starttime<am|pm> - endtime<am|pm>
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
        settingObject.setTime(intent.getStringExtra(RequestHandler.TIME));
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
