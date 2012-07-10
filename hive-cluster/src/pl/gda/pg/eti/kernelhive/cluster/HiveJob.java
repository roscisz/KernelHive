package pl.gda.pg.eti.kernelhive.cluster;

public class HiveJob {
	// OBSOLETE:
	
	private String deviceID, dataHost, dataPort, dataID;
	private UnitProxy unit;
	private int ID;
	
	public HiveJob(UnitProxy unit, String deviceID, int ID) {
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

	public void run() {
		System.out.println(this.toString());
		unit.runJob(this);		
	}

	public void setDataAddress(String status) {
		System.out.println(status);
		String[] dataAddress = status.split(" ");
		this.dataHost = dataAddress[0];
		this.dataPort = dataAddress[1];
		this.dataID = dataAddress[2];		
	}
		
}
