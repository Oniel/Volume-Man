package com.oniel.volumescheduler;

/**
 * Created by oniel on 1/3/2015.
 */
public class RequestHandler {

    /* request codes */
    public static final int REQ_NO_RESULT = 0;
    public static final int REQ_DEFAULT = 1;
    public static final int REQ_NEW_SETTING = 2;
    public static final int REQ_UPDATE_SETTING = 3;

    public final static String REQUEST_CODE = "REQUESTCODE";

    /* data return keys*/
    public final static String RET_TITLE = "TITLE";
    public final static String RET_FROM_HOURS = "FROMHOURS";
    public final static String RET_FROM_MINS = "FROMMINS";
    public final static String RET_TO_HOURS = "TOHOURS";
    public final static String RET_TO_MINS = "TOMINS";
    public final static String RET_TIMEFRAME = "TIMEFRAME";
    public final static String RET_DAYSOFWEEK = "DAYSOFWEEK";
    public final static String RET_PHONE = "PHONE";
    public final static String RET_PHONE_VIBRATION = "PHONEVIBRATION";
    public final static String RET_NOTIFICATION = "NOTIFICATION";
    public final static String RET_NOTIFICATION_VIBRATION = "NOTIFICATIONVIBRATION";
    public final static String RET_FEEDBACK = "FEEDBACK";
    public final static String RET_FEEDBACK_VIBRATION = "FEEDBACKVIBRATION";
    public final static String RET_MEDIA = "MEDIA";
    public final static String RET_MEDIA_VIBRATION = "MEDIAVIBRATION";
}
