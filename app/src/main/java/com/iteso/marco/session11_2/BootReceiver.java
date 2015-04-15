package com.iteso.marco.session11_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by marco on 4/14/15.
 */
public class BootReceiver extends BroadcastReceiver
{
    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Log.v("Alarms","Alarm set due to Boot completed");
            alarm.setAlarm(context);

        }

    }
}
