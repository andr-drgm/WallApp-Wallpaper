package com.example.wallappwallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

// TODO: Depricated...
public class WallPaperAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WallApp:wallpaper");
        wakeLock.acquire();

        // Code that executes on alarm

        Toast.makeText(context, "test alarm...", Toast.LENGTH_SHORT).show();

        //

        wakeLock.release();

    }

    public void setAlarm(Context context)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent i = new Intent(context, WallPaperAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
                            /// 24 hours

    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, WallPaperAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



}
