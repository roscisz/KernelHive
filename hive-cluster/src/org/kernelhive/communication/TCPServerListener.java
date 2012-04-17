package org.kernelhive.communication;

import java.nio.channels.SocketChannel;

public interface TCPServerListener {
	
	public void onConnection(SocketChannel channel);
	public void onTCPMessage(SocketChannel channel, String message);
	public void onDisconnection(SocketChannel channel);

}
