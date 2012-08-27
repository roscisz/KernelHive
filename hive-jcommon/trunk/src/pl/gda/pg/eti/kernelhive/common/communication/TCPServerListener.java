package pl.gda.pg.eti.kernelhive.common.communication;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface TCPServerListener {
	
	public void onConnection(SocketChannel channel);
	public void onTCPMessage(SocketChannel channel, ByteBuffer message);
	public void onDisconnection(SocketChannel channel);

}
