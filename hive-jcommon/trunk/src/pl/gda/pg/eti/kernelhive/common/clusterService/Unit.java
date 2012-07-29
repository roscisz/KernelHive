package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

public class Unit extends HasID {
	
	private static String deviceSeparator = ";";
	public Cluster cluster;
	
	public List<Device> devices = new ArrayList<Device>();
	
	public void update(String devicesString) {
		String[] devicesArray = devicesString.split(deviceSeparator);
	
		int devicesCount = Integer.parseInt(devicesArray[0]);
		
		devices.clear();
		for(int i = 1; i <= devicesCount; i++)
			devices.add(new Device(devicesArray[i]));		
	}
	
	@Override
	public String toString() {
		return devices.toString();
	}		

}
