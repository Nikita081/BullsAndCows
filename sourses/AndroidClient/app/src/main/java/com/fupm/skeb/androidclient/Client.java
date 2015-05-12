package com.fupm.skeb.androidclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
    private String serverMessage;
    public static  String serverIP = "192.168.0.101";
    public static final int port = 2222;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private PrintWriter out;
    private BufferedReader in;


    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }


    public void sendNumber(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }
    public void sendRiddle(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
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
            Socket socket = new Socket(serverAddr, port);
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

                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }


    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}


