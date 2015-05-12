package myServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private Socket clientSocket = null;
	private BufferedReader in = null;
	private PrintStream out = null;
	private final ClientThread[] threads;
	
	public ClientThread(Socket clientSocket, ClientThread[] threads){
		this.clientSocket = clientSocket;
		this.threads = threads;
	}
	
	public void run(){
		ClientThread[] threads = this.threads;
		String clientMessage;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream());
			
			while(true){
				clientMessage = in.readLine();
				System.out.println(clientMessage);
				out.println(clientMessage);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}