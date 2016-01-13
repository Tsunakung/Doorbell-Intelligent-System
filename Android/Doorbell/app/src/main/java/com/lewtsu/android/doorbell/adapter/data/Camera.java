package com.lewtsu.android.doorbell.adapter.data;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.constant.Constant;

public class Camera extends MapIconText implements IHandleItem {

    public Camera(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Log.d(Constant.TAG, "HANDLE");
    }

}
