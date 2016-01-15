package com.lewtsu.android.doorbell.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.lewtsu.android.doorbell.activity.MenuActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CallingService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notification();
            }
        }, 1000, 10000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(android.R.drawable.btn_star_big_on,
                "New notification", System.currentTimeMillis());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        String Title = "Notification Doorbell";
        String Message = "Fuck you.";

        Intent intent = new Intent(this, MenuActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, Title, Message, activity);
        notification.number += 1;

        notification.defaults = Notification.DEFAULT_SOUND; // Sound
        notification.defaults = Notification.DEFAULT_VIBRATE; // Vibrate

        notificationManager.notify(1, notification);
    }

}
