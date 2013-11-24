/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.Decoder;
import pl.gda.pg.eti.kernelhive.common.communication.NetworkAddress;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServerListener;

/**
 *
 * @author szymon
 */
public class UnitServer implements TCPServerListener {

	private static final Logger LOGGER = Logger.getLogger(UnitServer.class.getName());
	private TCPServer tcpServer;
	private Cluster cluster;
	private ClusterBean clusterBean;
	private Map<Integer, UnitProxy> unitsMap = new HashMap<>();
	private Map<Integer, Integer> unitsConnectionsCount = new HashMap<>();

	public UnitServer(String clusterHostname, int clusterPort, Cluster cluster,
			ClusterBean clusterBean) {
		this.cluster = cluster;
		this.clusterBean = clusterBean;
		tcpServer = new TCPServer(new NetworkAddress(clusterHostname, clusterPort), this);
	}

	public void start() throws CommunicationException {
		tcpServer.start();
	}

	public void stop() throws CommunicationException {
		tcpServer.stop();
	}

	@Override
	public void onConnection(SocketChannel channel) {
		String hostname = ClusterHelpers.getHostName(channel);

		LOGGER.info(String.format("Got connection from host %s", hostname));

		Unit unit = cluster.getUnit(hostname);
		if (unit == null) {
			unit = new Unit(cluster, hostname);
			unit.setUnitId(IdRepository.getId());
			unit.setConnected(true);
			cluster.addUnit(unit);
			LOGGER.info("Created new unit with ID " + unit.getId());
		}
		UnitProxy proxy = unitsMap.get(unit.getId());
		if (proxy == null) {
			proxy = new UnitProxy(channel, unit);
			unitsMap.put(unit.getId(), proxy);
			LOGGER.info("unitsMap.put " + unit.getId());
			unitsConnectionsCount.put(unit.getId(), 1);
		} else {
			unitsConnectionsCount.put(unit.getId(), unitsConnectionsCount.get(unit.getId()) + 1);
		}
		proxy.sendMessage(String.valueOf(unit.getId()));
	}

	@Override
	public void onTCPMessage(SocketChannel channel, ByteBuffer messageBuffer) {
		String message = Decoder.decode(messageBuffer);
		String hostname = ClusterHelpers.getHostName(channel);

		LOGGER.info(String.format("Message %s from host %s", message, hostname));

		// FIXME: define separators in one place
		String[] command = message.split(" ", 2);
		switch (command[0]) {
			case "UPDATE":
				commandUpdate(hostname, command[1]);
				break;
			case "OVER":
				commandOver(command[1]);
				break;
		}
	}

	@Override
	public void onDisconnection(SocketChannel channel) {
		String hostname = ClusterHelpers.getHostName(channel);
		LOGGER.info(String.format("Host %s disconnected.", hostname));

		Unit unit = cluster.getUnit(hostname);
		if (unit != null) {
			Integer connCount = unitsConnectionsCount.get(unit.getId());
			if (connCount != null && connCount > 0) {
				connCount--;
			} else {
				connCount = 0;
			}
			if (connCount == 0) {
				unit.setConnected(false);
				unitsMap.remove(unit.getId());
				unitsConnectionsCount.remove(unit.getId());
				LOGGER.info("unitsMap.remove " + unit.getId());
			}
		}
		LOGGER.info("Now we have " + cluster.getConnectedUnits().size() + " clients");

	}

	private void commandOver(String message) {
		String[] command = message.split(" ", 2);
		int id = Integer.parseInt(command[0]);
		onJobDone(id, command[1]);
	}

	private void onJobDone(int id, String status) {
		LOGGER.info(String.format("Job %d over.", id));

		boolean succeded = false;
		while (!succeded) {
			try {
				clusterBean.reportOver(id, status);
				LOGGER.info(String.format("Job %d over -reported", id));
				succeded = true;
			} catch (SOAPFaultException e) {
				LOGGER.log(Level.WARNING, "UnitServer failed to report job over. "
						+ "Trying again in 3 seconds... ", e);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	private void commandUpdate(String hostname, String data) {
		Unit unit = cluster.getUnit(hostname);
		unit.update(data);

		LOGGER.info(String.format("Now we have %d clients", unitsMap.size()));

		updateClusterInEngine();
	}

	private void updateClusterInEngine() {
		LOGGER.info(String.format("Updating cluster in engine: %s with %d units", cluster.hostname, cluster.getUnitList().size()));
		clusterBean.update(cluster);
		LOGGER.info("Updated cluster in engine");
	}

	public UnitProxy getProxy(int unitID) {
		LOGGER.info(String.format("getProxy for %d: %d units -> %s", unitID, unitsMap.size(), unitsMap.get(unitID).toString()));
		return unitsMap.get(unitID);
	}
}
