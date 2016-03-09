package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGetCalling extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        String time = null;
        String ip = null;
        try {
            if (Config.getConfig() == null)
                Config.loadConfig();
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return time;

        StringBuilder str = new StringBuilder();

        try {
            URL url = new URL("http://" + ip + "/piphp.php?type=GetCalling");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    str.append(line);
                }
                in.close();

                time = str.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
}
