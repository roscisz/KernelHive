/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.dao;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

/**
 *
 * @author szymon
 */
public class ClusterDefinition implements H2Entity {

	private Long id;
	private String hostname;
	private int tcpPort;
	private int udpPort;
	private int dataPort;

	public ClusterDefinition() {
	}

	public ClusterDefinition(Cluster data) {
		id = Long.valueOf(data.id);
		hostname = data.hostname;
		tcpPort = data.TCPPort;
		udpPort = data.UDPPort;
		dataPort = data.dataPort;
	}

	@Override
	@H2Getter
	public Long getId() {
		return id;
	}

	@H2Setter
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getTableName() {
		return "Clusters";
	}

	@H2Getter
	public String getHostname() {
		return hostname;
	}

	@H2Setter
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@H2Getter
	public int getTcpPort() {
		return tcpPort;
	}

	@H2Setter
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	@H2Getter
	public int getUdpPort() {
		return udpPort;
	}

	@H2Setter
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	@H2Getter
	public int getDataPort() {
		return dataPort;
	}

	@H2Setter
	public void setDataPort(int dataPort) {
		this.dataPort = dataPort;
	}
}
