package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;
import pl.gda.pg.eti.kernelhive.common.communication.NetworkAddress;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServerListener;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServer;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServerListener;

public class ClusterManager implements TCPServerListener, UDPServerListener {
	
	private Hashtable<SocketChannel, UnitProxy> unitsMap = new Hashtable<SocketChannel, UnitProxy>();
	private List<HiveJob> currentJobs = new ArrayList<HiveJob>();
	
	// OBSOLETE
	private static String processDataKernel = "__kernel void processData(__global const int* input, unsigned int dataSize, __global int* output) { int i = get_global_id(0); output[i] = input[i]; }";
		
	public ClusterManager() {
		try {
			TCPServer unitServer = new TCPServer(new NetworkAddress("localhost", 31338), this);
			DataPublisher dp = new DataPublisher(new NetworkAddress("localhost", 31340));
			dp.publish(123, "DANE PRZYKLADOWE JAVA");
			dp.publish(456, processDataKernel);
			UDPServer runnerServer = new UDPServer(31339, this);
		} catch (CommunicationException e) {
			// TODO: Exit gracefully
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConnection(SocketChannel channel) {
		
		System.out.println("Got connection from channel " + channel);		
		
	}

	@Override
	public void onTCPMessage(SocketChannel channel, String message) {			
		System.out.println("Message " + message + " from channel " + channel);
		// FIXME: define separators in one place
		String[] command = message.split(" ", 2);
		
		if(command[0].equals("UPDATE")) {
			commandUpdate(channel, command[1]);
		}
		/*else if(command[0].equals("test")) {
			test();			
		}*/
		else if(command[0].equals("OVER")) {
			commandOver(command[1]);
		}
		
	}

	private void commandUpdate(SocketChannel channel, String data) {
		UnitProxy proxy = unitsMap.get(channel);
		if(proxy == null) {
			proxy = new UnitProxy(channel);
			unitsMap.put(channel,  proxy);
		}
		proxy.update(data);
		System.out.println("Now we have " + unitsMap.size() + " clients");
	}
	
	private void commandOver(String message) {
		String[] command = message.split(" ", 2);
		int id = Integer.parseInt(command[0]);
		onJobDone(id, command[1]);		
	}
	
	private void test() {		
		prepareJobs();
		currentJobs.get(0).setDataAddress("localhost 31340 123");
		currentJobs.get(0).run();		
	}

	private void prepareJobs() {
		currentJobs.clear();
		int id = 0;
		for(UnitProxy up : unitsMap.values())
			for(Device d : up.getDevices()) {
				currentJobs.add(new HiveJob(up, d.name, id));
				id++;
			}
		System.out.println("Prepared " + currentJobs.size() + " jobs.");
	}

	@Override
	public void onDisconnection(SocketChannel channel) {
		
		System.out.println("Channel " + channel + " disconnected.");
		unitsMap.remove(channel);
		System.out.println("Now we have " + unitsMap.size() + " clients");		
		
	}

	@Override
	public void onUDPMessage(String message) {
		String[] report = message.split(" ", 3);
		int id = Integer.parseInt(report[0]);		
		int percent = Integer.parseInt(report[1]);
		
		// TODO: Update progress in Job representation
		System.out.println("JobID " + id + ": " + percent + "% done.");
	}

	private void onJobDone(int id, String status) {
		System.out.println("Job " + id + " over.");				
		
		/*id++;
		if(id < currentJobs.size()) {
			System.out.println("Running job " + id);
			currentJobs.get(id).setDataAddress(status);
			currentJobs.get(id).run();
		}
		else System.out.println("All jobs done.");
		*/		
	}

}