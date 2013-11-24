package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.channels.SocketChannel;

import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.Decoder;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;

public class UnitProxy {

	private SocketChannel socketChannel;
	private Unit unit;

	public UnitProxy(SocketChannel socketChannel, Unit unit) {
		this.socketChannel = socketChannel;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "UnitProxy [socketChannel=" + socketChannel + ", devices="
				+ unit + "]";
	}

	public void runJob(JobInfo jobInfo) {
		System.out.println("Sending job: " + jobInfo.toString());
		sendMessage(jobInfo.toString());
	}

	public void sendMessage(String message) {
		System.out.println("Sending to socketChannel " + socketChannel);
		TCPServer.sendMessage(socketChannel, Decoder.encode(message));
	}

	public Unit getUnit() {
		return unit;
	}
}
