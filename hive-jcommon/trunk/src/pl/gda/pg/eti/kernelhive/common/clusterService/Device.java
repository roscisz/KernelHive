package pl.gda.pg.eti.kernelhive.common.clusterService;

import javax.xml.bind.annotation.XmlTransient;

import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;

public class Device {
	
	public enum DeviceType {
		GPU,
		CPU
	}
	
	private static String parameterSeparator = ":";
	
	public String name;
	public String vendor;
	private boolean isAvailable;
	public int computeUnitsNumber;
	public int clock;
	public Long globalMemoryBytes;
	public Long localMemoryBytes;
	public int workGroupSize;
	
	public boolean busy = false;
	
	@XmlTransient
	public Unit unit;

	public Device() {
		
	}
		
	public Device(String serializedInfo, Unit myUnit) {
		unserialize(serializedInfo);
		this.unit = myUnit;
	}

	private void unserialize(String serializedInfo) {		
		String[] parameters = serializedInfo.split(parameterSeparator);		
		name = parameters[0];
		vendor = parameters[2];
		isAvailable = parameters[3].equals("1");
		computeUnitsNumber = Integer.parseInt(parameters[4]);
		clock = Integer.parseInt(parameters[5]);
		globalMemoryBytes = Long.parseLong(parameters[6]);
		localMemoryBytes = Long.parseLong(parameters[7]);
		workGroupSize = Integer.parseInt(parameters[8]);
	}

	@Override
	public String toString() {
		return "Device [name=" + name + ", vendor=" + vendor + ", isAvailable="
				+ isAvailable + ", computeUnitsNumber=" + computeUnitsNumber
				+ ", clock=" + clock + ", globalMemoryBytes="
				+ globalMemoryBytes + ", localMemoryBytes=" + localMemoryBytes
				+ ", workGroupSize=" + workGroupSize + "]";
	}

	public void updateReverseReferences(Unit unit) {
		this.unit = unit;		
	}

	public DeviceInfo getDeviceInfo() {
		return new DeviceInfo(this.toString());
	}	
	
	/**
	 * FIXME: should be moved to low-level OpenCl methods
	 */
	public DeviceType getDeviceType() {
		if(this.name.matches(".*CPU.*"))
			return DeviceType.CPU;
		return DeviceType.GPU;
	}

	public boolean isBusy() {
		return busy;
	}
	
	public boolean isAvailable() {
		if(name.matches(".*Quad.*"))
			return false; 
		return isAvailable;
	}
}
