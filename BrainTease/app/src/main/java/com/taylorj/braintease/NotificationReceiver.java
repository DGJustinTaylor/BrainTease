package com.taylorj.braintease;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver
{
    public static Boolean on = true;

    public static String theMessage;

    //This will notify the user daily(hopefully) with a new fact or joke
    @Override
    public void onReceive(Context context, Intent intent)
    {
        theMessage = Preferences.message;

        long when = System.currentTimeMillis();

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            nManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context.getApplicationContext(), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context.getApplicationContext());
        }

        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //This will build the actual message and notification that the user will see.
        builder = builder
                .setContentIntent(pIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Daily Fact")
                .setContentText(theMessage)
                .setAutoCancel(true)
                .setWhen(when);


        if(on)
        {
            nManager.notify(100, builder.build());
        }
    }
}
