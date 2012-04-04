package org.kernelhive.cluster;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RegistrationService implements Runnable {

	private static int PORTNO = 31331;
	private static int MAXCON = 30;
	
	@Override
	public void run() {
		//ServerSocket serverSocket = new ServerSocket(PORTNO, MAXCON);
		
		while(true) {
			//serverSocket.set
			//Socket socket = serverSocket.accept();
			
			
		}
		
	}
	


}
