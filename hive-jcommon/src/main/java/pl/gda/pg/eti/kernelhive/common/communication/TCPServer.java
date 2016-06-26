/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * TODO: TCP buffering
 */
public class TCPServer implements Runnable {

	// TODO: explain :)
	public static int MAX_MESSAGE_BYTES = 1492;
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private TCPServerListener listener;
	private ServerSocketChannel server;
	private NetworkAddress address;
	private Selector selector;
	private Map<SocketChannel, ByteBuffer> buffers = new HashMap<>();
	private Map<SocketChannel, ByteBuffer> commandSizeBuffers = new HashMap<>();
	private boolean stop = false;
	private final Thread thread;

	public TCPServer(NetworkAddress address, TCPServerListener listener) {
		this.listener = listener;
		this.address = address;

		// FIXME: Who's responsible for thread management?
		thread = new Thread(this);
	}

	public static void sendMessage(SocketChannel socketChannel, ByteBuffer message) {
		message.flip();
		try {
			socketChannel.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareSocket(int port) throws IOException {
		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		server.socket().bind(new InetSocketAddress(port));

		selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		stop = false;
		while (!stop) {
			try {
				handleSelector();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while receiving TCP message", e);
			}
		}
		try {
			server.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while closing connection", e);
		}
	}

	public void start() throws CommunicationException {
		try {
			prepareSocket(address.port);
			thread.start();
			System.out.println("TCP server starts listening on " + address.host + ":" + address.port + ".");
		} catch (IOException e) {
			throw new CommunicationException(e);
		}
	}

	public void stop() {
		this.stop = true;
	}

	private void handleSelector() throws IOException {
		selector.select();

		Set<SelectionKey> keys = selector.selectedKeys();
		Iterator<SelectionKey> i = keys.iterator();

		while (i.hasNext()) {
			processKey((SelectionKey) i.next());
			i.remove();
		}
	}

	private void processKey(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			processConnection(key);
		} else if (key.isReadable()) {
			processMessage(key);
		}
	}

	private void processConnection(SelectionKey key) throws IOException {
		SocketChannel socketChannel = server.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		listener.onConnection(socketChannel);
	}

	private void processMessage(SelectionKey key) throws IOException {
		SocketChannel client = (SocketChannel) key.channel();

		try {
			ByteBuffer incomingBuffer = readBuffer(client);

			if (buffers.containsKey(client)) {
				ByteBuffer existingBuffer = buffers.get(client);
				if (completeBuffer(existingBuffer, incomingBuffer)) {
					existingBuffer.rewind();
					listener.onTCPMessage(client, existingBuffer);
					buffers.remove(client);
				} else {
					return;
				}
			}

			while (incomingBuffer.hasRemaining()) {
				ByteBuffer commandSizeBuffer = getCommandSizeBuffer(client);
				commandSizeBuffer.put(incomingBuffer.get());
				if (commandSizeBuffer.position() < 4) {
					continue;
				}
				commandSizeBuffer.rewind();
				int commandSize = commandSizeBuffer.getInt();
				commandSizeBuffer.rewind();
				//int commandSize = incomingBuffer.getInt();

				// System.out.println("Command size: " + commandSize);

				if (incomingBuffer.remaining() >= commandSize) {
					listener.onTCPMessage(client, incomingBuffer);
				} else {
					ByteBuffer incompleteBuffer = ByteBuffer.allocate(commandSize);
					incompleteBuffer.order(ByteOrder.LITTLE_ENDIAN);
					incompleteBuffer.put(incomingBuffer);
					buffers.put(client, incompleteBuffer);
				}
			}
		} catch (CommunicationException ce) {
			return;
		}
	}

	private ByteBuffer getCommandSizeBuffer(SocketChannel client) {
		if (!commandSizeBuffers.containsKey(client)) {
			ByteBuffer commandSizeBuffer = ByteBuffer.allocate(4);
			commandSizeBuffer.order(ByteOrder.LITTLE_ENDIAN);
			commandSizeBuffers.put(client, commandSizeBuffer);
			return commandSizeBuffer;
		} else {
			return commandSizeBuffers.get(client);
		}

	}

	/**
	 *
	 * @param toComplete - the Buffer that needs to be completed with position
	 * at limit
	 * @param supplement - the complementary buffer
	 * @return boolean - true if the complementary buffer was sufficient to
	 * complete the toComplete buffer
	 */
	boolean completeBuffer(ByteBuffer toComplete, ByteBuffer supplement) {
		int remaining = toComplete.capacity() - toComplete.position();
		byte[] bytes = new byte[remaining];

		try {
			supplement.get(bytes);
		} catch (BufferUnderflowException bue) {
			toComplete.put(supplement);
			return false;
		}

		toComplete.put(bytes);

		return true;
	}

	private ByteBuffer readBuffer(SocketChannel client) throws CommunicationException, IOException {
		ByteBuffer buffer = prepareEmptyBuffer();

		int bytesRead = client.read(buffer);

		if (bytesRead <= 0) {
			client.close();
			listener.onDisconnection(client);
			throw new CommunicationException(null);
		}

		buffer.limit(bytesRead);
		buffer.rewind();

		return buffer;
	}

	public static ByteBuffer prepareEmptyBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_BYTES);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		return buffer;
	}

	public static byte[] byteBufferToArray(ByteBuffer buffer) {
		buffer.rewind();
		byte[] ret = new byte[buffer.remaining()];
		buffer.get(ret, 0, ret.length);
		return ret;
	}
}
