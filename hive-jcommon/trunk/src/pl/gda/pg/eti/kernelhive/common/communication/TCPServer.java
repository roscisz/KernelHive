package pl.gda.pg.eti.kernelhive.common.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/*
 * TODO: TCP buffering
 */
public class TCPServer implements Runnable {
	
	private static int MAX_MESSAGE_BYTES = 1024;
	
	private TCPServerListener listener;
	private ServerSocketChannel server;
	private Selector selector;
	
	public TCPServer(NetworkAddress address, TCPServerListener listener) throws CommunicationException {
		this.listener = listener;
		
		try {
			prepareSocket(address.host, address.port);
		} catch (IOException e) {
			throw new CommunicationException(e);
		}		
		
		System.out.println("TCP server starts listening on " + address.host + ":" + address.port + ".");
		
		// FIXME: Who's responsible for thread management?
		new Thread(this).start();
	}
	
	public static void sendMessage(SocketChannel socketChannel, String message) {
		if(message.equals(null)) System.out.println("NULL MESSAGE??");
		try {
			socketChannel.write(Decoder.encode(message));
		} catch (CommunicationException ce) {
			ce.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareSocket(String host, int port) throws IOException {
		server = ServerSocketChannel.open();
		server.configureBlocking(false);		
		server.socket().bind(new InetSocketAddress(host, port));
		
		selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		// FIXME: WHEN TO STOP
		while(true) {
			try {
				handleSelector();
			}
			catch(IOException e) {
				e.printStackTrace();				
			}
		}
	}
	
	private void handleSelector() throws IOException {
		selector.select();
		
		Set<SelectionKey> keys = selector.selectedKeys();
		Iterator<SelectionKey> i = keys.iterator();
		
		while(i.hasNext()) {
			processKey((SelectionKey) i.next());
			i.remove();				
		}
	}

	private void processKey(SelectionKey key) throws IOException {
		if(key.isAcceptable()) processConnection(key);
		else if(key.isReadable()) processMessage(key);
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
			ByteBuffer buffer = readBuffer(client);
			String message = Decoder.decode(buffer);
			message = message.split("\n")[0];
			message = message.split("\r")[0];
			listener.onTCPMessage(client, message);
		}
		catch(CommunicationException ce) {
			return;
		}
	}
	
	private ByteBuffer readBuffer(SocketChannel client) throws CommunicationException, IOException {
		ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_BYTES);

		if(client.read(buffer) <= 0) {
			client.close();
			listener.onDisconnection(client);
			throw new CommunicationException(null);
		}
		
		return buffer;
	}
}