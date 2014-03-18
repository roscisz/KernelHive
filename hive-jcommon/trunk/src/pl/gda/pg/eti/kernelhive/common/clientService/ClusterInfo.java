/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
package pl.gda.pg.eti.kernelhive.common.clientService;

import java.util.ArrayList;
import java.util.List;

public class ClusterInfo {

	public List<UnitInfo> unitInfos;
	public String hostname;
	public int TCPPort, UDPPort, dataPort;
	public int ID;

	public ClusterInfo() {
		
	}
	
	public ClusterInfo(int ID, List<UnitInfo> unitInfos, String hostname, int TCPPort, int UDPPort, int dataPort) {
		this.ID = ID;
		this.unitInfos = unitInfos;
		this.TCPPort = TCPPort;
		this.UDPPort = UDPPort;
		this.dataPort = dataPort;
	}

	@Override
	public String toString() {
		return "Cluster " + ID + " on " + hostname + " serving TCP (" + TCPPort + "), UDP + " + UDPPort + ", TCP data (" + dataPort +"), with " + unitInfos.size() + " units registered.";
	}	
}
