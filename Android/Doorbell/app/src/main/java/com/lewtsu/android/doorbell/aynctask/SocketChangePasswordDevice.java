package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class SocketChangePasswordDevice extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        String ip = null;
        try {
            ip = Config.getConfig().getString(Constant.CONNECT_IP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(ip == null)
            return "Not found IP Address";


        String password = params[0];
        String newPassword = params[1];
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.PING_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            out.println("ChangePasswordDevice");
            out.flush();
            Thread.sleep(100);
            out.println(password);
            out.flush();
            Thread.sleep(100);
            out.println(newPassword);
            out.flush();

            String receive = in.nextLine();

            response = receive;

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
