package com.lewtsu.android.doorbell.aynctask;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketSendSound extends AsyncTask<String, Void, Void> {

    private static SocketSendSound socketSendSound;
    private static Thread threadSendSound;
    private static Activity activity;
    private static ImageView imageButton;

    private boolean isStartSendSound;

    public static void stop() {
        if (SocketSendSound.socketSendSound == null)
            return;
        SocketSendSound.socketSendSound.isStartSendSound = false;
    }

    public static void start(Activity activity, ImageView imageButton) {
        stop();
        SocketSendSound.activity = activity;
        SocketSendSound.imageButton = imageButton;
        SocketSendSound.threadSendSound = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    SocketSendSound.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (SocketSendSound.imageButton != null)
                                SocketSendSound.imageButton.setEnabled(false);
                        }
                    });
                    SocketSendSound.socketSendSound = new SocketSendSound();
                    SocketSendSound.socketSendSound.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    SocketSendSound.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (SocketSendSound.imageButton != null)
                                SocketSendSound.imageButton.setEnabled(true);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        SocketSendSound.threadSendSound.start();
    }

    public static boolean isStart() {
        if (SocketSendSound.socketSendSound == null)
            return false;
        return SocketSendSound.socketSendSound.isStartSendSound;
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

        isStartSendSound = true;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.STREAMVOICE_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            socket.setSoTimeout(3000);
            socket.setOOBInline(true);
            OutputStream out = socket.getOutputStream();

            int minBuffer = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBuffer);

            int data;
            //byte[] buffer = new byte[128];
            //byte[] buffer = new byte[256];
            //byte[] buffer = new byte[512];
            //byte[] buffer = new byte[1024];
            //byte[] buffer = new byte[2048];
            //byte[] buffer = new byte[4096];
            byte[] buffer = new byte[4096];
            //byte[] buffer = new byte[minBuffer];

            recorder.startRecording();
            while (isStartSendSound && (data = recorder.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, data);
            }

            recorder.stop();
            recorder.release();
            out.close();
            socket.close();

            /*
            SocketSendSound.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SocketSendSound.imageButton != null)
                        SocketSendSound.imageButton.setImageResource(R.drawable.calling_microphoneun_unmute);
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
