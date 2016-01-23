package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.lewtsu.android.doorbell.R;

public class HomeActivity extends Activity {

    private ImageButton btnCamera, btnMissedCall, btnUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCamera = (ImageButton) findViewById(R.id.imgbtn_home_1);
        btnMissedCall = (ImageButton) findViewById(R.id.imgbtn_home_2);
        btnUnlock = (ImageButton) findViewById(R.id.imgbtn_home_3);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        btnCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    btnCamera.setImageResource(R.drawable.btn_camera_hold);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    btnCamera.setImageResource(R.drawable.btn_camera);
                return false;
            }
        });

        btnMissedCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MissedCallActivity.class);
                startActivity(intent);
            }
        });

        btnMissedCall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    btnMissedCall.setImageResource(R.drawable.btn_misscalled_hold);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    btnMissedCall.setImageResource(R.drawable.btn_misscalled);
                return false;
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("No", null).show();
            }
        });

        btnUnlock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    btnUnlock.setImageResource(R.drawable.btn_unlock_hold);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    btnUnlock.setImageResource(R.drawable.btn_unlock);
                return false;
            }
        });

    }

}
