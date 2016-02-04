package com.lewtsu.android.doorbell.adapter.data.Map;

import android.graphics.Bitmap;

public abstract class Map3 {

    public Bitmap icon;
    public String str;

    public Map3(Bitmap image, String text) {
        icon = image;
        str = text;
    }

}
