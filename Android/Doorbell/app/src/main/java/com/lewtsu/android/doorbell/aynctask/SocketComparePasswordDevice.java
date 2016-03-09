package com.lewtsu.android.doorbell.aynctask;

import android.os.AsyncTask;

import com.lewtsu.android.doorbell.constant.Constant;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketComparePasswordDevice extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        boolean isPassword = false;
        String ip = params[0];
        String password = params[1];
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Constant.PING_PORT);

            Socket socket = new Socket();
            socket.connect(inetSocketAddress, 3000);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            out.println("Password");
            out.flush();
            Thread.sleep(100);
            out.println(password);
            out.flush();

            String receive = in.nextLine();
            isPassword = receive.equalsIgnoreCase("true");

            out.close();
            in.close();
            socket.close();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isPassword;
    }
}
