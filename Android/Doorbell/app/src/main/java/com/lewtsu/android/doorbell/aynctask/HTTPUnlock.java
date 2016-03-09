package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;

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
            URL url = new URL("http://" + ip + "/PhpConRelay.php?ctl=1");
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
