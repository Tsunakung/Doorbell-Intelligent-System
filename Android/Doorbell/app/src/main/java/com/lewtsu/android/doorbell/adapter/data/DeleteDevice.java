package com.lewtsu.android.doorbell.adapter.data;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.activity.options.DeleteDeviceActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map1;

public class DeleteDevice extends Map1 implements IHandleItem {

    public DeleteDevice(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), DeleteDeviceActivity.class);
        view.getContext().startActivity(intent);
    }

}
