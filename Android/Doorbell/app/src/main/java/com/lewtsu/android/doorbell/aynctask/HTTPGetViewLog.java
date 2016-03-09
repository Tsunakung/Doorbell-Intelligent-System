package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.adapter.data.Map.Map4;
import com.lewtsu.android.doorbell.adapter.data.ViewLog;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPGetViewLog extends AsyncTask<Void, Void, List<Map4>> {

    @Override
    protected List<Map4> doInBackground(Void... params) {
        List<Map4> list = new ArrayList<>();
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return list;

        StringBuilder str = new StringBuilder();

        try {
            URL url = new URL("http://" + ip + "/piphp.php?type=GetViewLog");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(str.toString());

        try {
            String time, status, user;
            JSONObject jObj;
            JSONArray jArray = new JSONArray(str.toString());
            for (int i = 0; i < jArray.length(); ++i) {
                jObj = jArray.getJSONObject(i);
                time = jObj.getString("time");
                status = jObj.getString("status");
                user = jObj.getString("user_accept");

                if (status != null && status.indexOf("@") >= 0) {
                    status = status.substring(status.indexOf("@") + 1);
                    if (status.equalsIgnoreCase("Accepted")) {
                        status = "Accepted by " + user;
                    } else if(status.equalsIgnoreCase("Miss")) {
                        status = "Missed";
                    }
                }

                list.add(new ViewLog(null, time, status));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
