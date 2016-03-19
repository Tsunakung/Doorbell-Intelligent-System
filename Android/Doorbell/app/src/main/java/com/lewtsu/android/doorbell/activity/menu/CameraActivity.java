package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.aynctask.HTTPUnlock;
import com.lewtsu.android.doorbell.aynctask.SocketGetSound;
import com.lewtsu.android.doorbell.aynctask.SocketSendSound;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;
import com.lewtsu.android.doorbell.vlc.VideoVLC;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {

    private VideoVLC videoVLC;
    private SurfaceView mSurfaceVideo;
    private ImageView screenshot, unlock, sound, voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mSurfaceVideo = (SurfaceView) findViewById(R.id.surface_camera_1);
        videoVLC = new VideoVLC(this, mSurfaceVideo);

        screenshot = (ImageView) findViewById(R.id.imgbtn_camera_1);
        unlock = (ImageView) findViewById(R.id.imgbtn_camera_2);
        sound = (ImageView) findViewById(R.id.imgbtn_camera_3);
        voice = (ImageView) findViewById(R.id.imgbtn_camera_4);


        screenshot.setVisibility(View.INVISIBLE);
        unlock.setVisibility(View.INVISIBLE);
        //sound.setVisibility(View.INVISIBLE);
        //voice.setVisibility(View.INVISIBLE);

        screenshot.getLayoutParams().width = 0;
        unlock.getLayoutParams().width = 0;
        //sound.getLayoutParams().width = 0;
        //voice.getLayoutParams().width = 0;


        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HTTPUnlock().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SocketGetSound.isStart()) {
                    sound.setImageResource(R.drawable.btn_sound_hold);
                    SocketGetSound.stop();
                } else {
                    sound.setImageResource(R.drawable.btn_sound);
                    SocketGetSound.start(CameraActivity.this, sound);
                }
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SocketSendSound.isStart()) {
                    voice.setImageResource(R.drawable.btn_voice_hold);
                    SocketSendSound.stop();
                } else {
                    voice.setImageResource(R.drawable.btn_voice);
                    SocketSendSound.start(CameraActivity.this, voice);
                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        videoVLC.setSize();
    }


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
        SocketGetSound.stop();
        SocketSendSound.stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoVLC.releasePlayer();
        SocketGetSound.stop();
        SocketSendSound.stop();
    }

    public void takeScreenshot() {
        Toast.makeText(CameraActivity.this, "Capture", Toast.LENGTH_SHORT).show();

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");

        /*
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        */

        mSurfaceVideo.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(mSurfaceVideo.getDrawingCache());
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, df.format(now), df.format(now));

        mSurfaceVideo.setDrawingCacheEnabled(false);

        /*
        mSurfaceVideo.setDrawingCacheEnabled(true);
        mSurfaceVideo.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(mSurfaceVideo.getWidth(), mSurfaceVideo.getHeight(), Bitmap.Config.ARGB_8888);
        mSurfaceVideo.setDrawingCacheEnabled(false);
        */


        //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, df.format(now), df.format(now));
    }

}
