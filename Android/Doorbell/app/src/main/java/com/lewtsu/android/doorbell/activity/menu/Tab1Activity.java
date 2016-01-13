package com.lewtsu.android.doorbell.activity.menu;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.lewtsu.android.doorbell.R;

public class Tab1Activity extends AppCompatActivity {

    private VideoView vdo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
