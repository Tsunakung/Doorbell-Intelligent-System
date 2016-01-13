package com.lewtsu.android.doorbell.adapter.data;

import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.adapter.IHandleItem;

public class DeleteDevice extends MapIconText implements IHandleItem {

    public DeleteDevice(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {

    }

}
