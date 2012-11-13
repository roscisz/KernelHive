package pl.gda.pg.eti.kernelhive.common.clientService;

import java.util.List;

public class UnitInfo {
	
	public int ID;
	public List<DeviceInfo> deviceInfos;

	public UnitInfo() {
		
	}
	
	public UnitInfo(int ID, List<DeviceInfo> deviceInfos) {
		this.ID = ID;
		this.deviceInfos = deviceInfos;
	}		

}
