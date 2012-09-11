package pl.gda.pg.eti.kernelhive.common.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer implements Runnable {
	private static int MAX_DATAGRAM_BYTES = 1024;
	
	private UDPServerListener listener;
	private DatagramSocket serverSocket;
	
	public UDPServer(int port, UDPServerListener listener) throws CommunicationException {
				
		this.listener = listener;
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			throw new CommunicationException(e);
		}
		
		System.out.println("UDP server starts listening on port " + port + ".");
		
		new Thread(this).start();
	}

	@Override
	public void run() {        
        while(true)
        {
        	try {
        		processSocket();
        	}
        	catch(IOException e) {
        		e.printStackTrace();
        	}        	
        }
	}
	
	private void processSocket() throws IOException {
		String message = receiveMessage();
		message = message.split("\n")[0];
		message = message.split("\r")[0];
        listener.onUDPMessage(message);
     }
	
	private String receiveMessage() throws IOException {
		DatagramPacket receivedPacket = receivePacket();
		return new String(receivedPacket.getData());
	}

	private DatagramPacket receivePacket() throws IOException {
        byte[] receiveData = new byte[1024];        
        DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivedPacket);
        return receivedPacket;
	}
}
