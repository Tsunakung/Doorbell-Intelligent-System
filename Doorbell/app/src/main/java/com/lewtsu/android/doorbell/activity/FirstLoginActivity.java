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

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.constant.Constant;

public class FirstLoginActivity extends Activity {

    private InputMethodManager imm;
    private EditText editPad;
    private int pinSize = 4;
    private ImageView[] lock = new ImageView[pinSize];
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editPad = (EditText) findViewById(R.id.edit_register_keypad);
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
                    Intent intent = new Intent(FirstLoginActivity.this, ConfirmFirstLoginActivity.class);
                    intent.putExtra(Constant.PIN, pin);
                    startActivity(intent);
                    finish();
                }
            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_register_1);
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

        lock[0] = (ImageView) findViewById(R.id.img_register_lock1);
        lock[1] = (ImageView) findViewById(R.id.img_register_lock2);
        lock[2] = (ImageView) findViewById(R.id.img_register_lock3);
        lock[3] = (ImageView) findViewById(R.id.img_register_lock4);

        for (ImageView img : lock)
            img.setImageResource(R.drawable.lock);
    }

}
