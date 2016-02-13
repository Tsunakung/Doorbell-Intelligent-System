package com.lewtsu.android.doorbell.activity.options;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lewtsu.android.doorbell.MainActivity;
import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.SecurityConnectDeviceActivity;
import com.lewtsu.android.doorbell.aynctask.SocketChangePasswordDevice;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class ChangePasswordDeviceActivity extends Activity {

    private EditText editPassword, editNewPassword, editCoonfirmPassword;
    private Button btn;
    private String responseToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_device);


        editPassword = (EditText) findViewById(R.id.edit_changepassworddevice_1);
        editNewPassword = (EditText) findViewById(R.id.edit_changepassworddevice_2);
        editCoonfirmPassword = (EditText) findViewById(R.id.edit_changepassworddevice_3);
        btn = (Button) findViewById(R.id.btn_changepassworddevice_1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClick();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String password = editPassword.getText().toString();
                        String newPassword = editNewPassword.getText().toString();
                        String confirmNewPassword = editCoonfirmPassword.getText().toString();

                        if (!newPassword.equalsIgnoreCase(confirmNewPassword)) {
                            responseToast = "New Password does not match";
                        } else {
                            try {
                                responseToast = new SocketChangePasswordDevice().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, password, newPassword).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                endButtonClick();
                            }
                        });
                    }
                }).start();

            }
        });

    }

    private void startButtonClick() {
        editPassword.setEnabled(false);
        editNewPassword.setEnabled(false);
        editCoonfirmPassword.setEnabled(false);
        btn.setEnabled(false);
        btn.setText("Changing...");
    }

    private void endButtonClick() {
        editPassword.setEnabled(true);
        editNewPassword.setEnabled(true);
        editCoonfirmPassword.setEnabled(true);
        btn.setEnabled(true);
        btn.setText("Change Password");

        if (responseToast != null) {
            if (responseToast.equalsIgnoreCase("true")) {
                Toast.makeText(ChangePasswordDeviceActivity.this, "Change password device complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordDeviceActivity.this, SecurityConnectDeviceActivity.class);
                try {
                    intent.putExtra(Constant.CONNECT_IP, Config.getConfig().getString(Constant.CONNECT_IP));
                    Config.getConfig().remove(Constant.CONNECT_IP);
                    Config.writeConfig();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            } else {
                Toast.makeText(ChangePasswordDeviceActivity.this, responseToast, Toast.LENGTH_SHORT).show();
            }
        }
        responseToast = null;
    }

}
