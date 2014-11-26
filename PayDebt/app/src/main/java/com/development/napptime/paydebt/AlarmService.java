package com.development.napptime.paydebt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by arni on 11/26/14.
 */
public class AlarmService {
    private Context context;
    private PendingIntent mAlarmSender;
    public AlarmService(Context context) {
        this.context = context;
        Log.d("create","maettur");
    }

    public void startAlarm(String due, String title, String info, int alarmNumber){


        Intent indent = new Intent(context, AlarmReceiver.class);
        indent.putExtra("title", title);
        indent.putExtra("info", info);
        mAlarmSender = PendingIntent.getBroadcast(context, alarmNumber, indent, 0);
        Date rem = formatDate(due);
        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 20);
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        int a = 0;

        if(info.substring(0,4).equals("This")){
            a = 60000;
        }

        Log.d("jiiiiiiiiiiiiii"," " + a);

        am.set(AlarmManager.RTC_WAKEUP, rem.getTime() + a, mAlarmSender);
    }

    private Date formatDate(String due){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm-dd-MM-yyyy");
        Date date = new Date();
        due = "22:15-"+due;
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