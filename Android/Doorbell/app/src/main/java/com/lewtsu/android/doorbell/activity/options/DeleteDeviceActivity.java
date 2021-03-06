package com.lewtsu.android.doorbell.activity.options;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lewtsu.android.doorbell.MainActivity;
import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

public class DeleteDeviceActivity extends Activity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_device);

        btn = (Button) findViewById(R.id.btn_deletedevice_1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.getConfig().remove(Constant.CONNECT_IP);
                Config.writeConfig();

                Intent intent = new Intent(DeleteDeviceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

}
