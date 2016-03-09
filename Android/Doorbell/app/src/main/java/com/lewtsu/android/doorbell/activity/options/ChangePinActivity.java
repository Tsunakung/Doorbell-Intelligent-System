package com.lewtsu.android.doorbell.activity.options;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lewtsu.android.doorbell.MainActivity;
import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class ChangePinActivity extends Activity {

    private EditText editPin, editNewPin, editCoonfirmPin;
    private Button btn;
    private String responseToast, pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        try {
            pin = Config.getConfig().getString(Constant.PIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editPin = (EditText) findViewById(R.id.edit_changepin_1);
        editNewPin = (EditText) findViewById(R.id.edit_changepin_2);
        editCoonfirmPin = (EditText) findViewById(R.id.edit_changepin_3);
        btn = (Button) findViewById(R.id.btn_changepin_1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPin = editPin.getText().toString();
                String newPin = editNewPin.getText().toString();
                String confirmNewPin = editCoonfirmPin.getText().toString();

                if (!pin.equalsIgnoreCase(oldPin)) {
                    responseToast = "PIN incorrect";
                } else if (!newPin.equalsIgnoreCase(confirmNewPin)) {
                    responseToast = "New PIN does not match";
                } else if (newPin.length() != 4) {
                    responseToast = "PIN require 4 character";
                } else {
                    responseToast = "true";
                    try {
                        Config.getConfig().put(Constant.PIN, newPin);
                        Config.writeConfig();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (responseToast != null) {
                    if (responseToast.equalsIgnoreCase("true")) {
                        Toast.makeText(ChangePinActivity.this, "Change PIN complete", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePinActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(ChangePinActivity.this, responseToast, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}
