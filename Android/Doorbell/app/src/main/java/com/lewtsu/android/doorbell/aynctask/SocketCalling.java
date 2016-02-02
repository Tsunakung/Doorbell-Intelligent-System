package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lewtsu.android.doorbell.constant.Constant;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class SocketCalling extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        String ip = params[0];
        boolean isCalling = false;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.PING_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            out.println("Calling");
            out.flush();

            String receive = in.nextLine();

            isCalling = receive.equalsIgnoreCase("true");

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
        return isCalling;
    }
}
