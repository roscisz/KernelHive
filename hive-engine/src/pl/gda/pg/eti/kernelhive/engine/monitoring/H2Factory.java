/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;

/**
 *
 * @author szymon
 */
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
