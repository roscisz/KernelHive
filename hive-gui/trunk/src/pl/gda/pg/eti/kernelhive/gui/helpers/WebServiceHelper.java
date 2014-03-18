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
package pl.gda.pg.eti.kernelhive.gui.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.clientService.ClientBean;
import pl.gda.pg.eti.kernelhive.common.clientService.ClientBeanService;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBean;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBeanService;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;

public class WebServiceHelper {

	public MonitoringClientBean getMonitoringService() {
		try {
			URL url = new URL(AppConfiguration.getInstance().getEngineAddress(),
					"MonitoringClientBeanService/MonitoringClientBean?wsdl");
			return new MonitoringClientBeanService(url)
					.getMonitoringClientBeanPort();
		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error getting monitoring service", ex);
			return null;
		}
	}

	public ClientBean getClientService() {
		try {
			URL url = new URL(AppConfiguration.getInstance().getEngineAddress(),
					"ClientBeanService/ClientBean?wsdl");
			return new ClientBeanService(url)
					.getClientBeanPort();
		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error getting client service", ex);
			return null;
		}
	}
}
