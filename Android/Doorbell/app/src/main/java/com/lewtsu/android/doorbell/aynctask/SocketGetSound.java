package com.lewtsu.android.doorbell.aynctask;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketGetSound extends AsyncTask<String, Void, Void> {

    private static SocketGetSound socketGetSound;
    private static Thread threadGetSound;
    private static Activity activity;
    private static ImageView imageButton;

    private boolean isStartGetSound;

    public static void stop() {
        if (SocketGetSound.socketGetSound == null)
            return;
        SocketGetSound.socketGetSound.isStartGetSound = false;
    }

    public static void start(Activity activity, ImageView imageButton) {
        stop();
        SocketGetSound.activity = activity;
        SocketGetSound.imageButton = imageButton;
        SocketGetSound.threadGetSound = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    SocketGetSound.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (SocketGetSound.imageButton != null)
                                SocketGetSound.imageButton.setEnabled(false);
                        }
                    });
                    SocketGetSound.socketGetSound = new SocketGetSound();
                    SocketGetSound.socketGetSound.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    SocketGetSound.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (SocketGetSound.imageButton != null)
                                SocketGetSound.imageButton.setEnabled(true);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        SocketGetSound.threadGetSound.start();
    }

    public static boolean isStart() {
        if (SocketGetSound.socketGetSound == null)
            return false;
        return SocketGetSound.socketGetSound.isStartGetSound;
    }

    @Override
    protected Void doInBackground(String... params) {
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return null;


        isStartGetSound = true;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.STREAMSOUND_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            socket.setSoTimeout(3000);
            InputStream in = socket.getInputStream();

            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, 512, AudioTrack.MODE_STREAM);

            int data;
            byte[] buffer = new byte[512];

            track.play();
            while (isStartGetSound && (data = in.read(buffer, 0, buffer.length)) > 0) {
                track.write(buffer, 0, data);
            }

            track.stop();
            track.release();
            in.close();
            socket.close();

            /*
            SocketGetSound.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SocketGetSound.imageButton != null)
                        SocketGetSound.imageButton.setImageResource(R.drawable.btn_sound_hold);
                }
            });
            */
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            Log.e(Constant.TAG, "Can't connect " + ip);
        } catch (ConnectException e) {
            Log.e(Constant.TAG, "Can't connect " + ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
