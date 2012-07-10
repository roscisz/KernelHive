package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;

public class UnitProxy {
	private static String deviceSeparator = ";";
	
	private SocketChannel socketChannel;
	private List<Device> devices = new ArrayList<Device>();
	
	public UnitProxy(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void update(String devicesString) {
		String[] devicesArray = devicesString.split(deviceSeparator);
	
		int devicesCount = Integer.parseInt(devicesArray[0]);
		
		devices.clear();
		for(int i = 1; i <= devicesCount; i++)
			devices.add(new Device(devicesArray[i]));		
	}

	@Override
	public String toString() {
		return "UnitProxy [socketChannel=" + socketChannel + ", devices="
				+ devices + "]";
	}

	public List<Device> getDevices() {
		return devices;
	}
	
	public void runJob(HiveJob job) {
		sendMessage(job.toString());		
	}
	
	private void sendMessage(String message) {
		TCPServer.sendMessage(socketChannel, message);
	}

}
