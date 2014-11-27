package com.development.napptime.paydebt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by napptime on 11/26/14.
 *
 * This class serves the purpose of providing us with service to send out the push notification
 */
public class AlarmService {
    private Context context;

    public AlarmService(Context context) {
        this.context = context;
        Log.d("create","maettur");
    }

    public void startAlarm(String due, String title, String info, int alarmNumber){


        Intent indent = new Intent(context, AlarmReceiver.class);
        indent.putExtra("title", title);
        indent.putExtra("info", info);

        PendingIntent mAlarmSender = PendingIntent.getBroadcast(context, alarmNumber, indent, 0);
        Date rem = formatDate(due);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        long a = 0;
        long minute = 60000;
        String notificationContacts = "This";

        if(info.substring(0,4).equals(notificationContacts)){
            a = minute;
        }

        am.set(AlarmManager.RTC_WAKEUP, rem.getTime() + a, mAlarmSender);
    }

    // Returns a Date object from the string due
    private Date formatDate(String due){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm-dd-MM-yyyy");
        Date date = new Date();
        due = "12:00-"+due;
        try {
            date = formatter.parse(due);
            Log.d(due,date.toString());
            formatter.format(date);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}