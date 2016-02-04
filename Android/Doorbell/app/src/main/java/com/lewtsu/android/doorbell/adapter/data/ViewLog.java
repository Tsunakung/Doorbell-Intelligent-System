package com.lewtsu.android.doorbell.adapter.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map4;

public class ViewLog extends Map4 implements IHandleItem {

    public ViewLog(Bitmap image, String text1, String text2) {
        super(image, text1, text2);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {

    }

}
