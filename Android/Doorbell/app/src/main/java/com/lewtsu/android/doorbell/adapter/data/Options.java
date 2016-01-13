package com.lewtsu.android.doorbell.adapter.data;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lewtsu.android.doorbell.activity.menu.OptionsActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;

public class Options extends MapIconText implements IHandleItem {

    public Options(int image, String text) {
        super(image, text);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), OptionsActivity.class);
        view.getContext().startActivity(intent);
    }

}
