/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.monitoring;

import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoredEntity;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoredEntityType;

/**
 *
 * @author szymon
 */
public class MonitoredEntityBuilder {

	MonitoredEntity entity;

	public MonitoredEntityBuilder() {
		entity = new MonitoredEntity();
	}

	public MonitoredEntityBuilder setType(MonitoredEntityType type) {
		entity.setType(type);
		return this;
	}

	public MonitoredEntityBuilder setId(int id) {
		entity.setId(id);
		return this;
	}

	public MonitoredEntity get() {
		return entity;
	}

	public MonitoredEntityBuilder setUnitId(int unitId) {
		entity.setUnitId(unitId);
		return this;
	}

	public MonitoredEntityBuilder setClusterId(int clusterId) {
		entity.setClusterId(clusterId);
		return this;
	}

	public MonitoredEntityBuilder setDeviceId(int deviceId) {
		entity.setDeviceId(deviceId);
		return this;
	}
}
