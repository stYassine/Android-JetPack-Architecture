package com.example.jetpackmasterclass.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jetpackmasterclass.R;
import com.example.jetpackmasterclass.views.MainActivity;

public class NotificationHelper {

    private static final String CHANNEL_ID = "dogs channel id";
    private static final int NOTIFICATION_ID = 123;

    private static NotificationHelper instance;
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public static NotificationHelper getInstance(Context context) {
        if(instance == null){
            instance = new NotificationHelper(context);
        }
        return instance;
    }

    public void createNotification(){
        createNotificationChannel();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.dog_icon)
                .setLargeIcon(icon)
                .setContentTitle("dogs retrieved")
                .setContentText("This is a notification to let you know that the dog information has been retrieved")
                .setStyle(
                        new NotificationCompat.BigPictureStyle()
                        .bigPicture(icon)
                        .bigLargeIcon(null)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }
    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = CHANNEL_ID;
            String description = "dogs retrieved notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
