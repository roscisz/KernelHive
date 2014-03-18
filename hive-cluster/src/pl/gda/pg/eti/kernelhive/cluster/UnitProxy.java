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
