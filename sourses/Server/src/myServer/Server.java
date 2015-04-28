package myServer;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) throws IOException{
		
		Socket clientSocket = null;
		ServerSocket serverSocket = null;
		PrintWriter printwriter;
		
		try{
			serverSocket = new ServerSocket(3333);
			System.out.println("Server started. . .");
			clientSocket = serverSocket.accept();
			
		}catch(Exception e){}
		
		Scanner in1 = new Scanner(clientSocket.getInputStream());
		String mes;
		printwriter = new PrintWriter(clientSocket.getOutputStream());
       
		
		while(true){
			if(in1.hasNext()){
				mes = in1.nextLine();
				System.out.println("Client message :"+mes);
				printwriter.write(mes);
			}
		}
		
	}
}
