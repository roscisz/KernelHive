/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.rrd;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntity;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntityType;

/**
 *
 * @author szymon
 */
public class RrdDbFactory {

	private static final int STEP = 1;
	private static final int TIMEOUT = 5;

	public static RrdDb getUnitDb(int clusterId, int unitId) throws IOException {
		File f = new File(RrdHelper.getDbPath(clusterId, unitId));
		if (f.exists() && f.isFile()) {
			return new RrdDb(RrdHelper.getDbPath(clusterId, unitId));
		} else {
			return null;
		}
	}

	public static RrdDb createUnitDb(int cores, int clusterId, int unitId) throws IOException {
		Logger.getLogger(RrdDbFactory.class.getName()).info("Creating new RRD file: "
				+ RrdHelper.getDbPath(clusterId, unitId));
		RrdDef rrdDef = new RrdDef(RrdHelper.getDbPath(clusterId, unitId));
		rrdDef.setStep(STEP);
		rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.CPU_SPEED).getStringId(),
				DsType.GAUGE, TIMEOUT, Double.NaN, Double.NaN);
		for (int i = 0; i < cores; i++) {
			rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.CPU_USAGE, i).getStringId(),
					DsType.GAUGE, TIMEOUT, Double.NaN, Double.NaN);
		}
		rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.MEMORY).getStringId(),
				DsType.GAUGE, TIMEOUT, Double.NaN, Double.NaN);

		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5,
				RrdHelper.getSampleSize(RrdDbResolution.EXACT),
				RrdHelper.getSampleWindow(RrdDbResolution.EXACT));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5,
				RrdHelper.getSampleSize(RrdDbResolution.LOW),
				RrdHelper.getSampleWindow(RrdDbResolution.LOW));

		RrdDb rrdDb = new RrdDb(rrdDef);
		return rrdDb;
	}

	public static RrdDb getGpuDb(int clusterId, int unitId, int deviceId) throws IOException {
		File f = new File(RrdHelper.getDbPath(clusterId, unitId, deviceId));
		if (f.exists() && f.isFile()) {
			return new RrdDb(RrdHelper.getDbPath(clusterId, unitId, deviceId));
		} else {
			return null;
		}
	}

	public static RrdDb createGpuDb(int clusterId, int unitId, int deviceId) throws IOException {
		Logger.getLogger(RrdDbFactory.class.getName()).info("Creating new GPU RRD file: "
				+ RrdHelper.getDbPath(clusterId, unitId, deviceId));
		RrdDef rrdDef = new RrdDef(RrdHelper.getDbPath(clusterId, unitId, deviceId));
		rrdDef.setStep(STEP);
		rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.GPU_USAGE).getStringId(),
				DsType.GAUGE, TIMEOUT, Double.NaN, Double.NaN);
		rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.GPU_GLOBAL_MEMORY).getStringId(),
				DsType.GAUGE, TIMEOUT, Double.NaN, Double.NaN);
		rrdDef.addDatasource(new MonitoredEntity(MonitoredEntityType.FAN).getStringId(),
				DsType.GAUGE, TIMEOUT, 0, 100);
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5,
				RrdHelper.getSampleSize(RrdDbResolution.EXACT),
				RrdHelper.getSampleWindow(RrdDbResolution.EXACT));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5,
				RrdHelper.getSampleSize(RrdDbResolution.LOW),
				RrdHelper.getSampleWindow(RrdDbResolution.LOW));

		RrdDb rrdDb = new RrdDb(rrdDef);
		return rrdDb;
	}
}
