/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Object class for a single setting (object)
* ******************************
*/
package com.oniel.volumescheduler;

public class SettingObject {
    /* object attributes */
    private int id;
    private String title;
    private int fromHour, fromMin;
    private int toHour, toMin;
    private String timeFrame;
    private String daysofweek;
    private int phone, phoneVibration;
    private int notification, notificationVibration;
    private int feedback, feedbackVibration;
    private int media, mediaVibration;

    /* getters */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getFromHour() {
        return fromHour;
    }

    public int getFromMin() {
        return fromMin;
    }

    public int getToHour() {
        return toHour;
    }

    public int getToMin() {
        return toMin;
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

    public int getPhoneVibration() {
        return phoneVibration;
    }

    public int getNotification() {
        return notification;
    }

    public int getNotificationVibration() {
        return notificationVibration;
    }

    public int getFeedback() {
        return feedback;
    }

    public int getFeedbackVibration() {
        return feedbackVibration;
    }

    public int getMedia() {
        return media;
    }

    public int getMediaVibration() {
        return mediaVibration;
    }

    /* setters */

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public void setFromMin(int fromMin) {
        this.fromMin = fromMin;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public void setToMin(int toMin) {
        this.toMin = toMin;
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

    public void setPhoneVibration(int phoneVibration) {
        this.phoneVibration = phoneVibration;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public void setNotificationVibration(int notificationVibration) {
        this.notificationVibration = notificationVibration;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public void setFeedbackVibration(int feedbackVibration) {
        this.feedbackVibration = feedbackVibration;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public void setMediaVibration(int mediaVibration) {
        this.mediaVibration = mediaVibration;
    }
}
