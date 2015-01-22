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
package pl.gda.pg.eti.kernelhive.engine.monitoring.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pl.gda.pg.eti.kernelhive.engine.monitoring.DeviceCyclicData;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoringMessageType;

public class SequentialMessage implements Serializable, MonitoringMessage {

	private int unitId;
	private int clusterId;
	private int cpuCores;
	private int clockSpeed;
	private int memoryUsed;
	private List<Integer> cpuUsage = new ArrayList<>();
	private List<DeviceCyclicData> gpuDevices = new ArrayList<>();

	@Override
	public MonitoringMessageType getType() {
		return MonitoringMessageType.SEQUENTIAL;
	}

	@Override
	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	@Override
	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public int getClockSpeed() {
		return clockSpeed;
	}

	public void setClockSpeed(int clockSpeed) {
		this.clockSpeed = clockSpeed;
	}

	public int getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(int memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	public List<Integer> getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(List<Integer> cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public int getCpuCores() {
		return cpuCores;
	}

	public void setCpuCores(int cpuCores) {
		this.cpuCores = cpuCores;
	}

	public List<DeviceCyclicData> getGpuDevices() {
		return gpuDevices;
	}

	public void setGpuDevices(List<DeviceCyclicData> gpuDevices) {
		this.gpuDevices = gpuDevices;
	}
}
