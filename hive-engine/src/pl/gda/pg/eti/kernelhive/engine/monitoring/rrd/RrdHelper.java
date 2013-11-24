/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.rrd;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Sample;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntity;
import pl.gda.pg.eti.kernelhive.engine.monitoring.helpers.MonitoredEntityHelper;
import static pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdDbResolution.EXACT;
import static pl.gda.pg.eti.kernelhive.engine.monitoring.rrd.RrdDbResolution.LOW;

/**
 *
 * @author szymon
 */
public class RrdHelper {

	private static final String DB_PATH_TEMPLATE = "/tmp/kh-data-%s.rrd";

	private RrdHelper() {
	}

	public static String getDbPath(int clusterId, int unitId) {
		return getDbPath(String.format("unit-%d-%d", clusterId, unitId));
	}

	public static String getDbPath(int clusterId, int unitId, int deviceId) {
		return getDbPath(String.format("unit-%d-%d-%d", clusterId, unitId, deviceId));
	}

	public static String getDbPath(String name) {
		return String.format(DB_PATH_TEMPLATE, name);
	}

	public static int getSampleSize(RrdDbResolution resolution) {
		switch (resolution) {
			case EXACT:
				return 1;   // 1s
			case LOW:
				return 5;//60;  // 1 min
			default:
				throw new UnsupportedOperationException("No sample size for " + resolution);
		}
	}

	public static int getSampleWindow(RrdDbResolution resolution) {
		switch (resolution) {
			case EXACT:
				return 60 * 60; // hour
			case LOW:
				return 60 * 24; // 1 day
			default:
				throw new UnsupportedOperationException("No resolution for " + resolution);
		}
	}

	public static long getTimestamp() {
		return new Date().getTime() / 1000;
	}

	public static Path createGraph(MonitoredEntity monitoredEntity, RrdDbResolution resolution) {
		Path graphPath = MonitoredEntityHelper.getGraphPath(monitoredEntity);
		RrdGraphDef graphDef = new RrdGraphDef();
		long timestamp = getTimestamp();
		String seriesName = monitoredEntity.getStringId();
		String rawSeriesName = seriesName + "raw";
		graphPath.getParent().toFile().mkdirs();

		graphDef.setTimeSpan(timestamp - getSampleWindow(resolution) * getSampleSize(resolution),
				timestamp);
		String dbPath = monitoredEntity.getDeviceId() != null
				? getDbPath(monitoredEntity.getClusterId(), monitoredEntity.getUnitId(),
				monitoredEntity.getDeviceId())
				: getDbPath(monitoredEntity.getClusterId(), monitoredEntity.getUnitId());
		graphDef.datasource(rawSeriesName, dbPath,
				monitoredEntity.getStringId(), ConsolFun.AVERAGE);
		graphDef.datasource(seriesName,
				rawSeriesName + "," + monitoredEntity.getMultiplicationFactor() + ",*");
		graphDef.line(seriesName, new Color(0xFF, 0, 0), null, 1);
		graphDef.setFilename(graphPath.toString());
		graphDef.setWidth(500);
		graphDef.setHeight(300);
		graphDef.setTitle(monitoredEntity.getFriendlyName());
		Double maxValue = monitoredEntity.getMaxValue();
		if (maxValue != null) {
			graphDef.setMaxValue(maxValue);
		}
		Double minValue = monitoredEntity.getMinValue();
		if (minValue != null) {
			graphDef.setMinValue(minValue);
		}

		RrdGraph graph;
		try {
			graph = new RrdGraph(graphDef);
			BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			graph.render(bi.getGraphics());
			return graphPath;
		} catch (IOException ex) {
			Logger.getLogger(RrdHelper.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static void store(List<Integer> data, RrdDb db) {
		StringBuilder sb = new StringBuilder();
		for (Integer value : data) {
			sb.append(value);
			sb.append(':');
		}
		sb.deleteCharAt(sb.length() - 1);

		try {
			Sample sample = db.createSample();
			sample.setAndUpdate(sb.toString());
		} catch (IOException ex) {
			Logger.getLogger(RrdHelper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
