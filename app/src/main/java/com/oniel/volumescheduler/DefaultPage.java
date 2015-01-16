/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Default Sound Setting Page Logic (User)
* ******************************
*/
package com.oniel.volumescheduler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancel_btn;
        Button apply_btn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        /* initiate default settings shared preferences object */
        DefaultSettingHandler defaultSettingHandler = new DefaultSettingHandler();

        /* adjust icon and SeekBar values */
        //phone
        ImageView ic_phone = (ImageView) findViewById(R.id.cD_ic_phone);
        SeekBar s_phone = (SeekBar) findViewById(R.id.cD_sb_phone);
        s_phone.setMax(AudioManager.STREAM_RING);
        s_phone.setProgress(defaultSettingHandler.getPhone());
        initSeekBarImageView(s_phone, ic_phone, R.drawable.ic_phone_vol, R.drawable.ic_phone_off);
        //notifications
        ImageView ic_notification = (ImageView) findViewById(R.id.cD_ic_notification);
        SeekBar s_notification = (SeekBar) findViewById(R.id.cD_sb_notification);
        s_notification.setMax(AudioManager.STREAM_NOTIFICATION);
        s_notification.setProgress(defaultSettingHandler.getNotification());
        initSeekBarImageView(s_notification, ic_notification, R.drawable.ic_notification_vol, R.drawable.ic_notification_off);
        //feedback
        ImageView ic_feedback = (ImageView) findViewById(R.id.cD_ic_feedback);
        SeekBar s_feedback = (SeekBar) findViewById(R.id.cD_sb_feedback);
        s_feedback.setMax(AudioManager.STREAM_SYSTEM);
        s_feedback.setProgress(defaultSettingHandler.getFeedback());
        initSeekBarImageView(s_feedback, ic_feedback, R.drawable.ic_feedback_vol, R.drawable.ic_feedback_off);
        //media
        ImageView ic_media = (ImageView) findViewById(R.id.cD_ic_media);
        SeekBar s_media = (SeekBar) findViewById(R.id.cD_sb_media);
        s_media.setMax(AudioManager.STREAM_MUSIC);
        s_media.setProgress(defaultSettingHandler.getMedia());
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

    private void initSeekBarImageView(SeekBar sb, ImageView icon, int imageVol, int imageOff){
        /*LEFT OFF TODO
            1) set image icon
            2) set seekbar progress change onclick listener
         */
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
}
