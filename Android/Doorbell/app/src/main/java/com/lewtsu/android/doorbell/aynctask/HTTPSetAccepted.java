package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPSetAccepted extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return null;

        String user = params[0];
        try {
            URL url = new URL("http://" + ip + "/piphp.php?type=SetAccepted&user=" + user);
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
