package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;
import pl.gda.pg.eti.kernelhive.common.structure.Device;
import pl.gda.pg.eti.kernelhive.common.structure.Job;
import pl.gda.pg.eti.kernelhive.common.structure.Unit;

public class UnitProxy {

	private SocketChannel socketChannel;
	public Unit unit;
	
	
	public UnitProxy(SocketChannel socketChannel, Unit unit) {
		this.socketChannel = socketChannel;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "UnitProxy [socketChannel=" + socketChannel + ", devices="
				+ unit + "]";
	}

	/*
	public List<Device> getDevices() {
		return unit.devices;
	}*/
	
	public void runJob(Job job) {
		sendMessage(job.toString());
	}
	
	private void sendMessage(String message) {
		System.out.println("Sending to socketChannel " + socketChannel);
		TCPServer.sendMessage(socketChannel, message);
	}

}
