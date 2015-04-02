/*
********************************
* Author: Oniel Toledo
* Created: 01.03.2015
* Description: Adapter for MainPage list view (handler)
* ******************************
*/
package com.oniel.volumescheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapterHandler extends BaseAdapter {
    private Activity activity;
    private List<SettingObject> settingsList;
    private Context context;

    private LayoutInflater inflater;

    /* Constructor */
    public ListAdapterHandler(Activity activity, List<SettingObject> settingsList, Context context) {
        this.activity = activity;
        this.settingsList = settingsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return settingsList.size();
    }

    @Override
    public Object getItem(int location) {
        return settingsList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        convertView.setLongClickable(true); //opens item's pop-up menu
        convertView.setClickable(true); //opens SettingPage Activity for this setting

        //get the views for this particular layout (listitems)
        final TextView title = (TextView) convertView.findViewById(R.id.listItem_title);
        TextView time = (TextView) convertView.findViewById(R.id.listItem_timeFrame);
        TextView dow = (TextView) convertView.findViewById(R.id.listItem_daysofweek);

        final SettingObject item = settingsList.get(position);

        // title
        title.setText(item.getTitle());

        //time
        time.setText(item.getTimeFrame());

        // days of the week
        dow.setText(item.getDaysofweek());


        //on short click, open updateSetting use case with current parameters
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createSettingPageIntent(item);
                activity.startActivityForResult(intent, RequestHandler.REQ_UPDATE_SETTING);
            }
        });

        //on listitem long click create dialog prompt with 'edit' or cancel options
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("IN here");
                //create a dialog to be presented
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(title.getText());
                //edit button
                builder.setNegativeButton("Edit Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = createSettingPageIntent(item);
                        activity.startActivityForResult(intent, RequestHandler.REQ_UPDATE_SETTING);
                    }
                });
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingsList.remove(position);
                        notifyDataSetChanged();

                        //perform deletion
                        DatabaseHandler db = new DatabaseHandler(context);
                        db.deleteRow(item);

                    }
                });

                //show the dialog
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        return convertView;
    }

    private Intent createSettingPageIntent(SettingObject item){
        Intent intent = new Intent(activity, SettingPage.class);
        intent.putExtra(RequestHandler.REQUEST_CODE, RequestHandler.REQ_UPDATE_SETTING);
        intent.putExtra(RequestHandler.TITLE, item.getTitle());
        intent.putExtra(RequestHandler.START_HOUR, item.getFromHour());
        intent.putExtra(RequestHandler.START_MIN, item.getFromMin());
        intent.putExtra(RequestHandler.END_HOUR, item.getToHour());
        intent.putExtra(RequestHandler.END_MIN, item.getToMin());
        intent.putExtra(RequestHandler.DAYSOFWEEK, item.getDaysofweek());
        intent.putExtra(RequestHandler.PHONE, item.getPhone());
        intent.putExtra(RequestHandler.NOTIFICATION, item.getNotification());
        intent.putExtra(RequestHandler.FEEDBACK, item.getFeedback());
        intent.putExtra(RequestHandler.MEDIA, item.getMedia());
        intent.putExtra(RequestHandler.VIBRATION, item.getVibration());
//        intent.putExtra(RequestHandler.PHONE_VIBRATION, item.getPhoneVibration());
//        intent.putExtra(RequestHandler.NOTIFICATION_VIBRATION, item.getNotificationVibration());
//        intent.putExtra(RequestHandler.FEEDBACK_VIBRATION, item.getFeedbackVibration());
//        intent.putExtra(RequestHandler.MEDIA_VIBRATION, item.getMediaVibration());
        return intent;
    }
}
