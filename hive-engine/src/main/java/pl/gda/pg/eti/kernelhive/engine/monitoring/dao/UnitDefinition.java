/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
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
package pl.gda.pg.eti.kernelhive.engine.monitoring.dao;

import pl.gda.pg.eti.kernelhive.common.clusterService.IdHelpers;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

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
