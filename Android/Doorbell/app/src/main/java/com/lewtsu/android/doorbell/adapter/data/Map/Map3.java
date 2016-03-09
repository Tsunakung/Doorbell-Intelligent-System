package com.lewtsu.android.doorbell.adapter.data.Map;

import android.graphics.Bitmap;

import com.lewtsu.android.doorbell.aynctask.HTTPDownloadBitmap;

public abstract class Map3 {

    public Bitmap icon;
    public String str;
    //public boolean isDownload;
    public HTTPDownloadBitmap download;

    public Map3(Bitmap image, String text) {
        icon = image;
        str = text;
    }

}
