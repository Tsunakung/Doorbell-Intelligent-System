package com.lewtsu.android.doorbell.adapter.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map3;

public class MissedCall extends Map3 implements IHandleItem {

    public MissedCall(Bitmap image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {

    }

}
