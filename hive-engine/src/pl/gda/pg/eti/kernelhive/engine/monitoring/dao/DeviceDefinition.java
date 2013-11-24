/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.dao;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.IdHelpers;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

/**
 *
 * @author szymon
 */
public class DeviceDefinition implements H2Entity {

	private int clusterId;
	private int unitId;
	private int deviceId;
	private Long id;
	private String name;
	private String vendor;
	private int computeUnitsNumber;
	private int clock;
	private Long globalMemoryBytes;
	private Long localMemoryBytes;
	private int workGroupSize;

	public DeviceDefinition() {
	}

	public DeviceDefinition(Device data) {
		clusterId = data.getUnit().getClusterId();
		unitId = data.getUnit().getUnitId();
		deviceId = data.id;
		name = data.name;
		vendor = data.vendor;
		computeUnitsNumber = data.computeUnitsNumber;
		clock = data.clock;
		globalMemoryBytes = data.globalMemoryBytes;
		localMemoryBytes = data.localMemoryBytes;
		workGroupSize = data.workGroupSize;
		id = IdHelpers.getDeviceId(unitId, clusterId, deviceId);
	}

	@H2Getter
	public int getClusterId() {
		return clusterId;
	}

	@H2Setter
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	@H2Getter
	public int getUnitId() {
		return unitId;
	}

	@H2Setter
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	@H2Getter
	public int getDeviceId() {
		return deviceId;
	}

	@H2Setter
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	@H2Getter
	public String getName() {
		return name;
	}

	@H2Setter
	public void setName(String name) {
		this.name = name;
	}

	@H2Getter
	public String getVendor() {
		return vendor;
	}

	@H2Setter
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@H2Getter
	public int getComputeUnitsNumber() {
		return computeUnitsNumber;
	}

	@H2Setter
	public void setComputeUnitsNumber(int computeUnitsNumber) {
		this.computeUnitsNumber = computeUnitsNumber;
	}

	@H2Getter
	public int getClock() {
		return clock;
	}

	@H2Setter
	public void setClock(int clock) {
		this.clock = clock;
	}

	@H2Getter
	public Long getGlobalMemoryBytes() {
		return globalMemoryBytes;
	}

	@H2Setter
	public void setGlobalMemoryBytes(Long globalMemoryBytes) {
		this.globalMemoryBytes = globalMemoryBytes;
	}

	@H2Getter
	public Long getLocalMemoryBytes() {
		return localMemoryBytes;
	}

	@H2Setter
	public void setLocalMemoryBytes(Long localMemoryBytes) {
		this.localMemoryBytes = localMemoryBytes;
	}

	@H2Getter
	public int getWorkGroupSize() {
		return workGroupSize;
	}

	@H2Setter
	public void setWorkGroupSize(int workGroupSize) {
		this.workGroupSize = workGroupSize;
	}

	@Override
	@H2Getter
	public Long getId() {
		return id;
	}

	@H2Setter
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getTableName() {
		return "Devices";
	}
}