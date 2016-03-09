package com.lewtsu.android.doorbell.aynctask;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import com.lewtsu.android.doorbell.adapter.data.Map.Map3;

public class HTTPDownloadBitmap2 extends HTTPDownloadBitmap {

    private ArrayAdapter<Map3> adapter;
    private int position;

    public HTTPDownloadBitmap2(ArrayAdapter<Map3> adapter, int position, boolean resize) {
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
