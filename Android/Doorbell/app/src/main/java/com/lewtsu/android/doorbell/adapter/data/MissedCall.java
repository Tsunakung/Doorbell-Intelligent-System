package com.lewtsu.android.doorbell.adapter.data;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.activity.menu.MissedCallActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;

public class MissedCall extends MapIconText implements IHandleItem {

    public MissedCall(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), MissedCallActivity.class);
        view.getContext().startActivity(intent);
    }

}
