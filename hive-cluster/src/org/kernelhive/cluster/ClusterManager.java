package org.kernelhive.cluster;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.kernelhive.communication.CommunicationException;
import org.kernelhive.communication.IServerListener;
import org.kernelhive.communication.TCPServer;
import org.kernelhive.communication.UnitProxy;

public class ClusterManager implements IServerListener {
	
	private Dictionary<SocketChannel, UnitProxy> units = new Hashtable<SocketChannel, UnitProxy>();
		
	public ClusterManager() {
		try {
			TCPServer unitServer = new TCPServer("localhost", 31338, this);
		} catch (CommunicationException e) {
			// TODO: Exit gracefully
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConnection(SocketChannel channel) {
		
		System.out.println("Got connection from channel " + channel);
		units.put(channel, new UnitProxy(channel));
		System.out.println("Now we have " + units.size() + " clients");
		
	}

	@Override
	public void onMessage(SocketChannel channel, String message) {

		System.out.println("Message " + message + " from channel " + channel);
		UnitProxy proxy = units.get(channel);
		proxy.sendMessage("RE: " + message);
		
	}

	@Override
	public void onDisconnection(SocketChannel channel) {
		
		System.out.println("Channel " + channel + " disconnected.");
		units.remove(channel);
		System.out.println("Now we have " + units.size() + " clients");		
		
	}

}
