package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lewtsu.android.doorbell.adapter.data.ManageWifiListView;
import com.lewtsu.android.doorbell.adapter.data.Map.Map2;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketManageWifiScan extends AsyncTask<String, Void, List<Map2>> {

    @Override
    protected List<Map2> doInBackground(String... params) {
        List<Map2> list = new ArrayList<>();
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ip == null)
            return list;


        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.PING_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            out.println("ManageWifiScan");
            out.flush();

            String sizeStr, current, ssid, type = "Connected";
            int feq;

            sizeStr = in.nextLine();
            int size = Integer.parseInt(sizeStr);
            current = in.nextLine();
            if (current.equalsIgnoreCase("Not Connected"))
                type = "Not Connected";
            Map2 v = new ManageWifiListView(current, type, -1);
            list.add(v);
            for (int i = 0; i < size; ++i) {
                ssid = in.nextLine();
                type = in.nextLine();
                feq = Integer.parseInt(in.nextLine());
                if (!ssid.equalsIgnoreCase(current)) {
                    Map2 vv = new ManageWifiListView(ssid, type, feq);
                    list.add(vv);
                } else {
                    v.feq = feq;
                }
            }

            out.close();
            in.close();
            socket.close();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            Log.e(Constant.TAG, "Can't connect " + ip);
        } catch (ConnectException e) {
            Log.e(Constant.TAG, "Can't connect " + ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
