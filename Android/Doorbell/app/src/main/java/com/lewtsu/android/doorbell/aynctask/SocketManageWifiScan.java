package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lewtsu.android.doorbell.adapter.data.Map.Map2;
import com.lewtsu.android.doorbell.constant.Constant;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketManageWifiScan extends AsyncTask<String, Void, List<Map2>> {

    @Override
    protected List<Map2> doInBackground(String... params) {
        String ip = params[0];
        List<Map2> list = new ArrayList<Map2>();
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
            if(current.equalsIgnoreCase("Not Connected"))
                type = "Not Connected";
            Map2 v = new Map2(current, type, -1);
            list.add(v);
            for (int i = 0; i < size; ++i) {
                ssid = in.nextLine();
                type = in.nextLine();
                feq = Integer.parseInt(in.nextLine());
                if(!ssid.equalsIgnoreCase(current)) {
                    Map2 vv = new Map2(ssid, type, feq);
                    list.add(vv);
                } else {
                    v.feq = feq;
                }
            }

            out.close();
            in.close();
            socket.close();
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
