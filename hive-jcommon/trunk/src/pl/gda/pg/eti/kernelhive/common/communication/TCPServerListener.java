package pl.gda.pg.eti.kernelhive.common.communication;

import java.nio.channels.SocketChannel;

public interface TCPServerListener {
	
	public void onConnection(SocketChannel channel);
	public void onTCPMessage(SocketChannel channel, String message);
	public void onDisconnection(SocketChannel channel);

}
