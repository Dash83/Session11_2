package com.iteso.marco.session11_2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by marco on 4/14/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver
{
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service = new Intent(context, SchedulingService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context)
    {
        alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0 , intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


    }

    public void cancelAlarm(Context context)
    {
        if(alarmManager != null)
        {
            alarmManager.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
