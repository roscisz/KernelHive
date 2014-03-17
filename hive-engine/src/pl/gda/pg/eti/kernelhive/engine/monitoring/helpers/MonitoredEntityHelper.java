/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntity;

/**
 *
 * @author szymon
 */
public class MonitoredEntityHelper {

	private MonitoredEntityHelper() {
	}

	public static Path getGraphPath(MonitoredEntity monitoredEntity) {
		return Paths.get(String.format("/tmp/graphs/%s.gif", getFilename(monitoredEntity)));
	}

	public static String getGraphURL(MonitoredEntity monitoredEntity) {
		return String.format("/graphs/%s.gif", getFilename(monitoredEntity));
	}

	private static String getFilename(MonitoredEntity monitoredEntity) {
		return String.format("kh-graph-%d-%d-%s",
				monitoredEntity.getClusterId(), monitoredEntity.getUnitId(),
				monitoredEntity.getStringId());
	}
}
