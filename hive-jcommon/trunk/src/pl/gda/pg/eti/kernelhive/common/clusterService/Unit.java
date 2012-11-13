package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.UnitInfo;

public class Unit extends HasID {
	
	private static String deviceSeparator = ";";
	@XmlTransient
	public Cluster cluster;
	
	public List<Device> devices = new ArrayList<Device>();
	
	public Unit() {
		
	}
	
	public Unit(Cluster myCluster) {
		this.cluster = myCluster;
	}
	
	public void update(String devicesString) {
		String[] devicesArray = devicesString.split(deviceSeparator);
	
		int devicesCount = Integer.parseInt(devicesArray[0]);
		
		devices.clear();
		for(int i = 1; i <= devicesCount; i++)
			devices.add(new Device(devicesArray[i], this));
	}
	
	@Override
	public String toString() {
		return devices.toString();
	}

	public void updateReverseReferences(Cluster cluster) {
		this.cluster = cluster;
		for(Device device : devices)
			device.updateReverseReferences(this);
	}

	public UnitInfo getUnitInfo() {
		List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
		
		for(Device device : devices)
			deviceInfos.add(device.getDeviceInfo());
		
		return new UnitInfo(this.ID, deviceInfos);
	}		

}
