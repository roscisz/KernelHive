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
package pl.gda.pg.eti.kernelhive.engine.monitoring.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntity;

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
