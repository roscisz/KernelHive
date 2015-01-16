/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
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
