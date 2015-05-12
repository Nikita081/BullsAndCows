package com.fupm.skeb.androidclient;


import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class HttpClient {
    private String serverMessage;
    public static  String serverIP = "http://192.168.0.102/";
    public static final int port = 2222;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private PrintWriter out;
    private BufferedReader in;


    public HttpClient(OnMessageReceived listener) {
        mMessageListener = listener;

    }


    public void sendEP(String email,String passowrd) {
        if (out != null && !out.checkError()) {
            out.println(email);
            out.println(passowrd);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;
        URL url = null;
        try {
            url = new URL(serverIP);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // Starts the query

            conn.connect();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}
