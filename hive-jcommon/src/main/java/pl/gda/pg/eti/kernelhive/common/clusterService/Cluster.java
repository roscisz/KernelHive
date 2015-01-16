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
package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.UnitInfo;

public class Cluster implements Serializable {

	public Integer id = null;
	public String hostname;
	public int TCPPort;
	public int UDPPort;
	public int dataPort;
	public Map<String, Unit> unitList = new HashMap<>();
	private final Object monitor = new Object();
	private List<Job> jobsToRun = new ArrayList<>();
	private int jobsCompletedCount = 0;

	public Cluster() {
	}

	public Cluster(int clusterTCPPort, int clusterDataPort, int clusterUDPPort, String clusterTCPHostname) {
		this.TCPPort = clusterTCPPort;
		this.UDPPort = clusterUDPPort;
		this.dataPort = clusterDataPort;
		this.hostname = clusterTCPHostname;
	}

	public void runJob(Job jobToRun) {
		jobsToRun.add(jobToRun);
		synchronized (monitor) {
			System.out.println("Notify on cluster " + this);
			monitor.notifyAll();
		}
	}

	public ClusterInfo getClusterInfo() {
		List<UnitInfo> unitInfos = new ArrayList<>();

		for (Unit unit : unitList.values()) {
			unitInfos.add(unit.getUnitInfo());
		}


		return new ClusterInfo(id, unitInfos, hostname, TCPPort, UDPPort, dataPort);
	}

	public void updateReverseReferences() {
		for (Unit unit : unitList.values()) {
			unit.updateReverseReferences(this);
		}
	}

	/*public Job getJob() {
	 System.out.println("cluster getJob()");
	 try {
	 synchronized (monitor) {
	 while (true) {
	 if (jobsToRun.size() > 0) {
	 System.out.println("cluster getJob() success");
	 Job jobToRun = jobsToRun.get(0);
	 jobsToRun.remove(0);
	 return jobToRun;
	 } else {
	 System.out.println("Wait on cluster" + this);
	 monitor.wait();
	 System.out.println("Get job released");						
	 }
	 }
	 }
	 } catch (InterruptedException e) {
	 System.out.println("getJob interrupted");
	 return null;
	 }
	 }*/
	public Job getJob() {
		if (jobsToRun.size() > 0) {
			System.out.println("cluster getJob() success");
			Job jobToRun = jobsToRun.get(0);
			jobsToRun.remove(0);
			return jobToRun;
		}
		System.out.println("cluster getJob() no job");
		return null;
	}

	public List<Unit> getUnitList() {
		return new ArrayList(unitList.values());
	}

	public void addUnit(Unit unit) {
		unitList.put(unit.getHostname(), unit);
	}

	public Unit getUnit(String hostname) {
		return unitList.get(hostname);
	}

	public void removeUnit(String hostname) {
		unitList.remove(hostname);
	}

	public List<Unit> getConnectedUnits() {
		List<Unit> connectedUnits = new ArrayList<>();
		for (Unit unit : unitList.values()) {
			if (unit.isConnected()) {
				connectedUnits.add(unit);
			}
		}
		return connectedUnits;
	}

	public void merge(Cluster cluster) {
		this.hostname = cluster.hostname;
		this.TCPPort = cluster.TCPPort;
		this.UDPPort = cluster.UDPPort;
		this.dataPort = cluster.dataPort;

		Map<String, Unit> newUnits = new HashMap<>();
		// merge the devices list - add new ones, merge existing 
		// and omit ones that don't exist anymore
		for (Unit newUnit : cluster.getUnitList()) {
			Unit oldUnit = getUnit(newUnit.getHostname());
			// if device already exists, merge it and keep it
			// if not, add it to list
			if (oldUnit != null) {
				oldUnit.merge(newUnit);
				newUnits.put(oldUnit.getHostname(), oldUnit);
			} else {
				newUnit.cluster = this;
				newUnits.put(newUnit.getHostname(), newUnit);
			}
		}
		unitList = newUnits;
	}
}
