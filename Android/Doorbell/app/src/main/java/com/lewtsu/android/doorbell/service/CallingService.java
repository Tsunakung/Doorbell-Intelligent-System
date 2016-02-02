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
import com.lewtsu.android.doorbell.aynctask.SocketCalling;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class CallingService extends Service {

    boolean isCalling;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        isCalling = new SocketCalling().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Config.getConfig().getString(Constant.CONNECT_IP)).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isCalling = true;
                    if (isCalling) {
                        notification();

                        Intent intent = new Intent(CallingService.this, CallingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
