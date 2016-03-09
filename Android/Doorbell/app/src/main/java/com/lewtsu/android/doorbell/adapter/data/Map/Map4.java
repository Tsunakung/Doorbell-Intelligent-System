package com.lewtsu.android.doorbell.adapter.data.Map;

import android.graphics.Bitmap;

import com.lewtsu.android.doorbell.aynctask.HTTPDownloadBitmap;

public abstract class Map4 {

    public Bitmap icon;
    public String str1, str2;
    //public boolean isDownload;
    public HTTPDownloadBitmap download;

    public Map4(Bitmap image, String text1, String text2) {
        icon = image;
        str1 = text1;
        str2 = text2;
    }

}
