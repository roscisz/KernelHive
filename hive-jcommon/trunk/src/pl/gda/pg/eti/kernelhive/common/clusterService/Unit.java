package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.UnitInfo;

public class Unit {

	private static String deviceSeparator = ";";
	@XmlTransient
	public Cluster cluster;
	private boolean connected = false;
	public List<Device> devices = new ArrayList<>();
	private int unitId;
	private int clusterId;
	private String hostname;
	private int memorySize;
	private int cpuCores;

	public Unit() {
	}

	public Unit(Cluster cluster, String hostname) {
		this.cluster = cluster;
		clusterId = cluster.id;
		this.hostname = hostname;
	}

	public int getId() {
		return new BigDecimal(IdHelpers.getUnitId(unitId, clusterId)).intValue();
	}

	public void update(String serialized) {
		String[] parts = serialized.split(":");
		int offset = 0;
		unitId = Integer.parseInt(parts[offset++]);
		cpuCores = Integer.parseInt(parts[offset++]);
		memorySize = Integer.parseInt(parts[offset++]);
		int devicesCount = Integer.parseInt(parts[offset++]);
		devices.clear();
		for (int i = 0; i < devicesCount; i++) {
			String[] deviceProperties = Arrays.copyOfRange(parts, offset,
					offset + Device.getPropertiesCount());
			Device device = new Device(deviceProperties, this);
			devices.add(device);
			offset += Device.getPropertiesCount();
		}
	}

	/*@Override
	 public String toString() {
	 return devices.toString();
	 }*/
	public void updateReverseReferences(Cluster cluster) {
		this.cluster = cluster;
		for (Device device : devices) {
			device.updateReverseReferences(this);
		}
	}

	public UnitInfo getUnitInfo() {
		List<DeviceInfo> deviceInfos = new ArrayList<>();

		for (Device device : devices) {
			deviceInfos.add(device.getDeviceInfo());
		}

		return new UnitInfo(getId(), deviceInfos);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public int getCpuCores() {
		return cpuCores;
	}

	public void setCpuCores(int cpuCores) {
		this.cpuCores = cpuCores;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public Device getDevice(String identifier) {
		for (Device device : devices) {
			if (identifier.equals(device.identifier)) {
				return device;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%d:%d %s %d %d", clusterId, unitId, hostname, cpuCores, memorySize);
	}

	public void merge(Unit unit) {
		this.hostname = unit.getHostname();
		this.cpuCores = unit.getCpuCores();
		this.memorySize = unit.getMemorySize();

		List<Device> newDevices = new ArrayList<>();
		// merge the devices list - add new ones, merge existing 
		// and omit ones that don't exist anymore
		for (Device newDevice : unit.getDevices()) {
			Device oldDevice = getDevice(newDevice.identifier);
			// if device already exists, merge it and keep it
			// if not, add it to list
			if (oldDevice != null) {
				oldDevice.merge(newDevice);
				newDevices.add(oldDevice);
			} else {
				newDevice.updateReverseReferences(this);
				newDevices.add(newDevice);
			}
		}
		devices = newDevices;
	}
}
