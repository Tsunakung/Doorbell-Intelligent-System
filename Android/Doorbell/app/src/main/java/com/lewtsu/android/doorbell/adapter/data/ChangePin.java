package com.lewtsu.android.doorbell.adapter.data;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.activity.options.ChangePinActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;

public class ChangePin extends MapIconText implements IHandleItem {

    public ChangePin(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), ChangePinActivity.class);
        view.getContext().startActivity(intent);
    }

}
