package com.lewtsu.android.doorbell.adapter;

import android.view.View;
import android.widget.AdapterView;

public interface IHandleItem {

    public void hanndle(AdapterView<?> parent, View view, int position, long id);

}
