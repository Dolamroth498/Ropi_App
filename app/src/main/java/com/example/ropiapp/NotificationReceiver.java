package com.example.ropiapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "mentes_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Értesítés")
                .setContentText("Ezt az üzenetet az AlarmManager küldte.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        try {
            NotificationManagerCompat.from(context).notify(200, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
