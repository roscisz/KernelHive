/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

/**
 *
 * @author szymon
 */
public class DeviceCyclicData {

	private int clusterId;
	private int untiId;
	private int deviceId;
	private int memoryUsed;
	private int gpuUsage;
	private int fanSpeed;

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public int getUntiId() {
		return untiId;
	}

	public void setUntiId(int untiId) {
		this.untiId = untiId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(int memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	public int getGpuUsage() {
		return gpuUsage;
	}

	public void setGpuUsage(int gpuUsage) {
		this.gpuUsage = gpuUsage;
	}

	public int getFanSpeed() {
		return fanSpeed;
	}

	public void setFanSpeed(int fanSpeed) {
		this.fanSpeed = fanSpeed;
	}
}
