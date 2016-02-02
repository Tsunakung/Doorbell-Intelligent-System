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

public class SocketChangePasswordDevice extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String ip = params[0];
        String password = params[1];
        String newPassword = params[2];
        String response = null;
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
