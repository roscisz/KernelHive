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
package pl.gda.pg.eti.kernelhive.engine.monitoring.dao;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

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
