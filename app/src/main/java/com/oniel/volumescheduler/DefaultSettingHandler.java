/*
********************************
* Author: Oniel Toledo
* Created: 01.15.2015
* Description: Default Sound Settings Shared Preferences Object
* ******************************
*/
package com.oniel.volumescheduler;

import android.content.SharedPreferences;
import android.media.AudioManager;

public class DefaultSettingHandler {

    private SharedPreferences sharedPref;

    /* shared preference keys */
    private final static String ALREADY_SET = "ALREADYSET";

    private final static String PHONE_KEY = "PHONE";
    private final static String NOTIFICATION_KEY = "NOTIFICATION";
    private final static String FEEDBACK_KEY = "FEEDBACK";
    private final static String MEDIA_KEY = "MEDIA";

    private final static String PHONE_VIBRATION_KEY = "PHONEVIBRATION";
    private final static String NOTIFICATION_VIBRATION_KEY = "NOTIFICATIONVIBRATION";
    private final static String FEEDBACK_VIBRATION_KEY = "FEEDBACKVIBRATION";
    private final static String MEDIA_VIBRATIONA_KEY = "MEDIAVIBRATION";

    public DefaultSettingHandler(){}

    /* verification that the default settings have been set, if false default settings have never been
    * set and therefore must be applied - not too sure how i should handle this... */
    public boolean defaultSettingsExistance(){ return sharedPref.getBoolean(ALREADY_SET, false);}

    /* getters */
    public int getPhone(){return sharedPref.getInt(PHONE_KEY, AudioManager.STREAM_RING/2);} //if doesn't exist set to half
    public int getNotification(){return sharedPref.getInt(NOTIFICATION_KEY, AudioManager.STREAM_NOTIFICATION/2);}
    public int getFeedback(){return sharedPref.getInt(FEEDBACK_KEY,AudioManager.STREAM_SYSTEM/2);}
    public int getMedia(){return sharedPref.getInt(MEDIA_KEY,AudioManager.STREAM_MUSIC/2);}

    public int getPhoneVibrate(){return sharedPref.getInt(PHONE_VIBRATION_KEY,-1);}
    public int getNotificationVibrate(){return sharedPref.getInt(NOTIFICATION_VIBRATION_KEY,-1);}
    public int getFeedbackVibrate(){return sharedPref.getInt(FEEDBACK_VIBRATION_KEY,-1);}
    public int getMediaVibrate(){return sharedPref.getInt(MEDIA_VIBRATIONA_KEY,-1);}

    /* setter */
    public void setDefaultSound(int phone, int phone_v, int notification, int notification_v,
                                int feedback, int feedback_v, int media, int media_v){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ALREADY_SET, true);
        editor.putInt(PHONE_KEY, phone);
        editor.putInt(NOTIFICATION_KEY, notification);
        editor.putInt(FEEDBACK_KEY, feedback);
        editor.putInt(MEDIA_KEY, media);
        editor.putInt(PHONE_VIBRATION_KEY, phone_v);
        editor.putInt(NOTIFICATION_VIBRATION_KEY, notification_v);
        editor.putInt(FEEDBACK_VIBRATION_KEY, feedback_v);
        editor.putInt(MEDIA_VIBRATIONA_KEY, media_v);
        editor.commit();
    }

}

