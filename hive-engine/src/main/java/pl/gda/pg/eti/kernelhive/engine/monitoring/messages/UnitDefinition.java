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
package pl.gda.pg.eti.kernelhive.engine.monitoring.messages;

import java.io.Serializable;
import java.math.BigDecimal;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoringMessageType;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

public class UnitDefinition implements H2Entity, MonitoringMessage, Serializable {

	private int unitId;
	private int clusterId;
	private int cpuCount;
	private int memorySize;
	private int gpuDevicesCount;

	@Override
	public MonitoringMessageType getType() {
		return MonitoringMessageType.INITIAL;
	}

	@Override
	public String getTableName() {
		return "Units";
	}

	@H2Getter
	@Override
	public Long getId() {
		return (long) unitId;
	}

	public void setId(Long id) {
		this.unitId = new BigDecimal(id).intValue();
	}

	@Override
	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	@H2Getter
	@Override
	public int getClusterId() {
		return clusterId;
	}

	@H2Setter
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	@H2Getter
	public int getCpuCount() {
		return cpuCount;
	}

	@H2Setter
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
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
	public int getGpuDevicesCount() {
		return gpuDevicesCount;
	}

	@H2Setter
	public void setGpuDevicesCount(int gpuDevicesCount) {
		this.gpuDevicesCount = gpuDevicesCount;
	}
}
