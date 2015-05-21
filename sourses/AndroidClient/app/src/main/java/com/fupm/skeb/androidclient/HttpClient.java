package com.fupm.skeb.androidclient;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class HttpClient {
    private String serverMessage;
    public static  String ip = "http://192.168.0.101:10100";
    public static final int port = 10100;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private String TAG = "Client logging";
    public Socket socket;
    DataOutputStream wr;
    BufferedReader rd;

    public HttpClient(OnMessageReceived listener) {
        mMessageListener = listener;

    }

    public boolean sendMessage(String message) throws IOException {
        if (wr != null) {
            wr.writeBytes(message);
            wr.flush();
            return true;
        }
        return false;
    }

    public void stopClient() {

        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            URL url;
            String message = "Test Message!";
            HttpURLConnection connection;
            try {
                //Create connection
                url = new URL("http://192.168.0.101:10100");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(message.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches (false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                wr = new DataOutputStream (
                        connection.getOutputStream ());

                InputStream is = connection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(is));



                while (mRun) {
                    serverMessage = rd.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        // call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {



            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }


    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}


