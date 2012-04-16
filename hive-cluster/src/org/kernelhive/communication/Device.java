package org.kernelhive.communication;

public class Device {
	private static String parameterSeparator = ":";
	
	public String name;
	public String vendor;
	public boolean isAvailable;
	public int computeUnitsNumber;
	public int clock;
	public int globalMemoryBytes;
	public int localMemoryBytes;
	public int workGroupSize;

	public Device(String serializedInfo) {
		unserialize(serializedInfo);
	}

	private void unserialize(String serializedInfo) {
		String[] parameters = serializedInfo.split(parameterSeparator);
		
		name = parameters[0];
		vendor = parameters[1];
		isAvailable = Boolean.parseBoolean(parameters[2]);
		computeUnitsNumber = Integer.parseInt(parameters[3]);
		clock = Integer.parseInt(parameters[4]);
		globalMemoryBytes = Integer.parseInt(parameters[5]);
		localMemoryBytes = Integer.parseInt(parameters[6]);
		workGroupSize = Integer.parseInt(parameters[7]);
	}

	@Override
	public String toString() {
		return "Device [name=" + name + ", vendor=" + vendor + ", isAvailable="
				+ isAvailable + ", computeUnitsNumber=" + computeUnitsNumber
				+ ", clock=" + clock + ", globalMemoryBytes="
				+ globalMemoryBytes + ", localMemoryBytes=" + localMemoryBytes
				+ ", workGroupSize=" + workGroupSize + "]";
	}	
}
