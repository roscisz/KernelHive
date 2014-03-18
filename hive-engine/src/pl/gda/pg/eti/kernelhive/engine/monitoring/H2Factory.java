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

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;

public class H2Factory {

	public static UnitDefinition getUnit(Long id) {
		try {
			H2Persistance persistance = new H2Persistance(new H2Db().open());
			UnitDefinition unit = new UnitDefinition();
			unit.setId(id);
			return persistance.select(unit);
		} catch (H2Persistance.H2PersistanceException | H2Db.InitializationException ex) {
			Logger.getLogger(H2Factory.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static DeviceDefinition getDevice(Long id) {
		try {
			H2Persistance persistance = new H2Persistance(new H2Db().open());
			DeviceDefinition device = new DeviceDefinition();
			device.setId(id);
			return persistance.select(device);
		} catch (H2Persistance.H2PersistanceException | H2Db.InitializationException ex) {
			Logger.getLogger(H2Factory.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
