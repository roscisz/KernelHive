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
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.clusterService.IdHelpers;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import static pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntityType.CPU_USAGE;
import static pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntityType.MEMORY;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;

public class MonitoredEntity implements Serializable {

	private MonitoredEntityType type;
	private int id;
	private int unitId;
	private int clusterId;
	private Integer deviceId;

	public MonitoredEntity() {
	}

	public MonitoredEntity(MonitoredEntityType type) {
		this.type = type;
	}

	public MonitoredEntity(MonitoredEntityType type, int id) {
		this.type = type;
		this.id = id;
	}

	public MonitoredEntityType getType() {
		return type;
	}

	public void setType(MonitoredEntityType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStringId() {
		return this.type + "" + id;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getFriendlyName() {
		switch (type) {
			case CPU_USAGE:
				return "CPU " + id;
			case MEMORY:
				return "Memory";
			case CPU_SPEED:
				return "CPU speed";
			case GPU_USAGE:
				return "GPU usage";
			case GPU_GLOBAL_MEMORY:
				return "GPU global memory";
			case FAN:
				return "Fan speed";
			default:
				return "";
		}
	}

	public Double getMultiplicationFactor() {
		switch (type) {
			case CPU_USAGE:
				return 0.01d;
			case MEMORY:
				return 1000d;
			default:
				return 1d;
		}
	}

	public Double getMaxValue() {
		switch (type) {
			case CPU_USAGE:
			case FAN:
			case GPU_USAGE:
				return 100d;
			case MEMORY:
				UnitDefinition unit = H2Factory.getUnit(IdHelpers.getUnitId(unitId, clusterId));
				return unit == null ? null
						: ((double) unit.getMemorySize()) * getMultiplicationFactor();
			//case GPU_GLOBAL_MEMORY:
			//    DeviceDefinition device = H2Factory.getDevice(IdHelpers.getDeviceId(unitId, clusterId, deviceId));
			//    return device == null ? null
			//            : ((double)device.getGlobalMemoryBytes()) * getMultiplicationFactor();
			default:
				return null;
		}
	}

	public Double getMinValue() {
		switch (type) {
			case CPU_SPEED:
				return null;
			default:
				return 0d;
		}
	}
}
