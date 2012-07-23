package pl.gda.pg.eti.kernelhive.common.clusterService;

import pl.gda.pg.eti.kernelhive.common.structure.HasID;
import pl.gda.pg.eti.kernelhive.common.structure.Unit;

public class Job extends HasID {
	// OBSOLETE:	
	private String deviceID, dataHost, dataPort, dataID;
	
	public Unit unit;
	
	public Job(Unit unit, String deviceID) {
		this.unit = unit;
		this.deviceID = deviceID;		
	}
	
	@Override
	public String toString() {
		return "bin " + ID + " localhost 31339 localhost 31338 " + dataHost + 
				" " + dataPort +
				" localhost 31340 " + deviceID +
				" 1 0 4096 64 456 " + dataID;
	}

	public void setDataAddress(String status) {
		System.out.println(status);
		String[] dataAddress = status.split(" ");
		this.dataHost = dataAddress[0];
		this.dataPort = dataAddress[1];
		this.dataID = dataAddress[2];		
	}
		
}
