package com.lewtsu.android.doorbell.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.lewtsu.android.doorbell.activity.CallingActivity;
import com.lewtsu.android.doorbell.aynctask.HTTPGetCalling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CallingService extends Service {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static String time;
    public static long timeout = 60000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        time = new HTTPGetCalling().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (time != null) {
                        long t = 0;
                        try {
                            Date date = df.parse(time);
                            t = System.currentTimeMillis() - date.getTime();
                        } catch (ParseException e) {

                        }

                        if (t > 0 && !CallingActivity.isRunning) {

                            Intent intent = new Intent(CallingService.this, CallingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            try {
                                Thread.sleep(timeout);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(android.R.drawable.btn_star_big_on, "Doorbell Intelligent System", System.currentTimeMillis());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        String Title = "Doorbell Intelligent System";
        String Message = "Calling....";

        Intent intent = new Intent(this, CallingActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, Title, Message, activity);
        notification.number += 1;

        notification.defaults = Notification.DEFAULT_SOUND;
        notification.defaults = Notification.DEFAULT_VIBRATE;

        notificationManager.notify(1, notification);
    }

}
