/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rrd4j.core.RrdDb;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance.H2PersistanceException;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.UnitDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.MonitoringMessage;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.SequentialMessage;
import pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdDbFactory;
import pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdDbResolution;
import pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdHelper;

/**
 *
 * @author szymon
 */
public class MonitoringStorage {

	private final static Logger logger = Logger.getLogger(MonitoringStorage.class.getName());

	public void storeMessage(MonitoringMessage message) {
		switch (message.getType()) {
			case INITIAL:
				//storeInitialMessage((UnitDefinition) message);
				break;
			case SEQUENTIAL:
				try {
					storeSequentialMessage((SequentialMessage) message);
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "Error storing sequential message", ex);
				}
				break;
		}
	}

	public void storeInitialMessage(UnitDefinition message) {
		try {
			H2Db db = new H2Db();
			Connection connection = db.open();
			H2Persistance persistance = new H2Persistance(connection);
			persistance.createTable(message);
			persistance.save(message);
		} catch (H2PersistanceException | H2Db.InitializationException ex) {
			logger.log(Level.SEVERE, "Error while persisting unit", ex);
		}
	}

	public void storeSequentialMessage(SequentialMessage message) throws IOException {
		RrdDb db = null;
		try {
			logger.info("Storing message for cluster " + message.getClusterId());
			db = RrdDbFactory.getUnitDb(message.getClusterId(), message.getUnitId());
			if (db == null) {
				db = RrdDbFactory.createUnitDb(message.getCpuCores(),
						message.getClusterId(), message.getUnitId());
			}
			List<Integer> data = new LinkedList<>();
			data.add(new BigDecimal(new Date().getTime() / 1000).intValue());
			data.add(message.getClockSpeed());
			for (int cpuUsage : message.getCpuUsage()) {
				data.add(cpuUsage);
			}
			data.add(message.getMemoryUsed());
			RrdHelper.store(data, db);

			for (DeviceCyclicData gpu : message.getGpuDevices()) {
				storeCyclicGpuData(gpu);
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error while persisting sequential data", ex);
		} finally {
			if (db != null) {
				db.close();
			}
		}

		MonitoredEntity entity = new MonitoredEntity(MonitoredEntityType.FAN);
		entity.setClusterId(message.getClusterId());
		entity.setUnitId(message.getUnitId());
		entity.setDeviceId(0);
		RrdHelper.createGraph(entity, RrdDbResolution.EXACT);
	}

	public void storeCyclicGpuData(DeviceCyclicData gpu) throws IOException {
		RrdDb db = null;
		try {
			db = RrdDbFactory.getGpuDb(gpu.getClusterId(), gpu.getUntiId(),
					gpu.getDeviceId());
			if (db == null) {
				db = RrdDbFactory.createGpuDb(gpu.getClusterId(), gpu.getUntiId(),
						gpu.getDeviceId());
			}
			List<Integer> data = new LinkedList<>();
			data.add(new BigDecimal(new Date().getTime() / 1000).intValue());
			data.add(gpu.getGpuUsage());
			data.add(gpu.getMemoryUsed());
			data.add(gpu.getFanSpeed());
			RrdHelper.store(data, db);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error while persisting sequential data", ex);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
