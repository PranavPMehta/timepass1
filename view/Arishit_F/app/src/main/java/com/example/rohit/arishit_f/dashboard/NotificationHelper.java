package com.example.rohit.arishit_f.dashboard;

import android.content.Context;

import com.example.rohit.arishit_f.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper{

    public static void displayNotification(Context context, String title, String body){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MeetingCreate.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }
}
