/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.dao;

import pl.gda.pg.eti.kernelhive.common.clusterService.IdHelpers;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

/**
 *
 * @author szymon
 */
public class UnitDefinition implements H2Entity {

	private Long id;
	private int unitId;
	private int clusterId;
	private String hostname;
	private int memorySize;
	private int cpuCores;
	private int devicesCount;

	public UnitDefinition() {
	}

	public UnitDefinition(Unit data) {
		unitId = data.getUnitId();
		clusterId = data.getClusterId();
		id = IdHelpers.getUnitId(unitId, clusterId);
		hostname = data.getHostname();
		memorySize = data.getMemorySize();
		cpuCores = data.getCpuCores();
		devicesCount = data.getDevices().size();
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

	@H2Getter
	public int getUnitId() {
		return unitId;
	}

	@H2Setter
	public void setUnitId(int unitId) {
		this.unitId = unitId;
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
	public String getHostname() {
		return hostname;
	}

	@H2Setter
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@H2Getter
	public int getMemorySize() {
		return memorySize;
	}

	@H2Setter
	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	@H2Getter
	public int getCpuCores() {
		return cpuCores;
	}

	@H2Setter
	public void setCpuCores(int cpuCores) {
		this.cpuCores = cpuCores;
	}

	@H2Getter
	public int getDevicesCount() {
		return devicesCount;
	}

	@H2Setter
	public void setDevicesCount(int devicesCount) {
		this.devicesCount = devicesCount;
	}

	@Override
	public String getTableName() {
		return "Units";
	}
}
