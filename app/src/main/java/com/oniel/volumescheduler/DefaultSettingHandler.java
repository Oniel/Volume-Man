/*
********************************
* Author: Oniel Toledo
* Created: 01.15.2015
* Description: Default Sound Settings Shared Preferences Object
*  Handles storage, retrieval, and processing of the default volume settings.
* ******************************
*/
package com.oniel.volumescheduler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultSettingHandler extends ActionBarAc{





    public DefaultSettingHandler(Context context){
        //initiate shared preferences for a given activity
        sharedPref = context.getPreferences();
    }



}

