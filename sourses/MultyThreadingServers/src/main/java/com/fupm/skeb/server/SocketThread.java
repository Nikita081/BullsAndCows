/*package com.fupm.skeb.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketThread extends Thread {

	private int port;
	private ClientThread[] threads;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private int maxClients = 0;

	SocketThread(int port, ClientThread[] threads) {
		this.port = port;
		this.threads = threads;

	}

	public void run() {
		/* open server socket with default port (portNumber) */
		/*try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("Server started"+port);
		/* Create a client socket for each connecting, and pass it to new thread */
		/*while (true) {

			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				
					for (i = 0; i < threads.length; i++) {
						if (threads[i] == null) {
							(threads[i] = new ClientThread(clientSocket,
									threads,port)).start();
							break;

						}
					}
				
				if (i == threads.length) {
					PrintStream out = new PrintStream(
							clientSocket.getOutputStream());
					out.println("Srever too busy, please, try later");
					out.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}

		}
	}

}*/
