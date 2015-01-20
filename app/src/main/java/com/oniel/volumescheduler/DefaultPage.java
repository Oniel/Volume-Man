/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Default Sound Setting Page Logic (User)
* ******************************
*/
package com.oniel.volumescheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;


public class DefaultPage extends ActionBarActivity {
    /* globals */
    AudioManager audioManager; // device audio stream 
    SharedPreferences sharedPref; //this activities shared preferences file calling card
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancel_btn;
        Button apply_btn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /* set seekbar progress */
        //phone
        ImageView ic_phone = (ImageView) findViewById(R.id.cD_ic_phone);
        SeekBar s_phone = (SeekBar) findViewById(R.id.cD_sb_phone);
        s_phone.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        s_phone.setProgress(getPhone());
        initSeekBarImageView(s_phone, ic_phone, R.drawable.ic_phone_vol, R.drawable.ic_phone_off);
        //notifications
        ImageView ic_notification = (ImageView) findViewById(R.id.cD_ic_notification);
        SeekBar s_notification = (SeekBar) findViewById(R.id.cD_sb_notification);
        s_notification.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        s_notification.setProgress(getNotification());
        initSeekBarImageView(s_notification, ic_notification, R.drawable.ic_notification_vol, R.drawable.ic_notification_off);
        //feedback
        ImageView ic_feedback = (ImageView) findViewById(R.id.cD_ic_feedback);
        SeekBar s_feedback = (SeekBar) findViewById(R.id.cD_sb_feedback);
        s_feedback.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        s_feedback.setProgress(getFeedback());
        initSeekBarImageView(s_feedback, ic_feedback, R.drawable.ic_feedback_vol, R.drawable.ic_feedback_off);
        //media
        ImageView ic_media = (ImageView) findViewById(R.id.cD_ic_media);
        SeekBar s_media = (SeekBar) findViewById(R.id.cD_sb_media);
        s_media.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        s_media.setProgress(getMedia());
        initSeekBarImageView(s_media, ic_media, R.drawable.ic_media_vol, R.drawable.ic_media_off);
        //TODO add vibration checkboxes

        /* handle 'apply' button click - perform settings changes */
        apply_btn = (Button) findViewById(R.id.cD_btn_apply);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDefaultSetting();
                finish();
            }
        });

        /* handle 'cancel' button click - do nothing go back to main page */
        cancel_btn = (Button) findViewById(R.id.cD_btn_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /* perform default volume settings change */
    private void changeDefaultSetting(){
        //TODO change shared preferences file with updated settings
    }

    /* set seekbar listener and image update*/
    private void initSeekBarImageView(SeekBar sb, final ImageView icon, final int imageVolOn, final int imageVolOff){
        //set initial image icon
        if (sb.getProgress() == 0) icon.setImageResource(imageVolOff);
        else icon.setImageResource(imageVolOn);
        //set image change on seekbar change
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0) icon.setImageResource(imageVolOff);
                else icon.setImageResource(imageVolOn);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------
    /* default setting shared preferenes attributes and methods */

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
    
    /* verification that the default settings have been set, if false default settings have never been
    * set and therefore must be applied - not too sure how i should handle this... */
    public boolean defaultSettingsExistance(){ return sharedPref.getBoolean(ALREADY_SET, false);}

    /* getters */
    public int getPhone(){return sharedPref.getInt(PHONE_KEY,0);} //if doesn't exist set to half
    public int getNotification(){return sharedPref.getInt(NOTIFICATION_KEY, 0);}
    public int getFeedback(){return sharedPref.getInt(FEEDBACK_KEY,0);}
    public int getMedia(){return sharedPref.getInt(MEDIA_KEY,0);}

    //not currently implemented
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
