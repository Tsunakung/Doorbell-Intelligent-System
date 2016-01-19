package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.SurfaceView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;
import com.lewtsu.android.doorbell.vlc.VideoVLC;

import org.json.JSONException;

public class CameraActivity extends Activity {

    private VideoVLC videoVLC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        SurfaceView mSurfaceVideo = (SurfaceView) findViewById(R.id.surface_tab1_1);
        videoVLC = new VideoVLC(this, mSurfaceVideo);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoVLC.releasePlayer();
    }

}
