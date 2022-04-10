package com.abm2.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.abm2.R;

public class Receiver extends BroadcastReceiver {
    static int notificationId;
    String channel_id = "test";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("msg"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_fg)
                .setContentText(intent.getStringExtra("msg"))
                .setContentTitle("TERMinator Alert Notification")
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, notification);
    }

    private void createNotificationChannel(Context context, String channel_id) {
        CharSequence name = context.getResources().getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(description);

        //Set channel with system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
