/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Sound Setting Creation/Modification Page (User)
* ******************************
*/
package com.oniel.volumescheduler;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SettingPage extends ActionBarActivity {
    /* globals */
    private Context context = this;
    private int REQ_CODE = 0; //0 is NO_RESULT error
    private DatabaseHandler database;
    private AudioManager audioManager; // device audio stream
    private final static String PREF = "PREF";

    private EditText title;
    private TimePicker startTime_tp;
    private TimePicker endTime_tp;
    private boolean[] daysOfWeek; //0-sun, 1-mon,...
    private SeekBar phone_sb;
    private SeekBar notification_sb;
    private SeekBar feedback_sb;
    private SeekBar media_sb;
    private CheckBox vibration;

    private String origTitle;

    /* conflict class */
    public class Conflict {
        boolean conflict;
        String message;
        Conflict(){
            conflict = false;
            message = "";
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        REQ_CODE = getIntent().getIntExtra(RequestHandler.REQUEST_CODE, 0);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        database = new DatabaseHandler(this);

        initViews(); //only instantiates views

        /* initialize view values */
        if(REQ_CODE == RequestHandler.REQ_UPDATE_SETTING) {
            setViewsFromIntent(getIntent());
        } else {
            //init seekbars from default shared prefs
            SharedPreferences defaultPreferences = getSharedPreferences(PREF, MODE_PRIVATE);
            phone_sb.setProgress(defaultPreferences.getInt(RequestHandler.PHONE, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)/2));
            notification_sb.setProgress(defaultPreferences.getInt(RequestHandler.NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/2));
            feedback_sb.setProgress(defaultPreferences.getInt(RequestHandler.FEEDBACK, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)/2));
            media_sb.setProgress(defaultPreferences.getInt(RequestHandler.MEDIA, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2));
            vibration.setChecked(defaultPreferences.getBoolean(RequestHandler.VIBRATION, false));
        }


        /* handle cancel/ok buttons */
        Button cancel = (Button) findViewById(R.id.uS_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, R.string.toast_no_change, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button ok = (Button) findViewById(R.id.uS_btn_apply);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateSubmission()){
                    if(REQ_CODE == RequestHandler.REQ_UPDATE_SETTING) Toast.makeText(context, R.string.toast_setting_updated, Toast.LENGTH_SHORT).show();
                    else Toast.makeText(context, R.string.toast_setting_created, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    /* validate and prepare setting for saving */
    private boolean validateSubmission(){
        //TODO handle updateSetting
        //error check and intent activity submission
        if(title.getText().toString().matches("")) {
            Toast.makeText(this, R.string.toast_no_title, Toast.LENGTH_LONG).show(); return false;
        } else if(database.rowExistence(title.getText().toString())){
            Toast.makeText(this, R.string.toast_title_exists, Toast.LENGTH_LONG).show(); return false;
        } else {
            String startTime = timeTo24HrString(startTime_tp); //this maybe obselete if timeFrame can be used
            String endTime = timeTo24HrString(endTime_tp);

            String daysOfWeekStr = daysOfWeekToString();
            String timeFrame = getTimeFrame(startTime_tp, endTime_tp, daysOfWeekStr);

            if(startTime.equals(endTime)){ //time frames are the same
                Toast.makeText(this, R.string.toast_timeframe_invalid, Toast.LENGTH_LONG).show(); return false;
            }

            //check database for conflicting time frames with this new setting
//            Conflict conflict = checkTimeFrameConfliction(timeframe);
//            if(conflict.conflict){
//                Toast.makeText(this, R.string.toast_timeframe_conflict + conflict.message, Toast.LENGTH_LONG).show(); return false;
//            }

            String time = generate12HourTime(startTime, endTime);
            System.out.println("settingpage: " + time);

            /* no errors, return new setting as intent to MainPage */
            Intent resultIntent = new Intent();
            if(REQ_CODE == RequestHandler.REQ_UPDATE_SETTING)
                resultIntent.putExtra(RequestHandler.TITLE, origTitle + ":" + title.getText().toString());
            else resultIntent.putExtra(RequestHandler.TITLE, title.getText().toString());
            resultIntent.putExtra(RequestHandler.DAYSOFWEEK, daysOfWeekStr);
            resultIntent.putExtra(RequestHandler.TIME, time);
            resultIntent.putExtra(RequestHandler.TIMEFRAME, timeFrame);
            resultIntent.putExtra(RequestHandler.PHONE, phone_sb.getProgress());
            resultIntent.putExtra(RequestHandler.NOTIFICATION, notification_sb.getProgress());
            resultIntent.putExtra(RequestHandler.FEEDBACK, feedback_sb.getProgress());
            resultIntent.putExtra(RequestHandler.MEDIA, media_sb.getProgress());
            resultIntent.putExtra(RequestHandler.VIBRATION,vibration.isChecked());
            setResult(REQ_CODE, resultIntent); //REQ_CODE is the result code
            return true;
        }
    }

    /* time frame check procedure
       if single day
            if new time frame hours falls within existing time frame hours
            else if time frame hours
       if multiday


    */

    /* checks database for any existing setting who's timeframe interferes with the parameter THIS IS GOING TO BE A BITCH TO DO */
    private Conflict checkTimeFrameConfliction(String timeframe){
        //time variables of the new setting
        int newStartHour = Integer.parseInt(timeframe.split(":")[0]);
        int newStartMin = Integer.parseInt(timeframe.split(":")[1]);
        int newEndHour = Integer.parseInt(timeframe.split(":")[3]);
        int newEndMin = Integer.parseInt(timeframe.split(":")[4]);

        List<SettingObject> settingObjectList = database.getAllRows();
        //perform conflict check for each saved item in the database
        for(SettingObject setting : settingObjectList){
            //time variables of the existing setting
            int oldStartHour = Integer.parseInt(setting.getTimeFrame().split(":")[0]);
            int oldStartMin = Integer.parseInt(setting.getTimeFrame().split(":")[1]);
            int oldEndHour = Integer.parseInt(setting.getTimeFrame().split(":")[3]);
            int oldEndMin = Integer.parseInt(setting.getTimeFrame().split(":")[4]);

            //single day
            if(isTimeFrameSingleDay(timeframe)){
                //falls w/in existing time frame (broke into multiple else if's for readability
                if((oldStartHour<newStartHour && newStartHour<oldEndHour) || (oldStartHour<newEndHour && newEndHour<oldEndHour))
                    return dowConfliction(timeframe, setting.getTimeFrame(), setting.getTitle());
                else if(oldStartHour==newStartHour && oldStartMin<newStartMin)
                    return dowConfliction(timeframe, setting.getTimeFrame(), setting.getTitle());
                else if(oldStartHour==newEndHour && oldStartMin<newEndMin)
                    return dowConfliction(timeframe, setting.getTimeFrame(), setting.getTitle());
                else if(oldEndHour==newStartHour && newStartMin<oldStartMin)
                    return dowConfliction(timeframe, setting.getTimeFrame(), setting.getTitle());
                else if(oldEndHour==newEndHour && newEndMin<oldEndHour)
                    return dowConfliction(timeframe, setting.getTimeFrame(), setting.getTitle());
                else
                    return new Conflict(); //returns conflict.conflict=false
            //multiday -oposite of singe day hour check
            } else {
                //fals w/in existing time frame
                //2230 0030 -- 2230

            }

        }
        return new Conflict();
    }

    /* check to see if days of the week conflict */
    //days, everyday, repeat once
    private Conflict dowConfliction(String newTimeFrame, String oldTimeFrame, String oldTitle){
        Conflict conflict = new Conflict();

        return conflict;
    }

    /* return true if single day, return false if multiday */
    private boolean isTimeFrameSingleDay(String timeframe){
        return timeframe.split(":")[2].equals(timeframe.split(":")[5]);
    }

    //generate a time frame: startHour:startMinute:startDaysOfWeek:endHour:endMinute:endDaysOfWeek
    private String getTimeFrame(TimePicker startTime, TimePicker endTime, String dows){
        int sH = startTime.getCurrentHour();
        int sM = startTime.getCurrentMinute();
        int eH = endTime.getCurrentHour();
        int eM = endTime.getCurrentMinute();
        String timeframe = "";
        //handle single day and multiday timeframe setup
        if(sH == eH){
            if(sM < eM){        //case 1: single day
                timeframe = String.valueOf(sH)+":"+String.valueOf(sM)+":"+dows+":"+String.valueOf(eH)+":"+String.valueOf(eM)+":"+dows;
            } else if (sM > eM){    //case 1: multiday
                timeframe = String.valueOf(sH)+":"+String.valueOf(sM)+":"+dows+":"+String.valueOf(eH)+":"+String.valueOf(eM)+":"+increaseDOWby1(dows);
            }
        } else if(sH < eH){     //case 1: single day
            timeframe = String.valueOf(sH)+":"+String.valueOf(sM)+":"+dows+":"+String.valueOf(eH)+":"+String.valueOf(eM)+":"+dows;
        } else if (sH > eH){    //case 1: multiday
            timeframe = String.valueOf(sH)+":"+String.valueOf(sM)+":"+dows+":"+String.valueOf(eH)+":"+String.valueOf(eM)+":"+increaseDOWby1(dows);
        }
        return timeframe;
    }

    /* increment each day by 1 */
    private String increaseDOWby1(String dow){
        if(dow.contains(",")){
            String dowArr[] = dow.split(",");
            String newDows ="";
            for(int i=0; i < dowArr.length; i++){ //i know this is bad, too late now.
                if(dowArr[i].equals("Su")) dowArr[i]="Mo,";
                else if(dowArr[i].equals("Mo")) dowArr[i]="Tu,";
                else if(dowArr[i].equals("Tu")) dowArr[i]="We,";
                else if(dowArr[i].equals("We")) dowArr[i]="Th,";
                else if(dowArr[i].equals("Th")) dowArr[i]="Fr,";
                else if(dowArr[i].equals("Fr")) dowArr[i]="Sa,";
                else if(dowArr[i].equals("Sa")) dowArr[i]="Su,";
                newDows += dowArr[i];
            }
            return newDows.substring(0, newDows.length()-1); //because last char will be a comma ","
        } else return dow; //'repeat once' or 'everyday'
    }

    /* set existing view values from intent */
    private void setViewsFromIntent(Intent intent){
        title.setText(intent.getStringExtra(RequestHandler.TITLE));
        origTitle = intent.getStringExtra(RequestHandler.TITLE);
        //time pickers
        startTime_tp.setCurrentHour(Integer.parseInt(intent.getStringExtra(RequestHandler.TIMEFRAME).split(":")[0]));
        startTime_tp.setCurrentMinute(Integer.parseInt(intent.getStringExtra(RequestHandler.TIMEFRAME).split(":")[1]));
        endTime_tp.setCurrentHour(Integer.parseInt(intent.getStringExtra(RequestHandler.TIMEFRAME).split(":")[3]));
        endTime_tp.setCurrentMinute(Integer.parseInt(intent.getStringExtra(RequestHandler.TIMEFRAME).split(":")[4]));
        String dow = intent.getStringExtra(RequestHandler.TIMEFRAME).split(":")[2];

        //days of the week
        if(dow.equals("everyday")){
            for(int i=0; i<daysOfWeek.length; i++)
                daysOfWeek[i]=true;
            findViewById(R.id.dow_sun).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_mon).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_tues).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_wed).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_thurs).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_fri).setBackgroundResource(R.drawable.border_selected);
            findViewById(R.id.dow_sat).setBackgroundResource(R.drawable.border_selected);
        } else if(dow.contains(",")){
            String dowArr[] = dow.split(",");
            for (int i = 0; i < dowArr.length; i++) {
                if (dowArr[i].equals("Su")) {
                    daysOfWeek[0] = true;
                    View view = findViewById(R.id.dow_sun);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("Mo")) {
                    daysOfWeek[1] = true;
                    View view = findViewById(R.id.dow_mon);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("Tu")) {
                    daysOfWeek[2] = true;
                    View view = findViewById(R.id.dow_tues);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("We")) {
                    daysOfWeek[3] = true;
                    View view = findViewById(R.id.dow_wed);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("Th")) {
                    daysOfWeek[4] = true;
                    View view = findViewById(R.id.dow_thurs);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("Fr")) {
                    daysOfWeek[5] = true;
                    View view = findViewById(R.id.dow_fri);
                    view.setBackgroundResource(R.drawable.border_selected);
                } else if (dowArr[i].equals("Sa")) {
                    daysOfWeek[6] = true;
                    View view = findViewById(R.id.dow_sat);
                    view.setBackgroundResource(R.drawable.border_selected);
                }
            }
        }

        //volume preferences
        phone_sb.setProgress(intent.getIntExtra(RequestHandler.PHONE, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)));
        notification_sb.setProgress(intent.getIntExtra(RequestHandler.NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)));
        feedback_sb.setProgress(intent.getIntExtra(RequestHandler.FEEDBACK, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)));
        media_sb.setProgress(intent.getIntExtra(RequestHandler.MEDIA, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
    }

    /* init views */
    private void initViews(){
        title = (EditText) findViewById(R.id.uS_et_title);
        startTime_tp = (TimePicker) findViewById(R.id.uS_tp_start);
        endTime_tp = (TimePicker) findViewById(R.id.uS_tp_end);
        setTimePickerTextWhite(startTime_tp);
        setTimePickerTextWhite(endTime_tp);
        daysOfWeek = new boolean[7]; //inits to false

        //phone
        ImageView ic_phone = (ImageView) findViewById(R.id.uS_ic_phone);
        phone_sb = (SeekBar) findViewById(R.id.uS_sb_phone);
        phone_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        initSeekBarImageView(phone_sb, ic_phone, R.drawable.ic_phone_vol, R.drawable.ic_phone_off);
        //notifications
        ImageView ic_notification = (ImageView) findViewById(R.id.uS_ic_notification);
        notification_sb = (SeekBar) findViewById(R.id.uS_sb_notification);
        notification_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        initSeekBarImageView(notification_sb, ic_notification, R.drawable.ic_notification_vol, R.drawable.ic_notification_off);
        //feedback
        ImageView ic_feedback = (ImageView) findViewById(R.id.uS_ic_feedback);
        feedback_sb = (SeekBar) findViewById(R.id.uS_sb_feedback);
        feedback_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        initSeekBarImageView(feedback_sb, ic_feedback, R.drawable.ic_feedback_vol, R.drawable.ic_feedback_off);
        //media
        ImageView ic_media = (ImageView) findViewById(R.id.uS_ic_media);
        media_sb = (SeekBar) findViewById(R.id.uS_sb_media);
        media_sb.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        initSeekBarImageView(media_sb, ic_media, R.drawable.ic_media_vol, R.drawable.ic_media_off);
        //vibration
        vibration = (CheckBox) findViewById(R.id.uS_cb_vibrate);
        initCheckBoxVibration(vibration);
    }

    private String timeTo24HrString(TimePicker timePicker){
       return (timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString());
    }

    /* handle day of the week button click from UI */
    public void dowClick(View view){
        //change view background
        if (view.getBackground().getConstantState() == getResources().getDrawable(R.drawable.border_unselected).getConstantState())
            view.setBackgroundResource(R.drawable.border_selected);
        else view.setBackgroundResource(R.drawable.border_unselected);
        //update dayOfWeek bool (false=0=unselected)
        TextView dow = (TextView) view;
        String thisDow = dow.getText().toString();
        if (thisDow.equals("Su")) {
            daysOfWeek[0] = !daysOfWeek[0];
        } else if (thisDow.equals("Mo")) {
            daysOfWeek[1] = !daysOfWeek[1];
        } else if (thisDow.equals("Tu")) {
            daysOfWeek[2] = !daysOfWeek[2];
        } else if (thisDow.equals("We")) {
            daysOfWeek[3] = !daysOfWeek[3];
        } else if (thisDow.equals("Th")) {
            daysOfWeek[4] = !daysOfWeek[4];
        } else if (thisDow.equals("Fr")) {
            daysOfWeek[5] = !daysOfWeek[5];
        } else if (thisDow.equals("Sa")) {
            daysOfWeek[6] = !daysOfWeek[6];
        }
    }

    /* generate abbr string for days of the week*/
    public String daysOfWeekToString(){
        String dow="";
        if(daysOfWeek[0]) dow += "Su,";
        if(daysOfWeek[1]) dow += "Mo,";
        if(daysOfWeek[2]) dow += "Tu,";
        if(daysOfWeek[3]) dow += "We,";
        if(daysOfWeek[4]) dow += "Th,";
        if(daysOfWeek[5]) dow += "Fr,";
        if(daysOfWeek[6]) dow += "Sa,";

        if(dow.equals("")) dow = "repeat once";
        else if(dow.equals("Su,Mo,Tu,We,Th,Fr,Sa,")) dow = "everyday";
        else dow = dow.substring(0, dow.length()-1); //because last char will be a comma ","
        return dow;
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

    /* sets vol seekbars to silent (does not change shared prefs) */
    public void setSeekbarsSilent(){
        phone_sb.setProgress(0);
        notification_sb.setProgress(0);
        feedback_sb.setProgress(0);
        media_sb.setProgress(0);
    }

    /* return true if volumes are set to silent*/
    public boolean isSilent(){
        return (phone_sb.getProgress()==0 && notification_sb.getProgress()==0 && feedback_sb.getProgress()==0 && media_sb.getProgress()==0);
    }

    private String generate12HourTime(String _from, String _to){
        String timeFrame = changeTimeFormat12(Integer.parseInt(_from.split(":")[0]),
                Integer.parseInt(_from.split(":")[1]));
        timeFrame += " - " + changeTimeFormat12( Integer.parseInt(_to.split(":")[0]),
                Integer.parseInt(_to.split(":")[1]));
        return timeFrame;
    }
    // Used to convert 24hr format to 12hr format with AM/PM values
    private String changeTimeFormat12(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = " PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = " AM";
        } else if (hours == 12)
            timeSet = " PM";
        else
            timeSet = " AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        return (hours + ":" + minutes + timeSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_page, menu);
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

    /* change timepicker color to white*/
    public void setTimePickerTextWhite(TimePicker timePicker){
        Resources system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) timePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(Color.WHITE);
                ((EditText)child).setTextColor(Color.WHITE);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTxtColor", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTxtColor", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTxtColor", e);
            }
        }
    }
}
