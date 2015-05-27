/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Default Sound Setting Page Logic (User)
* Vibration:
*   if checked:     progress bars will be set to 0 and phone is placed on vibration only,
*                   but the previous seekbar volume settings are still saved (don't forget this impl. elsewhere!! on AlarmSchedule)
*   if unchecked:   previous progress bar setting will be reinstated, user will be able to change volume as necassary.
* ******************************
*/
package com.oniel.volumescheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class DefaultPage extends ActionBarActivity {
    /* globals */
    private AudioManager audioManager;
    public SharedPreferences sharedPref;
    public final static String PREF = "PREF";

    private SeekBar phone_sb;
    private SeekBar notification_sb;
    private SeekBar feedback_sb;
    private SeekBar media_sb;
    private CheckBox vibration;

    public Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancel_btn;
        Button apply_btn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        initDefaultSharedPref(); //launch shared preferences
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        initSeekBars();

        /* handle 'apply' button click - perform settings changes */
        apply_btn = (Button) findViewById(R.id.dP_btn_apply);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* update default vibration saved settings*/
                setDefaultSound(
                        phone_sb.getProgress(),
                        notification_sb.getProgress(),
                        feedback_sb.getProgress(),
                        media_sb.getProgress(),
                        vibration.isChecked()
                ); //after this getX() is valid for use

                /* change the phone's volume setting if another sound setting does not currently interfere */
                if(!RequestHandler.doSettingsInterfere()){
                    if(vibration.isChecked()){
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); //NOTE: previous volume settings are preserved
                        setVolumeControlStream(AudioManager.STREAM_MUSIC); //NOTE: ringer_mode_silent does not effect stream_music...idk y
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getMedia(), 0);

                    } else {

                        if(isSilent()){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            setVolumeControlStream(AudioManager.STREAM_MUSIC); //NOTE: ringer_mode_silent does not effect stream_music...idk y
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getMedia(), 0);
                        } else {
                            setVolumeControlStream(AudioManager.STREAM_RING);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, getPhone(), 0);
                            setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
                            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, getNotification(), 0);
                            setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, getFeedback(), 0);
                            setVolumeControlStream(AudioManager.STREAM_MUSIC);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getMedia(), 0);
                        }
                    }
                }

                Toast.makeText(context, R.string.toast_default_set, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /* handle 'cancel' button click - do nothing go back to main page */
        cancel_btn = (Button) findViewById(R.id.dP_btn_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, R.string.toast_no_change, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initSeekBars(){
        //phone
        ImageView ic_phone = (ImageView) findViewById(R.id.dP_ic_phone);
        phone_sb = (SeekBar) findViewById(R.id.dP_sb_phone);
        phone_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        phone_sb.setProgress(getPhone());
        initSeekBarImageView(phone_sb, ic_phone, R.drawable.ic_phone_vol, R.drawable.ic_phone_off);
        //notifications
        ImageView ic_notification = (ImageView) findViewById(R.id.dP_ic_notification);
        notification_sb = (SeekBar) findViewById(R.id.dP_sb_notification);
        notification_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        notification_sb.setProgress(getNotification());
        initSeekBarImageView(notification_sb, ic_notification, R.drawable.ic_notification_vol, R.drawable.ic_notification_off);
        //feedback
        ImageView ic_feedback = (ImageView) findViewById(R.id.dP_ic_feedback);
        feedback_sb = (SeekBar) findViewById(R.id.dP_sb_feedback);
        feedback_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        feedback_sb.setProgress(getFeedback());
        initSeekBarImageView(feedback_sb, ic_feedback, R.drawable.ic_feedback_vol, R.drawable.ic_feedback_off);
        //media
        ImageView ic_media = (ImageView) findViewById(R.id.dP_ic_media);
        media_sb = (SeekBar) findViewById(R.id.dP_sb_media);
        media_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        media_sb.setProgress(getMedia());
        initSeekBarImageView(media_sb, ic_media, R.drawable.ic_media_vol, R.drawable.ic_media_off);
        //vibration
        vibration = (CheckBox) findViewById(R.id.dP_cb_vibrate);
        initCheckBoxVibration(vibration);
        vibration.setChecked(getVibration());
    }

    /* set seekbar listener and image update*/
    private void initSeekBarImageView(SeekBar sb, final ImageView icon, final int imageVolOn, final int imageVolOff){
        //set initial image icon
        if (sb.getProgress() == 0) icon.setImageResource(imageVolOff);
        else icon.setImageResource(imageVolOn);
        //set image change on seekbar change
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //this could be made more efficeint if I could get the previous progress setting...
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //if ringer is silent, notification and feedback must also be silent (built into system)
                if(progress == 0) {
                    icon.setImageResource(imageVolOff);
                    if(seekBar.equals(phone_sb)){
                        notification_sb.setProgress(0);
                        feedback_sb.setProgress(0);
                    }

                } else {
                    //ringer cannot be silent if either notification or feedback are not silent (built into system)
                    if(phone_sb.getProgress()==0 && (seekBar.equals(notification_sb) || seekBar.equals(feedback_sb)))
                        phone_sb.setProgress(1);


                    icon.setImageResource(imageVolOn);
                    if(vibration.isChecked()) vibration.setChecked(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initCheckBoxVibration(CheckBox vibration){
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    setSeekbarsSilent();
                }
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
    // default setting shared preferences attributes and methods

    /* initialize default settings shared preferences*/
    public void initDefaultSharedPref(){sharedPref = getSharedPreferences(PREF,MODE_PRIVATE);}

    /* verification that the default settings have been set, if false default settings have never been
    * set and therefore must be applied - not too sure how i should handle this... */
    public boolean defaultSettingsExistance(){ return sharedPref.getBoolean(RequestHandler.ALREADY_SET, false);}

    /* get Default Sound Settings shared preferences values (if doesn't exist returns 1/2 volume as def) */
    public int getPhone(){return sharedPref.getInt(RequestHandler.PHONE, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)/2);}
    public int getNotification(){return sharedPref.getInt(RequestHandler.NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/2);}
    public int getFeedback(){return sharedPref.getInt(RequestHandler.FEEDBACK, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)/2);}
    public int getMedia(){return sharedPref.getInt(RequestHandler.MEDIA, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2);}
    public boolean getVibration(){return sharedPref.getBoolean(RequestHandler.VIBRATION, false);}

    /* set Default Sound Settings to the class's shared preferences */
    public void setDefaultSound(int phone, int notification, int feedback, int media, boolean vibration){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(RequestHandler.PHONE, phone);
        editor.putInt(RequestHandler.NOTIFICATION, notification);
        editor.putInt(RequestHandler.FEEDBACK, feedback);
        editor.putInt(RequestHandler.MEDIA, media);
        editor.putBoolean(RequestHandler.VIBRATION,vibration);
        editor.commit();
    }

    /* sets vol seekbars to silent (does not change shared prefs) */
    public void setSeekbarsSilent(){
        phone_sb.setProgress(0);
        notification_sb.setProgress(0);
        feedback_sb.setProgress(0);
        media_sb.setProgress(0);
    }

    /* return true if volumes are set to silent*/
    public boolean isSilent(){
        return (getPhone()==0 && getNotification()==0 && getFeedback()==0 && getMedia()==0);
    }

}
