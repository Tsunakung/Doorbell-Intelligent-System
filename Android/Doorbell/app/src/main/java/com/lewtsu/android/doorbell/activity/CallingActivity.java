package com.lewtsu.android.doorbell.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.aynctask.HTTPSetAccept;
import com.lewtsu.android.doorbell.aynctask.HTTPSetDeny;
import com.lewtsu.android.doorbell.aynctask.HTTPSetFinishAccept;
import com.lewtsu.android.doorbell.aynctask.HTTPUnlock;
import com.lewtsu.android.doorbell.aynctask.SocketGetSound;
import com.lewtsu.android.doorbell.aynctask.SocketSendSound;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;
import com.lewtsu.android.doorbell.service.CallingService;
import com.lewtsu.android.doorbell.vlc.VideoVLC;

import org.json.JSONException;

public class CallingActivity extends Activity {

    public static boolean isRunning = false;

    private SurfaceView mSurfaceVideo;
    private VideoVLC videoVLC;
    private ImageView acceptUncall, unlockUncall, denyUncall,
            unlockCall, denyCall, voiceCall;

    private boolean isAccept = false;
    private MediaPlayer mp;

    private Thread threadCallingTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        isRunning = true;

        mp = MediaPlayer.create(CallingActivity.this, R.raw.ringtone);

        mSurfaceVideo = (SurfaceView) findViewById(R.id.surface_calling_1);
        videoVLC = new VideoVLC(this, mSurfaceVideo);

        acceptUncall = (ImageView) findViewById(R.id.img_calling_1);
        denyUncall = (ImageView) findViewById(R.id.img_calling_2);
        unlockUncall = (ImageView) findViewById(R.id.img_calling_3);

        unlockCall = (ImageView) findViewById(R.id.img_calling_4);
        voiceCall = (ImageView) findViewById(R.id.img_calling_5);
        denyCall = (ImageView) findViewById(R.id.img_calling_6);

        unlockCall.setVisibility(View.INVISIBLE);
        voiceCall.setVisibility(View.INVISIBLE);
        denyCall.setVisibility(View.INVISIBLE);

        acceptUncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccept = true;
                acceptUncall.setVisibility(View.INVISIBLE);
                denyUncall.setVisibility(View.INVISIBLE);
                unlockUncall.setVisibility(View.INVISIBLE);

                unlockCall.setVisibility(View.VISIBLE);
                voiceCall.setVisibility(View.VISIBLE);
                denyCall.setVisibility(View.VISIBLE);

                new HTTPSetAccept().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Build.DEVICE);
                SocketGetSound.start(CallingActivity.this, null);
            }
        });

        denyUncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HTTPSetDeny().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Build.DEVICE);
                finishAffinity();
            }
        });

        unlockUncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CallingActivity.this);
                builder.setMessage("Do you want to unlock door ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new HTTPSetFinishAccept().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Build.DEVICE);
                                new HTTPUnlock().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", null).show();
            }
        });

        unlockCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CallingActivity.this);
                builder.setMessage("Do you want to unlock door ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SocketGetSound.stop();
                                new HTTPSetFinishAccept().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Build.DEVICE);
                                new HTTPUnlock().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", null).show();
            }
        });

        voiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SocketSendSound.isStart()) {
                    voiceCall.setImageResource(R.drawable.calling_microphoneun_unmute);
                    SocketSendSound.stop();
                } else {
                    voiceCall.setImageResource(R.drawable.calling_microphoneun_mute);
                    SocketSendSound.start(CallingActivity.this, voiceCall);
                }
            }
        });

        denyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketGetSound.stop();
                new HTTPSetFinishAccept().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Build.DEVICE);
                finishAffinity();
            }
        });

        callingStart();
        soundStart();
        videoStart();

    }

    private void videoStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!videoVLC.isStart())
                                try {
                                    videoVLC.createPlayer("http://" + Config.getConfig().getString(Constant.CONNECT_IP) + ":" + Constant.STREAMVIDEO_PORT + "/");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void soundStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mp.setLooping(true);
                mp.start();
                while (!isAccept) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mp.stop();
            }
        }).start();
    }

    private void callingStart() {
        if (threadCallingTimeout != null && threadCallingTimeout.getState() != Thread.State.TERMINATED)
            return;
        threadCallingTimeout = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(CallingService.timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isAccept) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finishAffinity();
                        }
                    });
                }
            }
        });
        threadCallingTimeout.start();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        videoVLC.setSize();
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        try {
            videoVLC.createPlayer("http://" + Config.getConfig().getString(Constant.CONNECT_IP) + ":" + Constant.STREAMVIDEO_PORT + "/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoVLC.releasePlayer();
    }
    */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoVLC.releasePlayer();
        mp.stop();
        SocketGetSound.stop();
        isRunning = false;
    }

}
