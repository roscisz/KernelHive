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

import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.ClusterDefinition;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IMonitoringClientBeanRemote;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance.H2PersistanceException;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.monitoring.helpers.MonitoredEntityHelper;
import pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdDbResolution;
import pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdHelper;

@Stateless
@WebService
public class MonitoringClientBean implements IMonitoringClientBeanRemote {

	private static final Logger logger = Logger.getLogger(MonitoringClientBean.class.getName());

	@Override
	@WebMethod
	public String getGraphPath(MonitoredEntity entity) {
		RrdHelper.createGraph(entity, RrdDbResolution.EXACT);
		logger.info("getGraphPath for " + entity.getClusterId() + " " + entity.getUnitId() + " " + entity.getDeviceId() + " " + MonitoredEntityHelper.getGraphURL(entity));
		return MonitoredEntityHelper.getGraphURL(entity);
	}

	@Override
	@WebMethod
	public List<UnitDefinition> getUnits() {
		try {
			H2Db db = new H2Db();
			Connection connection = db.open();
			H2Persistance persistance = new H2Persistance(connection);
			return persistance.selectAll(new UnitDefinition());
		} catch (H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Cannot create instance of H2Db", ex);
			return null;
		} catch (H2PersistanceException ex) {
			logger.log(Level.SEVERE, "Error getting list of units", ex);
			return null;
		}
	}

	@Override
	@WebMethod
	public List<UnitDefinition> getUnitsForCluster(int clusterId) {
		try {
			H2Db db = new H2Db();
			Connection connection = db.open();
			H2Persistance persistance = new H2Persistance(connection);
			return persistance.selectAll(new UnitDefinition(), "clusterId", String.valueOf(clusterId));
		} catch (H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Cannot create instance of H2Db", ex);
			return null;
		} catch (H2PersistanceException ex) {
			logger.log(Level.SEVERE, "Error getting list of units", ex);
			return null;
		}

	}

	@Override
	@WebMethod
	public List<ClusterDefinition> getClusters() {
		try {
			H2Db db = new H2Db();
			Connection connection = db.open();
			H2Persistance persistance = new H2Persistance(connection);
			return persistance.selectAll(new ClusterDefinition());
		} catch (H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Cannot create instance of H2Db", ex);
			return null;
		} catch (H2PersistanceException ex) {
			logger.log(Level.SEVERE, "Error getting list of units", ex);
			return null;
		}
	}

	@Override
	@WebMethod
	public List<DeviceDefinition> getDevices(int clusterId, int unitId) {
		try {
			H2Db db = new H2Db();
			Connection connection = db.open();
			H2Persistance persistance = new H2Persistance(connection);
			Map<String, String> params = new HashMap<>();
			params.put("clusterId", String.valueOf(clusterId));
			params.put("unitId", String.valueOf(unitId));
			return persistance.selectAll(new DeviceDefinition(), params);
		} catch (H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Cannot create instance of H2Db", ex);
			return null;
		} catch (H2PersistanceException ex) {
			logger.log(Level.SEVERE, "Error getting list of units", ex);
			return null;
		}
	}

	@Override
	@WebMethod
	public List<DeviceDefinition> getAllDevices() {
		try {
			H2Persistance persistance = new H2Persistance(new H2Db().open());
			return persistance.selectAll(new DeviceDefinition());
		} catch (H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Cannot create instance of H2Db", ex);
			return null;
		} catch (H2PersistanceException ex) {
			logger.log(Level.SEVERE, "Error getting list of units", ex);
			return null;
		}
	}

	@Override
	@WebMethod
	public List<PreviewObject> getPreviewData(int workflowId) {
		return HiveEngine.getInstance().getWorkflowPreview(workflowId);
	}
}
