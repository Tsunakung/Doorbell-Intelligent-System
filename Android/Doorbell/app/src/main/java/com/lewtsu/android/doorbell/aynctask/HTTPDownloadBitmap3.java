package com.lewtsu.android.doorbell.aynctask;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import com.lewtsu.android.doorbell.adapter.data.Map.Map4;

public class HTTPDownloadBitmap3 extends HTTPDownloadBitmap {

    private ArrayAdapter<Map4> adapter;
    private int position;

    public HTTPDownloadBitmap3(ArrayAdapter<Map4> adapter, int position, boolean resize) {
        super(resize);
        this.adapter = adapter;
        this.position = position;
        this.resize = resize;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        adapter.getItem(position).icon = bitmap;
        adapter.notifyDataSetChanged();
    }
}
