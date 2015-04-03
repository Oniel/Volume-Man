/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Object class for a single sound setting (object)
* ******************************
*/
package com.oniel.volumescheduler;

public class SettingObject {
    /* object attributes */
    private int id;
    private String title;
    //NOTE delete if not used
    //private int fromHour, fromMin;
    //private int toHour, toMin;

    private String startTime;
    private String endTime;
    private String timeFrame;
    private String daysofweek;
    private int phone;
    private int notification;
    private int feedback;
    private int media;
    private boolean vibration;

    /* getters */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

//NOTE delete if not used
//    public int getFromHour() {
//        return fromHour;
//    }
//
//    public int getFromMin() {
//        return fromMin;
//    }
//
//    public int getToHour() {
//        return toHour;
//    }
//
//    public int getToMin() {
//        return toMin;
//    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public String getDaysofweek() {
        return daysofweek;
    }

    public int getPhone() {
        return phone;
    }

    public int getNotification() {
        return notification;
    }


    public int getFeedback() {
        return feedback;
    }

    public int getMedia() {
        return media;
    }

    public boolean getVibration() {
        return vibration;
    }

    /* setters */

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//NOTE delete if not used
//    public void setFromHour(int fromHour) {
//        this.fromHour = fromHour;
//    }
//
//    public void setFromMin(int fromMin) {
//        this.fromMin = fromMin;
//    }
//
//    public void setToHour(int toHour) {
//        this.toHour = toHour;
//    }
//
//    public void setToMin(int toMin) {
//        this.toMin = toMin;
//    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public void setDaysofweek(String daysofweek) {
        this.daysofweek = daysofweek;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }
}
