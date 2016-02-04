package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.ConnectDeviceActivity;
import com.lewtsu.android.doorbell.aynctask.SocketPing;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class HomeActivity extends Activity {

    private ImageButton btnCamera, btnMissedCall, btnUnlock;
    private SocketPing ping;
    private boolean connected, pingStart;
    private int imageCamera, imageCameraHold;
    private Thread threadPing;

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
                if (connected) {
                    Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setMessage("Do you want to\r\ndisconnect from device ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Config.getConfig().remove(Constant.CONNECT_IP);
                                    Config.writeConfig();
                                    Intent intent = new Intent(HomeActivity.this, ConnectDeviceActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null).show();
                }
            }
        });

        btnCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    btnCamera.setImageResource(imageCameraHold);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    btnCamera.setImageResource(imageCamera);
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
                builder.setMessage("Are you sure ?")
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

    private void startThreadPing() {
        if(threadPing != null && threadPing.getState() != Thread.State.TERMINATED)
            return;
        pingStart = true;
        threadPing = new Thread(new Runnable() {
            @Override
            public void run() {
                while(pingStart) {
                    connected = false;
                    ping = new SocketPing();
                    try {
                        connected = ping.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Config.getConfig().getString(Constant.CONNECT_IP)).get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(connected) {
                                    btnCamera.setImageResource(R.drawable.btn_camera_online);
                                    imageCamera = R.drawable.btn_camera_online;
                                    imageCameraHold = R.drawable.btn_camera_online_hold;
                                } else {
                                    btnCamera.setImageResource(R.drawable.btn_camera_offline);
                                    imageCamera = R.drawable.btn_camera_offline;
                                    imageCameraHold = R.drawable.btn_camera_offline_hold;
                                }
                            }
                        });

                        Thread.sleep(5000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadPing.start();
    }

    private void stopThreadPing() {
        if(threadPing == null || threadPing.getState() == Thread.State.TERMINATED)
            return;
        pingStart = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startThreadPing();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopThreadPing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThreadPing();
    }

}
