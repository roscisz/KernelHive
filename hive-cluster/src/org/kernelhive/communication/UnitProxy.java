package org.kernelhive.communication;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;

public class UnitProxy {
	
	private SocketChannel socketChannel;
	
	public UnitProxy(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	public void sendMessage(String message) {
		
		try {
			socketChannel.write(Decoder.encode(message));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
