package org.kernelhive.cluster;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.kernelhive.communication.CommunicationException;
import org.kernelhive.communication.DataPublisher;
import org.kernelhive.communication.NetworkAddress;
import org.kernelhive.communication.TCPServerListener;
import org.kernelhive.communication.TCPServer;
import org.kernelhive.communication.UDPServer;
import org.kernelhive.communication.UDPServerListener;
import org.kernelhive.communication.UnitProxy;

public class ClusterManager implements TCPServerListener, UDPServerListener {
	
	private Hashtable<SocketChannel, UnitProxy> unitsMap = new Hashtable<SocketChannel, UnitProxy>();
		
	public ClusterManager() {
		try {
			TCPServer unitServer = new TCPServer(new NetworkAddress("localhost", 31338), this);
			DataPublisher dp = new DataPublisher(new NetworkAddress("localhost", 31340));
			dp.publish(123, "PRZYKLADOWE DANE");
			UDPServer runnerServer = new UDPServer(31339, this);
		} catch (CommunicationException e) {
			// TODO: Exit gracefully
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConnection(SocketChannel channel) {
		
		System.out.println("Got connection from channel " + channel);
		unitsMap.put(channel, new UnitProxy(channel));
		System.out.println("Now we have " + unitsMap.size() + " clients");
		
	}

	@Override
	public void onTCPMessage(SocketChannel channel, String message) {

		System.out.println("Message " + message + " from channel " + channel);
		UnitProxy proxy = unitsMap.get(channel);
		proxy.processMessage(message);

		// FIXME: remove debug code
		for(UnitProxy unit : unitsMap.values())
			System.out.println(unit.toString());

	}

	@Override
	public void onDisconnection(SocketChannel channel) {
		
		System.out.println("Channel " + channel + " disconnected.");
		unitsMap.remove(channel);
		System.out.println("Now we have " + unitsMap.size() + " clients");		
		
	}

	@Override
	public void onUDPMessage(String message) {
		System.out.println("Got UDP message " + message);		
	}

}
