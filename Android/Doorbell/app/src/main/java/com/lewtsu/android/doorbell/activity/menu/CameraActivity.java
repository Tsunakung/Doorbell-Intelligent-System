package com.lewtsu.android.doorbell.activity.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lewtsu.android.doorbell.R;

public class CameraActivity extends AppCompatActivity {

    private VideoView vdo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        vdo = (VideoView) findViewById(R.id.videoview_tab1_1);
        MediaController media = new MediaController(this);
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.vdo);
        //Uri uri = Uri.parse("http://www.dragoncute-mc.com/vdo.mp4");
        //Uri uri = Uri.parse("rtsp://media.5.ua:8080/tv/5tv/5tv1");
        //Uri uri = Uri.parse(temp.getAbsolutePath());
        //vdo.setVideoURI(uri);
        vdo.setVideoPath("rtsp://media.5.ua:8080/tv/5tv/5tv1");
        vdo.setVideoPath("http://pitaknorasate.asuscomm.com:8554/");
        vdo.setMediaController(media);
        vdo.requestFocus();
        //vdo.start();


    }

}
