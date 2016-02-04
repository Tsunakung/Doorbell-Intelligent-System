package com.lewtsu.android.doorbell.aynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.lewtsu.android.doorbell.adapter.data.Map.Map3;
import com.lewtsu.android.doorbell.adapter.data.MissedCall;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPUnlock extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return null;

        try {
            URL url = new URL("http://" + ip + "/piphp.php?type=Unlock");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
