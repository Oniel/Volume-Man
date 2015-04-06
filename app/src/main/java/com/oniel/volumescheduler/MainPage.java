/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Volume Scheduler Main Page Logic File (user)
* ******************************
*/
package com.oniel.volumescheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainPage extends ActionBarActivity {
    /* global objects */
    private final Context context = this;
    private ListView listView;
    private DatabaseHandler db;

    public List<SettingObject> settingsList;
    public ListAdapterHandler adapter;

    /* default methods */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button btn_newSettings;
        Button btn_default;

        /* initialize the list view */
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setClickable(true); //short click for item edit
        listView.setLongClickable(true); //long click for item edit pop-up menu

        /* connect to the app's database */
        db = new DatabaseHandler(this);

        /* populate the list view with db entries */
        if(db.getRowsCount() != 0) {
            updateListView();
            System.out.println("getRowsCOUNT != 0");
        }
        else System.out.println("getRowsCOUNT == 0");

        /* handle 'create a new setting' button click */
        btn_newSettings = (Button) findViewById(R.id.btn_new_setting);
        btn_newSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingPage.class);
                intent.putExtra(RequestHandler.REQUEST_CODE, RequestHandler.REQ_NEW_SETTING);
                startActivityForResult(intent, RequestHandler.REQ_NEW_SETTING); //request code: 2
            }
        });

        /* handle 'default settings' button click */
        btn_default = (Button) findViewById(R.id.btn_default);
        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DefaultPage.class);
                startActivity(intent); //start default activities page
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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

    @Override
    //handles data returned from another activity
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {

        //values returned for creating a new setting
        if (resultCode == RequestHandler.REQ_NEW_SETTING) {
            SettingObject settingObject = RequestHandler.createSettingObjectFromIntent(intent);
            db.addRow(settingObject); // add row to the database
            updateListView(); //regets rows from db and refills listview

        //if returning values to update for existing setting
        } else if (resultCode == RequestHandler.REQ_UPDATE_SETTING) {
            SettingObject settingObject = RequestHandler.createSettingObjectFromIntent(intent);
            String origTitle = settingObject.getTitle().split(":")[0]; //<origTitle>:<newTitle>
            settingObject.setTitle(settingObject.getTitle().split(":")[1]);
            db.updateRow(settingObject, origTitle); // add row to the database
            updateListView(); //re-gets rows from db and refills listview
        } else if (resultCode == RequestHandler.REQ_NO_RESULT){
            Toast.makeText(this, "no setting was added", Toast.LENGTH_SHORT).show();
        }
    }

    /* additional methods */
    private void updateListView(){
        System.out.println("Started: updateListView()");
        settingsList = db.getAllRows(); //get all database data
        adapter = new ListAdapterHandler(this, settingsList, context); //create an adapter with the list of settings
        listView.setAdapter(adapter); //populate the listView with the list of settings in adapter
        //note: adapter handles adding/removing adapters

    }



}
