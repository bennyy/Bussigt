package com.bom.bussig.Helpers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.bom.bussig.BussigApplication;
import com.bom.bussig.R;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.media.RingtoneManager.getDefaultUri;

/**
 * Created by Mackan on 2014-09-07.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
        //RouteSegment routeSegment = (RouteSegment)intent.getSerializableExtra("routeSegment");

        int requestID = (int) System.currentTimeMillis();
        Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(context, AlarmNotificationReceiver.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) BussigApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent contentIntent = PendingIntent.getActivity(BussigApplication.getContext(), requestID , resultIntent, FLAG_CANCEL_CURRENT);
        long[] pattern = {100,200,300,400,500,600,700,800,900};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(BussigApplication.getContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setContentTitle(BussigApplication.getContext().getString(R.string.alarm_notification_title))
                        .setContentText(BussigApplication.getContext().getString(R.string.alarm_notification))
                        //.addAction(R.drawable.ic_launcher, "Stoppa!", contentIntent)
                        //.addAction(R.drawable.ic_launcher, "Buu!", contentIntent)
                        .setVibrate(pattern)
                        .setLights(Color.BLUE, 500, 500)
                        .setStyle(new NotificationCompat.InboxStyle());

        // Builds the notification and issues it.
        mBuilder.setContentIntent(contentIntent);
        mNotifyMgr.notify(2, mBuilder.build());

        //Release the lock
        wl.release();
    }

    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context, int minutes){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra("minutesToDeparture", 5);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + minutes * 1000, pi);
    }
}
