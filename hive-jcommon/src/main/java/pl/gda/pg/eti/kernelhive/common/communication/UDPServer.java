/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
