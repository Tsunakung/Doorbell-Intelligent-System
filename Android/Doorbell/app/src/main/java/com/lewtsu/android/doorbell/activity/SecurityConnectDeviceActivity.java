package com.lewtsu.android.doorbell.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.aynctask.SocketComparePasswordDevice;
import com.lewtsu.android.doorbell.aynctask.SocketPing;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class SecurityConnectDeviceActivity extends Activity {

    private String ipConnect;
    private String password = "";
    private EditText editTextIP, editTextPassword;
    private Button btnConnect;
    private String responseToast;
    private Intent intent;
    private boolean isExtraIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityconnectdevice);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isExtraIp = true;
            ipConnect = extras.getString(Constant.CONNECT_IP);
        }

        editTextIP = (EditText) findViewById(R.id.edit_securityconnectdevice_1);
        editTextPassword = (EditText) findViewById(R.id.edit_securityconnectdevice_2);

        btnConnect = (Button) findViewById(R.id.btn_securityconnectdevice_1);

        editTextIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ipConnect = s.toString();
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onButtonStart();
                            }
                        });

                        try {

                            /*)
                            Config.getConfig().put(Constant.CONNECT_IP, "pitaknorasate.asuscomm.com");
                            Config.writeConfig();
                            intent = new Intent(SecurityConnectDeviceActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                            */

                            boolean isConnect = new SocketPing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipConnect).get();
                            boolean isPassword = new SocketComparePasswordDevice().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ipConnect, password).get();
                            if (ipConnect == null || ipConnect.length() == 0) {
                                responseToast = "Please input IPAddress";
                            } else if (!isConnect) {
                                responseToast = "IP device can't connect";
                            } else if (!isPassword) {
                                responseToast = "Password device wrong";
                            } else {
                                Config.getConfig().put(Constant.CONNECT_IP, ipConnect);
                                Config.writeConfig();
                                intent = new Intent(SecurityConnectDeviceActivity.this, MenuActivity.class);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onButtonComplete();
                            }
                        });
                    }
                }).start();
            }
        });

        editTextIP.requestFocus();

        if (ipConnect != null) {
            editTextIP.setText(ipConnect);
            editTextIP.setEnabled(false);
            editTextPassword.requestFocus();
        }

    }

    private void onButtonStart() {
        btnConnect.setEnabled(false);
        editTextIP.setEnabled(false);
        editTextPassword.setEnabled(false);
        btnConnect.setText("Connecting...");
    }

    private void onButtonComplete() {
        btnConnect.setEnabled(true);
        if(!isExtraIp)
            editTextIP.setEnabled(true);
        editTextPassword.setEnabled(true);
        btnConnect.setText("Connect");
        if (responseToast != null) {
            Toast.makeText(SecurityConnectDeviceActivity.this, responseToast, Toast.LENGTH_SHORT).show();
        } else if (intent != null) {
            startActivity(intent);
            finish();
        }
        responseToast = null;
        intent = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SecurityConnectDeviceActivity.this, ConnectDeviceActivity.class);
        startActivity(intent);
        finish();
    }

}
