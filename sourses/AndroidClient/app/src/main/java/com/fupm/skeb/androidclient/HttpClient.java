package com.fupm.skeb.androidclient;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;

import static android.os.SystemClock.sleep;

public class HttpClient {
    private String serverMessage;
    private static  String myurl = "http://192.168.0.101:10100/";
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private String TAG = "Client logging";
    private PrintWriter out;
    private BufferedReader in;
    private int len = 500;
    public HttpClient(OnMessageReceived listener) {
        mMessageListener = listener;

    }

    public boolean sendMessage(String message) throws IOException {
        if (out != null) {
            
            out.write(message);
            out.flush();
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
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Starts the query
            conn.connect();
            Log.d(TAG, "connected!!!!!!");
            //int response = conn.getResponseCode();
            //Log.d(TAG, "The response is: " + response);
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(conn.getOutputStream())), true);
            if(out != null){
                Log.d(TAG,"out open sucsessful");
            }

            /*in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            if(in!=null){
                Log.d(TAG,"in open sucsessful");
            }*/
            while(true){
                int a = 5, b = 10;
                a += b;
                b = a;
                sleep(5000);
            }
            // Convert the InputStream into a string
            /*while (mRun) {
                serverMessage = in.readLine();
                Log.d(TAG,"got server mesage: "+serverMessage);
                if (serverMessage != null && mMessageListener != null) {
                    // call the method messageReceived from MyActivity class
                    mMessageListener.messageReceived(serverMessage);
                }
                serverMessage = null;

            }*/


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){

                    out.close();
                Log.d(TAG,"out closed!!!");
            }
        }

    }


    public interface OnMessageReceived {
        void messageReceived(String message);
    }
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}


