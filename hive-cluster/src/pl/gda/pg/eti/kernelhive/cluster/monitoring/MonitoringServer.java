/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster.monitoring;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.UDPMessage;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServer;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServerListener;

/**
 *
 * @author szymon
 */
public class MonitoringServer implements UDPServerListener {

	private static final Logger logger = Logger.getLogger(MonitoringServer.class.getName());
	private MonitoringMessageSender messageSender;
	private int port;
	private UDPServer udpServer;
	private Cluster cluster;

	public MonitoringServer(int port, Cluster cluster) {
		this.port = port;
		this.cluster = cluster;
		udpServer = new UDPServer(port, this);
		try {
			messageSender = new MonitoringMessageSender();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Cannot create instance of MonitoringMessageSender", ex);
		}
	}

	public void start() throws CommunicationException {
		udpServer.start();
	}

	public void stop() {
		udpServer.stop();
	}

	@Override
	public void onUDPMessage(UDPMessage message) {
		//logger.log(Level.INFO, "Got monitoring message");
		byte[] bytes = message.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8 + bytes.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int clusterId = cluster.id;
		buffer.putShort((short) clusterId)
				.put(bytes);
		try {
			messageSender.send(buffer.array());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error while sending monitoring message", ex);
		}
	}
}
