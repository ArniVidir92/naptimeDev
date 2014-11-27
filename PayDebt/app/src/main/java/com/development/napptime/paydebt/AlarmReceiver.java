package com.development.napptime.paydebt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by napptime on 11/26/14.
 *
 * This class serves the purpose of receiving alarms uses the android notification service
 * to notify the user with push notification when appropriatly
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title,info;
        try{
            title = bundle.getString("title");
            info = bundle.getString("info");
        }catch (Error e){
            title = "Virkadi ekki";
            info = "Virkadi ekki";
        }


        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.irc_launcher, "DebtTrack!",
                System.currentTimeMillis());
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context, title, info, contentIntent);
        // Send the notification.
        mNM.notify(R.string.searchContact, notification);
    }
}
