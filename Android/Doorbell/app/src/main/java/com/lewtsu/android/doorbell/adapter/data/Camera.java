package com.lewtsu.android.doorbell.adapter.data;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.activity.menu.CameraActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;

public class Camera extends MapIconText implements IHandleItem {

    public Camera(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), CameraActivity.class);
        view.getContext().startActivity(intent);
    }

}
