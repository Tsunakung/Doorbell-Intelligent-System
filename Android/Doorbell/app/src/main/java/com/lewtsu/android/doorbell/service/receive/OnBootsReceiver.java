package com.lewtsu.android.doorbell.service.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lewtsu.android.doorbell.service.CallingService;

public class OnBootsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CallingService.class));
    }

}
