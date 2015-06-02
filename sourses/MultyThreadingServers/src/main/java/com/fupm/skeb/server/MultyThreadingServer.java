package com.fupm.skeb.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultyThreadingServer {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final int maxClients = 100;
	private static final ClientThread[] threads = new ClientThread[maxClients];
	private static final GameContent[] content = new GameContent[maxClients];

	public static void main(String[] args) {

		// enter port number
		int portNumber = 10100;// Integer.valueOf(args[0]).intValue();

		/* open server socket with default port (portNumber) */
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("Server started");
		/* Create a client socket for each connecting, and pass it to new thread */
		while (true) {

			try {
				clientSocket = serverSocket.accept();
				int i = 0;

				for (i = 0; i < threads.length; i++) {
					if (threads[i] == null) {
						(threads[i] = new ClientThread(clientSocket, threads,content))
								.start();
						break;

					}
				}

				if (i == maxClients) {
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

}
