package com.lewtsu.android.doorbell.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.aynctask.SocketPing;
import com.lewtsu.android.doorbell.constant.Constant;

import java.util.concurrent.ExecutionException;

public class ConnectDeviceActivity extends Activity {

    private String ipConnect;
    //private TextView textViewIpConnect, textViewStatus;
    private TextView textViewIpConnect;
    private Button buttonConnect, buttonManually;
    private Thread threadScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectdevice);

        textViewIpConnect = (TextView) findViewById(R.id.txt_connectdevice_1);
        //textViewStatus = (TextView) findViewById(R.id.txt_connectdevice_2);
        buttonConnect = (Button) findViewById(R.id.btn_connectdevice_1);
        buttonManually = (Button) findViewById(R.id.btn_connectdevice_2);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipConnect == null) {
                    scanDevice();
                    return;
                }
                Intent intent = new Intent(ConnectDeviceActivity.this, SecurityConnectDeviceActivity.class);
                intent.putExtra(Constant.CONNECT_IP, ipConnect);
                startActivity(intent);
                finish();
            }
        });

        buttonManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectDeviceActivity.this, SecurityConnectDeviceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        scanDevice();

    }

    private void scanDevice() {
        if (threadScan != null && threadScan.getState() != Thread.State.TERMINATED)
            return;
        threadScan = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewIpConnect.setText("Scanning...");
                        textViewIpConnect.setTextColor(0xFFA54800);
                        buttonConnect.setText("Scanning...");
                        buttonConnect.setEnabled(false);
                    }
                });


                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                Log.d(Constant.TAG, ip + " = " + wm.getConnectionInfo() + "");

                String[] ipSplit = ip.split("\\.");
                String ipAddress;
                boolean isCamera = false;
                for (int i = 0; i < 256; ++i) {
                    ipAddress = ipSplit[0] + "." + ipSplit[1] + "." + ipSplit[2] + "." + i;
                    try {
                        isCamera = new SocketPing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipAddress).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (isCamera) {
                        ipConnect = ipAddress;
                        break;
                    }
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonConnect.setEnabled(true);
                        if (ipConnect != null) {
                            //textViewStatus.setText("Online");
                            textViewIpConnect.setText("IP : " + ipConnect);
                            //textViewStatus.setTextColor(0xFF00FF00);
                            textViewIpConnect.setTextColor(0xFF00FF00);
                            buttonConnect.setText("Connect");
                        } else {
                            //textViewStatus.setText("Offline");
                            textViewIpConnect.setText("Doorbell not found");
                            //textViewStatus.setTextColor(0xFFFF0000);
                            textViewIpConnect.setTextColor(0xFFFF0000);
                            buttonConnect.setText("Scan");
                        }
                    }
                });

            }
        });
        threadScan.start();
    }

}
