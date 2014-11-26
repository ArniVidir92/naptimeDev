package com.development.napptime.paydebt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by arni on 11/26/14.
 */
public class AlarmService {
    private Context context;
    private PendingIntent mAlarmSender;
    public AlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 234324243, new Intent(context, AlarmReceiver.class), 0);
        Log.d("create","maettur");
    }

    public void startAlarm(String due, String title, String info){

        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 20);
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        Log.d("create", "maettur");
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
    }
}