package com.lewtsu.android.doorbell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lewtsu.android.doorbell.activity.FirstLoginActivity;
import com.lewtsu.android.doorbell.activity.LoginActivity;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private boolean isFirstLogin;

    private Handler handler;
    private Runnable runnable;
    private long delay = 3000L;
    private long time = delay;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Config.loadConfig();

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = Config.getConfig();
                isFirstLogin = !json.isNull(Constant.PIN);
            }
        }).start();

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                Intent intent;
                if (isFirstLogin) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                } else
                    intent = new Intent(MainActivity.this, FirstLoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable, time);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay - (System.currentTimeMillis() - startTime);
    }

}
