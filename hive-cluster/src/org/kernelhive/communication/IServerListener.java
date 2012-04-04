package org.kernelhive.communication;

import java.nio.channels.SocketChannel;

public interface IServerListener {
	
	public void onConnection(SocketChannel channel);
	public void onMessage(SocketChannel channel, String message);
	public void onDisconnection(SocketChannel channel);

}
