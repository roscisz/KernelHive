/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.clusterService;

import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;

public class Device {

	public enum DeviceType {

		GPU,
		CPU
	}
	private static final int PROPERTIES_COUNT = 6;
	public int id;
	public String identifier;
	public String name;
	public String vendor;
	public boolean isAvailable;
	public int computeUnitsNumber;
	public int clock;
	public Long globalMemoryBytes;
	public Long localMemoryBytes;
	public int workGroupSize;
	public boolean busy = false;
	private Unit unit;

	public Device() {
	}

	public Device(String[] parameters, Unit myUnit) {
		unserialize(parameters);
		this.unit = myUnit;
	}

	private void unserialize(String[] parameters) {
		int offset = 0;
		id = Integer.valueOf(parameters[offset++]);
		name = parameters[offset++];
		vendor = parameters[offset++];
		identifier = parameters[offset++];
		globalMemoryBytes = Long.getLong(parameters[offset++]);
		isAvailable = parameters[offset++].equals("1");
		/*name = parameters[0];
		 vendor = parameters[1];
		 isAvailable = !parameters[2].equals("0");
		 computeUnitsNumber = Integer.parseInt(parameters[3]);
		 clock = Integer.parseInt(parameters[4]);
		 globalMemoryBytes = Long.parseLong(parameters[5]);
		 localMemoryBytes = Long.parseLong(parameters[6]);
		 workGroupSize = Integer.parseInt(parameters[7]);*/
	}

	@Override
	public String toString() {
		return "Device [name=" + name + ", vendor=" + vendor + ", isAvailable="
				+ isAvailable + ", isBusy=" + busy + ", computeUnitsNumber=" + computeUnitsNumber
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

	public static int getPropertiesCount() {
		return PROPERTIES_COUNT;
	}

	public Unit getUnit() {
		return unit;
	}

	/**
	 * Overrides changable data (such as clock speed) from the updated object
	 *
	 * @param device
	 */
	public void merge(Device device) {
		clock = device.clock;
	}

	/**
	 * FIXME: should be moved to low-level OpenCl methods
	 */
	public DeviceType getDeviceType() {
		return this.name.matches(".*CPU.*")
				? DeviceType.CPU
				: DeviceType.GPU;
	}

	public boolean isBusy() {
		return busy;
	}

	public boolean isAvailable() {
		return isAvailable && !name.matches(".*Quad.*");
	}
}
