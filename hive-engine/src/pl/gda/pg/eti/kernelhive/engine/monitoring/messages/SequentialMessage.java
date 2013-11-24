/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pl.gda.pg.eti.kernelhive.engine.monitoring.DeviceCyclicData;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoringMessageType;

/**
 *
 * @author szymon
 */
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
