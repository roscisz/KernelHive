package pl.gda.pg.eti.kernelhive.common.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer implements Runnable {

	private static int MAX_DATAGRAM_BYTES = 1024;
	private final Logger logger = Logger.getLogger(getClass().getName());
	private final int port;
	private UDPServerListener listener;
	private DatagramSocket serverSocket;
	private final Thread thread;
	private boolean stop = false;

	public UDPServer(int port, UDPServerListener listener) {
		this.listener = listener;
		this.port = port;
		thread = new Thread(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				processSocket();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Cannot open UDP socket", e);
			}
		}
	}

	public void start() throws CommunicationException {
		stop = false;
		try {
			serverSocket = new DatagramSocket(port);
			thread.start();
			System.out.println("UDP server starts listening on port " + port + ".");
		} catch (SocketException e) {
			throw new CommunicationException(e);
		}
	}

	public void stop() {
		stop = true;
	}

	private void processSocket() throws IOException {
		listener.onUDPMessage(new UDPMessage(receiveMessage()));
	}

	private byte[] receiveMessage() throws IOException {
		DatagramPacket receivedPacket = receivePacket();
		return receivedPacket.getData();
	}

	private DatagramPacket receivePacket() throws IOException {
		byte[] receiveData = new byte[MAX_DATAGRAM_BYTES];
		DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivedPacket);
		return receivedPacket;
	}
}
