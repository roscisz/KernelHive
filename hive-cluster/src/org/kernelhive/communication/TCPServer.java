package org.kernelhive.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class TCPServer implements Runnable {
	
	private IServerListener listener;
	private ServerSocketChannel server;
	private Selector selector;
	
	public TCPServer(String host, int port, IServerListener listener) throws CommunicationException {
		this.listener = listener;
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			
			server.socket().bind(new InetSocketAddress(host, port));
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommunicationException(e);
		}		
		
		System.out.println("Server starts listening on " + host + ":" + port + ".");
		
		// FIXME: Who's responsible for thread management?
		new Thread(this).start();
	}

	@Override
	public void run() {
		// FIXME: WHEN TO STOP
		while(true) {
			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> i = keys.iterator();
			
			while(i.hasNext()) {
				processKey((SelectionKey) i.next());
				i.remove();				
			}
			/*
			for(SelectionKey key : keys) {	
				processKey(key);				
			}
			*/						
		}
		
	}

	private void processKey(SelectionKey key) {
		if(key.isAcceptable()) processConnection(key);
		else if(key.isReadable()) processMessage(key);
				
	
		// else I don't know this type of key...
		
	}

	private void processConnection(SelectionKey key) {
		SocketChannel socketChannel;
		try {
			socketChannel = server.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			listener.onConnection(socketChannel);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void processMessage(SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		
		/*
		if(!client.isOpen()) {
			System.out.println("AHA!");
			//key.cancel();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}*/

		// FIXME: define MAX message size
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		try {
			if(client.read(buffer) <= 0) {
				client.close();
				listener.onDisconnection(client);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
					
		try {
			listener.onMessage(client, Decoder.decode(buffer));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
	}
}
