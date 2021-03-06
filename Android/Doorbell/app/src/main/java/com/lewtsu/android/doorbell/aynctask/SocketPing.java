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
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketPing extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        String ip = params[0];
        int timeout = 1000;
        if (params.length > 1)
            timeout = Integer.parseInt(params[1]);
        boolean isDoorbell = false;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.PING_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, timeout);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            out.println("Ping");
            out.flush();

            String receive = in.nextLine();

            isDoorbell = receive.equalsIgnoreCase("true");

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
        return isDoorbell;
    }
}
