package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.adapter.data.Map.Map3;
import com.lewtsu.android.doorbell.adapter.data.MissedCall;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPGetMissCall extends AsyncTask<Void, Void, List<Map3>> {

    @Override
    protected List<Map3> doInBackground(Void... params) {
        List<Map3> list = new ArrayList<>();
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
            URL url = new URL("http://" + ip + "/piphp.php?type=GetMissCall");
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

        /*
        try {
            String imgUrl, time;
            Bitmap bmp;

            JSONArray jArray = new JSONArray(str.toString());
            for (int i = 0; i < jArray.length(); ++i) {
                time = jArray.getString(i);
                imgUrl = "http://" + ip + "/img/" + time + "/5.jpg";
                try {
                    bmp = BitmapFactory.decodeStream(new URL(imgUrl.replace(" ", "%20")).openConnection().getInputStream());
                    list.add(new MissedCall(bmp, time));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

        try {
            String time;

            JSONArray jArray = new JSONArray(str.toString());
            for (int i = 0; i < jArray.length(); ++i) {
                time = jArray.getString(i);
                list.add(new MissedCall(null, time));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
