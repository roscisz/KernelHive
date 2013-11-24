/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.messages;

import java.io.Serializable;
import java.math.BigDecimal;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoringMessageType;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Entity;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Getter;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Setter;

/**
 *
 * @author szymon
 */
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