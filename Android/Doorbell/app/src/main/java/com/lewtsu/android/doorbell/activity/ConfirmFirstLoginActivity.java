package com.lewtsu.android.doorbell.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class ConfirmFirstLoginActivity extends Activity {

    private InputMethodManager imm;
    private EditText editPad;
    private int pinSize = 4;
    private ImageView[] lock = new ImageView[pinSize];
    private String pinLock;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmfirstlogin);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pinLock = extras.getString(Constant.PIN);
        }

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editPad = (EditText) findViewById(R.id.edit_confirmregister_keypad);
        imm.hideSoftInputFromWindow(editPad.getWindowToken(), 0);
        editPad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pin = s.toString();
                for (int i = 0; i < pinSize; ++i) {
                    if (i < pin.length()) {
                        lock[i].setImageResource(R.drawable.unlock);
                    } else {
                        lock[i].setImageResource(R.drawable.lock);
                    }
                }
                if (pin.length() == pinSize) {
                    imm.hideSoftInputFromWindow(editPad.getWindowToken(), 0);
                    Intent intent;
                    if (!pin.equalsIgnoreCase(pinLock)) {
                        Toast.makeText(ConfirmFirstLoginActivity.this, "PIN Incorrect", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ConfirmFirstLoginActivity.this, FirstLoginActivity.class);
                    } else {
                        try {
                            Config.getConfig().put(Constant.PIN, pin);
                            Config.writeConfig();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent = new Intent(ConfirmFirstLoginActivity.this, ConnectDeviceActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_confirmregister_1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPad.requestFocus();
                imm.showSoftInput(editPad, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        editPad.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        lock[0] = (ImageView) findViewById(R.id.img_confirmregister_lock1);
        lock[1] = (ImageView) findViewById(R.id.img_confirmregister_lock2);
        lock[2] = (ImageView) findViewById(R.id.img_confirmregister_lock3);
        lock[3] = (ImageView) findViewById(R.id.img_confirmregister_lock4);

        for (ImageView img : lock)
            img.setImageResource(R.drawable.lock);
    }

}
