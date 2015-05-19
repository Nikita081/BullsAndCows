package com.fupm.skeb.androidclient;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
    private String serverMessage;
    public static  String serverIP = "10.55.126.14";
    public static final int port = 10100;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private String TAG = "Client logging";
    public Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }


    public void sendRiddle(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }
    public boolean sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
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

            // here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(serverIP);
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting...");

            // create a socket to make the connection with the server
            socket = new Socket(serverAddr, port);
            Log.e("TCP Server IP", serverIP);
            try {

                // send the message to the server
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                // receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                // in this while the client listens for the messages sent by the
                // server
                while (mRun) {
                    serverMessage = in.readLine();

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
                out.close();
                in.close();
                socket.close();
                Log.i(TAG,"socket closed");


            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }


    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}


                /*StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                //byte[] b = (byte[])response;
                                //String out = new String(response);
                                String a = (String)response;
                                hello1.setText(a);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hello1.setText("That didn't work!");
                    }
                });
                queue.add(stringRequest);*/