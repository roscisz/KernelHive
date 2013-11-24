/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author szymon
 */
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
