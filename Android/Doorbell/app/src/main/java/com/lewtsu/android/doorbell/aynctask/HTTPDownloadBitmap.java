package com.lewtsu.android.doorbell.aynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class HTTPDownloadBitmap extends AsyncTask<String, Void, Bitmap> {

    public boolean resize;

    public HTTPDownloadBitmap() {
        this(true);
    }

    public HTTPDownloadBitmap(boolean resize) {
        this.resize = resize;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bmp = null;
        String urlStr = params[0];
        try {
            bmp = BitmapFactory.decodeStream(new URL(urlStr.replace(" ", "%20")).openConnection().getInputStream());
            if(this.resize && bmp != null)
                bmp = Bitmap.createScaledBitmap(bmp, 64, 36, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
