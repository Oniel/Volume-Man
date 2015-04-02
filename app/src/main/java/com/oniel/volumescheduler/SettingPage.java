/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Sound Setting Creation/Modification Page (User)
* ******************************
*/
package com.oniel.volumescheduler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;


public class SettingPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        /* get calling intention */
        int resultCode = getIntent().getIntExtra(RequestHandler.REQUEST_CODE, -1); // 2 new, 3 update

        /* initialize activity items */
        TextView title = (TextView) findViewById(R.id.uS_lbl_title);
        title.setText(getIntent().getStringExtra(RequestHandler.TITLE));

        TimePicker startTime = (TimePicker) findViewById(R.id.uS_tp_start);
        //LEFT OFF TODO need to get the current time if no intent data
        //startTime.setCurrentHour(getIntent().getIntExtra(RequestHandler.START_HOUR, 0));
        TimePicker endTime = (TimePicker) findViewById(R.id.uS_tp_end);
        //endTime.setCurrentMinute(getIntent().getIntExtra(RequestHandler.START_MIN, 0));
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
}
